import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un administrateur du système
 */
public class Admin extends User implements IObserver {
    private Connection connection;

    public Admin(String id, String name, String email, String password) {
        super(id, name, email, password);
        this.connection = DatabaseConfig.getConnection();
    }

    /**
     * Affiche l'historique des notifications envoyées
     */
    public void showSentNotifications() {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                "SELECT message, date_envoi FROM notifications_envoyees " +
                "WHERE id_expediteur = ? " +
                "ORDER BY date_envoi DESC"
            );
            stmt.setString(1, this.getId());
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nHistorique des notifications envoyées par " + name + ":");
            boolean hasNotifications = false;
            while (rs.next()) {
                hasNotifications = true;
                String message = rs.getString("message");
                Timestamp sentDate = rs.getTimestamp("date_envoi");
                System.out.println("Le " + sentDate + " : " + message);
            }

            if (!hasNotifications) {
                System.out.println("Aucune notification envoyée");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique : " + e.getMessage());
        }
    }

    @Override
    public void receiveNotification(String message, String sender) {
        // Les administrateurs ne reçoivent plus de notifications
    }
} 