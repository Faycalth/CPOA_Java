package Planning;
import Planning.RamasseurDeBalle;
import java.sql.Connection;
import persistance.ConnexionBD;
import Planning.*;
import planningDAO.RamasseurDeBalleDAO;


public class Main {
    
    public static void main(String[] args) {
        
        RamasseurDeBalleDAO jeveuxsecher = null;
        
        System.out.println(jeveuxsecher.getLesRamasseurs());
        System.out.println("Drogue");
    }
}
