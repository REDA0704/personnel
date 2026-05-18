package ui;
 
import javax.swing.*;
import java.awt.*;
import personnel.*;
import util.PermissionChecker;
 
/**
 * Fenêtre principale de l'application.
 * Affiche la barre de menu et gère la navigation.
 * S'affiche après une connexion réussie.
 */
public class FenetrePrincipale extends JFrame {
    
    private GestionPersonnel gestionPersonnel;
    private Employe employeConnecte;
    private PermissionChecker permissionChecker;
    private JPanel panelCentral;
    private CardLayout cardLayout;
    private PanelGestionPersonnels panelPersonnels;
    private static final String APP_NAME = "Gestion Personnel";
    private static final String APP_VERSION = "1.0";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    
    public FenetrePrincipale(GestionPersonnel gestionPersonnel, Employe employeConnecte) {
        this.gestionPersonnel = gestionPersonnel;
        this.employeConnecte = employeConnecte;
        this.permissionChecker = new PermissionChecker(employeConnecte);
        
        initialiserFenetre();
        initialiserMenus();
        initialiserPanels();
        
        this.setVisible(true);
    }
    
    private void initialiserFenetre() {
        String titre = APP_NAME + " - v" + APP_VERSION;
        if (employeConnecte != null) {
            titre += " (" + employeConnecte.getPrenom() + " " + employeConnecte.getNom() + ")";
        }
        this.setTitle(titre);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
    }
    
    private void initialiserMenus() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Gestion
        JMenu menuGestion = new JMenu("Gestion");
        
        JMenuItem itemLigues = new JMenuItem("Gérer les ligues");
        itemLigues.addActionListener(e -> afficherPanel("ligues"));
        menuGestion.add(itemLigues);
        
        JMenuItem itemPersonnels = new JMenuItem("Gérer les personnels");
        itemPersonnels.addActionListener(e -> afficherPanel("personnels"));
        menuGestion.add(itemPersonnels);
        
        JMenuItem itemCompte = new JMenuItem("Gérer le compte");
        itemCompte.addActionListener(e -> afficherPanel("compte"));
        menuGestion.add(itemCompte);
        
        menuBar.add(menuGestion);
        
        // Menu Fichier
        JMenu menuFichier = new JMenu("Fichier");
        
        JMenuItem itemDeconnexion = new JMenuItem("Se déconnecter");
        itemDeconnexion.addActionListener(e -> deconnecter());
        menuFichier.add(itemDeconnexion);
        
        menuFichier.addSeparator();
        
        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> quitter());
        menuFichier.add(itemQuitter);
        
        menuBar.add(menuFichier);
        
        this.setJMenuBar(menuBar);
    }
    
    private void initialiserPanels() {
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);

        // Création des panels
        panelCentral.add(new PanelAccueil(this), "accueil");
        panelCentral.add(new PanelGestionLigues(gestionPersonnel, this), "ligues");
        
        // ✅ STOCKE LA RÉFÉRENCE
        panelPersonnels = new PanelGestionPersonnels(gestionPersonnel, this);
        panelCentral.add(panelPersonnels, "personnels");
        
        panelCentral.add(new PanelCompte(this), "compte");

        this.add(panelCentral);

        // Afficher l'accueil par défaut
        cardLayout.show(panelCentral, "accueil");
    }
    
    public void afficherPanel(String nomPanel) {
        cardLayout.show(panelCentral, nomPanel);
    }
    
    
    /**
     * Change la ligue du panel des employés
     */
    public void setLiguePersonnels(Ligue ligue) {
        panelPersonnels.setLigue(ligue);
    }
    
    
    /**
     * Récupère l'employé actuellement connecté
     */
    public Employe getEmployeConnecte() {
        return employeConnecte;
    }
    
    
    /**
     * Récupère l'instance de GestionPersonnel
     */
    public GestionPersonnel getGestionPersonnel() {
        return gestionPersonnel;
    }
    
    
    public PermissionChecker getPermissionChecker() {
        return permissionChecker;
    }
    
    
    private void deconnecter() {
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Êtes-vous sûr de vouloir vous déconnecter?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmation == JOptionPane.YES_OPTION) {
            this.dispose();
            // Relancer la fenêtre de login
            try {
                new FenetreLogin(gestionPersonnel);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la reconnexion",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void quitter() {
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Êtes-vous sûr de vouloir quitter?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmation == JOptionPane.YES_OPTION) {
            this.dispose();
            System.exit(0);
        }
    }
}