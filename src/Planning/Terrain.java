package Planning;

public class Terrain {
    
    private int numTerrain;
    private int lieuTerrain;
    private int typeTerrain;
    private int nombrePlace;

    public Terrain() {
    }

    public Terrain(int numTerrain, int lieuTerrain, int typeTerrain, int nombrePlace) {
        this.numTerrain = numTerrain;
        this.lieuTerrain = lieuTerrain;
        this.typeTerrain = typeTerrain;
        this.nombrePlace = nombrePlace;
    }

    public int getNumTerrain() {
        return numTerrain;
    }

    public int getLieuTerrain() {
        return lieuTerrain;
    }

    public int getTypeTerrain() {
        return typeTerrain;
    }

    public int getNombrePlace() {
        return nombrePlace;
    }

    public void setNumTerrain(int numTerrain) {
        this.numTerrain = numTerrain;
    }

    public void setLieuTerrain(int lieuTerrain) {
        this.lieuTerrain = lieuTerrain;
    }

    public void setTypeTerrain(int typeTerrain) {
        this.typeTerrain = typeTerrain;
    }

    public void setNombrePlace(int nombrePlace) {
        this.nombrePlace = nombrePlace;
    }
    
    
}
