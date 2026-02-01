fichier_sql


- Script de création des tables



CREATE TABLE ligue (
    id_ligue INT AUTO_INCREMENT PRIMARY KEY,
    nom_ligue VARCHAR(100) NOT NULL
);

CREATE TABLE employe (
    id_employe INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    date_de_depart DATE,
    date_de_retour DATE,
    id_ligue INT,
    CONSTRAINT fk_employe_ligue
        FOREIGN KEY (id_ligue)
        REFERENCES ligue(id_ligue)
        ON DELETE SET NULL
);