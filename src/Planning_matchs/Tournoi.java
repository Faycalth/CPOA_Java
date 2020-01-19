package Planning_matchs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Tournoi
{
    private int numTournoi;
    private String typeTournoi;
    private List<Match> listeMatchs;
    private Timestamp dateTournoi;
    private List<Joueur> listeJoueurs;

    public Tournoi(int numTournoi, String typeTournoi, Timestamp dateTournoi, List<Joueur> listeJoueurs)
    {
        this.numTournoi = numTournoi;
        this.typeTournoi = typeTournoi;
        this.listeMatchs = new ArrayList<>();
        this.dateTournoi = dateTournoi;
        this.listeJoueurs = listeJoueurs;
    }
    
    public Tournoi(int numTournoi, String typeTournoi, Timestamp dateTournoi)
    {
        this.numTournoi = numTournoi;
        this.typeTournoi = typeTournoi;
        this.dateTournoi = dateTournoi;
        this.listeMatchs = new ArrayList<>();
    }

    public Timestamp getDateTournoi()
    {
        return dateTournoi;
    }

    public List<Joueur> getListeJoueurs()
    {
        return listeJoueurs;
    }

    public List<Match> getListeMatchs()
    {
        return listeMatchs;
    }

    public int getNumTournoi()
    {
        return numTournoi;
    }

    public String getTypeTournoi()
    {
        return typeTournoi;
    }

    public void setDateTournoi(Timestamp dateTournoi)
    {
        this.dateTournoi = dateTournoi;
    }

    public void setNumTournoi(int numTournoi)
    {
        this.numTournoi = numTournoi;
    }

    public void setListeJoueurs(List<Joueur> listeJoueurs)
    {
        this.listeJoueurs = listeJoueurs;
    }

    public void setTypeTournoi(String typeTournoi)
    {
        this.typeTournoi = typeTournoi;
    }

    public void setListeMatchs(List<Match> listeMatchs)
    {
        this.listeMatchs = listeMatchs;
    }   
}
