package Planning;


public class RamasseurDeBalle extends Personne {
    
    private int idRamasseurDeBalle;
    private EquipeRamasseur idEquipe;

    public RamasseurDeBalle(int idRamasseurDeBalle, int numP, String nomP, String prenomP, String nationaliteP) {
        super(numP, nomP, prenomP, nationaliteP);
        this.idRamasseurDeBalle = idRamasseurDeBalle;
    }

    public int getIdRamasseurDeBalle() {
        return idRamasseurDeBalle;
    }

    public EquipeRamasseur getIdEquipe() {
        return idEquipe;
    }

    public void setIdRamasseurDeBalle(int idRamasseurDeBalle) {
        this.idRamasseurDeBalle = idRamasseurDeBalle;
    }

    public void setIdEquipe(EquipeRamasseur idEquipe) {
        this.idEquipe = idEquipe;
    }
    
    
    
    
    
    
    
    
}
