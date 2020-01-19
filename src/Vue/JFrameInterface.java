package Vue;

import DAO.ArbitreDAO;
import DAO.CourtDAO;
import DAO.EquipeRamasseurDAO;
import DAO.JoueurDAO;
import DAO.MatchDAO;
import DAO.RamasseurDAO;
import DAO.ReservationDAO;
import persistance.OracleDataSourceDAO;
import Planning_matchs.Arbitre;
import Planning_matchs.Court;
import Planning_matchs.EquipeRamasseur;
import Planning_matchs.Joueur;
import Planning_matchs.Match;
import Planning_matchs.Ramasseur;
import Planning_matchs.Reservation;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
public class JFrameInterface extends javax.swing.JFrame {

     private static OracleDataSourceDAO ods;
    private static Connection c;
    private static ArbitreDAO arbitre;
    private static CourtDAO court;
    private static EquipeRamasseurDAO equipe;
    private static JoueurDAO joueur;
    private static MatchDAO match;
    private static RamasseurDAO ramasseur;
    private static ReservationDAO reservation;
    private static List<Arbitre> listeArbitreChaise;
    private static List<Arbitre> listeArbitreLigne;
    private static List<Arbitre> listeArbitre;
    private static List<Court> listeCourt;
    private static List<EquipeRamasseur> listeEquipeRamasseur;
    private static List<Joueur> listeJoueurDispo;
    private static List<Joueur> listeJoueurPlace;
    private static List<Joueur> listeJoueur;
    private static List<Match> listeMatchDispo;
    private static List<Match> listeMatchPlace;
    private static List<Ramasseur> listeRamasseur;
    private static List<Reservation> listeReservation;
    private static DefaultTableModel tableModelPlanning;
    private static DefaultTableModel tableModelArbitreLigne;
    
    public JFrameInterface() {
       initComponents();
        arbitre = new ArbitreDAO();
        court = new CourtDAO();
        equipe = new EquipeRamasseurDAO();
        joueur = new JoueurDAO();
        match = new MatchDAO();
        ramasseur = new RamasseurDAO();
        reservation = new ReservationDAO();
        listeArbitreChaise = new ArrayList<>();
        listeArbitreLigne = new ArrayList<>();
        listeArbitre = new ArrayList<>();
        listeCourt = new ArrayList<>();
        listeEquipeRamasseur = new ArrayList<>();
        listeJoueur = new ArrayList<>();
        listeJoueurDispo = new ArrayList<>();
        listeJoueurPlace = new ArrayList<>();
        listeMatchDispo = new ArrayList<>();
        listeMatchPlace = new ArrayList<>();
        listeRamasseur = new ArrayList<>();
        listeReservation = new ArrayList<>();
        ods = OracleDataSourceDAO.getOracleDataSource();
        try
        {
            c = ods.getConnection();
            arbitre.setConnection(c);
            arbitre.setDataSource(ods);
            court.setConnection(c);
            court.setDataSource(ods);
            equipe.setConnection(c);
            equipe.setDataSource(ods);
            joueur.setConnection(c);
            joueur.setDataSource(ods);
            match.setConnection(c);
            match.setDataSource(ods);
            ramasseur.setConnection(c);
            ramasseur.setDataSource(ods);
            reservation.setConnection(c);
            reservation.setDataSource(ods);         
        }
        catch(SQLException e)
        {
            System.out.println("Erreur !! " + e);
        }
        listeArbitre = arbitre.getLesArbitres();
        listeCourt = court.getLesCours();
        listeEquipeRamasseur = equipe.getLesEquipes();
        listeMatchPlace = match.getLesMatchsPlace();
        listeRamasseur = ramasseur.getLesRamasseurs();
        listeReservation = reservation.getLesReservations();
        listeJoueur = joueur.getListeJoueur();
        jPanelConfirmer.setVisible(false);
        jPanelPlanning.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelRamasseur.setVisible(false);
        jPanelArbitre.setVisible(false);
        jPanelAccueil.setVisible(true);
        jPanelAjouterMatch.setVisible(false);
        refreshListeMatch();
        jComboBoxMinute.setEnabled(false);
        refreshListeJoueur();
        for(Court c:listeCourt)
        {
            jComboBoxNumeroCourt.addItem(c.toString());
        }
        tableModelPlanning  = new DefaultTableModel();
    }

    
    private void updatePlanning() 
    {
        tableModelPlanning = match.updateRow();
        jTablePlanning.setModel(tableModelPlanning);
    }
    
    private void refreshArbitreLigne()
    {
        jComboBoxArbitreLigne.removeAllItems();
        
    }
    
    private void refreshArbitreChaise()
    {
        jComboBoxArbitreChaise.removeAllItems();
        
    }
    
    private void refreshListeMatch()
    {
        jComboBoxNumeroMatch.removeAllItems();
        listeMatchDispo = match.getLesMatchsDispo();
        for(Match m:listeMatchDispo)
        {
            jComboBoxNumeroMatch.addItem(m.toString());
        }
    }
    private void refreshArbitre()
    {
        jComboBoxArbitreLigne.removeAllItems();
        jComboBoxArbitreChaise.removeAllItems();
        listeArbitreLigne = arbitre.getLesArbitresLigne(match.chercherMatch(jComboBoxNumeroMatch.getSelectedItem().toString(), listeMatchPlace), joueur.chercherJoueur(jComboBoxNumeroJoueur1.getSelectedItem().toString(), listeJoueur), joueur.chercherJoueur(jComboBoxNumeroJoueur2.getSelectedItem().toString(), listeJoueur));
        listeArbitreChaise = arbitre.getLesArbitresChaise(match.chercherMatch(jComboBoxNumeroMatch.getSelectedItem().toString(), listeMatchPlace), joueur.chercherJoueur(jComboBoxNumeroJoueur1.getSelectedItem().toString(), listeJoueur), joueur.chercherJoueur(jComboBoxNumeroJoueur2.getSelectedItem().toString(), listeJoueur));
        for(Arbitre a:listeArbitreLigne)
        {
            jComboBoxArbitreLigne.addItem(a.toString());
        }
        for(Arbitre a:listeArbitreChaise)
        {
            jComboBoxArbitreChaise.addItem(a.toString());
        }
    }
    
    private void refreshListeJoueur()
    {
        recupListeJoueur();
        jComboBoxNumeroJoueur1.removeAllItems();
        for(Joueur j:listeJoueurDispo)
        {         
            jComboBoxNumeroJoueur1.addItem(j.toString());
        }
        jComboBoxNumeroJoueur2.removeAllItems();
        for(Joueur j:listeJoueurDispo)
        {
            if(!j.toString().equals(jComboBoxNumeroJoueur1.getSelectedItem().toString()))
                jComboBoxNumeroJoueur2.addItem(j.toString());
        }
    }
    
