package ui;
 
import javax.swing.*;
import personnel.GestionPersonnel;
import personnel.Employe;
 
/**
 * Fenêtre de login - Point d'entrée visuel de l'application.
 * Affiche le panel de connexion et gère la navigation.
 */
public class FenetreLogin extends JFrame implements LoginPanel.LoginCallback {
    
    private GestionPersonnel gestionPersonnel;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 650;
    
    public FenetreLogin(GestionPersonnel gestionPersonnel) {
        this.gestionPersonnel = gestionPersonnel;
        
        initialiserFenetre();
        afficherLogin();
        
        this.setVisible(true);
    }
    
    private void initialiserFenetre() {
        this.setTitle("Gestion Personnel - Connexion");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        
        mainPanel = new JPanel();
        this.add(mainPanel);
    }
    
    private void afficherLogin() {
        mainPanel.removeAll();
        // Passe GestionPersonnel au LoginPanel
        loginPanel = new LoginPanel(gestionPersonnel, this);
        mainPanel.add(loginPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    @Override
    public void onLoginSuccess(Employe employe) {
        // Connexion réussie : afficher la fenêtre principale
        this.dispose(); // Fermer la fenêtre de login
        
        // Créer et afficher la fenêtre principale avec l'employé connecté
        new FenetrePrincipale(gestionPersonnel, employe);
    }
}