package Planning_matchs;

import java.util.List;

public class Arbitre
{
    private int numArbitre;
    private String nomArbitre;
    private String categorie;
    private String nationalite;
    
    public Arbitre(int numArbitre, String nomArbitre, String categorie, String nationalite)
    {
        this.categorie = categorie;
        this.nationalite = nationalite;
        this.nomArbitre = nomArbitre;
        this.numArbitre = numArbitre;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getNationalite() {
        return nationalite;
    }

    public String getNomArbitre() {
        return nomArbitre;
    }

    public int getNumArbitre() {
        return numArbitre;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public void setNomArbitre(String nomArbitre) {
        this.nomArbitre = nomArbitre;
    }

    public void setNumArbitre(int numArbitre) {
        this.numArbitre = numArbitre;
    }

    @Override
    public String toString() {
        return "Arbitre n°" + numArbitre;
    }
    
    
}
