package Planning;


public class Joueur extends Personne{
    
    private int nbTour;
    private boolean qualifie;
    
    public Joueur(int numP, String nomP, String prenomP, String nationaliteP, int nbTourJ, boolean qualifieJ)
    {
        super(numP, nomP, prenomP, nationaliteP);
        this.nbTour = nbTourJ;
        this.qualifie = qualifieJ;
    }

    public int getNbTour() {
        return nbTour;
    }

    public boolean isQualifie() {
        return qualifie;
    }

    public void setNbTour(int nbTour) {
        this.nbTour = nbTour;
    }

    public void setQualifie(boolean qualifie) {
        this.qualifie = qualifie;
    }
    
    
 }
