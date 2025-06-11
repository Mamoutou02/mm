import java.util.Scanner;
import java.util.List;

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
            showAdminMenu(admin, notificationService, scanner);
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

    private static void showAdminMenu(Admin admin, NotificationService notificationService, Scanner scanner) {
        while (true) {
            System.out.println("\nMenu Administrateur");
            System.out.println("1. Ajouter un employé");
            System.out.println("2. Voir la liste des employés");
            System.out.println("3. Désabonner un employé");
            System.out.println("4. Envoyer une notification");
            System.out.println("5. Voir l'historique des notifications envoyées");
            System.out.println("6. Quitter");
            System.out.print("Choix : ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addNewEmployee(notificationService);
                    break;
                case 2:
                    showEmployeeList(notificationService);
                    break;
                case 3:
                    System.out.print("Email de l'employé à désabonner : ");
                    String emailUnsubscribe = scanner.nextLine();
                    boolean found = false;
                    for (Employee emp : notificationService.getEmployees()) {
                        if (emp.getEmail().equals(emailUnsubscribe)) {
                            if (emp.isSubscribed()) {
                                notificationService.unsubscribe(emp);
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
                case 4:
                    System.out.print("Message : ");
                    String message = scanner.nextLine();
                    notificationService.notifyObservers(message, admin.getId());
                    break;
                case 5:
                    admin.showSentNotifications();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private static void showEmployeeList(NotificationService notificationService) {
        System.out.println("\nListe des employés :");
        List<Employee> employees = notificationService.getEmployees();
        if (employees.isEmpty()) {
            System.out.println("Aucun employé enregistré.");
        } else {
            for (Employee emp : employees) {
                System.out.println("- " + emp.getName() + " (" + emp.getEmail() + ") - " +
                        (emp.isSubscribed() ? "Abonné" : "Non abonné"));
            }
        }
    }

    private static void addNewEmployee(NotificationService notificationService) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nAjout d'un nouvel employé");
        
        String id;
        while (true) {
            System.out.print("ID (2 chiffres maximum) : ");
            String input = scanner.nextLine();
            if (input.matches("^\\d{1,2}$")) {
                id = "EMP" + input;
                break;
            } else {
                System.out.println("Erreur : Veuillez entrer un nombre entre 0 et 99");
            }
        }
        
        System.out.print("Nom : ");
        String name = scanner.nextLine();
        
        System.out.print("Email : ");
        String email = scanner.nextLine();
        
        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();
        
        try {
            // Créer et ajouter l'employé
            Employee newEmployee = new Employee(id, name, email, password);
            notificationService.addEmployee(newEmployee);
            System.out.println("Employé ajouté avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de l'employé : " + e.getMessage());
        }
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