/**
 * Interface définissant le contrat pour le service de notification
 * Implémente le pattern Observer pour la gestion des notifications
 */
public interface INotificationService {
    /**
     * Ajoute un abonné au service de notification
     * Cette méthode est appelée quand un employé souhaite recevoir des notifications
     * @param observer L'employé à ajouter aux abonnés
     */
    void subscribe(IObserver observer);
    
    /**
     * Retire un abonné du service de notification
     * Cette méthode est appelée quand un employé ne souhaite plus recevoir de notifications
     * @param observer L'employé à retirer des abonnés
     */
    void unsubscribe(IObserver observer);
    
    /**
     * Envoie une notification à tous les abonnés
     * Seuls les administrateurs peuvent envoyer des notifications
     * @param message Le message à envoyer
     * @param senderId L'identifiant de l'expéditeur (doit être un admin)
     */
    void notifyObservers(String message, String senderId);
    
    /**
     * Vérifie si un employé est actuellement abonné
     * @param observerId L'identifiant de l'employé à vérifier
     * @return true si l'employé est abonné, false sinon
     */
    boolean isSubscribed(String observerId);
    
    /**
     * Retourne la liste de tous les abonnés actuels
     * @return La liste des employés actuellement abonnés aux notifications
     */
    java.util.List<IObserver> getSubscribers();
} 