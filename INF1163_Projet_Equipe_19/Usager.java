public class Usager{
    private String numCompte;
    private int NIP;
    

    public Usager(String numCompte, int NIP){
        this.numCompte = numCompte;
        this.NIP = NIP;
    }

    public String getNumCompte(){
        return this.numCompte;
    }

    public void setNumCompte(String numCompte){
        this.numCompte = numCompte;
    }

    public int getNIP(){
        return this.NIP;
    }

     public void setNIP(int NIP){
        this.NIP = NIP;
    }


   

}
