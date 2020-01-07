
package Planning;


public class Match {
    
    private int numMatch;
    private String horaireMatch;
    private String typeMatch;
    private boolean finaleMatch;
    private int scoreGagnant;
    private int ScorePerdant;

    public Match(int numMatch, String horaireMatch, String typeMatch, boolean finaleMatch, int scoreGagnant, int ScorePerdant) {
        this.numMatch = numMatch;
        this.horaireMatch = horaireMatch;
        this.typeMatch = typeMatch;
        this.finaleMatch = finaleMatch;
        this.scoreGagnant = scoreGagnant;
        this.ScorePerdant = ScorePerdant;
    }

    public int verifNationalites(){
        return 0;
    }
    
    public int verifPresencePlanning(){
        return 0;    
    }
    
    public int getNumMatch() {
        return numMatch;
    }

    public String getHoraireMatch() {
        return horaireMatch;
    }

    public String getTypeMatch() {
        return typeMatch;
    }

    public boolean isFinaleMatch() {
        return finaleMatch;
    }

    public int getScoreGagnant() {
        return scoreGagnant;
    }

    public int getScorePerdant() {
        return ScorePerdant;
    }

    public void setNumMatch(int numMatch) {
        this.numMatch = numMatch;
    }

    public void setHoraireMatch(String horaireMatch) {
        this.horaireMatch = horaireMatch;
    }

    public void setTypeMatch(String typeMatch) {
        this.typeMatch = typeMatch;
    }

    public void setFinaleMatch(boolean finaleMatch) {
        this.finaleMatch = finaleMatch;
    }

    public void setScoreGagnant(int scoreGagnant) {
        this.scoreGagnant = scoreGagnant;
    }

    public void setScorePerdant(int ScorePerdant) {
        this.ScorePerdant = ScorePerdant;
    }
    
    
}
