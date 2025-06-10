import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un employé
 */
public class Employee extends User implements IObserver {
    private List<String> notifications;
    private boolean isSubscribed;

    /**
     * Constructeur
     * @param id L'identifiant de l'employé
     * @param name Le nom de l'employé
     * @param email L'email de l'employé
     * @param password Le mot de passe de l'employé
     */
    public Employee(String id, String name, String email, String password) {
        super(id, name, email, password);
        this.notifications = new ArrayList<>();
        this.isSubscribed = false;
    }

    @Override
    public void receiveNotification(String message, String sender) {
        if (isSubscribed) {
            String notification = String.format("Message de %s : %s", sender, message);
            notifications.add(notification);
            System.out.println(String.format("[%s] a reçu : %s", this.name, notification));
        }
    }

    /**
     * Affiche l'historique des notifications reçues par l'employé
     */
    public void showNotificationHistory() {
        System.out.println("\nHistorique des notifications pour " + name + ":");
        if (notifications.isEmpty()) {
            System.out.println("Aucune notification reçue");
        } else {
            for (int i = 0; i < notifications.size(); i++) {
                System.out.println((i + 1) + ". " + notifications.get(i));
            }
        }
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
} 