package Planning;


abstract class Personne {
    
   private int numPersonne;
   private String nom;
   private String prenom;
   private String nationalite;
   
   
   public Personne(int numP, String nomP, String prenomP, String nationaliteP)
   {
       this.numPersonne = numP;
       this.nom = nomP;
       this.prenom = prenomP;
       this.nationalite = nationaliteP;            
   }

    public int getNumPersonne() {
        return numPersonne;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNumPersonne(int numPersonne) {
        this.numPersonne = numPersonne;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }
   
   
   
   
          
}
