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

public class JoueurDAO
{
    private DataSource ds;
    private Connection connexionBD;
    private List<Joueur> listeJoueur;
    private List<Joueur> listeJoueurDispo;
    private List<Joueur> listeJoueurPlace;
    private Joueur joueur;
    private Statement stmt;
    private Statement _stmt;
    private ResultSet resultat;
    private ResultSet _resultat;
    private ResultSet __resultat;
    private ResultSet ___resultat;
    private String ts;
    int i;
    
    public void getLesJoueurs(String jour, String mois, String annee, String heure, String minute)
    {
        try
        {
            ts = jour + "/" + mois + "/" + annee.substring(2) + " " + heure + ":" + minute;
            stmt = connexionBD.createStatement();
            _stmt = connexionBD.createStatement();
            resultat = stmt.executeQuery("select * from joueur where numjoueur in(select  numjoueur1 from (match join joueur a on numjoueur1 = a.numjoueur) join joueur b on numjoueur2 = b.numjoueur where datematch = '" + ts + "') order by numjoueur");
            listeJoueur = new ArrayList<>();
            i = _stmt.executeUpdate("delete from joueurplace"); //On supprime les anciens joueurs placés   
            while(resultat.next())
            {
                joueur = new Joueur(resultat.getInt(1), resultat.getString(2), resultat.getString(3), resultat.getString(4));        
                i = _stmt.executeUpdate("insert into joueurplace values(" + joueur.getNumJoueur() + ",'" + joueur.getNomJoueur() + "','" + joueur.getStatut() + "','" + joueur.getNationalite() + "')");
            }
            resultat.close();
            _resultat = stmt.executeQuery("select * from joueur where numjoueur in(select  numjoueur2 from (match join joueur a on numjoueur1 = a.numjoueur) join joueur b on numjoueur2 = b.numjoueur where datematch = '" + ts + "') order by numjoueur");
            while(_resultat.next())
            {
                joueur = new Joueur(_resultat.getInt(1), _resultat.getString(2), _resultat.getString(3), _resultat.getString(4));
                i = _stmt.executeUpdate("insert into joueurplace values(" + joueur.getNumJoueur() + ",'" + joueur.getNomJoueur() + "','" + joueur.getStatut() + "','" + joueur.getNationalite() + "')");
            }
            int l = _stmt.executeUpdate("commit");
            _resultat.close();
            __resultat = stmt.executeQuery("select * from joueurplace order by numjoueur");
            while(__resultat.next())
            {
                joueur = new Joueur(__resultat.getInt(1), __resultat.getString(2), __resultat.getString(3), __resultat.getString(4));
                if(!listeJoueur.contains(joueur))
                    listeJoueur.add(joueur);
            }
            __resultat.close();
            listeJoueurPlace = listeJoueur;
            listeJoueur.clear();
            ___resultat = stmt.executeQuery("select * from joueur where numjoueur not in(select numjoueur from joueurplace) order by numjoueur");
            while(___resultat.next())
            {
                joueur = new Joueur(___resultat.getInt(1), ___resultat.getString(2), ___resultat.getString(3), ___resultat.getString(4));
                if(!listeJoueur.contains(joueur))
                    listeJoueur.add(joueur);
            }
            ___resultat.close();
            listeJoueurDispo = listeJoueur;
        }
        catch(SQLException e)
        {
            System.out.println("Erreur getLesJoueurs() ! : " + e);
        }
       
    }
    
    public Joueur chercherJoueur(String _numJoueur, List<Joueur> listeJoueur)
    {
        for(Joueur joueur:listeJoueur)
        {
            if(_numJoueur.equals((joueur.toString())))
            {
                return joueur;
            }
        }
        return null;
    }

    public List<Joueur> getListeJoueur()
    {
        try
        {
            stmt = connexionBD.createStatement();
            resultat = stmt.executeQuery("select * from joueur");
            listeJoueur = new ArrayList<>();
            
            while(resultat.next())
            {
                joueur = new Joueur(resultat.getInt(1), resultat.getString(2), resultat.getString(3), resultat.getString(4));
                listeJoueur.add(joueur);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
        }
        return listeJoueur;
    }

    public List<Joueur> getListeJoueurDispo()
    {
        return listeJoueurDispo;
    }
    
    
 
    public List<Joueur> getListeJoueurPlace()
    {
        return listeJoueurPlace;
    }
    
    
    
    public void setDataSource(DataSource ds)
    {
        this.ds = ds;
    }

    public void setConnection(Connection c)
    {
        this.connexionBD = c;
    }
}
