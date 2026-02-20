CREATE TABLE LIGUE (
    num_ligue INT AUTO_INCREMENT PRIMARY KEY,
    nom varchar (100) NOT NULL
);

CREATE TABLE EMPLOYE (
    num_employe INT AUTO_INCREMENT PRIMARY KEY,
    nom varchar (100) NOT NULL,
    prenom varchar (100) NOT NULL,
    mail varchar (255) NOT NULL UNIQUE,
    password varchar (255) NOT NULL,
    date_arrivee DATE NOT NULL,
    date_depart DATE,
    num_ligue INT,

    FOREIGN KEY (num_ligue) REFERENCES LIGUE(num_ligue)
);