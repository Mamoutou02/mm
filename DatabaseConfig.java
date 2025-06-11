import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe de configuration de la base de données
 * Gère la connexion unique à MySQL (Pattern Singleton pour la connexion)
 */
public class DatabaseConfig {
    // Configuration de la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Instance unique de la connexion (Pattern Singleton)
    private static Connection connection = null;

    /**
     * Bloc statique exécuté au chargement de la classe
     * Charge le driver MySQL une seule fois
     */
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("ERREUR: Driver MySQL introuvable!");
            e.printStackTrace();
        }
    }

    /**
     * Obtient une connexion à la base de données
     * Si la connexion n'existe pas, elle est créée (Pattern Singleton)
     * @return La connexion à la base de données
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                System.err.println("ERREUR: Connexion à la base de données impossible!");
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Ferme la connexion à la base de données
     * À appeler quand l'application se termine
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion.");
            }
        }
    }
} 