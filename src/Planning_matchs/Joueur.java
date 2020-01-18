package Planning_matchs;

public class Joueur
{
    private int numJoueur;
    private String nomJoueur;
    private String nationalite;
    private boolean estElimine;
    private boolean qualifDirect;
    private String statut;

    public Joueur(int numJoueur, String nomJoueur, boolean estElimine, boolean qualifDirect, String statut)
    {
        this.numJoueur = numJoueur;
        this.nomJoueur = nomJoueur;
        this.estElimine = estElimine;
        this.qualifDirect = qualifDirect;
        this.statut = statut;
    }
    
    public Joueur(int numJoueur, String nomJoueur, String statut, String nationalite)
    {
        this.numJoueur = numJoueur;
        this.nomJoueur = nomJoueur;
        this.estElimine = false;
        this.qualifDirect = false;
        this.statut = statut;
        this.nationalite = nationalite;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    
    
    public String getNomJoueur()
    {
        return nomJoueur;
    }

    public int getNumJoueur() {
        return numJoueur;
    }

    public String getStatut() {
        return statut;
    }

    public void setEstElimine(boolean estElimine) {
        this.estElimine = estElimine;
    }

    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public void setNumJoueur(int numJoueur) {
        this.numJoueur = numJoueur;
    }

    public boolean isQualifDirect() {
        return qualifDirect;
    }

    public void setQualifDirect(boolean qualifDirect) {
        this.qualifDirect = qualifDirect;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    

    @Override
    public String toString()
    {
        return "Joueur n°" + numJoueur;
    }
   
}
