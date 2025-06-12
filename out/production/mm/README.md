# Application d'Envoi d'Emails

Une application Java permettant l'envoi d'emails via Gmail avec différents niveaux d'accès.

## Rôles Utilisateurs

### 👨‍💼 Administrateur

L'administrateur dispose des droits suivants :

1. **Gestion du Système**
   - Configuration des paramètres SMTP
   - Gestion des identifiants Gmail
   - Définition des règles de sécurité

2. **Supervision**
   - Consultation des logs d'envoi
   - Suivi des statistiques
   - Monitoring des erreurs

3. **Gestion des Utilisateurs**
   - Création des comptes employés
   - Attribution des permissions
   - Désactivation des comptes

### 👤 Employé

L'employé a accès aux fonctionnalités suivantes :

 **Gestion des Abonnements**
   - S'abonner aux notifications
   - Se désabonner des notifications
   - Consulté les message reçue de l'admin



## 🛠️ Prérequis d'Installation

### Configuration Système
- Java 8 ou supérieur
- Système d'exploitation : Windows, Linux ou MacOS
- Connexion Internet stable

### Dépendances Maven
```xml
<dependency>
    <groupId>com.sun.mail</groupId>
    <artifactId>jakarta.mail</artifactId>
    <version>2.0.1</version>
</dependency>
```

### Configuration Gmail
1. **Compte Gmail**
   - Créer un compte Gmail professionnel
   - Activer la validation en deux étapes

2. **Sécurité**
   - Générer un mot de passe d'application
   - Configurer les paramètres de sécurité Gmail

3. **Configuration Application**
   - Renseigner l'email dans la constante `EMAIL`
   - Ajouter le mot de passe d'application dans `PASSWORD`
   - Vérifier les paramètres SMTP

## ⚠️ Notes Importantes

- Ne jamais partager les identifiants Gmail
- Conserver le mot de passe d'application en sécurité
