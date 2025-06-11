import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service principal de gestion des notifications
 * Implémente le pattern Observer pour la gestion des notifications
 * Gère la persistance des données dans MySQL
 */
public class NotificationService implements INotificationService {
    // Connexion à la base de données
    private Connection connection;
    private EmailSender emailSender;

    /**
     * Constructeur : initialise la connexion à la base de données
     */
    public NotificationService() {
        this.connection = DatabaseConfig.getConnection();
        this.emailSender = new EmailSender();
    }

    /**
     * Ajoute un nouvel administrateur dans le système
     * Vérifie d'abord si l'administrateur n'existe pas déjà
     * @param admin L'administrateur à ajouter
     */
    public void addAdmin(Admin admin) {
        try {
            // Vérifier si l'admin existe déjà
            PreparedStatement checkStmt = connection.prepareStatement(
                "SELECT id FROM utilisateurs WHERE id = ? OR email = ?"
            );
            checkStmt.setString(1, admin.getId());
            checkStmt.setString(2, admin.getEmail());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                return; // Admin existe déjà, on ne fait rien
            }

            // Si l'admin n'existe pas, on l'ajoute
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO utilisateurs (id, nom, email, mot_de_passe, type_utilisateur) VALUES (?, ?, ?, ?, 'ADMIN')"
            );
            stmt.setString(1, admin.getId());
            stmt.setString(2, admin.getName());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    /**
     * Ajoute un nouvel employé dans le système
     * Crée l'entrée dans la table utilisateurs et employes
     * @param employee L'employé à ajouter
     */
    public void addEmployee(Employee employee) {
        try {
            // Vérifier si l'email existe déjà
            PreparedStatement checkStmt = connection.prepareStatement(
                "SELECT id FROM utilisateurs WHERE email = ?"
            );
            checkStmt.setString(1, employee.getEmail());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà.");
            }

            connection.setAutoCommit(false);

            // Ajouter dans la table utilisateurs
            PreparedStatement userStmt = connection.prepareStatement(
                "INSERT INTO utilisateurs (id, nom, email, mot_de_passe, type_utilisateur) VALUES (?, ?, ?, ?, 'EMPLOYE')"
            );
            userStmt.setString(1, employee.getId());
            userStmt.setString(2, employee.getName());
            userStmt.setString(3, employee.getEmail());
            userStmt.setString(4, employee.getPassword());
            userStmt.executeUpdate();

            // Ajouter dans la table employes
            PreparedStatement empStmt = connection.prepareStatement(
                "INSERT INTO employes (id_utilisateur, est_abonne) VALUES (?, false)"
            );
            empStmt.setString(1, employee.getId());
            empStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Erreur lors du rollback : " + ex.getMessage());
            }
            throw new RuntimeException("Erreur lors de l'ajout de l'employé : " + e.getMessage());
        }
    }

    /**
     * Authentifie un utilisateur (admin ou employé)
     * @param email Email de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @return L'utilisateur authentifié ou null si échec
     */
    public User authenticate(String email, String password) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT * FROM utilisateurs WHERE email = ? AND mot_de_passe = ?"
            );
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("nom");
                String userType = rs.getString("type_utilisateur");

                // Création de l'objet approprié selon le type d'utilisateur
                if ("ADMIN".equals(userType)) {
                    return new Admin(id, name, email, password);
                } else {
                    Employee emp = new Employee(id, name, email, password);
                    // Récupération du statut d'abonnement
                    PreparedStatement empStmt = connection.prepareStatement(
                        "SELECT est_abonne FROM employes WHERE id_utilisateur = ?"
                    );
                    empStmt.setString(1, id);
                    ResultSet empRs = empStmt.executeQuery();
                    if (empRs.next()) {
                        emp.setSubscribed(empRs.getBoolean("est_abonne"));
                    }
                    return emp;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return null;
    }

    /**
     * Abonne un employé aux notifications
     * @param observer L'employé à abonner
     */
    @Override
    public void subscribe(IObserver observer) {
        if (observer instanceof Employee) {
            Employee employee = (Employee) observer;
            try {
                PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE employes SET est_abonne = true WHERE id_utilisateur = ?"
                );
                stmt.setString(1, employee.getId());
                stmt.executeUpdate();
            employee.setSubscribed(true);
            } catch (SQLException e) {
                System.err.println("Erreur: " + e.getMessage());
            }
        }
    }

    /**
     * Désabonne un employé des notifications
     * @param observer L'employé à désabonner
     */
    @Override
    public void unsubscribe(IObserver observer) {
        if (observer instanceof Employee) {
            Employee employee = (Employee) observer;
            try {
                PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE employes SET est_abonne = false WHERE id_utilisateur = ?"
                );
                stmt.setString(1, employee.getId());
                stmt.executeUpdate();
            employee.setSubscribed(false);
            } catch (SQLException e) {
                System.err.println("Erreur: " + e.getMessage());
            }
        }
    }

    /**
     * Envoie une notification à tous les employés abonnés
     * Seuls les administrateurs peuvent envoyer des notifications
     * @param message Le message à envoyer
     * @param senderId L'ID de l'expéditeur (doit être un admin)
     */
    @Override
    public void notifyObservers(String message, String senderId) {
        try {
            // Vérifier si l'expéditeur est un admin
            PreparedStatement checkStmt = connection.prepareStatement(
                "SELECT type_utilisateur, nom, email FROM utilisateurs WHERE id = ? AND type_utilisateur = 'ADMIN'"
            );
            checkStmt.setString(1, senderId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                return; // L'expéditeur n'est pas un admin
            }

            String senderName = rs.getString("nom");
            String senderEmail = rs.getString("email");
            connection.setAutoCommit(false); // Début de la transaction

            // Enregistrer la notification
            PreparedStatement notifStmt = connection.prepareStatement(
                "INSERT INTO notifications_envoyees (id_expediteur, message) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            notifStmt.setString(1, senderId);
            notifStmt.setString(2, message);
            notifStmt.executeUpdate();

            // Récupérer l'ID de la notification
            ResultSet generatedKeys = notifStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int notificationId = generatedKeys.getInt(1);

                // Récupérer les emails des employés abonnés
                PreparedStatement empStmt = connection.prepareStatement(
                    "SELECT u.email FROM utilisateurs u " +
                    "JOIN employes e ON u.id = e.id_utilisateur " +
                    "WHERE e.est_abonne = true AND u.id != ?"
                );
                empStmt.setString(1, senderId);
                ResultSet empRs = empStmt.executeQuery();

                // Envoyer aux employés abonnés
                PreparedStatement recipientStmt = connection.prepareStatement(
                    "INSERT INTO notifications_recues (id_notification, id_destinataire) " +
                    "SELECT ?, e.id_utilisateur FROM employes e " +
                    "WHERE e.est_abonne = true AND e.id_utilisateur != ?"
                );
                recipientStmt.setInt(1, notificationId);
                recipientStmt.setString(2, senderId);
                recipientStmt.executeUpdate();

                // Envoyer les emails aux employés abonnés
                while (empRs.next()) {
                    String recipientEmail = empRs.getString("email");
                    emailSender.sendEmail(
                        recipientEmail,
                        "Nouvelle notification de " + senderName,
                        "Message de " + senderName + " :\n\n" + message
                    );
                }

                // Notifier les employés en temps réel
                for (Employee employee : getEmployees()) {
                    if (employee.isSubscribed() && !employee.getId().equals(senderId)) {
                        employee.receiveNotification(message, senderName);
                    }
                }
            }

            connection.commit(); // Validation de la transaction
        } catch (SQLException e) {
            try {
                connection.rollback(); // Annulation en cas d'erreur
            } catch (SQLException ex) {
                System.err.println("Erreur lors du rollback : " + ex.getMessage());
            }
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    /**
     * Vérifie si un employé est abonné aux notifications
     * @param observerId L'ID de l'employé à vérifier
     * @return true si l'employé est abonné, false sinon
     */
    @Override
    public boolean isSubscribed(String observerId) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT est_abonne FROM employes WHERE id_utilisateur = ?"
            );
            stmt.setString(1, observerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("est_abonne");
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return false;
    }

    /**
     * Récupère la liste de tous les abonnés
     * @return Liste des employés abonnés aux notifications
     */
    @Override
    public List<IObserver> getSubscribers() {
        List<IObserver> subscribers = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT u.* FROM utilisateurs u " +
                "JOIN employes e ON u.id = e.id_utilisateur " +
                "WHERE e.est_abonne = true"
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee emp = new Employee(
                    rs.getString("id"),
                    rs.getString("nom"),
                    rs.getString("email"),
                    rs.getString("mot_de_passe")
                );
                emp.setSubscribed(true);
                subscribers.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return subscribers;
    }

    /**
     * Récupère la liste de tous les employés
     * @return Liste de tous les employés (abonnés ou non)
     */
    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT u.*, e.est_abonne FROM utilisateurs u " +
                "JOIN employes e ON u.id = e.id_utilisateur " +
                "WHERE u.type_utilisateur = 'EMPLOYE'"
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee emp = new Employee(
                    rs.getString("id"),
                    rs.getString("nom"),
                    rs.getString("email"),
                    rs.getString("mot_de_passe")
                );
                emp.setSubscribed(rs.getBoolean("est_abonne"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return employees;
    }

    /**
     * Permet à un administrateur de désabonner un employé
     * @param admin L'administrateur effectuant l'action
     * @param employee L'employé à désabonner
     */
    public void adminUnsubscribeEmployee(Admin admin, Employee employee) {
        try {
            // Vérifier que l'utilisateur est bien un admin
            PreparedStatement checkStmt = connection.prepareStatement(
                "SELECT type_utilisateur FROM utilisateurs WHERE id = ? AND type_utilisateur = 'ADMIN'"
            );
            checkStmt.setString(1, admin.getId());
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                System.err.println("Action non autorisée : seuls les administrateurs peuvent désabonner des employés.");
                return;
            }

            // Désabonner l'employé
            PreparedStatement stmt = connection.prepareStatement(
                "UPDATE employes SET est_abonne = false WHERE id_utilisateur = ?"
            );
            stmt.setString(1, employee.getId());
            stmt.executeUpdate();
            
            // Mettre à jour l'état de l'objet employé
            employee.setSubscribed(false);

            // Enregistrer l'action dans les notifications
            PreparedStatement notifStmt = connection.prepareStatement(
                "INSERT INTO notifications_envoyees (id_expediteur, message) VALUES (?, ?)"
            );
            notifStmt.setString(1, admin.getId());
            notifStmt.setString(2, "Désabonnement forcé de l'employé " + employee.getName());
            notifStmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur lors du désabonnement : " + e.getMessage());
        }
    }
} 