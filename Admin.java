import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un administrateur du système
 */
public class Admin extends User implements IObserver {
    private List<String> sentNotifications;
    private List<String> receivedNotifications;

    public Admin(String id, String name, String email, String password) {
        super(id, name, email, password);
        this.sentNotifications = new ArrayList<>();
        this.receivedNotifications = new ArrayList<>();
    }

    /**
     * Ajoute une notification à l'historique des notifications envoyées
     * @param message Le message envoyé
     */
    public void addSentNotification(String message) {
        sentNotifications.add(message);
    }

    /**
     * Affiche l'historique des notifications envoyées
     */
    public void showSentNotifications() {
        System.out.println("\nHistorique des notifications envoyées par " + name + ":");
        if (sentNotifications.isEmpty()) {
            System.out.println("Aucune notification envoyée");
        } else {
            for (int i = 0; i < sentNotifications.size(); i++) {
                System.out.println((i + 1) + ". " + sentNotifications.get(i));
            }
        }
    }

    @Override
    public void receiveNotification(String message, String sender) {
        String notification = String.format("Message de %s : %s", sender, message);
        receivedNotifications.add(notification);
        System.out.println(String.format("[Admin %s] a reçu : %s", this.name, notification));
    }

    /**
     * Affiche l'historique des notifications reçues
     */
    public void showReceivedNotifications() {
        System.out.println("\nHistorique des notifications reçues par l'administrateur " + name + ":");
        if (receivedNotifications.isEmpty()) {
            System.out.println("Aucune notification reçue");
        } else {
            for (int i = 0; i < receivedNotifications.size(); i++) {
                System.out.println((i + 1) + ". " + receivedNotifications.get(i));
            }
        }
    }
} 