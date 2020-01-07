package Planning;


public class CourtCentral extends Terrain{
    
    private int niveauTerrain;

    public CourtCentral(int niveauTerrain, int numTerrain, int lieuTerrain, int typeTerrain, int nombrePlace) {
        super(numTerrain, lieuTerrain, typeTerrain, nombrePlace);
        this.niveauTerrain = niveauTerrain;
    }

    public int getNiveauTerrain() {
        return niveauTerrain;
    }

    public void setNiveauTerrain(int niveauTerrain) {
        this.niveauTerrain = niveauTerrain;
    }
    
    
}
