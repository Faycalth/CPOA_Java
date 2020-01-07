package Planning;


public class ArbitreLigne extends Personne{
    
    private String categorie;

    public ArbitreLigne(String categorie, int numP, String nomP, String prenomP, String nationaliteP) {
        super(numP, nomP, prenomP, nationaliteP);
        this.categorie = categorie;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
    
    
    
    
    
}
