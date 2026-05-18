package ui;
 
import javax.swing.*;
import personnel.GestionPersonnel;
 
/**
 * Classe Main - Point d'entrée de l'application graphique.
 * Lance d'abord la fenêtre de login.
 */
public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                jdbc.JDBC jdbc = new jdbc.JDBC();
                
                // Créer le root s'il n'existe pas
                if (jdbc.estVide()) {
                    jdbc.creerRoot();
                }
                

                GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
                
                new FenetreLogin(gestionPersonnel);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Erreur lors du démarrage de l'application:\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
