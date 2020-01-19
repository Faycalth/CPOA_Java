package DAO;

import Planning_matchs.Tournoi;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class TournoiDAO
{
    private DataSource ds;
    private Connection connexionBD;
    private List<Tournoi> listeTournoi;
    private Tournoi tournoi;
    private Statement stmt;
    private ResultSet resultat;
    
    public List<Tournoi> getLesTournoi()
    {
        try
        {
            stmt = connexionBD.createStatement();
            resultat = stmt.executeQuery("select * from tournoi");
            listeTournoi = new ArrayList<>();
            
            while(resultat.next())
            {
                tournoi = new Tournoi(resultat.getInt(1), resultat.getString(2), resultat.getTimestamp(3));
                listeTournoi.add(tournoi);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
        }
        return listeTournoi;
    }
}
