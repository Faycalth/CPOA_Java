package interfaceDAO;
import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import Planning.RamasseurDeBalle;
import Planning.EquipeRamasseur;
/**
* Interface DAO pour un article
*/
public interface IPlanningDAO {
public List<RamasseurDeBalle> getEquipeRamasseur();

public void setDataSource(DataSource ds);
public void setConnection(Connection connexionBD);



/*public List<Article> findById(int refArticle);
public List<Article> findByLibelle();
public List<Article> getArticlesCategorie(String uneCategorie );
public int setLesArticles(List<Article> lesArticles);
public void insertArticle(Article a);
public boolean supprArticle(int refArticle);*/
}