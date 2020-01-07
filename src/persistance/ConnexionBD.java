package persistance;
import java.sql.*;
public class ConnexionBD {

private Connection conn; // l’instance en champ privé
/**
* Méthode qui va retourner l’instance de connexion ou la créer si elle n'existe pas
* @return Connection
*/
public Connection getConnection() throws SQLException {
//On teste si la connexion n’est pas deja ouverte
if (conn == null) {
// on cree la connexion
try {
// Connexion à MariaDB de l’IUT (OK juin 2018) :
DriverManager.getConnection ("jdbc:mysql://iutdoua-web.univ-lyon1.fr:3306/p1806102", // ici nom de votre base
"p1806102", "369044"); // ici vos login et pwd
System.out.println("==> connexion à MariaDB effectuee !");
// (c’est bien la BD MariaDB mais les processus sont encore appelés mySQL dans la version APACHE de l’IUT…)
} catch (Exception e) {
System.out.println("**** La connexion BD a echoue....");
e.printStackTrace();
}
}
// si la connexion existe, on la renvoie
return conn;
}
}