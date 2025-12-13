import java.util.ArrayList;

public class Emprunt{
    private ArrayList<Livre> livresEmpruntes;
    private Usager utilisateur;
    private boolean statutEmprunt;

    Emprunt(Usager utilisateur){
        this.utilisateur = utilisateur;
        this.livresEmpruntes = new ArrayList<Livre>();
        this.statutEmprunt = true;
    }

    public void ajouterLivre(Livre livre){
        livresEmpruntes.add(livre);
    }

    public ArrayList<Livre> getLivres(){
        return this.livresEmpruntes;
    }
}
