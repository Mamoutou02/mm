import java.util.Scanner;

/**
 * Classe principale pour tester le système de notification
 */
public class Main {
    private static NotificationService notificationService;
    private static Scanner scanner;

    public static void main(String[] args) {
        notificationService = new NotificationService();
        scanner = new Scanner(System.in);

        // Création d'un administrateur
        Admin admin = new Admin("admin1", "Admin Principal", "admin@gmail.com", "admin123");
        notificationService.addAdmin(admin);

        while (true) {
            System.out.println("\n=== Système de Notification ===");
            System.out.println("1. Connexion Administrateur");
            System.out.println("2. Connexion Employé");
            System.out.println("3. Quitter");
            System.out.print("Choix : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne

            switch (choice) {
                case 1:
                    handleAdminLogin();
                    break;
                case 2:
                    handleEmployeeLogin();
                    break;
                case 3:
                    System.out.println("Au revoir !");
                    scanner.close();
                    return;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private static void handleAdminLogin() {
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();

        User user = notificationService.authenticate(email, password);
        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            handleAdminMenu(admin);
        } else {
            System.out.println("Identifiants administrateur invalides !");
        }
    }

    private static void handleEmployeeLogin() {
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();

        User user = notificationService.authenticate(email, password);
        if (user instanceof Employee) {
            Employee employee = (Employee) user;
            handleEmployeeMenu(employee);
        } else {
            System.out.println("Identifiants employé invalides !");
        }
    }

    private static void handleAdminMenu(Admin admin) {
        while (true) {
            System.out.println("\n=== Menu Administrateur ===");
            System.out.println("1. Ajouter un employé");
            System.out.println("2. Lister les employés");
            System.out.println("3. Vérifier l'abonnement d'un employé");
            System.out.println("4. Désabonner un employé");
            System.out.println("5. Envoyer une notification");
            System.out.println("6. Voir l'historique des notifications envoyées");
            System.out.println("7. Déconnexion");
            System.out.print("Choix : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne

            switch (choice) {
                case 1:
                    addNewEmployee();
                    break;
                case 2:
                    System.out.println("\nListe des employés :");
                    for (Employee emp : notificationService.getEmployees()) {
                        System.out.println("- " + emp.getName() + " (" + emp.getEmail() + ") - " +
                                (emp.isSubscribed() ? "Abonné" : "Non abonné"));
                    }
                    break;
                case 3:
                    System.out.print("Email de l'employé : ");
                    String emailCheck = scanner.nextLine();
                    for (Employee emp : notificationService.getEmployees()) {
                        if (emp.getEmail().equals(emailCheck)) {
                            System.out.println(emp.getName() + " est " +
                                    (emp.isSubscribed() ? "abonné" : "non abonné"));
                            break;
                        }
                    }
                    break;
                case 4:
                    System.out.print("Email de l'employé à désabonner : ");
                    String emailUnsubscribe = scanner.nextLine();
                    boolean found = false;
                    for (Employee emp : notificationService.getEmployees()) {
                        if (emp.getEmail().equals(emailUnsubscribe)) {
                            if (emp.isSubscribed()) {
                                notificationService.adminUnsubscribeEmployee(admin, emp);
                                System.out.println("L'employé a été désabonné avec succès.");
                            } else {
                                System.out.println("Cet employé n'est pas abonné.");
                            }
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Aucun employé trouvé avec cet email.");
                    }
                    break;
                case 5:
                    System.out.print("Message : ");
                    String message = scanner.nextLine();
                    notificationService.notifyObservers(message, admin.getId());
                    System.out.println("Notification envoyée !");
                    break;
                case 6:
                    admin.showSentNotifications();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private static void addNewEmployee() {
        System.out.println("\n=== Ajout d'un nouvel employé ===");
        System.out.print("ID (numéro unique) : ");
        String id = scanner.nextLine();
        System.out.print("Nom : ");
        String name = scanner.nextLine();
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();

        Employee newEmployee = new Employee(id, name, email, password);
        notificationService.addEmployee(newEmployee);
        System.out.println("Employé ajouté avec succès !");
    }

    private static void handleEmployeeMenu(Employee employee) {
        while (true) {
            System.out.println("\n=== Menu Employé ===");
            System.out.println("1. S'abonner aux notifications");
            System.out.println("2. Se désabonner des notifications");
            System.out.println("3. Voir mes notifications");
            System.out.println("4. Déconnexion");
            System.out.print("Choix : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer le retour à la ligne

            switch (choice) {
                case 1:
                    notificationService.subscribe(employee);
                    break;
                case 2:
                    notificationService.unsubscribe(employee);
                    break;
                case 3:
                    employee.showNotificationHistory();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }
} 