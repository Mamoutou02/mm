/**
 * Interface définissant le contrat pour le service de notification
 */
public interface INotificationService {
    /**
     * Ajoute un abonné au service de notification
     * @param observer L'employé à ajouter
     */
    void subscribe(IObserver observer);
    
    /**
     * Retire un abonné du service de notification
     * @param observer L'employé à retirer
     */
    void unsubscribe(IObserver observer);
    
    /**
     * Envoie une notification à tous les abonnés
     * @param message Le message à envoyer
     * @param sender L'expéditeur du message
     */
    void notifyObservers(String message, String senderId);
    
    /**
     * Vérifie si un employé est abonné
     * @param observerId L'identifiant de l'employé
     * @return true si l'employé est abonné, false sinon
     */
    boolean isSubscribed(String observerId);
    
    /**
     * Retourne la liste des abonnés
     * @return La liste des abonnés
     */
    java.util.List<IObserver> getSubscribers();
} 