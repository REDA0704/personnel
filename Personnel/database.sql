CREATE TABLE LIGUE (
    num_ligue INT AUTO_INCREMENT PRIMARY KEY,
    nom varchar (100) NOT NULL
);

CREATE TABLE EMPLOYE (
    num_employe INT AUTO_INCREMENT PRIMARY KEY,
    nom varchar (100) NOT NULL,
    prenom varchar (100) NOT NULL,
    mail varchar (100) NOT NULL UNIQUE,
    password varchar (100) NOT NULL,
    date_arrivee DATE NOT NULL,
    date_depart DATE,
    num_ligue INT,

    FOREIGN KEY (num_ligue) REFERENCES LIGUE(num_ligue)
);

CREATE TABLE ADMINISTRER (
    num_ligue INT UNIQUE NOT NULL,
    num_employe INT UNIQUE NOT NULL,
    PRIMARY KEY (num_ligue, num_employe),
    FOREIGN KEY (num_ligue) REFERENCES LIGUE(num_ligue),
    FOREIGN KEY (num_employe) REFERENCES EMPLOYE(num_employe)
);