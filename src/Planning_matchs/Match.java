package Planning_matchs;

import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;

public class Match
{
    private int numMatch;
    private int numTournoi;
    private String nomMatch;
    private Timestamp dateMatch;
    private List<Arbitre> listeArbitreLigne;
    private List<Joueur> participantMatch;
    private Arbitre arbitreChaise;
    private Joueur gagnant;
    private Joueur perdant;
    private List<EquipeRamasseur> listeEquipeRamasseur;
    private String score;

    public Match(int numMatch, int numTournoi, String typeMatch, Timestamp dateMatch, Arbitre arbitreChaise, Joueur gagnant, Joueur perdant)
    {
        this.numMatch = numMatch;
        this.numTournoi = numTournoi;
        this.dateMatch = dateMatch;
        this.listeArbitreLigne = new ArrayList<>();
        this.participantMatch = new ArrayList<>();
        this.nomMatch = participantMatch.get(0).getNomJoueur() + " VS " + participantMatch.get(0).getNomJoueur();
        this.arbitreChaise = arbitreChaise;
    }
    
    public Match(int numMatch, int numTournoi, Timestamp dateMatch)
    {
        this.numMatch = numMatch;
        this.numTournoi = numTournoi;
        this.dateMatch = dateMatch;
        this.listeArbitreLigne = new ArrayList<>();
        this.participantMatch = new ArrayList<>();
        this.listeEquipeRamasseur = new ArrayList<>();
        this.score = "";
    }
    
    public void setGagnant(Joueur modGagnant)
    {
        gagnant = modGagnant;
    }
    
    
    
    public void setPerdant(Joueur modPerdant)
    {
        perdant = modPerdant;
    }

    public void ajouterListeArbitreLigne(Arbitre arbitre)
    {
        listeArbitreLigne.add(arbitre);
    }

    public void setArbitreChaise(Arbitre arbitre)
    {
        this.arbitreChaise = arbitre;
    }

    public String getScore() 
    {
        return score;
    }

    public void setNomMatch(String nomMatch)
    {
        this.nomMatch = nomMatch;
    }

    public void setScore(String score) 
    {
        this.score = score;
    }
    
    

    public String getNom()
    {
        return nomMatch;
    }

    public Timestamp getDateMatch()
    {
        return dateMatch;
    }

    public int getNumMatch()
    {
        return numMatch;
    }  

    public void setDateMatch(Timestamp dateMatch) {
        this.dateMatch = dateMatch;
    }
   
    public Joueur getGagnant()
    {
        return gagnant;
    }
    
    public Joueur getPerdant()
    {
        return perdant;
    }
    @Override
    public String toString()
    {
        return "match n°"+ Integer.toString(numMatch);
    }

    

    
}
