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

public class ReservationDAO
{
    private DataSource ds;
    private Connection connexionBD;
    private List<Reservation> listeReservation;
    private Reservation reservation;
    private Statement stmt;
    private ResultSet resultat;
    
    public List<Reservation> getLesReservations()
    {
        try
        {
            stmt = connexionBD.createStatement();
            resultat = stmt.executeQuery("select numreservation, datereservation from reservation");
            listeReservation = new ArrayList<>();
            
            while(resultat.next())
            {
                reservation = new Reservation(resultat.getInt(1), resultat.getTimestamp(2));
                listeReservation.add(reservation);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Erreur ! : " + e);
        }
        return listeReservation;
    }
    
     public String afficherHeureReservation(Timestamp ts)
    {
        String minute, heure;
        if(ts.getHours() < 10)
            heure = "0" + Integer.toString(ts.getHours());
        else
            heure = Integer.toString(ts.getHours());
        if(ts.getMinutes()< 10)
            minute = "0" + Integer.toString(ts.getMinutes());
        else
            minute = Integer.toString(ts.getMinutes());
        return(heure + ":" + minute);
    }
    
    public String afficherDateReservation(Timestamp ts)
    {
        String jour, mois, annee;
        if(ts.getDate()< 10)
            jour = "0" + Integer.toString(ts.getDate());
        else
            jour = Integer.toString(ts.getDate());
        if(ts.getMonth()< 10)
            mois = "0" + Integer.toString(ts.getMonth());
        else
            mois = Integer.toString(ts.getMonth());
        if(ts.getYear()< 10)
            annee = "0" + Integer.toString(ts.getYear());
        else
            annee = Integer.toString(ts.getYear());
        return(jour + "/" + mois + "/" + annee);
    }
    
    public String afficherHorraireReservation(Timestamp ts)
    {
        return afficherDateReservation(ts) + " " + afficherHeureReservation(ts);
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
