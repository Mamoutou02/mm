# Application d'Envoi d'Emails

Une application Java permettant l'envoi d'emails via Gmail avec différents niveaux d'accès.

## Rôles Utilisateurs

### 👨‍💼 Administrateur

L'administrateur dispose des droits suivants :

1. **Gestion des Employés**
   - Ajouter un employé
   - Voir la liste des employés
   - Désabonner un employé

2. **Gestion des Notifications**
   - Envoyer une notification
   - Voir l'historique des notifications envoyées

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
