package DAO;

import Planning_matchs.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class RamasseurDAO
{
    private DataSource ds;
    private Connection connexionBD;
    private List<Ramasseur> listeRamasseur;
    private Ramasseur ramasseur;
    private Statement stmt;
    private ResultSet resultat;
    
    public List<Ramasseur> getLesRamasseurs()
    {
        try
        {
            stmt = connexionBD.createStatement();
            resultat = stmt.executeQuery("select numramasseur,nom from ramasseur");
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
