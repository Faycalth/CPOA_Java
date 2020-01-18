package DAO;

import Planning_matchs.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class EquipeRamasseurDAO
{
    private DataSource ds;
    private Connection connexionBD;
    private List<EquipeRamasseur> listeEquipeRamasseur;
    private List<Ramasseur> listeRamasseur;
    private EquipeRamasseur equipeRamasseur;
    private Ramasseur ramasseur;
    private Statement stmt;
    private ResultSet resultat;
    
    public List<EquipeRamasseur> getLesEquipes()
    {
        try
        {
            stmt = connexionBD.createStatement();
            resultat = stmt.executeQuery("select numequipe from equiperamasseur");
            listeEquipeRamasseur = new ArrayList<>();
            
            while(resultat.next())
            {
                equipeRamasseur = new EquipeRamasseur(resultat.getInt(1));
                listeEquipeRamasseur.add(equipeRamasseur);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
        }
        return listeEquipeRamasseur;
    }
    
    public List<Ramasseur> remplirEquipe(int numEquipe) throws SQLException //equipe1.setListeRamasseur(remplirEquipe(1));
    {
        try
        {
            stmt = connexionBD.createStatement();
            resultat = stmt.executeQuery("select numramasseur,nom from ramasseur where numequipe = " + numEquipe);
            listeRamasseur = new ArrayList<>();
            while(resultat.next())
            {
                ramasseur = new Ramasseur(resultat.getInt(1), resultat.getString(2));
                listeRamasseur.add(ramasseur);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
        }
        return listeRamasseur;
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
