/**
 * Interface définissant le contrat pour les observateurs (employés)
 */
public interface IObserver {
    /**
     * Méthode appelée lorsqu'une notification est reçue
     * @param message Le message de la notification
     * @param sender L'expéditeur du message
     */
    void receiveNotification(String message, String sender);
    
    /**
     * Retourne l'identifiant de l'employé
     * @return L'identifiant unique de l'employé
     */
    String getId();
    
    /**
     * Retourne le nom de l'employé
     * @return Le nom de l'employé
     */
    String getName();
} 