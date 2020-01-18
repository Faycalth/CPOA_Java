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

public class Fenetre extends javax.swing.JFrame
{
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
    
    public Fenetre()
    {
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
        panelConfirmation.setVisible(false);
        panelPlanning.setVisible(false);
        PanelScore.setVisible(false);
        panelPlacerRamasseurs.setVisible(false);
        panelPlacerArbitre.setVisible(false);
        panelAccueil.setVisible(true);
        refreshListeMatch();
        comboBoxMinute.setEnabled(false);
        refreshListeJoueur();
        for(Court c:listeCourt)
        {
            comboBoxNumeroCourt.addItem(c.toString());
        }
        tableModelPlanning  = new DefaultTableModel();
    }
    
    private void updatePlanning() 
    {
        tableModelPlanning = match.updateRow();
        tablePlanning.setModel(tableModelPlanning);
    }
    
    private void refreshArbitreLigne()
    {
        comboBoxArbitreLigne.removeAllItems();
        
    }
    
    private void refreshListeMatch()
    {
        comboBoxNumeroMatch.removeAllItems();
        listeMatchDispo = match.getLesMatchsDispo();
        for(Match m:listeMatchDispo)
        {
            comboBoxNumeroMatch.addItem(m.toString());
        }
    }
    private void refreshArbitre()
    {
        comboBoxArbitreLigne.removeAllItems();
        comboBoxArbitreChaise.removeAllItems();
        listeArbitreLigne = arbitre.getLesArbitresLigne(match.chercherMatch(comboBoxNumeroMatch.getSelectedItem().toString(), listeMatchPlace), joueur.chercherJoueur(comboBoxNumeroJoueur1.getSelectedItem().toString(), listeJoueur), joueur.chercherJoueur(comboBoxNumeroJoueur2.getSelectedItem().toString(), listeJoueur));
        listeArbitreChaise = arbitre.getLesArbitresChaise(match.chercherMatch(comboBoxNumeroMatch.getSelectedItem().toString(), listeMatchPlace), joueur.chercherJoueur(comboBoxNumeroJoueur1.getSelectedItem().toString(), listeJoueur), joueur.chercherJoueur(comboBoxNumeroJoueur2.getSelectedItem().toString(), listeJoueur));
        for(Arbitre a:listeArbitreLigne)
        {
            comboBoxArbitreLigne.addItem(a.toString());
        }
        for(Arbitre a:listeArbitreChaise)
        {
            comboBoxArbitreChaise.addItem(a.toString());
        }
    }
    
    private void refreshListeJoueur()
    {
        recupListeJoueur();
        comboBoxNumeroJoueur1.removeAllItems();
        for(Joueur j:listeJoueurDispo)
        {         
            comboBoxNumeroJoueur1.addItem(j.toString());
        }
        comboBoxNumeroJoueur2.removeAllItems();
        for(Joueur j:listeJoueurDispo)
        {
            if(!j.toString().equals(comboBoxNumeroJoueur1.getSelectedItem().toString()))
                comboBoxNumeroJoueur2.addItem(j.toString());
        }
    }
    
