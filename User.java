/**
 * Classe abstraite représentant un utilisateur du système
 */
public abstract class User {
    protected String id;
    protected String name;
    protected String email;
    protected String password;

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Vérifie si les identifiants sont corrects
     * @param email Email de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @return true si les identifiants sont corrects, false sinon
     */
    public boolean authenticate(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }
} 