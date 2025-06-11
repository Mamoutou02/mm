/**
 * Interface définissant le contrat pour les observateurs (employés et admins)
 * Fait partie du pattern Observer pour la gestion des notifications
 * Les classes Employee et Admin implémentent cette interface
 */
public interface IObserver {
    /**
     * Méthode appelée lorsqu'une notification est reçue
     * Cette méthode est invoquée par le NotificationService pour chaque abonné
     * @param message Le contenu de la notification
     * @param sender Le nom de l'expéditeur (admin)
     */
    void receiveNotification(String message, String sender);
    
    /**
     * Retourne l'identifiant unique de l'observateur
     * Utilisé pour identifier de manière unique chaque employé/admin
     * @return L'identifiant unique
     */
    String getId();
    
    /**
     * Retourne le nom de l'observateur
     * Utilisé pour l'affichage dans les messages
     * @return Le nom de l'employé/admin
     */
    String getName();
} 