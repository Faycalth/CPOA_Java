package DAO;

import Planning_matchs.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class CourtDAO
{
    private DataSource ds;
    private Connection connexionBD;
    private List<Court> listeCourt;
    private Court court;
    private Statement stmt;
    private ResultSet resultat;
    
    public List<Court> getLesCours()
    {
        try
        {
            stmt = connexionBD.createStatement();
            resultat = stmt.executeQuery("select numcourt from court");
            listeCourt = new ArrayList<>();
            
            while(resultat.next())
            {
                court = new Court(resultat.getInt(1));
                listeCourt.add(court);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
        }
        return listeCourt;
    }
    
    public Court chercherCourt(String _court, List<Court> listeCourt)
    {
        for(Court court:listeCourt)
        {
            if(_court.equals(court.toString()))
            {
                return court;
            }
        }
        return null;
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
