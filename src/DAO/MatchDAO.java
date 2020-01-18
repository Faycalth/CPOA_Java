package DAO;

import Planning_matchs.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import javax.swing.table.DefaultTableModel;

public class MatchDAO
{
    private DataSource ds;
    private Connection connexionBD;
    private List<Match> listeMatch;
    private Match match;
    private Statement stmt;
    private Statement _stmt;
    private Statement __stmt;
    private ResultSet resultat;
    
    public List<Match> getLesMatchsDispo()
    {
        try
        {
            stmt = connexionBD.createStatement();
            resultat = stmt.executeQuery("select nummatch, numtournoi, datematch from match where datematch is null order by nummatch");
            listeMatch = new ArrayList<>();
            
            while(resultat.next())
            {
                match = new Match(resultat.getInt(1), resultat.getInt(2), resultat.getTimestamp(3));
                listeMatch.add(match);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
        }
        return listeMatch;
    }
    
    public boolean MatchBienPlace(Match match, List<Match> listeMatchPlace, Court court)
    {
         try
        {
            stmt = connexionBD.createStatement();
            resultat = stmt.executeQuery("select nummatch, numcourt, datematch from match where datematch is not null and numcourt = " + court.getNumCourt() + " and nummatch <> " + match.getNumMatch() + " order by nummatch");           
            while(resultat.next())
            {
                if(match.getDateMatch().getTime() - resultat.getTimestamp(3).getTime() > -3600000 && match.getDateMatch().getTime() - resultat.getTimestamp(3).getTime() < 3600000)
                {
                    return false;
                }
            }
            return true;
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
            return false;
        }
         
    }
    
    public void setScore(Match match, String score)
    {
        try
        {
            stmt = connexionBD.createStatement();
            int i = stmt.executeUpdate("update match set score = '" + score + "' where nummatch = " + match.getNumMatch());
            i = stmt.executeUpdate("commit");
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
        }
    }
    
    public Match chercherMatch(String _match, List<Match> listeMatch)
    {
        for(Match match:listeMatch)
        {
            if(_match.equals(match.toString()))
            {
                return match;
            }
        }
        return null;
    }
    
    public boolean placerUnMatch(List<Match> listeMatchDispo, List<Match> listeMatchPlace, Match match, String jour, String mois, String annee, String heure, String minute, Joueur joueur1, Joueur joueur2, Court court)
    {
        try
        {
            stmt = connexionBD.createStatement();
            _stmt = connexionBD.createStatement();
            __stmt = connexionBD.createStatement();
            int l = stmt.executeUpdate("update match set datematch = '" + jour + "/" + mois + "/" + annee.substring(2) + " " + heure + ":" + minute + "'" + " where nummatch = " + Integer.toString(match.getNumMatch()));
            l = stmt.executeUpdate("commit");
            resultat = _stmt.executeQuery("select datematch from match where nummatch = " + match.getNumMatch());
            if(resultat.next())
            {
                match.setDateMatch(resultat.getTimestamp(1));
            }
            listeMatchDispo.remove(match);
            listeMatchPlace.add(match);
            l = __stmt.executeUpdate("update match set numjoueur1 = " + joueur1.getNumJoueur() + " where nummatch = " + match.getNumMatch());
            l = __stmt.executeUpdate("update match set numjoueur2 = " + joueur2.getNumJoueur() + " where nummatch = " + match.getNumMatch());
            l = __stmt.executeUpdate("update match set numcourt = " + court.getNumCourt() + " where nummatch = " + match.getNumMatch());
            l = __stmt.executeUpdate("commit");
            return true;
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
            return false;
        }
        
    }
    
    public boolean retirerMatch(List<Match> listeMatchDispo, List<Match> listeMatchPlace, Match match)
    {
        try
        {
            stmt = connexionBD.createStatement();
            int l = stmt.executeUpdate("update match set datematch = null where nummatch = " + match.getNumMatch());
            l = stmt.executeUpdate("update match set score = null where nummatch = " + match.getNumMatch());
            l = stmt.executeUpdate("update match set numjoueur1 = null where nummatch = " + match.getNumMatch());
            l = stmt.executeUpdate("update match set numjoueur2 = null where nummatch = " + match.getNumMatch());
            l = stmt.executeUpdate("update match set gagnant = null where nummatch = " + match.getNumMatch());
            l = stmt.executeUpdate("update match set perdant = null where nummatch = " + match.getNumMatch());
            l = stmt.executeUpdate("update match set numcourt = null where nummatch = " + match.getNumMatch());
            l = stmt.executeUpdate("commit");
            listeMatchPlace.remove(match);
            listeMatchDispo.add(match);
            return true;
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
            return false;
        }     
    }
    
    public List<Match> getLesMatchsPlace()
    {
        try
        {
            stmt = connexionBD.createStatement();
            resultat = stmt.executeQuery("select nummatch, numtournoi, datematch from match where datematch is not null order by nummatch");
            listeMatch = new ArrayList<>();
            
            while(resultat.next())
            {
                match = new Match(resultat.getInt(1), resultat.getInt(2), resultat.getTimestamp(3));
                listeMatch.add(match);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
        }
        return listeMatch;
    }
    
    public String afficherHeureMatch(Match match)
    {
        return match.getDateMatch().toString().substring(11, 16);
    }
    
    public String afficherDateMatch(Match match)
    {
        return match.getDateMatch().toString().substring(0, 10);
    }
    
    public String afficherHorraireMatch(Match match)
    {
        return afficherDateMatch(match) + "   à   " + afficherHeureMatch(match);
    }
    
    public void setDataSource(DataSource ds)
    {
        this.ds = ds;
    }

    public void setConnection(Connection c)
    {
        this.connexionBD = c;
    }

    public  DefaultTableModel updateRow()
    {
        try
        {
            stmt = connexionBD.createStatement(); // 2 conexions car la premiere se termine trop tot
            _stmt = connexionBD.createStatement();
            resultat = stmt.executeQuery("select nummatch, numjoueur1, a.nomjoueur, numjoueur2, b.nomjoueur, datematch, numcourt, score , a.nationalite, b.nationalite from (match join joueur a on numjoueur1 = a.numjoueur) join joueur b on numjoueur2 = b.numjoueur where datematch is not null order by nummatch");
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("N° Match");
            tableModel.addColumn("Nom du Match");
            tableModel.addColumn("Date du Match");
            tableModel.addColumn("N° Court");
            tableModel.addColumn("Score");
            while(resultat.next())// 1 seul résulat.next par stmt
            {
                Joueur joueur1 = new Joueur(resultat.getInt(2), resultat.getString(3), "", resultat.getString(9));
                Joueur joueur2 = new Joueur(resultat.getInt(4), resultat.getString(5), "", resultat.getString(10));
                Match matchs = new Match(resultat.getInt(1), 0, resultat.getTimestamp(6));//timestamp = DD/MM/YYYY HH:MM"
                matchs.setNomMatch(joueur1.getNomJoueur() + " VS " + joueur2.getNomJoueur());                   
                matchs.setScore(resultat.getString(8));
                if(matchs.getScore() != null)                 
                {
                    if(matchs.getScore().charAt(0) == '2')
                    {
                        matchs.setGagnant(joueur1);
                        matchs.setPerdant(joueur2);
                    }
                    else if(matchs.getScore().charAt(4) == '2')
                    {
                        matchs.setGagnant(joueur2);
                        matchs.setPerdant(joueur1);
                    }
                    _stmt.executeUpdate("update match set gagnant = '" + matchs.getGagnant().getNomJoueur() + "' where nummatch = " + matchs.getNumMatch());
                    _stmt.executeUpdate("update match set perdant = '" + matchs.getPerdant().getNomJoueur() + "' where nummatch = " + matchs.getNumMatch());
                    _stmt.executeUpdate("commit");
                }
                Court court = new Court(resultat.getInt(7));
                tableModel.addRow(new Object[]{matchs.toString(), matchs.getNom(), matchs.getDateMatch().toString().subSequence(0, 16), court.getNumCourt(), matchs.getScore()});
            }
            
            return tableModel;
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
        }
        return null;
    }


    
}