    private void recupListeJoueur()
    {
        joueur.getLesJoueurs(jComboBoxJour.getSelectedItem().toString(), jComboBoxMois.getSelectedItem().toString(), jComboBoxAnnee.getSelectedItem().toString(), jComboBoxHeure.getSelectedItem().toString(), jComboBoxMinute.getSelectedItem().toString());
        listeJoueurPlace = joueur.getListeJoueurPlace();
        listeJoueurDispo = joueur.getListeJoueurDispo();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelScore = new javax.swing.JPanel();
        jPanelErreurScore = new javax.swing.JPanel();
        jLabelErreurScore = new javax.swing.JLabel();
        jPanelCadreScore = new javax.swing.JPanel();
        jButtonValiderScore = new javax.swing.JButton();
        jLabelJoueur1Et2Score = new javax.swing.JLabel();
        jComboBoxScoreJoueur2 = new javax.swing.JComboBox<String>();
        jComboBoxScoreJoueur1 = new javax.swing.JComboBox<String>();
        jPanelHorizontalScore = new javax.swing.JPanel();
        jLabelScore = new javax.swing.JLabel();
        jPanelVerticalScore = new javax.swing.JPanel();
        jPanelConteneurScore = new javax.swing.JPanel();
        jButtonAccueilScore = new javax.swing.JButton();
        jPanelConteneurScore2 = new javax.swing.JPanel();
        jButtonPlanningScore = new javax.swing.JButton();
        jPanelOpenTennisScore = new javax.swing.JPanel();
        jLabelOpenTennisScore = new javax.swing.JLabel();
        jPanelConfirmer = new javax.swing.JPanel();
        jPanelVertical = new javax.swing.JPanel();
        jPanelConteneurConfirmation = new javax.swing.JPanel();
        jButtonAccueilConfirmation = new javax.swing.JButton();
        jPanelOpenTennisConfirmation = new javax.swing.JPanel();
        jLabelOpenTennisConfirmation = new javax.swing.JLabel();
        jPanelHorizontal = new javax.swing.JPanel();
        jLabelConfirmation = new javax.swing.JLabel();
        jPanelCadreConfirmation = new javax.swing.JPanel();
        jButtonConfirmer = new javax.swing.JButton();
        jLabelPhraseConfirmation = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanelArbitre = new javax.swing.JPanel();
        jPanelVerticalArbitre = new javax.swing.JPanel();
        jPanelConteneurArbitre = new javax.swing.JPanel();
        jButtonAccueilArbitre = new javax.swing.JButton();
        jPanelOpenTennisArbitre = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanelHorizontalArbitre = new javax.swing.JPanel();
        jLabelAjouterArbitre = new javax.swing.JLabel();
        jScrollPaneArbitre = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabelSuivantArbitre = new javax.swing.JButton();
        jPanelCadreArbitre = new javax.swing.JPanel();
        jLabelArbitreChaise = new javax.swing.JLabel();
        jLabelArbitreLigne = new javax.swing.JLabel();
        jComboBoxArbitreLigne = new javax.swing.JComboBox<String>();
        jComboBoxArbitreChaise = new javax.swing.JComboBox<String>();
        jButtonAjouterArbitre = new javax.swing.JButton();
        jPanelRamasseur = new javax.swing.JPanel();
        jPanelVerticalAjouterRamasseur = new javax.swing.JPanel();
        jPanelConteneurRamasseur = new javax.swing.JPanel();
        jButtonAccueilRamasseur = new javax.swing.JButton();
        jPanelOpenTennisRamasseur = new javax.swing.JPanel();
        jLabelOpenTennisRamasseur = new javax.swing.JLabel();
        jButtonSuivantRamasseur = new javax.swing.JButton();
        jPanelHorizontalRmasseur = new javax.swing.JPanel();
        jLabeAjouterRamasseurs = new javax.swing.JLabel();
        jPanelAjouterMatch = new javax.swing.JPanel();
        jButtonSuivantAjouterMatch = new javax.swing.JButton();
        jPanelHorizontalAjouterMatch = new javax.swing.JPanel();
        jLabelAjouterMatch = new javax.swing.JLabel();
        jPanelCadreAjouterMatch = new javax.swing.JPanel();
        jLabelJoueur1 = new javax.swing.JLabel();
        jComboBoxNumeroJoueur1 = new javax.swing.JComboBox<String>();
        jComboBoxNumeroJoueur2 = new javax.swing.JComboBox<String>();
        jLabelJoueur2 = new javax.swing.JLabel();
        jComboBoxHeure = new javax.swing.JComboBox<String>();
        jComboBoxMinute = new javax.swing.JComboBox<String>();
        jLabelHeureSepare = new javax.swing.JLabel();
        jLabelCourt = new javax.swing.JLabel();
        jComboBoxNumeroCourt = new javax.swing.JComboBox();
        jLabelHeure = new javax.swing.JLabel();
        jLabelDate = new javax.swing.JLabel();
        jLabelNumeroMatch = new javax.swing.JLabel();
        jComboBoxNumeroMatch = new javax.swing.JComboBox<String>();
        jComboBoxJour = new javax.swing.JComboBox<String>();
        jLabelDateSeparateur = new javax.swing.JLabel();
        jComboBoxMois = new javax.swing.JComboBox<String>();
        jLabelDateSeparateur2 = new javax.swing.JLabel();
        jComboBoxAnnee = new javax.swing.JComboBox<String>();
        jPanelVerticalAjouterMatch = new javax.swing.JPanel();
        jPanelOpenTennisAjouterMatch = new javax.swing.JPanel();
        jLabelOpenTennisAjouterMatch = new javax.swing.JLabel();
        jPanelConteneurAjouterMatch = new javax.swing.JPanel();
        jButtonAccueilPlanning2 = new javax.swing.JButton();
        jPanelConteneurAjouterMatch2 = new javax.swing.JPanel();
        jPlanningAjouterMatch = new javax.swing.JButton();
        jPanelPlanning = new javax.swing.JPanel();
        jScrollPanePlanning = new javax.swing.JScrollPane();
        jTablePlanning = new javax.swing.JTable();
        jPanelVerticalPlanning = new javax.swing.JPanel();
        jPanelConteneur = new javax.swing.JPanel();
        jButtonPlanningAccueil = new javax.swing.JButton();
        jPanelConteneur2 = new javax.swing.JPanel();
        jButtonAjouterMatchPlanning = new javax.swing.JButton();
        jPanelOpenTennisPlanning = new javax.swing.JPanel();
        jLabelOpenTennisPlanning = new javax.swing.JLabel();
        jButtonSupprimerMatch = new javax.swing.JButton();
        jButtonAjouterScore = new javax.swing.JButton();
        jPanelHorizontalPlanning = new javax.swing.JPanel();
        jLabelPlanning = new javax.swing.JLabel();
        jPanelErreur = new javax.swing.JPanel();
        jLabelErreurPlanning = new javax.swing.JLabel();
        jPanelAccueil = new javax.swing.JPanel();
        jPanelVerticalAccueil = new javax.swing.JPanel();
        jPanelOpenTennisAccueil = new javax.swing.JPanel();
        jLabelOpenTennisAccueil = new javax.swing.JLabel();
        jPanelConteneurAccueil = new javax.swing.JPanel();
        jButtonAccueilPlanning = new javax.swing.JButton();
        jPanelConteneurAccueil2 = new javax.swing.JPanel();
        jButtonAccueilAjouterMatch = new javax.swing.JButton();
        jPanelHorizontalAccueil = new javax.swing.JPanel();
        jLabelAccueil = new javax.swing.JLabel();
        jLabelPhraseAccueil = new javax.swing.JLabel();
        jLabelPhraseAccueil2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelScore.setPreferredSize(new java.awt.Dimension(1055, 495));

        javax.swing.GroupLayout jPanelErreurScoreLayout = new javax.swing.GroupLayout(jPanelErreurScore);
        jPanelErreurScore.setLayout(jPanelErreurScoreLayout);
        jPanelErreurScoreLayout.setHorizontalGroup(
            jPanelErreurScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 735, Short.MAX_VALUE)
            .addGroup(jPanelErreurScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelErreurScoreLayout.createSequentialGroup()
                    .addGap(367, 367, 367)
                    .addComponent(jLabelErreurScore)
                    .addContainerGap(368, Short.MAX_VALUE)))
        );
        jPanelErreurScoreLayout.setVerticalGroup(
            jPanelErreurScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
            .addGroup(jPanelErreurScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelErreurScoreLayout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addComponent(jLabelErreurScore)
                    .addContainerGap(28, Short.MAX_VALUE)))
        );

        jPanelCadreScore.setBackground(new java.awt.Color(85, 65, 118));

        jButtonValiderScore.setText("Valider");
        jButtonValiderScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonValiderScoreActionPerformed(evt);
            }
        });

        jLabelJoueur1Et2Score.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelJoueur1Et2Score.setForeground(new java.awt.Color(240, 240, 240));
        jLabelJoueur1Et2Score.setText("Joueur 1   -   Joueur 2");

        jComboBoxScoreJoueur2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2" }));
        jComboBoxScoreJoueur2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxScoreJoueur2ActionPerformed(evt);
            }
        });

        jComboBoxScoreJoueur1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2" }));
        jComboBoxScoreJoueur1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxScoreJoueur1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCadreScoreLayout = new javax.swing.GroupLayout(jPanelCadreScore);
        jPanelCadreScore.setLayout(jPanelCadreScoreLayout);
        jPanelCadreScoreLayout.setHorizontalGroup(
            jPanelCadreScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCadreScoreLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonValiderScore)
                .addGap(25, 25, 25))
            .addGroup(jPanelCadreScoreLayout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addGroup(jPanelCadreScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadreScoreLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jComboBoxScoreJoueur1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(jComboBoxScoreJoueur2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelJoueur1Et2Score))
                .addContainerGap(172, Short.MAX_VALUE))
        );
        jPanelCadreScoreLayout.setVerticalGroup(
            jPanelCadreScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCadreScoreLayout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jLabelJoueur1Et2Score)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanelCadreScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxScoreJoueur1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxScoreJoueur2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74)
                .addComponent(jButtonValiderScore)
                .addContainerGap())
        );

        jPanelHorizontalScore.setBackground(new java.awt.Color(53, 23, 89));
        jPanelHorizontalScore.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelScore.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabelScore.setForeground(new java.awt.Color(255, 255, 255));
        jLabelScore.setText("Score");
        jPanelHorizontalScore.add(jLabelScore, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        jPanelVerticalScore.setBackground(new java.awt.Color(53, 23, 89));
        jPanelVerticalScore.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelConteneurScore.setBackground(new java.awt.Color(54, 33, 89));

        jButtonAccueilScore.setBackground(new java.awt.Color(85, 65, 118));
        jButtonAccueilScore.setForeground(new java.awt.Color(240, 240, 240));
        jButtonAccueilScore.setText("Accueil");
        jButtonAccueilScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAccueilScoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConteneurScoreLayout = new javax.swing.GroupLayout(jPanelConteneurScore);
        jPanelConteneurScore.setLayout(jPanelConteneurScoreLayout);
        jPanelConteneurScoreLayout.setHorizontalGroup(
            jPanelConteneurScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
            .addGroup(jPanelConteneurScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurScoreLayout.createSequentialGroup()
                    .addGap(58, 58, 58)
                    .addComponent(jButtonAccueilScore)
                    .addContainerGap(59, Short.MAX_VALUE)))
        );
        jPanelConteneurScoreLayout.setVerticalGroup(
            jPanelConteneurScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
            .addGroup(jPanelConteneurScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurScoreLayout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jButtonAccueilScore)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanelVerticalScore.add(jPanelConteneurScore, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanelConteneurScore2.setBackground(new java.awt.Color(54, 33, 89));

        jButtonPlanningScore.setBackground(new java.awt.Color(85, 65, 118));
        jButtonPlanningScore.setForeground(new java.awt.Color(240, 240, 240));
        jButtonPlanningScore.setText("Planning");
        jButtonPlanningScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlanningScoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConteneurScore2Layout = new javax.swing.GroupLayout(jPanelConteneurScore2);
        jPanelConteneurScore2.setLayout(jPanelConteneurScore2Layout);
        jPanelConteneurScore2Layout.setHorizontalGroup(
            jPanelConteneurScore2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
            .addGroup(jPanelConteneurScore2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurScore2Layout.createSequentialGroup()
                    .addGap(54, 54, 54)
                    .addComponent(jButtonPlanningScore)
                    .addContainerGap(55, Short.MAX_VALUE)))
        );
        jPanelConteneurScore2Layout.setVerticalGroup(
            jPanelConteneurScore2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
            .addGroup(jPanelConteneurScore2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurScore2Layout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jButtonPlanningScore)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanelVerticalScore.add(jPanelConteneurScore2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 190, 40));

        jPanelOpenTennisScore.setBackground(new java.awt.Color(85, 65, 118));

        jLabelOpenTennisScore.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelOpenTennisScore.setForeground(new java.awt.Color(255, 255, 255));
        jLabelOpenTennisScore.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanelOpenTennisScoreLayout = new javax.swing.GroupLayout(jPanelOpenTennisScore);
        jPanelOpenTennisScore.setLayout(jPanelOpenTennisScoreLayout);
        jPanelOpenTennisScoreLayout.setHorizontalGroup(
            jPanelOpenTennisScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOpenTennisScoreLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabelOpenTennisScore, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanelOpenTennisScoreLayout.setVerticalGroup(
            jPanelOpenTennisScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOpenTennisScoreLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOpenTennisScore)
                .addContainerGap())
        );

        jPanelVerticalScore.add(jPanelOpenTennisScore, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 50));

        javax.swing.GroupLayout jPanelScoreLayout = new javax.swing.GroupLayout(jPanelScore);
        jPanelScore.setLayout(jPanelScoreLayout);
        jPanelScoreLayout.setHorizontalGroup(
            jPanelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelScoreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelVerticalScore, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelScoreLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelHorizontalScore, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelScoreLayout.createSequentialGroup()
                                .addGap(159, 159, 159)
                                .addComponent(jPanelCadreScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15))))
                    .addGroup(jPanelScoreLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jPanelErreurScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanelScoreLayout.setVerticalGroup(
            jPanelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelScoreLayout.createSequentialGroup()
                .addComponent(jPanelHorizontalScore, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(jPanelCadreScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelErreurScore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanelVerticalScore, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
        );

        jPanelConfirmer.setMaximumSize(new java.awt.Dimension(2131, 788));
        jPanelConfirmer.setPreferredSize(new java.awt.Dimension(1055, 495));

        jPanelVertical.setBackground(new java.awt.Color(53, 23, 89));
        jPanelVertical.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelConteneurConfirmation.setBackground(new java.awt.Color(54, 33, 89));

        jButtonAccueilConfirmation.setBackground(new java.awt.Color(54, 33, 89));
        jButtonAccueilConfirmation.setForeground(new java.awt.Color(240, 240, 240));
        jButtonAccueilConfirmation.setText("Accueil");
        jButtonAccueilConfirmation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAccueilConfirmationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConteneurConfirmationLayout = new javax.swing.GroupLayout(jPanelConteneurConfirmation);
        jPanelConteneurConfirmation.setLayout(jPanelConteneurConfirmationLayout);
        jPanelConteneurConfirmationLayout.setHorizontalGroup(
            jPanelConteneurConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
            .addGroup(jPanelConteneurConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurConfirmationLayout.createSequentialGroup()
                    .addGap(58, 58, 58)
                    .addComponent(jButtonAccueilConfirmation)
                    .addContainerGap(59, Short.MAX_VALUE)))
        );
        jPanelConteneurConfirmationLayout.setVerticalGroup(
            jPanelConteneurConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
            .addGroup(jPanelConteneurConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurConfirmationLayout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jButtonAccueilConfirmation)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanelVertical.add(jPanelConteneurConfirmation, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanelOpenTennisConfirmation.setBackground(new java.awt.Color(85, 65, 118));

        jLabelOpenTennisConfirmation.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelOpenTennisConfirmation.setForeground(new java.awt.Color(255, 255, 255));
        jLabelOpenTennisConfirmation.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanelOpenTennisConfirmationLayout = new javax.swing.GroupLayout(jPanelOpenTennisConfirmation);
        jPanelOpenTennisConfirmation.setLayout(jPanelOpenTennisConfirmationLayout);
        jPanelOpenTennisConfirmationLayout.setHorizontalGroup(
            jPanelOpenTennisConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOpenTennisConfirmationLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabelOpenTennisConfirmation, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanelOpenTennisConfirmationLayout.setVerticalGroup(
            jPanelOpenTennisConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOpenTennisConfirmationLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOpenTennisConfirmation)
                .addContainerGap())
        );

        jPanelVertical.add(jPanelOpenTennisConfirmation, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 50));

        jPanelHorizontal.setBackground(new java.awt.Color(53, 23, 89));
        jPanelHorizontal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelConfirmation.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabelConfirmation.setForeground(new java.awt.Color(255, 255, 255));
        jLabelConfirmation.setText("Confirmation");
        jPanelHorizontal.add(jLabelConfirmation, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        jPanelCadreConfirmation.setBackground(new java.awt.Color(54, 33, 89));

        jButtonConfirmer.setText("Valider");
        jButtonConfirmer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfirmerActionPerformed(evt);
            }
        });

        jLabelPhraseConfirmation.setForeground(new java.awt.Color(240, 240, 240));
        jLabelPhraseConfirmation.setText("Voulez-vous confirmer ?");

        javax.swing.GroupLayout jPanelCadreConfirmationLayout = new javax.swing.GroupLayout(jPanelCadreConfirmation);
        jPanelCadreConfirmation.setLayout(jPanelCadreConfirmationLayout);
        jPanelCadreConfirmationLayout.setHorizontalGroup(
            jPanelCadreConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadreConfirmationLayout.createSequentialGroup()
                .addGroup(jPanelCadreConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadreConfirmationLayout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jButtonConfirmer))
                    .addGroup(jPanelCadreConfirmationLayout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabelPhraseConfirmation)))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        jPanelCadreConfirmationLayout.setVerticalGroup(
            jPanelCadreConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadreConfirmationLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabelPhraseConfirmation)
                .addGap(88, 88, 88)
                .addComponent(jButtonConfirmer)
                .addContainerGap(106, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 531, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addComponent(jLabel12)
                    .addContainerGap(481, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addComponent(jLabel12)
                    .addContainerGap(50, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanelConfirmerLayout = new javax.swing.GroupLayout(jPanelConfirmer);
        jPanelConfirmer.setLayout(jPanelConfirmerLayout);
        jPanelConfirmerLayout.setHorizontalGroup(
            jPanelConfirmerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConfirmerLayout.createSequentialGroup()
                .addComponent(jPanelVertical, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelConfirmerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelConfirmerLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelHorizontal, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 36, Short.MAX_VALUE))
                    .addGroup(jPanelConfirmerLayout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanelConfirmerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelCadreConfirmation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanelConfirmerLayout.setVerticalGroup(
            jPanelConfirmerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConfirmerLayout.createSequentialGroup()
                .addComponent(jPanelVertical, javax.swing.GroupLayout.PREFERRED_SIZE, 685, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanelConfirmerLayout.createSequentialGroup()
                .addComponent(jPanelHorizontal, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelCadreConfirmation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );

        jPanelArbitre.setPreferredSize(new java.awt.Dimension(1055, 495));

        jPanelVerticalArbitre.setBackground(new java.awt.Color(53, 23, 89));
        jPanelVerticalArbitre.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelConteneurArbitre.setBackground(new java.awt.Color(54, 33, 89));

        jButtonAccueilArbitre.setBackground(new java.awt.Color(54, 33, 89));
        jButtonAccueilArbitre.setForeground(new java.awt.Color(240, 240, 240));
        jButtonAccueilArbitre.setText("Accueil");
        jButtonAccueilArbitre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAccueilArbitreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConteneurArbitreLayout = new javax.swing.GroupLayout(jPanelConteneurArbitre);
        jPanelConteneurArbitre.setLayout(jPanelConteneurArbitreLayout);
        jPanelConteneurArbitreLayout.setHorizontalGroup(
            jPanelConteneurArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
            .addGroup(jPanelConteneurArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurArbitreLayout.createSequentialGroup()
                    .addGap(58, 58, 58)
                    .addComponent(jButtonAccueilArbitre)
                    .addContainerGap(59, Short.MAX_VALUE)))
        );
        jPanelConteneurArbitreLayout.setVerticalGroup(
            jPanelConteneurArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
            .addGroup(jPanelConteneurArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurArbitreLayout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jButtonAccueilArbitre)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanelVerticalArbitre.add(jPanelConteneurArbitre, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanelOpenTennisArbitre.setBackground(new java.awt.Color(85, 65, 118));

        jLabel46.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanelOpenTennisArbitreLayout = new javax.swing.GroupLayout(jPanelOpenTennisArbitre);
        jPanelOpenTennisArbitre.setLayout(jPanelOpenTennisArbitreLayout);
        jPanelOpenTennisArbitreLayout.setHorizontalGroup(
            jPanelOpenTennisArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOpenTennisArbitreLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanelOpenTennisArbitreLayout.setVerticalGroup(
            jPanelOpenTennisArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOpenTennisArbitreLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel46)
                .addContainerGap())
        );

        jPanelVerticalArbitre.add(jPanelOpenTennisArbitre, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 50));

        jPanelHorizontalArbitre.setBackground(new java.awt.Color(53, 23, 89));
        jPanelHorizontalArbitre.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelAjouterArbitre.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabelAjouterArbitre.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAjouterArbitre.setText("Ajouter les arbitres");
        jPanelHorizontalArbitre.add(jLabelAjouterArbitre, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPaneArbitre.setViewportView(jTable1);

        jLabelSuivantArbitre.setBackground(new java.awt.Color(54, 33, 89));
        jLabelSuivantArbitre.setForeground(new java.awt.Color(240, 240, 240));
        jLabelSuivantArbitre.setText("Suivant");
        jLabelSuivantArbitre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLabelSuivantArbitreActionPerformed(evt);
            }
        });

        jPanelCadreArbitre.setBackground(new java.awt.Color(85, 65, 118));

        jLabelArbitreChaise.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelArbitreChaise.setForeground(new java.awt.Color(240, 240, 240));
        jLabelArbitreChaise.setText("Arbitre de chaise :");

        jLabelArbitreLigne.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelArbitreLigne.setForeground(new java.awt.Color(240, 240, 240));
        jLabelArbitreLigne.setText("Arbitre de ligne :");

        jComboBoxArbitreLigne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxArbitreLigneActionPerformed(evt);
            }
        });

        jComboBoxArbitreChaise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxArbitreChaiseActionPerformed(evt);
            }
        });

        jButtonAjouterArbitre.setText("Ajouter");
        jButtonAjouterArbitre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAjouterArbitreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCadreArbitreLayout = new javax.swing.GroupLayout(jPanelCadreArbitre);
        jPanelCadreArbitre.setLayout(jPanelCadreArbitreLayout);
        jPanelCadreArbitreLayout.setHorizontalGroup(
            jPanelCadreArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadreArbitreLayout.createSequentialGroup()
                .addGroup(jPanelCadreArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCadreArbitreLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAjouterArbitre))
                    .addGroup(jPanelCadreArbitreLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanelCadreArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelArbitreChaise)
                            .addComponent(jLabelArbitreLigne))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelCadreArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxArbitreLigne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxArbitreChaise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 42, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelCadreArbitreLayout.setVerticalGroup(
            jPanelCadreArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadreArbitreLayout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addGroup(jPanelCadreArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelArbitreChaise)
                    .addComponent(jComboBoxArbitreLigne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanelCadreArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelArbitreLigne)
                    .addComponent(jComboBoxArbitreChaise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(jButtonAjouterArbitre)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelArbitreLayout = new javax.swing.GroupLayout(jPanelArbitre);
        jPanelArbitre.setLayout(jPanelArbitreLayout);
        jPanelArbitreLayout.setHorizontalGroup(
            jPanelArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelArbitreLayout.createSequentialGroup()
                .addComponent(jPanelVerticalArbitre, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelArbitreLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelHorizontalArbitre, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(36, Short.MAX_VALUE))
                    .addGroup(jPanelArbitreLayout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jPanelCadreArbitre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelArbitreLayout.createSequentialGroup()
                                .addComponent(jScrollPaneArbitre, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(119, 119, 119))
                            .addGroup(jPanelArbitreLayout.createSequentialGroup()
                                .addComponent(jLabelSuivantArbitre)
                                .addGap(95, 95, 95))))))
        );
        jPanelArbitreLayout.setVerticalGroup(
            jPanelArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelVerticalArbitre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelArbitreLayout.createSequentialGroup()
                .addGroup(jPanelArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelArbitreLayout.createSequentialGroup()
                        .addComponent(jPanelHorizontalArbitre, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(jScrollPaneArbitre, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelArbitreLayout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(jPanelCadreArbitre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38)
                .addComponent(jLabelSuivantArbitre)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanelRamasseur.setPreferredSize(new java.awt.Dimension(1055, 495));

        jPanelVerticalAjouterRamasseur.setBackground(new java.awt.Color(53, 23, 89));
        jPanelVerticalAjouterRamasseur.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelConteneurRamasseur.setBackground(new java.awt.Color(54, 33, 89));

        jButtonAccueilRamasseur.setForeground(new java.awt.Color(54, 33, 89));
        jButtonAccueilRamasseur.setText("Accueil");
        jButtonAccueilRamasseur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAccueilRamasseurActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConteneurRamasseurLayout = new javax.swing.GroupLayout(jPanelConteneurRamasseur);
        jPanelConteneurRamasseur.setLayout(jPanelConteneurRamasseurLayout);
        jPanelConteneurRamasseurLayout.setHorizontalGroup(
            jPanelConteneurRamasseurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
            .addGroup(jPanelConteneurRamasseurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurRamasseurLayout.createSequentialGroup()
                    .addGap(58, 58, 58)
                    .addComponent(jButtonAccueilRamasseur)
                    .addContainerGap(59, Short.MAX_VALUE)))
        );
        jPanelConteneurRamasseurLayout.setVerticalGroup(
            jPanelConteneurRamasseurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
            .addGroup(jPanelConteneurRamasseurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurRamasseurLayout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jButtonAccueilRamasseur)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanelVerticalAjouterRamasseur.add(jPanelConteneurRamasseur, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanelOpenTennisRamasseur.setBackground(new java.awt.Color(85, 65, 118));

        jLabelOpenTennisRamasseur.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelOpenTennisRamasseur.setForeground(new java.awt.Color(255, 255, 255));
        jLabelOpenTennisRamasseur.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanelOpenTennisRamasseurLayout = new javax.swing.GroupLayout(jPanelOpenTennisRamasseur);
        jPanelOpenTennisRamasseur.setLayout(jPanelOpenTennisRamasseurLayout);
        jPanelOpenTennisRamasseurLayout.setHorizontalGroup(
            jPanelOpenTennisRamasseurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOpenTennisRamasseurLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabelOpenTennisRamasseur, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanelOpenTennisRamasseurLayout.setVerticalGroup(
            jPanelOpenTennisRamasseurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOpenTennisRamasseurLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOpenTennisRamasseur)
                .addContainerGap())
        );

        jPanelVerticalAjouterRamasseur.add(jPanelOpenTennisRamasseur, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 50));

        jButtonSuivantRamasseur.setBackground(new java.awt.Color(54, 33, 89));
        jButtonSuivantRamasseur.setForeground(new java.awt.Color(240, 240, 240));
        jButtonSuivantRamasseur.setText("Suivant");
        jButtonSuivantRamasseur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSuivantRamasseurActionPerformed(evt);
            }
        });

        jPanelHorizontalRmasseur.setBackground(new java.awt.Color(53, 23, 89));
        jPanelHorizontalRmasseur.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabeAjouterRamasseurs.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabeAjouterRamasseurs.setForeground(new java.awt.Color(255, 255, 255));
        jLabeAjouterRamasseurs.setText("Ajouter les ramasseurs");
        jPanelHorizontalRmasseur.add(jLabeAjouterRamasseurs, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        javax.swing.GroupLayout jPanelRamasseurLayout = new javax.swing.GroupLayout(jPanelRamasseur);
        jPanelRamasseur.setLayout(jPanelRamasseurLayout);
        jPanelRamasseurLayout.setHorizontalGroup(
            jPanelRamasseurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRamasseurLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanelVerticalAjouterRamasseur, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelRamasseurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRamasseurLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelHorizontalRmasseur, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelRamasseurLayout.createSequentialGroup()
                        .addGap(363, 363, 363)
                        .addComponent(jButtonSuivantRamasseur)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanelRamasseurLayout.setVerticalGroup(
            jPanelRamasseurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRamasseurLayout.createSequentialGroup()
                .addComponent(jPanelHorizontalRmasseur, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 217, Short.MAX_VALUE)
                .addComponent(jButtonSuivantRamasseur)
                .addGap(164, 164, 164))
            .addGroup(jPanelRamasseurLayout.createSequentialGroup()
                .addComponent(jPanelVerticalAjouterRamasseur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelAjouterMatch.setPreferredSize(new java.awt.Dimension(1055, 495));

        jButtonSuivantAjouterMatch.setBackground(new java.awt.Color(54, 33, 89));
        jButtonSuivantAjouterMatch.setForeground(new java.awt.Color(240, 240, 240));
        jButtonSuivantAjouterMatch.setText("Suivant");
        jButtonSuivantAjouterMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSuivantAjouterMatchActionPerformed(evt);
            }
        });

        jPanelHorizontalAjouterMatch.setBackground(new java.awt.Color(53, 23, 89));
        jPanelHorizontalAjouterMatch.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelAjouterMatch.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabelAjouterMatch.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAjouterMatch.setText("Ajouter un match");
        jPanelHorizontalAjouterMatch.add(jLabelAjouterMatch, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        jPanelCadreAjouterMatch.setBackground(new java.awt.Color(85, 65, 118));
        jPanelCadreAjouterMatch.setForeground(new java.awt.Color(240, 240, 240));

        jLabelJoueur1.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelJoueur1.setForeground(new java.awt.Color(240, 240, 240));
        jLabelJoueur1.setText("Joueur 1 :");

        jComboBoxNumeroJoueur1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNumeroJoueur1ActionPerformed(evt);
            }
        });

        jLabelJoueur2.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelJoueur2.setForeground(new java.awt.Color(240, 240, 240));
        jLabelJoueur2.setText("Joueur 2 :");

        jComboBoxHeure.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10", "11", "12" }));
        jComboBoxHeure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxHeureActionPerformed(evt);
            }
        });

        jComboBoxMinute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "30" }));
        jComboBoxMinute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxMinuteActionPerformed(evt);
            }
        });

        jLabelHeureSepare.setForeground(new java.awt.Color(240, 240, 240));
        jLabelHeureSepare.setText(":");

        jLabelCourt.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelCourt.setForeground(new java.awt.Color(240, 240, 240));
        jLabelCourt.setText("N ° Court :");

        jComboBoxNumeroCourt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNumeroCourtActionPerformed(evt);
            }
        });

        jLabelHeure.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelHeure.setForeground(new java.awt.Color(240, 240, 240));
        jLabelHeure.setText("Heure : ");

        jLabelDate.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelDate.setForeground(new java.awt.Color(240, 240, 240));
        jLabelDate.setText("Date :");

        jLabelNumeroMatch.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelNumeroMatch.setForeground(new java.awt.Color(240, 240, 240));
        jLabelNumeroMatch.setText("Numéto du match :");

        jComboBoxNumeroMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNumeroMatchActionPerformed(evt);
            }
        });

        jComboBoxJour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        jComboBoxJour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxJourActionPerformed(evt);
            }
        });

        jLabelDateSeparateur.setForeground(new java.awt.Color(240, 240, 240));
        jLabelDateSeparateur.setText("-");

        jComboBoxMois.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        jComboBoxMois.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxMoisActionPerformed(evt);
            }
        });

        jLabelDateSeparateur2.setForeground(new java.awt.Color(240, 240, 240));
        jLabelDateSeparateur2.setText("-");

        jComboBoxAnnee.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2020", "2021" }));
        jComboBoxAnnee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxAnneeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCadreAjouterMatchLayout = new javax.swing.GroupLayout(jPanelCadreAjouterMatch);
        jPanelCadreAjouterMatch.setLayout(jPanelCadreAjouterMatchLayout);
        jPanelCadreAjouterMatchLayout.setHorizontalGroup(
            jPanelCadreAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadreAjouterMatchLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(jPanelCadreAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCadreAjouterMatchLayout.createSequentialGroup()
                        .addComponent(jLabelDate)
                        .addGap(28, 28, 28)
                        .addComponent(jComboBoxJour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabelDateSeparateur)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxMois, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelDateSeparateur2)
                        .addGap(11, 11, 11)
                        .addComponent(jComboBoxAnnee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelCadreAjouterMatchLayout.createSequentialGroup()
                        .addComponent(jLabelCourt)
                        .addGap(18, 18, 18)
                        .addGroup(jPanelCadreAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCadreAjouterMatchLayout.createSequentialGroup()
                                .addComponent(jLabelJoueur1)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxNumeroJoueur1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                                .addComponent(jLabelJoueur2)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxNumeroJoueur2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(162, Short.MAX_VALUE))
                            .addGroup(jPanelCadreAjouterMatchLayout.createSequentialGroup()
                                .addComponent(jComboBoxNumeroCourt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanelCadreAjouterMatchLayout.createSequentialGroup()
                        .addGroup(jPanelCadreAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCadreAjouterMatchLayout.createSequentialGroup()
                                .addComponent(jLabelNumeroMatch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBoxNumeroMatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelCadreAjouterMatchLayout.createSequentialGroup()
                                .addComponent(jLabelHeure)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBoxHeure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelHeureSepare)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBoxMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanelCadreAjouterMatchLayout.setVerticalGroup(
            jPanelCadreAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCadreAjouterMatchLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanelCadreAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxNumeroMatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNumeroMatch))
                .addGap(26, 26, 26)
                .addGroup(jPanelCadreAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDate)
                    .addComponent(jComboBoxJour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDateSeparateur)
                    .addComponent(jComboBoxMois, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDateSeparateur2)
                    .addComponent(jComboBoxAnnee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanelCadreAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelHeure)
                    .addComponent(jComboBoxHeure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelHeureSepare))
                .addGap(29, 29, 29)
                .addGroup(jPanelCadreAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCourt)
                    .addComponent(jComboBoxNumeroCourt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addGroup(jPanelCadreAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxNumeroJoueur1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelJoueur2)
                    .addComponent(jComboBoxNumeroJoueur2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelJoueur1))
                .addGap(34, 34, 34))
        );

        jPanelVerticalAjouterMatch.setBackground(new java.awt.Color(53, 23, 89));
        jPanelVerticalAjouterMatch.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelVerticalAjouterMatch.setPreferredSize(new java.awt.Dimension(1055, 495));
        jPanelVerticalAjouterMatch.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelOpenTennisAjouterMatch.setBackground(new java.awt.Color(85, 65, 118));

        jLabelOpenTennisAjouterMatch.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelOpenTennisAjouterMatch.setForeground(new java.awt.Color(255, 255, 255));
        jLabelOpenTennisAjouterMatch.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanelOpenTennisAjouterMatchLayout = new javax.swing.GroupLayout(jPanelOpenTennisAjouterMatch);
        jPanelOpenTennisAjouterMatch.setLayout(jPanelOpenTennisAjouterMatchLayout);
        jPanelOpenTennisAjouterMatchLayout.setHorizontalGroup(
            jPanelOpenTennisAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOpenTennisAjouterMatchLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabelOpenTennisAjouterMatch, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanelOpenTennisAjouterMatchLayout.setVerticalGroup(
            jPanelOpenTennisAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOpenTennisAjouterMatchLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOpenTennisAjouterMatch)
                .addContainerGap())
        );

        jPanelVerticalAjouterMatch.add(jPanelOpenTennisAjouterMatch, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 50));

        jPanelConteneurAjouterMatch.setBackground(new java.awt.Color(54, 33, 89));

        jButtonAccueilPlanning2.setBackground(new java.awt.Color(54, 33, 89));
        jButtonAccueilPlanning2.setForeground(new java.awt.Color(240, 240, 240));
        jButtonAccueilPlanning2.setText("Accueil");
        jButtonAccueilPlanning2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAccueilPlanning2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConteneurAjouterMatchLayout = new javax.swing.GroupLayout(jPanelConteneurAjouterMatch);
        jPanelConteneurAjouterMatch.setLayout(jPanelConteneurAjouterMatchLayout);
        jPanelConteneurAjouterMatchLayout.setHorizontalGroup(
            jPanelConteneurAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
            .addGroup(jPanelConteneurAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurAjouterMatchLayout.createSequentialGroup()
                    .addGap(58, 58, 58)
                    .addComponent(jButtonAccueilPlanning2)
                    .addContainerGap(59, Short.MAX_VALUE)))
        );
        jPanelConteneurAjouterMatchLayout.setVerticalGroup(
            jPanelConteneurAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
            .addGroup(jPanelConteneurAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurAjouterMatchLayout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jButtonAccueilPlanning2)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanelVerticalAjouterMatch.add(jPanelConteneurAjouterMatch, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanelConteneurAjouterMatch2.setBackground(new java.awt.Color(54, 33, 89));

        jPlanningAjouterMatch.setBackground(new java.awt.Color(54, 33, 89));
        jPlanningAjouterMatch.setForeground(new java.awt.Color(240, 240, 240));
        jPlanningAjouterMatch.setText("Planning");
        jPlanningAjouterMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPlanningAjouterMatchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConteneurAjouterMatch2Layout = new javax.swing.GroupLayout(jPanelConteneurAjouterMatch2);
        jPanelConteneurAjouterMatch2.setLayout(jPanelConteneurAjouterMatch2Layout);
        jPanelConteneurAjouterMatch2Layout.setHorizontalGroup(
            jPanelConteneurAjouterMatch2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
            .addGroup(jPanelConteneurAjouterMatch2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurAjouterMatch2Layout.createSequentialGroup()
                    .addGap(54, 54, 54)
                    .addComponent(jPlanningAjouterMatch)
                    .addContainerGap(55, Short.MAX_VALUE)))
        );
        jPanelConteneurAjouterMatch2Layout.setVerticalGroup(
            jPanelConteneurAjouterMatch2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
            .addGroup(jPanelConteneurAjouterMatch2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurAjouterMatch2Layout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jPlanningAjouterMatch)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanelVerticalAjouterMatch.add(jPanelConteneurAjouterMatch2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 190, 40));

        javax.swing.GroupLayout jPanelAjouterMatchLayout = new javax.swing.GroupLayout(jPanelAjouterMatch);
        jPanelAjouterMatch.setLayout(jPanelAjouterMatchLayout);
        jPanelAjouterMatchLayout.setHorizontalGroup(
            jPanelAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAjouterMatchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelVerticalAjouterMatch, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAjouterMatchLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanelAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelHorizontalAjouterMatch, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelCadreAjouterMatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(13, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAjouterMatchLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSuivantAjouterMatch, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(96, 96, 96))))
        );
        jPanelAjouterMatchLayout.setVerticalGroup(
            jPanelAjouterMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAjouterMatchLayout.createSequentialGroup()
                .addComponent(jPanelHorizontalAjouterMatch, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelCadreAjouterMatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonSuivantAjouterMatch)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanelVerticalAjouterMatch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanelPlanning.setPreferredSize(new java.awt.Dimension(1055, 495));

        jTablePlanning.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPanePlanning.setViewportView(jTablePlanning);

        jPanelVerticalPlanning.setBackground(new java.awt.Color(53, 23, 89));
        jPanelVerticalPlanning.setMaximumSize(new java.awt.Dimension(190, 200));
        jPanelVerticalPlanning.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelConteneur.setBackground(new java.awt.Color(54, 33, 89));

        jButtonPlanningAccueil.setBackground(new java.awt.Color(54, 33, 89));
        jButtonPlanningAccueil.setForeground(new java.awt.Color(240, 240, 240));
        jButtonPlanningAccueil.setText("Accueil");
        jButtonPlanningAccueil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlanningAccueilActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConteneurLayout = new javax.swing.GroupLayout(jPanelConteneur);
        jPanelConteneur.setLayout(jPanelConteneurLayout);
        jPanelConteneurLayout.setHorizontalGroup(
            jPanelConteneurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConteneurLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jButtonPlanningAccueil)
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanelConteneurLayout.setVerticalGroup(
            jPanelConteneurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelConteneurLayout.createSequentialGroup()
                .addGap(0, 15, Short.MAX_VALUE)
                .addComponent(jButtonPlanningAccueil))
        );

        jPanelVerticalPlanning.add(jPanelConteneur, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanelConteneur2.setBackground(new java.awt.Color(54, 33, 89));

        jButtonAjouterMatchPlanning.setBackground(new java.awt.Color(54, 33, 89));
        jButtonAjouterMatchPlanning.setForeground(new java.awt.Color(240, 240, 240));
        jButtonAjouterMatchPlanning.setText("Ajouter un match");
        jButtonAjouterMatchPlanning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAjouterMatchPlanningActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConteneur2Layout = new javax.swing.GroupLayout(jPanelConteneur2);
        jPanelConteneur2.setLayout(jPanelConteneur2Layout);
        jPanelConteneur2Layout.setHorizontalGroup(
            jPanelConteneur2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
            .addGroup(jPanelConteneur2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneur2Layout.createSequentialGroup()
                    .addGap(29, 29, 29)
                    .addComponent(jButtonAjouterMatchPlanning)
                    .addContainerGap(30, Short.MAX_VALUE)))
        );
        jPanelConteneur2Layout.setVerticalGroup(
            jPanelConteneur2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
            .addGroup(jPanelConteneur2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneur2Layout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jButtonAjouterMatchPlanning)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanelVerticalPlanning.add(jPanelConteneur2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 190, 40));

        jPanelOpenTennisPlanning.setBackground(new java.awt.Color(85, 65, 118));

        jLabelOpenTennisPlanning.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelOpenTennisPlanning.setForeground(new java.awt.Color(255, 255, 255));
        jLabelOpenTennisPlanning.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanelOpenTennisPlanningLayout = new javax.swing.GroupLayout(jPanelOpenTennisPlanning);
        jPanelOpenTennisPlanning.setLayout(jPanelOpenTennisPlanningLayout);
        jPanelOpenTennisPlanningLayout.setHorizontalGroup(
            jPanelOpenTennisPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOpenTennisPlanningLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabelOpenTennisPlanning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanelOpenTennisPlanningLayout.setVerticalGroup(
            jPanelOpenTennisPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOpenTennisPlanningLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOpenTennisPlanning)
                .addContainerGap())
        );

        jPanelVerticalPlanning.add(jPanelOpenTennisPlanning, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 50));

        jButtonSupprimerMatch.setBackground(new java.awt.Color(54, 33, 89));
        jButtonSupprimerMatch.setForeground(new java.awt.Color(240, 240, 240));
        jButtonSupprimerMatch.setText("Supprimer le match");
        jButtonSupprimerMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSupprimerMatchActionPerformed(evt);
            }
        });

        jButtonAjouterScore.setBackground(new java.awt.Color(54, 33, 89));
        jButtonAjouterScore.setForeground(new java.awt.Color(240, 240, 240));
        jButtonAjouterScore.setText("Mettre le score");
        jButtonAjouterScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAjouterScoreActionPerformed(evt);
            }
        });

        jPanelHorizontalPlanning.setBackground(new java.awt.Color(53, 23, 89));
        jPanelHorizontalPlanning.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelPlanning.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabelPlanning.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPlanning.setText("Planning");
        jPanelHorizontalPlanning.add(jLabelPlanning, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        javax.swing.GroupLayout jPanelErreurLayout = new javax.swing.GroupLayout(jPanelErreur);
        jPanelErreur.setLayout(jPanelErreurLayout);
        jPanelErreurLayout.setHorizontalGroup(
            jPanelErreurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelErreurLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelErreurPlanning, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(324, Short.MAX_VALUE))
        );
        jPanelErreurLayout.setVerticalGroup(
            jPanelErreurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelErreurLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabelErreurPlanning)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelPlanningLayout = new javax.swing.GroupLayout(jPanelPlanning);
        jPanelPlanning.setLayout(jPanelPlanningLayout);
        jPanelPlanningLayout.setHorizontalGroup(
            jPanelPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPlanningLayout.createSequentialGroup()
                .addComponent(jPanelVerticalPlanning, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPlanningLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanelPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelErreur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPanePlanning, javax.swing.GroupLayout.PREFERRED_SIZE, 808, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelPlanningLayout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(jButtonSupprimerMatch)
                        .addGap(153, 153, 153)
                        .addComponent(jButtonAjouterScore))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPlanningLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelHorizontalPlanning, javax.swing.GroupLayout.PREFERRED_SIZE, 837, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanelPlanningLayout.setVerticalGroup(
            jPanelPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPlanningLayout.createSequentialGroup()
                .addComponent(jPanelHorizontalPlanning, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jScrollPanePlanning, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanelPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSupprimerMatch)
                    .addComponent(jButtonAjouterScore))
                .addGap(18, 18, 18)
                .addComponent(jPanelErreur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
            .addComponent(jPanelVerticalPlanning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanelAccueil.setPreferredSize(new java.awt.Dimension(1055, 495));

        jPanelVerticalAccueil.setBackground(new java.awt.Color(53, 23, 89));
        jPanelVerticalAccueil.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelOpenTennisAccueil.setBackground(new java.awt.Color(85, 65, 118));

        jLabelOpenTennisAccueil.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabelOpenTennisAccueil.setForeground(new java.awt.Color(255, 255, 255));
        jLabelOpenTennisAccueil.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanelOpenTennisAccueilLayout = new javax.swing.GroupLayout(jPanelOpenTennisAccueil);
        jPanelOpenTennisAccueil.setLayout(jPanelOpenTennisAccueilLayout);
        jPanelOpenTennisAccueilLayout.setHorizontalGroup(
            jPanelOpenTennisAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOpenTennisAccueilLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelOpenTennisAccueil, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(42, 42, 42))
        );
        jPanelOpenTennisAccueilLayout.setVerticalGroup(
            jPanelOpenTennisAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelOpenTennisAccueilLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelOpenTennisAccueil)
                .addContainerGap())
        );

        jPanelVerticalAccueil.add(jPanelOpenTennisAccueil, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 190, 50));

        jPanelConteneurAccueil.setBackground(new java.awt.Color(54, 33, 89));
        jPanelConteneurAccueil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelConteneurAccueilMouseClicked(evt);
            }
        });

        jButtonAccueilPlanning.setBackground(new java.awt.Color(54, 33, 89));
        jButtonAccueilPlanning.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jButtonAccueilPlanning.setForeground(new java.awt.Color(240, 240, 240));
        jButtonAccueilPlanning.setText("Planning");
        jButtonAccueilPlanning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAccueilPlanningActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConteneurAccueilLayout = new javax.swing.GroupLayout(jPanelConteneurAccueil);
        jPanelConteneurAccueil.setLayout(jPanelConteneurAccueilLayout);
        jPanelConteneurAccueilLayout.setHorizontalGroup(
            jPanelConteneurAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
            .addGroup(jPanelConteneurAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurAccueilLayout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jButtonAccueilPlanning)
                    .addContainerGap(44, Short.MAX_VALUE)))
        );
        jPanelConteneurAccueilLayout.setVerticalGroup(
            jPanelConteneurAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
            .addGroup(jPanelConteneurAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurAccueilLayout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(jButtonAccueilPlanning)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanelVerticalAccueil.add(jPanelConteneurAccueil, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanelConteneurAccueil2.setBackground(new java.awt.Color(54, 33, 89));
        jPanelConteneurAccueil2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelConteneurAccueil2MouseClicked(evt);
            }
        });

        jButtonAccueilAjouterMatch.setBackground(new java.awt.Color(54, 33, 89));
        jButtonAccueilAjouterMatch.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jButtonAccueilAjouterMatch.setForeground(new java.awt.Color(240, 240, 240));
        jButtonAccueilAjouterMatch.setText("Ajouter un match");
        jButtonAccueilAjouterMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAccueilAjouterMatchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelConteneurAccueil2Layout = new javax.swing.GroupLayout(jPanelConteneurAccueil2);
        jPanelConteneurAccueil2.setLayout(jPanelConteneurAccueil2Layout);
        jPanelConteneurAccueil2Layout.setHorizontalGroup(
            jPanelConteneurAccueil2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 193, Short.MAX_VALUE)
            .addGroup(jPanelConteneurAccueil2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurAccueil2Layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(jButtonAccueilAjouterMatch)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanelConteneurAccueil2Layout.setVerticalGroup(
            jPanelConteneurAccueil2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
            .addGroup(jPanelConteneurAccueil2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelConteneurAccueil2Layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(jButtonAccueilAjouterMatch)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanelVerticalAccueil.add(jPanelConteneurAccueil2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 190, 40));

        jPanelHorizontalAccueil.setBackground(new java.awt.Color(53, 23, 89));
        jPanelHorizontalAccueil.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelAccueil.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabelAccueil.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAccueil.setText("Accueil");
        jPanelHorizontalAccueil.add(jLabelAccueil, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        jLabelPhraseAccueil.setText("Bienvenue à l'Open de Lyon.");

        jLabelPhraseAccueil2.setText("Vous trouvez ici les résultats du tournoi en LIVE !");

        javax.swing.GroupLayout jPanelAccueilLayout = new javax.swing.GroupLayout(jPanelAccueil);
        jPanelAccueil.setLayout(jPanelAccueilLayout);
        jPanelAccueilLayout.setHorizontalGroup(
            jPanelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1055, Short.MAX_VALUE)
            .addGroup(jPanelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelAccueilLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanelVerticalAccueil, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelHorizontalAccueil, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jPanelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelAccueilLayout.createSequentialGroup()
                    .addGap(381, 381, 381)
                    .addGroup(jPanelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelAccueilLayout.createSequentialGroup()
                            .addGap(37, 37, 37)
                            .addComponent(jLabelPhraseAccueil))
                        .addComponent(jLabelPhraseAccueil2))
                    .addContainerGap(394, Short.MAX_VALUE)))
        );
        jPanelAccueilLayout.setVerticalGroup(
            jPanelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 607, Short.MAX_VALUE)
            .addGroup(jPanelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelAccueilLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanelVerticalAccueil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelAccueilLayout.createSequentialGroup()
                            .addComponent(jPanelHorizontalAccueil, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(492, 492, 492)))
                    .addContainerGap()))
            .addGroup(jPanelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelAccueilLayout.createSequentialGroup()
                    .addGap(281, 281, 281)
                    .addComponent(jLabelPhraseAccueil, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabelPhraseAccueil2)
                    .addContainerGap(930, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1055, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelConfirmer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelArbitre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelRamasseur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelAjouterMatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelPlanning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelAccueil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 607, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelConfirmer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelArbitre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelRamasseur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelAjouterMatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelPlanning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelAccueil, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonValiderScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonValiderScoreActionPerformed

        if(jComboBoxScoreJoueur1.getSelectedItem().toString().equals("2") || jComboBoxScoreJoueur2.getSelectedItem().toString().equals("2"))
        {
            jLabelErreurScore.setText("");
            jPanelScore.setVisible(false);
            jPanelPlanning.setVisible(true);
            jPanelAccueil.setVisible(false);
            jPanelArbitre.setVisible(false);
            jPanelConfirmer.setVisible(false);
            jPanelRamasseur.setVisible(false);
            jPanelAjouterMatch.setVisible(false);
            
            match.setScore(match.chercherMatch(jTablePlanning.getModel().getValueAt(jTablePlanning.getSelectedRow(), 0).toString(), listeMatchPlace),jComboBoxScoreJoueur1.getSelectedItem().toString() + " - " + jComboBoxScoreJoueur2.getSelectedItem().toString());
            updatePlanning();
            jComboBoxScoreJoueur1.setSelectedItem("0");
            jComboBoxScoreJoueur2.setSelectedItem("0");
        }
        else
        jLabelErreurScore.setText("L'un des deux joueurs doit avoir 2 sets gagnés !");
    }//GEN-LAST:event_jButtonValiderScoreActionPerformed

    private void jComboBoxScoreJoueur2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxScoreJoueur2ActionPerformed
        if(jComboBoxScoreJoueur2.getSelectedItem().toString().equals("2"))
        {
            if(jComboBoxScoreJoueur1.getItemCount() == 3)
            jComboBoxScoreJoueur1.removeItemAt(2);
        }
        else
        if(jComboBoxScoreJoueur1.getItemCount() == 2)
        jComboBoxScoreJoueur1.addItem("2");
    }//GEN-LAST:event_jComboBoxScoreJoueur2ActionPerformed

    private void jComboBoxScoreJoueur1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxScoreJoueur1ActionPerformed
        if(jComboBoxScoreJoueur1.getSelectedItem().toString().equals("2"))
        {
            if(jComboBoxScoreJoueur2.getItemCount() == 3)
            jComboBoxScoreJoueur2.removeItemAt(2);
        }
        else
        if(jComboBoxScoreJoueur2.getItemCount() == 2)
        jComboBoxScoreJoueur2.addItem("2");
    }//GEN-LAST:event_jComboBoxScoreJoueur1ActionPerformed

    private void jButtonConfirmerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfirmerActionPerformed
        jPanelConfirmer.setVisible(false);
        jPanelAccueil.setVisible(true);
        jPanelPlanning.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelRamasseur.setVisible(false);
        jPanelArbitre.setVisible(false);
        jPanelAjouterMatch.setVisible(false);
    }//GEN-LAST:event_jButtonConfirmerActionPerformed

    private void jLabelSuivantArbitreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLabelSuivantArbitreActionPerformed
        jPanelArbitre.setVisible(false);
        jPanelConfirmer.setVisible(true);
        jPanelPlanning.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelRamasseur.setVisible(false);
        jPanelAccueil.setVisible(false);
        jPanelAjouterMatch.setVisible(false);
    }//GEN-LAST:event_jLabelSuivantArbitreActionPerformed

    private void jButtonAjouterArbitreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAjouterArbitreActionPerformed
        refreshArbitreLigne();
    }//GEN-LAST:event_jButtonAjouterArbitreActionPerformed

    private void jButtonSuivantRamasseurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSuivantRamasseurActionPerformed
        jPanelRamasseur.setVisible(false);
        jPanelArbitre.setVisible(true);
        jPanelConfirmer.setVisible(false);
        jPanelPlanning.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelAccueil.setVisible(false);
        jPanelAjouterMatch.setVisible(false);
    }//GEN-LAST:event_jButtonSuivantRamasseurActionPerformed

    private void jComboBoxNumeroJoueur1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxNumeroJoueur1ActionPerformed
        jComboBoxNumeroJoueur2.removeAllItems();
        for(Joueur j:listeJoueurDispo)
        {
            if(jComboBoxNumeroJoueur1.getSelectedItem() == null)
            return;
            else if(!j.toString().equals(jComboBoxNumeroJoueur1.getSelectedItem().toString()))
            jComboBoxNumeroJoueur2.addItem(j.toString());
        }
    }//GEN-LAST:event_jComboBoxNumeroJoueur1ActionPerformed

    private void jComboBoxHeureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxHeureActionPerformed
        if(jComboBoxHeure.getSelectedItem().toString().equals("12"))
        {
            jComboBoxMinute.setEnabled(true);
        }
        else
        {
            jComboBoxMinute.setSelectedItem("00");
            jComboBoxMinute.setEnabled(false);
        }
        refreshListeJoueur();
    }//GEN-LAST:event_jComboBoxHeureActionPerformed

    private void jComboBoxMinuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxMinuteActionPerformed
        refreshListeJoueur();
    }//GEN-LAST:event_jComboBoxMinuteActionPerformed

    private void jComboBoxNumeroCourtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxNumeroCourtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxNumeroCourtActionPerformed

    private void jComboBoxNumeroMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxNumeroMatchActionPerformed

    }//GEN-LAST:event_jComboBoxNumeroMatchActionPerformed

    private void jComboBoxJourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxJourActionPerformed
        refreshListeJoueur();
    }//GEN-LAST:event_jComboBoxJourActionPerformed

    private void jComboBoxMoisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxMoisActionPerformed
        refreshListeJoueur();
    }//GEN-LAST:event_jComboBoxMoisActionPerformed

    private void jComboBoxAnneeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxAnneeActionPerformed
        refreshListeJoueur();
    }//GEN-LAST:event_jComboBoxAnneeActionPerformed

    private void jButtonSupprimerMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSupprimerMatchActionPerformed
        if(jTablePlanning.getSelectedColumn() != -1)
        {
            jLabelErreurPlanning.setText("");
            match.retirerMatch(listeMatchDispo, listeMatchPlace, match.chercherMatch(jTablePlanning.getModel().getValueAt(jTablePlanning.getSelectedRow(), 0).toString(), listeMatchPlace));
            updatePlanning();
            refreshListeMatch();
            refreshListeJoueur();
        }
        else
        jLabelErreurPlanning.setText("Sélectionner une ligne avant la suppression d'un match !");
    }//GEN-LAST:event_jButtonSupprimerMatchActionPerformed

    private void jButtonAjouterScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAjouterScoreActionPerformed
        if(jTablePlanning.getSelectedColumn() == -1)
        {
            jLabelErreurPlanning.setText("Séléctioner une ligne avant de reporter le score.");
        }
        else
        {
            jLabelErreurPlanning.setText("");
            jPanelPlanning.setVisible(false);
            jPanelScore.setVisible(true);
            jPanelAccueil.setVisible(false);
            jPanelArbitre.setVisible(false);
            jPanelConfirmer.setVisible(false);
            jPanelRamasseur.setVisible(false);
            jPanelAjouterMatch.setVisible(false);
            
        }
    }//GEN-LAST:event_jButtonAjouterScoreActionPerformed

    private void jButtonAccueilPlanningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAccueilPlanningActionPerformed
        jPanelPlanning.setVisible(true);
        jPanelAccueil.setVisible(false);
        jPanelConfirmer.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelRamasseur.setVisible(false);
        jPanelArbitre.setVisible(false);
        jPanelAjouterMatch.setVisible(false);
    }//GEN-LAST:event_jButtonAccueilPlanningActionPerformed

    private void jPanelConteneurAccueilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelConteneurAccueilMouseClicked

    }//GEN-LAST:event_jPanelConteneurAccueilMouseClicked

    private void jButtonAccueilAjouterMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAccueilAjouterMatchActionPerformed
        jPanelAjouterMatch.setVisible(true);
        jPanelAccueil.setVisible(false);
        jPanelConfirmer.setVisible(false);
        jPanelPlanning.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelRamasseur.setVisible(false);
        jPanelArbitre.setVisible(false);
    }//GEN-LAST:event_jButtonAccueilAjouterMatchActionPerformed

    private void jPanelConteneurAccueil2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelConteneurAccueil2MouseClicked

    }//GEN-LAST:event_jPanelConteneurAccueil2MouseClicked

    private void jButtonPlanningAccueilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlanningAccueilActionPerformed
        jPanelAccueil.setVisible(true);
        jPanelPlanning.setVisible(false);
        jPanelConfirmer.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelRamasseur.setVisible(false);
        jPanelArbitre.setVisible(false);
        jPanelAjouterMatch.setVisible(false);
    }//GEN-LAST:event_jButtonPlanningAccueilActionPerformed

    private void jButtonAjouterMatchPlanningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAjouterMatchPlanningActionPerformed
        jPanelAjouterMatch.setVisible(true);
        jPanelPlanning.setVisible(false);
        jPanelAccueil.setVisible(false);
        jPanelArbitre.setVisible(false);
        jPanelConfirmer.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelRamasseur.setVisible(false);
    }//GEN-LAST:event_jButtonAjouterMatchPlanningActionPerformed

    private void jButtonSuivantAjouterMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSuivantAjouterMatchActionPerformed
        jPanelAccueil.setVisible(false);
        jPanelRamasseur.setVisible(true);
        jPanelArbitre.setVisible(false);
        jPanelConfirmer.setVisible(false);
        jPanelPlanning.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelAjouterMatch.setVisible(false);
        if(match.placerUnMatch(listeMatchDispo, listeMatchPlace, match.chercherMatch(jComboBoxNumeroMatch.getSelectedItem().toString(), listeMatchDispo), jComboBoxJour.getSelectedItem().toString(), jComboBoxMois.getSelectedItem().toString(), jComboBoxAnnee.getSelectedItem().toString(), jComboBoxHeure.getSelectedItem().toString(), jComboBoxMinute.getSelectedItem().toString(), joueur.chercherJoueur(jComboBoxNumeroJoueur1.getSelectedItem().toString(), listeJoueur), joueur.chercherJoueur(jComboBoxNumeroJoueur2.getSelectedItem().toString(), listeJoueur), court.chercherCourt(jComboBoxNumeroCourt.getSelectedItem().toString(), listeCourt)))
        {
            if(match.MatchBienPlace(match.chercherMatch(jComboBoxNumeroMatch.getSelectedItem().toString(), listeMatchPlace), listeMatchPlace, court.chercherCourt(jComboBoxNumeroCourt.getSelectedItem().toString(), listeCourt)))
            {
                jLabelPhraseAccueil2.setText("                                          Le " + jComboBoxNumeroMatch.getSelectedItem().toString() + " a bien été rajouté.");
                jComboBoxNumeroMatch.removeItem(jComboBoxNumeroMatch.getSelectedItem());
            }
            else
            {
                jLabelPhraseAccueil2.setText("                 Un match est déja placé sur ce court au même créneau !");
                match.retirerMatch(listeMatchDispo, listeMatchPlace, match.chercherMatch(jComboBoxNumeroMatch.getSelectedItem().toString(), listeMatchPlace));
            }
        }
        refreshArbitre();
        refreshListeJoueur();
    }//GEN-LAST:event_jButtonSuivantAjouterMatchActionPerformed

    private void jPlanningAjouterMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPlanningAjouterMatchActionPerformed
        jPanelPlanning.setVisible(true);
        jPanelAjouterMatch.setVisible(false);
        jPanelConfirmer.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelRamasseur.setVisible(false);
        jPanelArbitre.setVisible(false);
        jPanelAccueil.setVisible(false);
    }//GEN-LAST:event_jPlanningAjouterMatchActionPerformed

    private void jButtonAccueilPlanning2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAccueilPlanning2ActionPerformed
        jPanelAccueil.setVisible(true);
        jPanelAjouterMatch.setVisible(false);
        jPanelConfirmer.setVisible(false);
        jPanelPlanning.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelRamasseur.setVisible(false);
        jPanelArbitre.setVisible(false);
    }//GEN-LAST:event_jButtonAccueilPlanning2ActionPerformed

    private void jButtonAccueilRamasseurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAccueilRamasseurActionPerformed
        jPanelAccueil.setVisible(true);
        jPanelRamasseur.setVisible(false);
        jPanelConfirmer.setVisible(false);
        jPanelPlanning.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelArbitre.setVisible(false);
        jPanelAjouterMatch.setVisible(false);
    }//GEN-LAST:event_jButtonAccueilRamasseurActionPerformed

    private void jButtonPlanningScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlanningScoreActionPerformed
        jPanelPlanning.setVisible(true);
        jPanelScore.setVisible(false);
        jPanelConfirmer.setVisible(false);
        jPanelRamasseur.setVisible(false);
        jPanelArbitre.setVisible(false);
        jPanelAccueil.setVisible(false);
        jPanelAjouterMatch.setVisible(false);
    }//GEN-LAST:event_jButtonPlanningScoreActionPerformed

    private void jButtonAccueilScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAccueilScoreActionPerformed
        jPanelAccueil.setVisible(true);
        jPanelScore.setVisible(false);
        jPanelConfirmer.setVisible(false);
        jPanelPlanning.setVisible(false);
        jPanelRamasseur.setVisible(false);
        jPanelArbitre.setVisible(false);
        jPanelAjouterMatch.setVisible(false);
    }//GEN-LAST:event_jButtonAccueilScoreActionPerformed

    private void jButtonAccueilArbitreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAccueilArbitreActionPerformed
        jPanelAccueil.setVisible(true);
        jPanelArbitre.setVisible(false);
        jPanelConfirmer.setVisible(false);
        jPanelPlanning.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelRamasseur.setVisible(false);
        jPanelAjouterMatch.setVisible(false);
    }//GEN-LAST:event_jButtonAccueilArbitreActionPerformed

    private void jComboBoxArbitreLigneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxArbitreLigneActionPerformed
       
    }//GEN-LAST:event_jComboBoxArbitreLigneActionPerformed

    private void jComboBoxArbitreChaiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxArbitreChaiseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxArbitreChaiseActionPerformed

    private void jButtonAccueilConfirmationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAccueilConfirmationActionPerformed
        jPanelAccueil.setVisible(true);
        jPanelArbitre.setVisible(false);
        jPanelConfirmer.setVisible(false);
        jPanelPlanning.setVisible(false);
        jPanelScore.setVisible(false);
        jPanelRamasseur.setVisible(false);
        jPanelAjouterMatch.setVisible(false);
    }//GEN-LAST:event_jButtonAccueilConfirmationActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAccueilAjouterMatch;
    private javax.swing.JButton jButtonAccueilArbitre;
    private javax.swing.JButton jButtonAccueilConfirmation;
    private javax.swing.JButton jButtonAccueilPlanning;
    private javax.swing.JButton jButtonAccueilPlanning2;
    private javax.swing.JButton jButtonAccueilRamasseur;
    private javax.swing.JButton jButtonAccueilScore;
    private javax.swing.JButton jButtonAjouterArbitre;
    private javax.swing.JButton jButtonAjouterMatchPlanning;
    private javax.swing.JButton jButtonAjouterScore;
    private javax.swing.JButton jButtonConfirmer;
    private javax.swing.JButton jButtonPlanningAccueil;
    private javax.swing.JButton jButtonPlanningScore;
    private javax.swing.JButton jButtonSuivantAjouterMatch;
    private javax.swing.JButton jButtonSuivantRamasseur;
    private javax.swing.JButton jButtonSupprimerMatch;
    private javax.swing.JButton jButtonValiderScore;
    private javax.swing.JComboBox<String> jComboBoxAnnee;
    private javax.swing.JComboBox<String> jComboBoxArbitreChaise;
    private javax.swing.JComboBox<String> jComboBoxArbitreLigne;
    private javax.swing.JComboBox<String> jComboBoxHeure;
    private javax.swing.JComboBox<String> jComboBoxJour;
    private javax.swing.JComboBox<String> jComboBoxMinute;
    private javax.swing.JComboBox<String> jComboBoxMois;
    private javax.swing.JComboBox jComboBoxNumeroCourt;
    private javax.swing.JComboBox<String> jComboBoxNumeroJoueur1;
    private javax.swing.JComboBox<String> jComboBoxNumeroJoueur2;
    private javax.swing.JComboBox<String> jComboBoxNumeroMatch;
    private javax.swing.JComboBox<String> jComboBoxScoreJoueur1;
    private javax.swing.JComboBox<String> jComboBoxScoreJoueur2;
    private javax.swing.JLabel jLabeAjouterRamasseurs;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabelAccueil;
    private javax.swing.JLabel jLabelAjouterArbitre;
    private javax.swing.JLabel jLabelAjouterMatch;
    private javax.swing.JLabel jLabelArbitreChaise;
    private javax.swing.JLabel jLabelArbitreLigne;
    private javax.swing.JLabel jLabelConfirmation;
    private javax.swing.JLabel jLabelCourt;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelDateSeparateur;
    private javax.swing.JLabel jLabelDateSeparateur2;
    private javax.swing.JLabel jLabelErreurPlanning;
    private javax.swing.JLabel jLabelErreurScore;
    private javax.swing.JLabel jLabelHeure;
    private javax.swing.JLabel jLabelHeureSepare;
    private javax.swing.JLabel jLabelJoueur1;
    private javax.swing.JLabel jLabelJoueur1Et2Score;
    private javax.swing.JLabel jLabelJoueur2;
    private javax.swing.JLabel jLabelNumeroMatch;
    private javax.swing.JLabel jLabelOpenTennisAccueil;
    private javax.swing.JLabel jLabelOpenTennisAjouterMatch;
    private javax.swing.JLabel jLabelOpenTennisConfirmation;
    private javax.swing.JLabel jLabelOpenTennisPlanning;
    private javax.swing.JLabel jLabelOpenTennisRamasseur;
    private javax.swing.JLabel jLabelOpenTennisScore;
    private javax.swing.JLabel jLabelPhraseAccueil;
    private javax.swing.JLabel jLabelPhraseAccueil2;
    private javax.swing.JLabel jLabelPhraseConfirmation;
    private javax.swing.JLabel jLabelPlanning;
    private javax.swing.JLabel jLabelScore;
    private javax.swing.JButton jLabelSuivantArbitre;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelAccueil;
    private javax.swing.JPanel jPanelAjouterMatch;
    private javax.swing.JPanel jPanelArbitre;
    private javax.swing.JPanel jPanelCadreAjouterMatch;
    private javax.swing.JPanel jPanelCadreArbitre;
    private javax.swing.JPanel jPanelCadreConfirmation;
    private javax.swing.JPanel jPanelCadreScore;
    private javax.swing.JPanel jPanelConfirmer;
    private javax.swing.JPanel jPanelConteneur;
    private javax.swing.JPanel jPanelConteneur2;
    private javax.swing.JPanel jPanelConteneurAccueil;
    private javax.swing.JPanel jPanelConteneurAccueil2;
    private javax.swing.JPanel jPanelConteneurAjouterMatch;
    private javax.swing.JPanel jPanelConteneurAjouterMatch2;
    private javax.swing.JPanel jPanelConteneurArbitre;
    private javax.swing.JPanel jPanelConteneurConfirmation;
    private javax.swing.JPanel jPanelConteneurRamasseur;
    private javax.swing.JPanel jPanelConteneurScore;
    private javax.swing.JPanel jPanelConteneurScore2;
    private javax.swing.JPanel jPanelErreur;
    private javax.swing.JPanel jPanelErreurScore;
    private javax.swing.JPanel jPanelHorizontal;
    private javax.swing.JPanel jPanelHorizontalAccueil;
    private javax.swing.JPanel jPanelHorizontalAjouterMatch;
    private javax.swing.JPanel jPanelHorizontalArbitre;
    private javax.swing.JPanel jPanelHorizontalPlanning;
    private javax.swing.JPanel jPanelHorizontalRmasseur;
    private javax.swing.JPanel jPanelHorizontalScore;
    private javax.swing.JPanel jPanelOpenTennisAccueil;
    private javax.swing.JPanel jPanelOpenTennisAjouterMatch;
    private javax.swing.JPanel jPanelOpenTennisArbitre;
    private javax.swing.JPanel jPanelOpenTennisConfirmation;
    private javax.swing.JPanel jPanelOpenTennisPlanning;
    private javax.swing.JPanel jPanelOpenTennisRamasseur;
    private javax.swing.JPanel jPanelOpenTennisScore;
    private javax.swing.JPanel jPanelPlanning;
    private javax.swing.JPanel jPanelRamasseur;
    private javax.swing.JPanel jPanelScore;
    private javax.swing.JPanel jPanelVertical;
    private javax.swing.JPanel jPanelVerticalAccueil;
    private javax.swing.JPanel jPanelVerticalAjouterMatch;
    private javax.swing.JPanel jPanelVerticalAjouterRamasseur;
    private javax.swing.JPanel jPanelVerticalArbitre;
    private javax.swing.JPanel jPanelVerticalPlanning;
    private javax.swing.JPanel jPanelVerticalScore;
    private javax.swing.JButton jPlanningAjouterMatch;
    private javax.swing.JScrollPane jScrollPaneArbitre;
    private javax.swing.JScrollPane jScrollPanePlanning;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTablePlanning;
    // End of variables declaration//GEN-END:variables
}
