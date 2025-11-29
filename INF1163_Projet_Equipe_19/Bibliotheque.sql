CREATE DATABASE bibliotheque;
USE bibliotheque;

# Cree l'inventaire de la bibliotheque 

CREATE TABLE Catalogue (
    RFID INT PRIMARY KEY,
    TITRE VARCHAR(50),
    AUTEUR VARCHAR(50),
    EDITION VARCHAR(50),
    Date_parution INT,
    Nombre_pages INT
    Disponiblite BOOLEAN DEFAULT TRUE 
);

# Inserer les livres de linventaire de la bibliotheque

INSERT INTO Catalogue (RFID, TITRE, AUTEUR, EDITION, Date_parution, Nombre_pages)
VALUES 
    (123456, 'La peste', 'Albert Camus', 'Gallimard', 1947, 336),
    (789012, 'Les misérables', 'Victor Hugo', 'Folio Junior', 2011, 291),
    (234567, 'L\'Étranger', 'Albert Camus', 'Gallimard', 1942, 186),
    (345678, 'Le Petit Prince', 'Antoine de Saint-Exupéry', 'Gallimard', 1943, 96),
    (456789, '1984', 'George Orwell', 'Folio', 1949, 432),
    (567890, 'Harry Potter à l\'école des sorciers', 'J.K. Rowling', 'Gallimard Jeunesse', 1997, 320),
    (678901, 'Le Seigneur des Anneaux', 'J.R.R. Tolkien', 'Christian Bourgois', 1954, 1216),
    (890123, 'Germinal', 'Émile Zola', 'Pocket', 1885, 592),
    (901234, 'Le Rouge et le Noir', 'Stendhal', 'Le Livre de Poche', 1830, 720),
    (112345, 'Madame Bovary', 'Gustave Flaubert', 'Gallimard', 1857, 464),
    (223456, 'Les Fleurs du mal', 'Charles Baudelaire', 'Pocket', 1857, 320),
    (334567, 'Vingt mille lieues sous les mers', 'Jules Verne', 'Le Livre de Poche', 1869, 544);

# Cree la base de donnee pour les utilisateurs

CREATE TABLE Utilisateurs (
    numero_compte VARCHAR(50) PRIMARY KEY,
    nip INT
);

# Inserer les  utilisateurs tests 
INSERT INTO Utilisateurs (numero_compte, nip)
VALUES 
    ('jean123', 1234),
    ('jeanne456', 1234);

# Cree la table demprunt

CREATE TABLE Emprunts (
    emprunt_id INT PRIMARY KEY AUTO_INCREMENT,
    numero_compte VARCHAR(50),
    RFID INT,
    date_emprunt DATE,
    date_retour_prevue DATE,
    date_retour_reelle DATE,
    FOREIGN KEY (numero_compte) REFERENCES Utilisateurs(numero_compte),
    FOREIGN KEY (RFID) REFERENCES Catalogue(RFID)
);

