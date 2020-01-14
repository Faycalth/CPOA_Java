package planningDAO;
import interfaceDAO.IPlanningDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import Planning.RamasseurDeBalle;
import persistance.ConnexionBD;



public abstract class RamasseurDeBalleDAO implements IPlanningDAO {
private static Connection connexionBD;

public RamasseurDeBalleDAO (Connection conn) {
RamasseurDeBalleDAO.connexionBD = conn;
}
public List<RamasseurDeBalle> getLesRamasseurs() {
connexionBD = ConnexionBD.getConnection();
ResultSet rset = null;
Statement stmt = null;
List<RamasseurDeBalle> EquipeRamasseur = null;
String query = "SELECT numpersonne, nom, prenom, nationalite, idramasseurdeballe from ramasseurdeballe";
RamasseurDeBalle ram;
try {
stmt = connexionBD.createStatement();
EquipeRamasseur = new ArrayList<>();
rset = stmt.executeQuery(query);

while (rset.next()) {
// rappel du constructeur pour ne pas avoir d’erreur :
// public Article(int cle, String lib, String cat, String souscat, double prix, int qtte)
ram = new RamasseurDeBalle(rset.getInt(1), rset.getInt(2), rset.getString(3), rset.getString(4), rset.getString(5));
EquipeRamasseur.add(ram);
}
} catch (SQLException ex) {
Logger.getLogger( RamasseurDeBalleDAO.class.getName() ).log(Level.SEVERE, null, ex);
} finally {
try {
if (connexionBD != null) {
connexionBD.close();
}
} catch (SQLException ex) {
System.out.println(ex.getMessage());
}
}
return EquipeRamasseur;
}
public void insertRamasseur(RamasseurDeBalle a) {
connexionBD = ConnexionBD.getConnection();
PreparedStatement pstmt = null;
String sc = null;
String query = "INSERT INTO RamasseurDeBalle (numpersonne, nom, prenom, nationalite, idramasseurdeballe) VALUES (?,?,?,?,?)"; // 5 valeurs
try {
pstmt = connexionBD.prepareStatement(query);
pstmt.setInt(1, a.getNumPersonne());
pstmt.setString(2, a.getNom());
pstmt.setString(3, a.getPrenom());
pstmt.setString(4, a.getNationalite());
pstmt.setInt(5, a.getIdRamasseurDeBalle());
int n = pstmt.executeUpdate();
if (n == 1) {
System.out.println(" Insertion reussie d'un Article en BD ");
} else {
System.out.println(" *** Echec Insertion de l'Article en BD : cet ID existe déjà ");
}
} catch (SQLException ex) {
Logger.getLogger( RamasseurDeBalleDAO.class.getName() ).log(Level.SEVERE, null, ex);

} finally {
try {
if (connexionBD != null) {
connexionBD.close();
}
} catch (SQLException ex) {
System.out.println(ex.getMessage());
}
}
}
@Override
public void setConnection(Connection conn) {
RamasseurDeBalleDAO.connexionBD = conn;
}
/**
* Méthode qui enregistre une liste d'articles dans la BD d'un coup
* c'est quasiment le même code qu’insererArticle() mais avec une boucle sur le
* preparedStatement()
*/
public int setLesRamasseurs(List<RamasseurDeBalle> EquipeRamasseur) {
int nbRamasseurInseres = 0;
PreparedStatement pstmt = null;
String sc = null;
RamasseurDeBalle a = null;
String query = "INSERT INTO RamasseurDeBalle (numpersonne, nom, prenom, nationalite, idramasseurdeballe) VALUES (?,?,?,?,?)"; // 5 valeurs
int nbRamasseur = EquipeRamasseur.size() ;
try {
for (int i = 0; i < nbRamasseur; i++) {
a = EquipeRamasseur.get(i);
pstmt = connexionBD.prepareStatement(query);
pstmt.setInt(1, a.getNumPersonne());
pstmt.setString(2, a.getNom());
pstmt.setString(3, a.getPrenom());
pstmt.setString(4, a.getNationalite());
pstmt.setInt(5, a.getIdRamasseurDeBalle());
int n = pstmt.executeUpdate();
if (n == 1) {
nbRamasseurInseres++ ;
} else {
System.out.println(" *** Echec Insertion du ramasseur de balle en BD : cet ID existe déjà ");
}
}
} catch (SQLException e) {
System.out.print("setLesArticles() : problème avec la BD..." + e.getMessage());
Logger.getLogger( RamasseurDeBalleDAO.class.getName() ).log(Level.SEVERE, null, e);
} finally {
try {
if (connexionBD != null) {
connexionBD.close();
}
} catch (SQLException ex) {
System.out.println(ex.getMessage());
}
} 
return nbRamasseurInseres;

}
} 
