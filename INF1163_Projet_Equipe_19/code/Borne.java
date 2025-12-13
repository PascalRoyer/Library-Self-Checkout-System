import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Borne{
    private HashMap<Integer, Livre> catalogue;
    private HashMap<String, Usager> listeUsager;
    private Emprunt session;
    private Usager Connexion;
    private Connection conn;

    public Borne(){
        this.catalogue = new HashMap();
        this.listeUsager = new HashMap();
        this.session = null;
        this.Connexion = null;

        try {
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite:bibliotheque.db");
            createTables();
            loadCatalogue();
            loadUsagers();
        } catch (Exception e) {
            System.out.println("Erreur de connexion à la base de données: " + e.getMessage());
        }
    }

    private void createTables() {
        try (Statement stmt = conn.createStatement()) {
            // Create Catalogue table
            String sql = "CREATE TABLE IF NOT EXISTS Catalogue (" +
                         "RFID INTEGER PRIMARY KEY," +
                         "TITRE TEXT," +
                         "AUTEUR TEXT," +
                         "EDITION TEXT," +
                         "Date_parution INTEGER," +
                         "Nombre_pages INTEGER," +
                         "Disponibilite BOOLEAN DEFAULT 1)";
            stmt.execute(sql);

            // Create Utilisateurs table
            sql = "CREATE TABLE IF NOT EXISTS Utilisateurs (" +
                  "numero_compte TEXT PRIMARY KEY," +
                  "nip INTEGER)";
            stmt.execute(sql);

            // Create Emprunts table
            sql = "CREATE TABLE IF NOT EXISTS Emprunts (" +
                  "emprunt_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                  "numero_compte TEXT," +
                  "RFID INTEGER," +
                  "date_emprunt TEXT," +
                  "date_retour_prevue TEXT," +
                  "date_retour_reelle TEXT," +
                  "FOREIGN KEY (numero_compte) REFERENCES Utilisateurs(numero_compte)," +
                  "FOREIGN KEY (RFID) REFERENCES Catalogue(RFID))";
            stmt.execute(sql);

            // Insert sample data if not exists
            insertSampleData();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création des tables: " + e.getMessage());
        }
    }

    private void insertSampleData() {
        try (Statement stmt = conn.createStatement()) {
            // Check if data exists
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Catalogue");
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Inserting sample data...");
                // Insert books
                String[] books = {
                    "INSERT INTO Catalogue (RFID, TITRE, AUTEUR, EDITION, Date_parution, Nombre_pages) VALUES (123456, 'La peste', 'Albert Camus', 'Gallimard', 1947, 336)",
                    "INSERT INTO Catalogue (RFID, TITRE, AUTEUR, EDITION, Date_parution, Nombre_pages) VALUES (789012, 'Les misérables', 'Victor Hugo', 'Folio Junior', 2011, 291)",
                    "INSERT INTO Catalogue (RFID, TITRE, AUTEUR, EDITION, Date_parution, Nombre_pages) VALUES (234567, 'L''Étranger', 'Albert Camus', 'Gallimard', 1942, 186)",
                    "INSERT INTO Catalogue (RFID, TITRE, AUTEUR, EDITION, Date_parution, Nombre_pages) VALUES (345678, 'Le Petit Prince', 'Antoine de Saint-Exupéry', 'Gallimard', 1943, 96)",
                    "INSERT INTO Catalogue (RFID, TITRE, AUTEUR, EDITION, Date_parution, Nombre_pages) VALUES (456789, '1984', 'George Orwell', 'Folio', 1949, 432)",
                    "INSERT INTO Catalogue (RFID, TITRE, AUTEUR, EDITION, Date_parution, Nombre_pages) VALUES (567890, 'Harry Potter à l''école des sorciers', 'J.K. Rowling', 'Gallimard Jeunesse', 1997, 320)",
                    "INSERT INTO Catalogue (RFID, TITRE, AUTEUR, EDITION, Date_parution, Nombre_pages) VALUES (678901, 'Le Seigneur des Anneaux', 'J.R.R. Tolkien', 'Christian Bourgois', 1954, 1216)",
                    "INSERT INTO Catalogue (RFID, TITRE, AUTEUR, EDITION, Date_parution, Nombre_pages) VALUES (890123, 'Germinal', 'Émile Zola', 'Pocket', 1885, 592)",
                    "INSERT INTO Catalogue (RFID, TITRE, AUTEUR, EDITION, Date_parution, Nombre_pages) VALUES (901234, 'Le Rouge et le Noir', 'Stendhal', 'Le Livre de Poche', 1830, 720)",
                    "INSERT INTO Catalogue (RFID, TITRE, AUTEUR, EDITION, Date_parution, Nombre_pages) VALUES (112345, 'Madame Bovary', 'Gustave Flaubert', 'Gallimard', 1857, 464)"
                };
                for (String book : books) {
                    stmt.execute(book);
                }

                // Insert users
                stmt.execute("INSERT INTO Utilisateurs (numero_compte, nip) VALUES ('jean123', 1234)");
                stmt.execute("INSERT INTO Utilisateurs (numero_compte, nip) VALUES ('jeanne456', 1234)");
                System.out.println("Sample data inserted.");
            } else {
                System.out.println("Sample data already exists.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion des données: " + e.getMessage());
        }
    }

    private void loadCatalogue() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Catalogue")) {
            while (rs.next()) {
                int rfid = rs.getInt("RFID");
                String titre = rs.getString("TITRE");
                String auteur = rs.getString("AUTEUR");
                String edition = rs.getString("EDITION");
                int dateParu = rs.getInt("Date_parution");
                int nbPage = rs.getInt("Nombre_pages");
                boolean dispo = rs.getBoolean("Disponibilite");
                Livre livre = new Livre(rfid, titre, auteur, dateParu, nbPage, edition, !dispo, null);
                catalogue.put(rfid, livre);
                System.out.println("Loaded book: " + titre);
            }
            System.out.println("Total books loaded: " + catalogue.size());
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement du catalogue: " + e.getMessage());
        }
    }

    private void loadUsagers() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Utilisateurs")) {
            while (rs.next()) {
                String numCompte = rs.getString("numero_compte");
                int nip = rs.getInt("nip");
                Usager usager = new Usager(numCompte, nip);
                listeUsager.put(numCompte, usager);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des usagers: " + e.getMessage());
        }
    }

    private void updateBookAvailability(int rfid, boolean available) {
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE Catalogue SET Disponibilite = ? WHERE RFID = ?")) {
            pstmt.setBoolean(1, available);
            pstmt.setInt(2, rfid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la disponibilité: " + e.getMessage());
        }
    }

    private void insertEmprunt(String numCompte, int rfid, String dateEmprunt, String dateRetourPrevue) {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Emprunts (numero_compte, RFID, date_emprunt, date_retour_prevue) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, numCompte);
            pstmt.setInt(2, rfid);
            pstmt.setString(3, dateEmprunt);
            pstmt.setString(4, dateRetourPrevue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de l'emprunt: " + e.getMessage());
        }
    }

    private void updateReturnDate(String numCompte, int rfid, String dateRetourReelle) {
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE Emprunts SET date_retour_reelle = ? WHERE numero_compte = ? AND RFID = ? AND date_retour_reelle IS NULL")) {
            pstmt.setString(1, dateRetourReelle);
            pstmt.setString(2, numCompte);
            pstmt.setInt(3, rfid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la date de retour: " + e.getMessage());
        }
    }

    public String getSessionSummary() {
        if (this.session == null || this.session.getLivres().isEmpty()) {
            return "Aucun livre sélectionné.";
        }
        StringBuilder sb = new StringBuilder("Livres sélectionnés:\n");
        for (Livre l : this.session.getLivres()) {
            sb.append("- Titre: ").append(l.getTitre())
              .append("\n  Auteur: ").append(l.getAuteur())
              .append("\n  Édition: ").append(l.getEdition())
              .append("\n  Année: ").append(l.getDateParu())
              .append("\n  Pages: ").append(l.getNbPage())
              .append("\n  RFID: ").append(l.getRFID())
              .append("\n  Date de retour: ").append(l.getDateRetour())
              .append("\n\n");
        }
        sb.append("Total: ").append(this.session.getLivres().size()).append(" livre(s)");
        return sb.toString();
    }

    public String getCatalogueSummary() {
        StringBuilder sb = new StringBuilder("Catalogue des livres:\n\n");
        sb.append(String.format("%-10s %-30s %-20s %-10s %-5s %-10s\n", "RFID", "Titre", "Auteur", "Année", "Pages", "Disponible"));
        sb.append("-".repeat(90) + "\n");
        for (Livre l : catalogue.values()) {
            String dispo = l.getEstEmprunte() ? "Non" : "Oui";
            sb.append(String.format("%-10d %-30s %-20s %-10d %-5d %-10s\n",
                l.getRFID(), l.getTitre().length() > 29 ? l.getTitre().substring(0, 26) + "..." : l.getTitre(),
                l.getAuteur().length() > 19 ? l.getAuteur().substring(0, 16) + "..." : l.getAuteur(),
                l.getDateParu(), l.getNbPage(), dispo));
        }
        return sb.toString();
    }

    public Map<Integer, Livre> getCatalogue() {
        return catalogue;
    }

    public List<String[]> getMesEmprunts() {
        List<String[]> emprunts = new ArrayList<>();
        if (this.Connexion == null) {
            return emprunts;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(
                "SELECT e.RFID, c.TITRE, c.AUTEUR, e.date_emprunt, e.date_retour_prevue " +
                "FROM Emprunts e JOIN Catalogue c ON e.RFID = c.RFID " +
                "WHERE e.numero_compte = ? AND e.date_retour_reelle IS NULL")) {
            pstmt.setString(1, this.Connexion.getNumCompte());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] emprunt = {
                    String.valueOf(rs.getInt("RFID")),
                    rs.getString("TITRE"),
                    rs.getString("AUTEUR"),
                    rs.getString("date_emprunt"),
                    rs.getString("date_retour_prevue")
                };
                emprunts.add(emprunt);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des emprunts: " + e.getMessage());
        }
        return emprunts;
    }


    public String saisirExemplaire(int RFID){
        Livre l = catalogue.get(RFID);
        if (this.session == null){
            return "Erreur: Veuillez vous authentifier.";
        }

        if (l == null){
            return "Exemplaire non trouvé.";
        }
        else if (l.getEstEmprunte())
        {
            return "Le livre est déjà emprunté.";
        }
        else {
            l.setEstEmprunte(true);
            this.session.ajouterLivre(l);
            // Do not update DB yet, wait for confirmation
            return "Le livre est ajouté à l'emprunt.";
        }
    }

    public void terminerSaisie(){
        if (this.session == null){
            System.out.println("Erreur: Veuillez vous authentifier.");
            return;
        }

        if (this.session.getLivres().isEmpty()){
            System.out.println("Aucun document saisi.");
        }
        else{
            System.out.println("Livres à empruntés:");
            for (Livre l : this.session.getLivres())
            {
                System.out.println("- " + l.getTitre() + " par " + l.getAuteur() + " (RFID: " + l.getRFID() + ") - Retour: " + l.getDateRetour());
            }
            System.out.println(" " + this.session.getLivres().size());
        }
    }
    public void validerCompte(String numCompte){
          Usager u = listeUsager.get(numCompte);
            if (u != null){
                this.Connexion = u;
                System.out.println("Succès! Veuillez entrer votre NIP.");
                
            }
            else{
                this.Connexion = null;
                System.out.println("Erreur: Mauvais Identifiant.");
            }
    }

    public void validerNIP(int NIP){
            if (this.Connexion != null){
                if (this.Connexion.getNIP() == NIP){
                    this.session = new Emprunt(this.Connexion);
                    System.out.println("Authentification réussie.");
                }
                else{
                System.out.println("Erreur: Mauvais NIP.");
                }
            }     
    }

    public void confirmerEmprunt(String typeRecu){
        if (this.session == null){
            System.out.println("Erreur: Veuillez vous authentifier.");
            return;
        }
        
        if (this.session.getLivres().isEmpty()){
            System.out.println("Aucun livre choisi. Veuillez entrer au moins un livre.");
            return;
        }
        
        try {
            conn.setAutoCommit(false); // Start transaction
            
            // Insert into Emprunts table and update availability
            LocalDate today = LocalDate.now();
            LocalDate returnDate = today.plusWeeks(2); // Assume 2 weeks loan
            for (Livre l : this.session.getLivres()) {
                insertEmprunt(this.Connexion.getNumCompte(), l.getRFID(), today.toString(), returnDate.toString());
                updateBookAvailability(l.getRFID(), false); // Now update DB
                l.setDateRetour(returnDate.toString());
            }
            
            conn.commit(); // Commit all changes at once
            
            if (typeRecu.equals("courriel")){
                System.out.println("Reçu envoyé par courriel.");
            }
            else if (typeRecu.equals("imprimé")){
                System.out.println("Impression du reçu.");
            }
            System.out.println("Merci et au revoir!");
            this.session = null;
            this.Connexion = null;
        } catch (SQLException e) {
            try {
                conn.rollback(); // Rollback on error
                System.out.println("Erreur lors de la confirmation: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                System.out.println("Erreur lors du rollback: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true); // Restore auto-commit
            } catch (SQLException e) {
                System.out.println("Erreur lors de la restauration de l'auto-commit: " + e.getMessage());
            }
        }
    }

    public boolean retournerLivre(int rfid) {
        if (this.Connexion == null) {
            System.out.println("Erreur: Veuillez vous authentifier.");
            return false;
        }
        Livre l = catalogue.get(rfid);
        if (l == null) {
            System.out.println("Exemplaire non trouvé.");
            return false;
        }
        System.out.println("Livre trouvé: " + l.getTitre() + ", estEmprunte: " + l.getEstEmprunte());
        if (!l.getEstEmprunte()) {
            System.out.println("Ce livre n'est pas emprunté.");
            return false;
        }
        // Check if borrowed by this user
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Emprunts WHERE numero_compte = ? AND RFID = ? AND date_retour_reelle IS NULL")) {
            pstmt.setString(1, this.Connexion.getNumCompte());
            pstmt.setInt(2, rfid);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Ce livre n'est pas emprunté par vous. NumCompte: " + this.Connexion.getNumCompte() + ", RFID: " + rfid);
                return false;
            }
            System.out.println("Emprunt trouvé pour cet utilisateur.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification: " + e.getMessage());
            return false;
        }
        
        // Update DB in transaction
        try {
            conn.setAutoCommit(false);
            l.setEstEmprunte(false);
            updateBookAvailability(rfid, true);
            updateReturnDate(this.Connexion.getNumCompte(), rfid, LocalDate.now().toString());
            conn.commit();
            System.out.println("Livre retourné: " + l.getTitre());
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Erreur lors du retour: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                System.out.println("Erreur lors du rollback: " + rollbackEx.getMessage());
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Erreur lors de la restauration de l'auto-commit: " + e.getMessage());
            }
        }
    }

    public void annulerSaisie(){
        if (this.session != null){
            for (Livre l : this.session.getLivres()){
                l.setEstEmprunte(false);
                // No DB update needed since it wasn't updated during scanning
            }
        }

        this.session = null;
        this.Connexion = null;
       

        System.out.println("Session annulée. À Bientôt!");
    }
}

    
    


