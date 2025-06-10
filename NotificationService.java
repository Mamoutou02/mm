import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implémentation du service de notification
 */
public class NotificationService implements INotificationService {
    private List<Employee> employees;
    private List<Admin> admins;
    private Map<String, User> usersByEmail;

    public NotificationService() {
        this.employees = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.usersByEmail = new HashMap<>();
    }

    /**
     * Ajoute un administrateur au système
     */
    public void addAdmin(Admin admin) {
        admins.add(admin);
        usersByEmail.put(admin.getEmail(), admin);
    }

    /**
     * Ajoute un employé au système
     */
    public void addEmployee(Employee employee) {
        employees.add(employee);
        usersByEmail.put(employee.getEmail(), employee);
    }

    /**
     * Authentifie un utilisateur
     * @return L'utilisateur authentifié ou null si échec
     */
    public User authenticate(String email, String password) {
        User user = usersByEmail.get(email);
        if (user != null && user.authenticate(email, password)) {
            return user;
        }
        return null;
    }

    @Override
    public void subscribe(IObserver observer) {
        if (observer instanceof Employee) {
            Employee employee = (Employee) observer;
            employee.setSubscribed(true);
            System.out.println(employee.getName() + " s'est abonné au service de notification");
        }
    }

    @Override
    public void unsubscribe(IObserver observer) {
        if (observer instanceof Employee) {
            Employee employee = (Employee) observer;
            employee.setSubscribed(false);
            System.out.println(employee.getName() + " s'est désabonné du service de notification");
        }
    }

    @Override
    public void notifyObservers(String message, String senderId) {
        User sender = null;
        
        // Recherche de l'expéditeur
        for (Admin admin : admins) {
            if (admin.getId().equals(senderId)) {
                sender = admin;
                admin.addSentNotification(message);
                break;
            }
        }
        if (sender == null) {
            for (Employee emp : employees) {
                if (emp.getId().equals(senderId)) {
                    sender = emp;
                    break;
                }
            }
        }

        final String senderName = sender != null ? sender.getName() : "Inconnu";

        // Envoi aux employés abonnés
        for (Employee employee : employees) {
            if (!employee.getId().equals(senderId) && employee.isSubscribed()) {
                employee.receiveNotification(message, senderName);
            }
        }

        // Envoi aux administrateurs (sauf si c'est l'expéditeur)
        for (Admin admin : admins) {
            if (!admin.getId().equals(senderId)) {
                admin.receiveNotification(message, senderName);
            }
        }
    }

    @Override
    public boolean isSubscribed(String observerId) {
        for (Employee employee : employees) {
            if (employee.getId().equals(observerId)) {
                return employee.isSubscribed();
            }
        }
        return false;
    }

    @Override
    public List<IObserver> getSubscribers() {
        List<IObserver> subscribers = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.isSubscribed()) {
                subscribers.add(employee);
            }
        }
        // Ajouter les administrateurs à la liste des abonnés
        subscribers.addAll(admins);
        return subscribers;
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }
} 