    private void recupListeJoueur()
    {
        joueur.getLesJoueurs(comboBoxJour.getSelectedItem().toString(), comboBoxMois.getSelectedItem().toString(), comboBoxAnnee.getSelectedItem().toString(), comboBoxHeure.getSelectedItem().toString(), comboBoxMinute.getSelectedItem().toString());
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

        panelPlacerArbitre = new javax.swing.JPanel();
        labelValiderPlacerArbitre = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jPanel45 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        labelArbitreChaise = new javax.swing.JLabel();
        labelArbitreLigne = new javax.swing.JLabel();
        comboBoxArbitreLigne = new javax.swing.JComboBox<String>();
        comboBoxArbitreChaise = new javax.swing.JComboBox<String>();
        jButton2 = new javax.swing.JButton();
        panelPlanning = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePlanning = new javax.swing.JTable();
        buttonRetourPanelPlanning = new javax.swing.JButton();
        buttonrScore = new javax.swing.JButton();
        labelErreurPlanning = new javax.swing.JLabel();
        buttonSupprimerMatch = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jPanel42 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        panelPlacerRamasseurs = new javax.swing.JPanel();
        buttonOkPanelPlacerRamasseurs = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        PanelScore = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        labelJoueur1Joueur2Score = new javax.swing.JLabel();
        comboBoxScoreJoueur2 = new javax.swing.JComboBox<String>();
        labelTiretScore = new javax.swing.JLabel();
        comboBoxScoreJoueur1 = new javax.swing.JComboBox<String>();
        jPanel10 = new javax.swing.JPanel();
        ErreurScore = new javax.swing.JLabel();
        panelConfirmation = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        panelAccueil = new javax.swing.JPanel();
        buttonValiderAcceuil = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        labelJoueur1 = new javax.swing.JLabel();
        comboBoxNumeroJoueur1 = new javax.swing.JComboBox<String>();
        comboBoxNumeroJoueur2 = new javax.swing.JComboBox<String>();
        labelJoueur2 = new javax.swing.JLabel();
        comboBoxHeure = new javax.swing.JComboBox<String>();
        comboBoxMinute = new javax.swing.JComboBox<String>();
        jLabel6 = new javax.swing.JLabel();
        labelCourt = new javax.swing.JLabel();
        comboBoxNumeroCourt = new javax.swing.JComboBox();
        jLabel31 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        labelErreurScore = new javax.swing.JLabel();
        comboBoxNumeroMatch = new javax.swing.JComboBox<String>();
        comboBoxJour = new javax.swing.JComboBox<String>();
        jLabel4 = new javax.swing.JLabel();
        comboBoxMois = new javax.swing.JComboBox<String>();
        jLabel5 = new javax.swing.JLabel();
        comboBoxAnnee = new javax.swing.JComboBox<String>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelValiderPlacerArbitre.setText("Valider");
        labelValiderPlacerArbitre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labelValiderPlacerArbitreActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jPanel5.setBackground(new java.awt.Color(53, 23, 89));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel43.setBackground(new java.awt.Color(54, 33, 89));

        jLabel44.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Bienvenue à ");

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel44)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel44)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanel44.setBackground(new java.awt.Color(54, 33, 89));

        jLabel45.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("l'Open de Lyon !");

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel45)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 190, 40));

        jPanel45.setBackground(new java.awt.Color(85, 65, 118));

        jLabel46.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel45Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel46)
                .addContainerGap())
        );

        jPanel5.add(jPanel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 50));

        jPanel1.setBackground(new java.awt.Color(53, 23, 89));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Placer les arbitres");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        jPanel7.setBackground(new java.awt.Color(85, 65, 118));

        labelArbitreChaise.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        labelArbitreChaise.setForeground(new java.awt.Color(240, 240, 240));
        labelArbitreChaise.setText("Arbitre de chaise :");

        labelArbitreLigne.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        labelArbitreLigne.setForeground(new java.awt.Color(240, 240, 240));
        labelArbitreLigne.setText("Arbitre de ligne :");

        jButton2.setText("Ajouter");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelArbitreChaise)
                    .addComponent(labelArbitreLigne))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxArbitreLigne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxArbitreChaise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelArbitreChaise)
                    .addComponent(comboBoxArbitreLigne, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelArbitreLigne)
                    .addComponent(comboBoxArbitreChaise, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelPlacerArbitreLayout = new javax.swing.GroupLayout(panelPlacerArbitre);
        panelPlacerArbitre.setLayout(panelPlacerArbitreLayout);
        panelPlacerArbitreLayout.setHorizontalGroup(
            panelPlacerArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlacerArbitreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelPlacerArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPlacerArbitreLayout.createSequentialGroup()
                        .addGap(609, 609, 609)
                        .addComponent(labelValiderPlacerArbitre)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlacerArbitreLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelPlacerArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlacerArbitreLayout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlacerArbitreLayout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(70, 70, 70))))))
            .addGroup(panelPlacerArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlacerArbitreLayout.createSequentialGroup()
                    .addContainerGap(301, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(471, Short.MAX_VALUE)))
        );
        panelPlacerArbitreLayout.setVerticalGroup(
            panelPlacerArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
            .addGroup(panelPlacerArbitreLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(labelValiderPlacerArbitre)
                .addContainerGap())
            .addGroup(panelPlacerArbitreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlacerArbitreLayout.createSequentialGroup()
                    .addContainerGap(133, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(83, Short.MAX_VALUE)))
        );

        tablePlanning.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablePlanning);

        buttonRetourPanelPlanning.setText("Retour");
        buttonRetourPanelPlanning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRetourPanelPlanningActionPerformed(evt);
            }
        });

        buttonrScore.setText("Reporter le score");
        buttonrScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonrScoreActionPerformed(evt);
            }
        });

        buttonSupprimerMatch.setText("Supprimer le match");
        buttonSupprimerMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSupprimerMatchActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(53, 23, 89));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel40.setBackground(new java.awt.Color(54, 33, 89));

        jLabel41.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Bienvenue à ");

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel41)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel41)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanel41.setBackground(new java.awt.Color(54, 33, 89));

        jLabel42.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("l'Open de Lyon !");

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel42)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel42)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 190, 40));

        jPanel42.setBackground(new java.awt.Color(85, 65, 118));

        jLabel43.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel43)
                .addContainerGap())
        );

        jPanel6.add(jPanel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 50));

        jPanel2.setBackground(new java.awt.Color(53, 23, 89));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Planning");
        jPanel2.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        javax.swing.GroupLayout panelPlanningLayout = new javax.swing.GroupLayout(panelPlanning);
        panelPlanning.setLayout(panelPlanningLayout);
        panelPlanningLayout.setHorizontalGroup(
            panelPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlanningLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPlanningLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelPlanningLayout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panelPlanningLayout.createSequentialGroup()
                                .addComponent(labelErreurPlanning)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(panelPlanningLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 736, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(panelPlanningLayout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(buttonRetourPanelPlanning)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonSupprimerMatch)
                        .addGap(76, 76, 76)
                        .addComponent(buttonrScore)
                        .addGap(80, 80, 80))))
        );
        panelPlanningLayout.setVerticalGroup(
            panelPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlanningLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlanningLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67)
                        .addGroup(panelPlanningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonRetourPanelPlanning)
                            .addComponent(buttonrScore)
                            .addComponent(buttonSupprimerMatch))
                        .addGap(75, 75, 75)
                        .addComponent(labelErreurPlanning)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        buttonOkPanelPlacerRamasseurs.setText("OK");
        buttonOkPanelPlacerRamasseurs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkPanelPlacerRamasseursActionPerformed(evt);
            }
        });

        jPanel13.setBackground(new java.awt.Color(53, 23, 89));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel37.setBackground(new java.awt.Color(54, 33, 89));

        jLabel38.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Bienvenue à ");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel38)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.add(jPanel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanel38.setBackground(new java.awt.Color(54, 33, 89));

        jLabel39.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("l'Open de Lyon !");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel39)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.add(jPanel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 190, 40));

        jPanel39.setBackground(new java.awt.Color(85, 65, 118));

        jLabel40.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel39Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel40)
                .addContainerGap())
        );

        jPanel13.add(jPanel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 50));

        jPanel3.setBackground(new java.awt.Color(53, 23, 89));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Placer les ramasseurs");
        jPanel3.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        javax.swing.GroupLayout panelPlacerRamasseursLayout = new javax.swing.GroupLayout(panelPlacerRamasseurs);
        panelPlacerRamasseurs.setLayout(panelPlacerRamasseursLayout);
        panelPlacerRamasseursLayout.setHorizontalGroup(
            panelPlacerRamasseursLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlacerRamasseursLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelPlacerRamasseursLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPlacerRamasseursLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPlacerRamasseursLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonOkPanelPlacerRamasseurs)
                        .addGap(385, 385, 385))))
        );
        panelPlacerRamasseursLayout.setVerticalGroup(
            panelPlacerRamasseursLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPlacerRamasseursLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPlacerRamasseursLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPlacerRamasseursLayout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelPlacerRamasseursLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonOkPanelPlacerRamasseurs)
                        .addGap(86, 86, 86))))
        );

        jPanel17.setBackground(new java.awt.Color(53, 23, 89));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel34.setBackground(new java.awt.Color(54, 33, 89));

        jLabel35.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Bienvenue à ");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel35)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanel35.setBackground(new java.awt.Color(54, 33, 89));

        jLabel36.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("l'Open de Lyon !");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel36)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 190, 40));

        jPanel36.setBackground(new java.awt.Color(85, 65, 118));

        jLabel37.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel37)
                .addContainerGap())
        );

        jPanel17.add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 50));

        jPanel4.setBackground(new java.awt.Color(53, 23, 89));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Score");
        jPanel4.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        jPanel8.setBackground(new java.awt.Color(85, 65, 118));

        jButton6.setText("Valider");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        labelJoueur1Joueur2Score.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        labelJoueur1Joueur2Score.setForeground(new java.awt.Color(240, 240, 240));
        labelJoueur1Joueur2Score.setText("Joueur 1   -   Joueur 2");

        comboBoxScoreJoueur2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2" }));
        comboBoxScoreJoueur2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxScoreJoueur2ActionPerformed(evt);
            }
        });

        labelTiretScore.setText("-");

        comboBoxScoreJoueur1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2" }));
        comboBoxScoreJoueur1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxScoreJoueur1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addGap(25, 25, 25))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(comboBoxScoreJoueur1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(comboBoxScoreJoueur2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelJoueur1Joueur2Score))
                .addContainerGap(172, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(328, 328, 328)
                    .addComponent(labelTiretScore)
                    .addContainerGap(155, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(labelJoueur1Joueur2Score)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxScoreJoueur1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxScoreJoueur2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74)
                .addComponent(jButton6)
                .addContainerGap())
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(labelTiretScore)
                    .addContainerGap(227, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 735, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(367, 367, 367)
                    .addComponent(ErreurScore)
                    .addContainerGap(368, Short.MAX_VALUE)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addComponent(ErreurScore)
                    .addContainerGap(28, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout PanelScoreLayout = new javax.swing.GroupLayout(PanelScore);
        PanelScore.setLayout(PanelScoreLayout);
        PanelScoreLayout.setHorizontalGroup(
            PanelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelScoreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(PanelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelScoreLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PanelScoreLayout.createSequentialGroup()
                                .addGap(159, 159, 159)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(PanelScoreLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        PanelScoreLayout.setVerticalGroup(
            PanelScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelScoreLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
        );

        jButton1.setText("Valider");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel21.setBackground(new java.awt.Color(53, 23, 89));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel31.setBackground(new java.awt.Color(54, 33, 89));

        jLabel32.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Bienvenue à ");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel32)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel21.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanel32.setBackground(new java.awt.Color(54, 33, 89));

        jLabel33.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("l'Open de Lyon !");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel33)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel21.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 190, 40));

        jPanel33.setBackground(new java.awt.Color(85, 65, 118));

        jLabel34.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel34)
                .addContainerGap())
        );

        jPanel21.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 50));

        jPanel29.setBackground(new java.awt.Color(53, 23, 89));
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel28.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Confirmation");
        jPanel29.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        javax.swing.GroupLayout panelConfirmationLayout = new javax.swing.GroupLayout(panelConfirmation);
        panelConfirmation.setLayout(panelConfirmationLayout);
        panelConfirmationLayout.setHorizontalGroup(
            panelConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConfirmationLayout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelConfirmationLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(374, 374, 374)
                        .addComponent(jButton1))
                    .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 1112, Short.MAX_VALUE))
        );
        panelConfirmationLayout.setVerticalGroup(
            panelConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConfirmationLayout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, 685, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 103, Short.MAX_VALUE))
            .addGroup(panelConfirmationLayout.createSequentialGroup()
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelConfirmationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelConfirmationLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelConfirmationLayout.createSequentialGroup()
                        .addGap(288, 288, 288)
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        buttonValiderAcceuil.setText("Valider");
        buttonValiderAcceuil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonValiderAcceuilActionPerformed(evt);
            }
        });

        jButton3.setText("Accéder au planning");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel25.setBackground(new java.awt.Color(53, 23, 89));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel26.setBackground(new java.awt.Color(85, 65, 118));

        jLabel22.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("OPEN TENNIS");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel22)
                .addContainerGap())
        );

        jPanel25.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 50));

        jPanel27.setBackground(new java.awt.Color(54, 33, 89));

        jLabel23.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Bienvenue à ");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel23)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 40));

        jPanel28.setBackground(new java.awt.Color(54, 33, 89));

        jLabel24.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("l'Open de Lyon !");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel24)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel25.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 190, 40));

        jPanel30.setBackground(new java.awt.Color(53, 23, 89));
        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel29.setFont(new java.awt.Font("Bell MT", 1, 36)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Accueil");
        jPanel30.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, -1, -1));

        jPanel9.setBackground(new java.awt.Color(85, 65, 118));
        jPanel9.setForeground(new java.awt.Color(240, 240, 240));

        labelJoueur1.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        labelJoueur1.setForeground(new java.awt.Color(240, 240, 240));
        labelJoueur1.setText("Joueur 1 :");

        comboBoxNumeroJoueur1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxNumeroJoueur1ActionPerformed(evt);
            }
        });

        labelJoueur2.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        labelJoueur2.setForeground(new java.awt.Color(240, 240, 240));
        labelJoueur2.setText("Joueur 2 :");

        comboBoxHeure.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10", "11", "12" }));
        comboBoxHeure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxHeureActionPerformed(evt);
            }
        });

        comboBoxMinute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "30" }));
        comboBoxMinute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxMinuteActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(240, 240, 240));
        jLabel6.setText(":");

        labelCourt.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        labelCourt.setForeground(new java.awt.Color(240, 240, 240));
        labelCourt.setText("N ° Court :");

        comboBoxNumeroCourt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxNumeroCourtActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(240, 240, 240));
        jLabel31.setText("Heure : ");

        jLabel30.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(240, 240, 240));
        jLabel30.setText("Date :");

        labelErreurScore.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        labelErreurScore.setForeground(new java.awt.Color(240, 240, 240));
        labelErreurScore.setText("Numéto du match :");

        comboBoxNumeroMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxNumeroMatchActionPerformed(evt);
            }
        });

        comboBoxJour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        comboBoxJour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxJourActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(240, 240, 240));
        jLabel4.setText("-");

        comboBoxMois.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        comboBoxMois.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxMoisActionPerformed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(240, 240, 240));
        jLabel5.setText("-");

        comboBoxAnnee.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2020", "2021" }));
        comboBoxAnnee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxAnneeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addGap(28, 28, 28)
                        .addComponent(comboBoxJour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboBoxMois, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addGap(11, 11, 11)
                        .addComponent(comboBoxAnnee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(labelCourt)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(labelJoueur1)
                                .addGap(18, 18, 18)
                                .addComponent(comboBoxNumeroJoueur1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                                .addComponent(labelJoueur2)
                                .addGap(18, 18, 18)
                                .addComponent(comboBoxNumeroJoueur2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(162, Short.MAX_VALUE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(comboBoxNumeroCourt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(labelErreurScore)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboBoxNumeroMatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboBoxHeure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboBoxMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxNumeroMatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelErreurScore))
                .addGap(26, 26, 26)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(comboBoxJour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(comboBoxMois, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(comboBoxAnnee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(comboBoxHeure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(29, 29, 29)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCourt)
                    .addComponent(comboBoxNumeroCourt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxNumeroJoueur1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelJoueur2)
                    .addComponent(comboBoxNumeroJoueur2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelJoueur1))
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout panelAccueilLayout = new javax.swing.GroupLayout(panelAccueil);
        panelAccueil.setLayout(panelAccueilLayout);
        panelAccueilLayout.setHorizontalGroup(
            panelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccueilLayout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAccueilLayout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonValiderAcceuil, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(111, 111, 111))
                    .addGroup(panelAccueilLayout.createSequentialGroup()
                        .addGroup(panelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAccueilLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, 826, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelAccueilLayout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(28, Short.MAX_VALUE))))
        );
        panelAccueilLayout.setVerticalGroup(
            panelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccueilLayout.createSequentialGroup()
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(panelAccueilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAccueilLayout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAccueilLayout.createSequentialGroup()
                        .addComponent(buttonValiderAcceuil)
                        .addContainerGap())))
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelAccueil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(panelPlanning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 1100, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(PanelScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1065, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 541, Short.MAX_VALUE)
                    .addComponent(panelPlacerRamasseurs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 542, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelPlacerArbitre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelConfirmation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelAccueil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 286, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(panelPlanning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 140, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(PanelScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(326, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelPlacerRamasseurs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelPlacerArbitre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(panelConfirmation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buttonValiderAcceuilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonValiderAcceuilActionPerformed
        panelAccueil.setVisible(false);
        panelPlacerRamasseurs.setVisible(true);       
        if(match.placerUnMatch(listeMatchDispo, listeMatchPlace, match.chercherMatch(comboBoxNumeroMatch.getSelectedItem().toString(), listeMatchDispo), comboBoxJour.getSelectedItem().toString(), comboBoxMois.getSelectedItem().toString(), comboBoxAnnee.getSelectedItem().toString(), comboBoxHeure.getSelectedItem().toString(), comboBoxMinute.getSelectedItem().toString(), joueur.chercherJoueur(comboBoxNumeroJoueur1.getSelectedItem().toString(), listeJoueur), joueur.chercherJoueur(comboBoxNumeroJoueur2.getSelectedItem().toString(), listeJoueur), court.chercherCourt(comboBoxNumeroCourt.getSelectedItem().toString(), listeCourt)))
        {    
            if(match.MatchBienPlace(match.chercherMatch(comboBoxNumeroMatch.getSelectedItem().toString(), listeMatchPlace), listeMatchPlace, court.chercherCourt(comboBoxNumeroCourt.getSelectedItem().toString(), listeCourt)))
            {            
                jLabel7.setText("                                          Le " + comboBoxNumeroMatch.getSelectedItem().toString() + " a bien été rajouté.");
                comboBoxNumeroMatch.removeItem(comboBoxNumeroMatch.getSelectedItem());            
            }
            else
            {
                jLabel7.setText("                 Un match est déja placé sur ce court au même créneau !");
                match.retirerMatch(listeMatchDispo, listeMatchPlace, match.chercherMatch(comboBoxNumeroMatch.getSelectedItem().toString(), listeMatchPlace));
            }
        }
        refreshArbitre();
        refreshListeJoueur();      
    }//GEN-LAST:event_buttonValiderAcceuilActionPerformed

    private void comboBoxNumeroMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxNumeroMatchActionPerformed
        
    }//GEN-LAST:event_comboBoxNumeroMatchActionPerformed

    private void comboBoxAnneeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxAnneeActionPerformed
        refreshListeJoueur();
    }//GEN-LAST:event_comboBoxAnneeActionPerformed

    private void comboBoxMinuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxMinuteActionPerformed
        refreshListeJoueur();
    }//GEN-LAST:event_comboBoxMinuteActionPerformed

    private void comboBoxHeureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxHeureActionPerformed
        if(comboBoxHeure.getSelectedItem().toString().equals("12"))
        {
            comboBoxMinute.setEnabled(true);
        }
        else
        {
            comboBoxMinute.setSelectedItem("00");
            comboBoxMinute.setEnabled(false);
        }
        refreshListeJoueur();
    }//GEN-LAST:event_comboBoxHeureActionPerformed

    private void comboBoxJourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxJourActionPerformed
        refreshListeJoueur();
    }//GEN-LAST:event_comboBoxJourActionPerformed

    private void comboBoxMoisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxMoisActionPerformed
        refreshListeJoueur();
    }//GEN-LAST:event_comboBoxMoisActionPerformed

    private void comboBoxNumeroJoueur1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxNumeroJoueur1ActionPerformed
        comboBoxNumeroJoueur2.removeAllItems();
        for(Joueur j:listeJoueurDispo)
        {
            if(comboBoxNumeroJoueur1.getSelectedItem() == null)
                return;
            else if(!j.toString().equals(comboBoxNumeroJoueur1.getSelectedItem().toString()))
                comboBoxNumeroJoueur2.addItem(j.toString());
        }
    }//GEN-LAST:event_comboBoxNumeroJoueur1ActionPerformed

    private void buttonRetourPanelPlanningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRetourPanelPlanningActionPerformed
        panelPlanning.setVisible(false);
        panelAccueil.setVisible(true);
    }//GEN-LAST:event_buttonRetourPanelPlanningActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        labelErreurPlanning.setText("");
        panelAccueil.setVisible(false);
        panelPlanning.setVisible(true);
        updatePlanning();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void buttonrScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonrScoreActionPerformed
        if(tablePlanning.getSelectedColumn() == -1)
        {
            labelErreurPlanning.setText("Séléctioner une ligne avant de reporter le score.");
        }
        else
        {
            labelErreurPlanning.setText("");
            panelPlanning.setVisible(false);
            PanelScore.setVisible(true);
        }
    }//GEN-LAST:event_buttonrScoreActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        if(comboBoxScoreJoueur1.getSelectedItem().toString().equals("2") || comboBoxScoreJoueur2.getSelectedItem().toString().equals("2"))
        {
            ErreurScore.setText("");
            PanelScore.setVisible(false);
            panelPlanning.setVisible(true);
            match.setScore(match.chercherMatch(tablePlanning.getModel().getValueAt(tablePlanning.getSelectedRow(), 0).toString(), listeMatchPlace),comboBoxScoreJoueur1.getSelectedItem().toString() + " - " + comboBoxScoreJoueur2.getSelectedItem().toString());
            updatePlanning();
            comboBoxScoreJoueur1.setSelectedItem("0");
            comboBoxScoreJoueur2.setSelectedItem("0");
        }
        else
        ErreurScore.setText("L'un des deux joueurs doit avoir 2 sets gagnés !");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void buttonSupprimerMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSupprimerMatchActionPerformed
        if(tablePlanning.getSelectedColumn() != -1)
        {
            labelErreurPlanning.setText("");
            match.retirerMatch(listeMatchDispo, listeMatchPlace, match.chercherMatch(tablePlanning.getModel().getValueAt(tablePlanning.getSelectedRow(), 0).toString(), listeMatchPlace));
            updatePlanning();
            refreshListeMatch();
            refreshListeJoueur();
        }
        else
            labelErreurPlanning.setText("Sélectionner une ligne avant la suppression d'un match !");
    }//GEN-LAST:event_buttonSupprimerMatchActionPerformed

    private void buttonOkPanelPlacerRamasseursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkPanelPlacerRamasseursActionPerformed
        panelPlacerRamasseurs.setVisible(false);
        panelPlacerArbitre.setVisible(true);
    }//GEN-LAST:event_buttonOkPanelPlacerRamasseursActionPerformed

    private void labelValiderPlacerArbitreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labelValiderPlacerArbitreActionPerformed
        panelPlacerArbitre.setVisible(false);
        panelConfirmation.setVisible(true);
    }//GEN-LAST:event_labelValiderPlacerArbitreActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        panelConfirmation.setVisible(false);
        panelAccueil.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void comboBoxScoreJoueur1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxScoreJoueur1ActionPerformed
        if(comboBoxScoreJoueur1.getSelectedItem().toString().equals("2"))
        {
            if(comboBoxScoreJoueur2.getItemCount() == 3)
                comboBoxScoreJoueur2.removeItemAt(2);
        }
        else
            if(comboBoxScoreJoueur2.getItemCount() == 2)
                comboBoxScoreJoueur2.addItem("2");
    }//GEN-LAST:event_comboBoxScoreJoueur1ActionPerformed

    private void comboBoxScoreJoueur2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxScoreJoueur2ActionPerformed
        if(comboBoxScoreJoueur2.getSelectedItem().toString().equals("2"))
        {
            if(comboBoxScoreJoueur1.getItemCount() == 3)
                comboBoxScoreJoueur1.removeItemAt(2);
        }
        else
            if(comboBoxScoreJoueur1.getItemCount() == 2)
                comboBoxScoreJoueur1.addItem("2");
    }//GEN-LAST:event_comboBoxScoreJoueur2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        refreshArbitreLigne();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void comboBoxNumeroCourtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxNumeroCourtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxNumeroCourtActionPerformed

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
            java.util.logging.Logger.getLogger(Fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run()
            {
                new Fenetre().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ErreurScore;
    private javax.swing.JPanel PanelScore;
    private javax.swing.JButton buttonOkPanelPlacerRamasseurs;
    private javax.swing.JButton buttonRetourPanelPlanning;
    private javax.swing.JButton buttonSupprimerMatch;
    private javax.swing.JButton buttonValiderAcceuil;
    private javax.swing.JButton buttonrScore;
    private javax.swing.JComboBox<String> comboBoxAnnee;
    private javax.swing.JComboBox<String> comboBoxArbitreChaise;
    private javax.swing.JComboBox<String> comboBoxArbitreLigne;
    private javax.swing.JComboBox<String> comboBoxHeure;
    private javax.swing.JComboBox<String> comboBoxJour;
    private javax.swing.JComboBox<String> comboBoxMinute;
    private javax.swing.JComboBox<String> comboBoxMois;
    private javax.swing.JComboBox comboBoxNumeroCourt;
    private javax.swing.JComboBox<String> comboBoxNumeroJoueur1;
    private javax.swing.JComboBox<String> comboBoxNumeroJoueur2;
    private javax.swing.JComboBox<String> comboBoxNumeroMatch;
    private javax.swing.JComboBox<String> comboBoxScoreJoueur1;
    private javax.swing.JComboBox<String> comboBoxScoreJoueur2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel labelArbitreChaise;
    private javax.swing.JLabel labelArbitreLigne;
    private javax.swing.JLabel labelCourt;
    private javax.swing.JLabel labelErreurPlanning;
    private javax.swing.JLabel labelErreurScore;
    private javax.swing.JLabel labelJoueur1;
    private javax.swing.JLabel labelJoueur1Joueur2Score;
    private javax.swing.JLabel labelJoueur2;
    private javax.swing.JLabel labelTiretScore;
    private javax.swing.JButton labelValiderPlacerArbitre;
    private javax.swing.JPanel panelAccueil;
    private javax.swing.JPanel panelConfirmation;
    private javax.swing.JPanel panelPlacerArbitre;
    private javax.swing.JPanel panelPlacerRamasseurs;
    private javax.swing.JPanel panelPlanning;
    private javax.swing.JTable tablePlanning;
    // End of variables declaration//GEN-END:variables

    

    
}
