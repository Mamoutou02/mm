-- Création de la base de données
CREATE DATABASE IF NOT EXISTS db;
USE db;

-- Table des utilisateurs (commune pour admin et employés)
CREATE TABLE IF NOT EXISTS utilisateurs (
    id VARCHAR(50) PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    type_utilisateur ENUM('ADMIN', 'EMPLOYE') NOT NULL
);

-- Table des employés
CREATE TABLE IF NOT EXISTS employes (
    id_utilisateur VARCHAR(50) PRIMARY KEY,
    est_abonne BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateurs(id)
);

-- Table des notifications envoyées
CREATE TABLE IF NOT EXISTS notifications_envoyees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_expediteur VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    date_envoi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_expediteur) REFERENCES utilisateurs(id)
);

-- Table des notifications reçues par les employés
CREATE TABLE IF NOT EXISTS notifications_recues (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_notification INT NOT NULL,
    id_destinataire VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_notification) REFERENCES notifications_envoyees(id),
    FOREIGN KEY (id_destinataire) REFERENCES utilisateurs(id)
);

-- Insertion de l'administrateur par défaut (seulement s'il n'existe pas déjà)
INSERT IGNORE INTO utilisateurs (id, nom, email, mot_de_passe, type_utilisateur) 
VALUES ('admin1', 'Admin Principal', 'admin@gmail.com', 'admin123', 'ADMIN'); 