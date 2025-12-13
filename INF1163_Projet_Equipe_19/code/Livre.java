public class Livre{
    private int RFID;
    private String titre;
    private String auteur;
    private int dateParu;
    private int nbPage;
    private String edition;
    private boolean estEmprunte;
    private String dateRetour;


    public Livre(int RFID, String titre, String auteur, int dateParu, int nbPage, String edition ,boolean estEmprunte, String dateRetour){
        this.RFID = RFID;
        this.titre = titre;
        this.auteur = auteur;
        this.dateParu = dateParu;
        this.nbPage = nbPage;
        this.edition = edition;
        this.estEmprunte = estEmprunte;
        this.dateRetour = dateRetour;
    }

    public int getRFID(){
        return this.RFID;
    }

    public void setRFID(int RFID){
        this.RFID = RFID;
    }

    public String getTitre(){
        return this.titre;
    }

    public void setTitre(String titre){
        this.titre = titre;
    }

    public String getAuteur(){
        return this.auteur;
    }

    public void setAuteur(String auteur){
        this.auteur = auteur;
    }

    public int getDateParu(){
        return this.dateParu;
    }

    public void setDateParu(int dateParu){
        this.dateParu = dateParu;
    }

    public int getNbPage(){
        return this.nbPage;
    }

    public void setNbPage(int nbPage){
        this.nbPage = nbPage;
    }

    public String getEdition(){
        return this.edition;
    }

    public void setEdition(String edition){
        this.edition = edition;
    }

    public boolean getEstEmprunte(){
        return this.estEmprunte;
    }

    public String getDateRetour(){
        return this.dateRetour;
    }

    public void setDateRetour(String dateRetour){
        this.dateRetour = dateRetour;
    }

    public void setEstEmprunte(boolean estEmprunte){
        this.estEmprunte = estEmprunte;
    }
}