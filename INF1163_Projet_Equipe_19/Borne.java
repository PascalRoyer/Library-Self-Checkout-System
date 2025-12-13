import java.util.HashMap;

public class Borne{
    private HashMap<Integer, Livre> catalogue;
    private HashMap<String, Usager> listeUsager;
    private Emprunt session;
    private Usager Connexion;


    public Borne(){
        this.catalogue = new HashMap();
        this.listeUsager = new HashMap();
        this.session = null;

        /* Test de données
        Usager jean = new Usager("jean123", 1234);
        Usager jeanne = new Usager("jeanne456", 1234);
        this.listeUsager.put(jean.getNumCompte() , jean);
        this.listeUsager.put(jeanne.getNumCompte() , jeanne);

        Livre livre = new Livre(1001, "Le Petit Prince", "Antoine de Saint-Exupéry", 1943, 96, "Reynal & Hitchcock", false, null);
        this.catalogue.put(1001, livre);
        */
    }


    public void saisirExemplaire(int RFID){
        Livre l = catalogue.get(RFID);
        if (this.session == null){
            System.out.println("Erreur: Veuillez vous authentifier.");
            return;
        }

        if (l == null){
            System.out.println("Exemplaire non trouvé.");
        }
        else if (l.getEstEmprunte())
        {
            System.out.println("Le livre est déjà emprunté.");
        }
            else {
            l.setEstEmprunte(true);
            this.session.ajouterLivre(l);
            System.out.println("Le livre est ajouté à l'emprunt.");
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
                System.out.println("- " + l.getTitre() + " " + l.getDateRetour());
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
        
        if (typeRecu.equals("courriel")){
            System.out.println("Reçu envoyé par courriel.");
        }
        else if (typeRecu.equals("imprimé")){
            System.out.println("Impression du reçu.");
        }
        System.out.println("Merci et au revoir!");
        this.session = null;
        this.Connexion = null;
    }

    public void annulerSaisie(){
        if (this.session != null){
            for (Livre l : this.session.getLivres()){
                l.setEstEmprunte(false);
            }
        }

        this.session = null;
        this.Connexion = null;
       

        System.out.println("Session annulée. À Bientôt!");
    }
}

    
    


