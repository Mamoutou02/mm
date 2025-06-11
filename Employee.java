import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un employé
 */
public class Employee extends User implements IObserver {
    private boolean isSubscribed;
    private Connection connection;

    /**
     * Constructeur
     * @param id L'identifiant de l'employé
     * @param name Le nom de l'employé
     * @param email L'email de l'employé
     * @param password Le mot de passe de l'employé
     */

    public Employee(String id, String name, String email, String password) {
        super(id, name, email, password);
        this.isSubscribed = false;
        this.connection = DatabaseConfig.getConnection();
    }

    /**
     * @param message Le contenu de la notification
     * @param sender Le nom de l'expéditeur (admin)
     */
    @Override
    public void receiveNotification(String message, String sender) {
        if (isSubscribed) {
            System.out.println(String.format("[%s] a reçu : Message de %s : %s", this.name, sender, message));
        }
    }

    /**
     * Affiche l'historique des notifications reçues par l'employé
     */
    public void showNotificationHistory() {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT sn.message, sn.date_envoi, u.nom as sender_name " +
                "FROM notifications_recues rn " +
                "JOIN notifications_envoyees sn ON rn.id_notification = sn.id " +
                "JOIN utilisateurs u ON sn.id_expediteur = u.id " +
                "WHERE rn.id_destinataire = ? " +
                "ORDER BY sn.date_envoi DESC"
            );
            stmt.setString(1, this.getId());
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nHistorique des notifications pour " + name + ":");
            boolean hasNotifications = false;
            while (rs.next()) {
                hasNotifications = true;
                String message = rs.getString("message");
                Timestamp sentDate = rs.getTimestamp("date_envoi");
                String senderName = rs.getString("sender_name");
                System.out.println("Le " + sentDate + " de " + senderName + " : " + message);
            }

            if (!hasNotifications) {
                System.out.println("Aucune notification reçue");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique : " + e.getMessage());
        }
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
} 