package Planning;


public class ArbitreChaise extends Personne{
    
    private String Categorie;
    private int nbMatch;

    public ArbitreChaise(String Categorie, int numP, String nomP, String prenomP, String nationaliteP) {
        super(numP, nomP, prenomP, nationaliteP);
        this.Categorie = Categorie;
    }
    
    public int verifNbMatch()
    {
        return 0;
    }

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String Categorie) {
        this.Categorie = Categorie;
    }
       
}
