package Planning;
import Planning.RamasseurDeBalle;
import java.sql.Connection;
import persistance.ConnexionBD;


public class Main {
    
    private static Connection connexionBD;
    
    public static void main(String[] args) {
        
        connexionBD = ConnexionBD.getConnection();
    }
}
