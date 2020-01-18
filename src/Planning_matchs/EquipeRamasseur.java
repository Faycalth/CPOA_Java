package Planning_matchs;

import java.util.List;

public class EquipeRamasseur
{
    private int numEquipe;
    private List<Ramasseur> listeRamasseur;

    public EquipeRamasseur(int numEquipe)
    {
        this.numEquipe = numEquipe;
    }
    
    public void setListeRamasseur(List<Ramasseur> modListeRamasseur)
    {
        listeRamasseur = modListeRamasseur;
    }

    public List<EquipeRamasseur> getEquipeRamasseur()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
