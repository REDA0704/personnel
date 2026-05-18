package ui;
 
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import ui.FenetrePrincipale;
 
/**
 * Panel d'accueil - Page principale après connexion
 * Affiche un message de bienvenue et 2 boutons principaux
 */
public class PanelAccueil extends JPanel {
    
    // Couleurs
    private static final Color PRIMARY_COLOR = new Color(41, 98, 255);
    private static final Color SECONDARY_COLOR = new Color(100, 150, 100);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 251);
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BUTTON_HOVER_1 = new Color(30, 85, 220);
    private static final Color BUTTON_HOVER_2 = new Color(85, 135, 85);
    
    private FenetrePrincipale fenetrePrincipale;
    
    public PanelAccueil(FenetrePrincipale fenetrePrincipale) {
        this.fenetrePrincipale = fenetrePrincipale;
        initialiserUI();
    }
    
    private void initialiserUI() {
        this.setLayout(new GridBagLayout());
        this.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // === PANEL CONTENU PRINCIPAL ===
        JPanel contenuPanel = new JPanel();
        contenuPanel.setLayout(new BoxLayout(contenuPanel, BoxLayout.Y_AXIS));
        contenuPanel.setOpaque(false);
        
        // === MESSAGE DE BIENVENUE ===
        JLabel bienvenuLabel = new JLabel("Bienvenue !");
        bienvenuLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        bienvenuLabel.setForeground(TEXT_COLOR);
        bienvenuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenuPanel.add(bienvenuLabel);
        
        contenuPanel.add(Box.createVerticalStrut(10));
        
        // Sous-titre
        JLabel sousTitreLabel = new JLabel("Choisissez une section pour commencer");
        sousTitreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sousTitreLabel.setForeground(new Color(120, 130, 140));
        sousTitreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenuPanel.add(sousTitreLabel);
        
        contenuPanel.add(Box.createVerticalStrut(60));
        
        // === PANEL BOUTONS ===
        JPanel boutonsPanel = new JPanel();
        boutonsPanel.setLayout(new GridLayout(1, 2, 40, 0));
        boutonsPanel.setOpaque(false);
        boutonsPanel.setMaximumSize(new Dimension(800, 200));
        
        // === BOUTON 1 : GÉRER LE COMPTE ===
        BoutonPersonnalise bouton1 = new BoutonPersonnalise(
            "Gérer le compte",
            "Paramètres de votre compte",
            PRIMARY_COLOR,
            BUTTON_HOVER_1
        );
        bouton1.addActionListener(e -> fenetrePrincipale.afficherPanel("compte"));
        boutonsPanel.add(bouton1);
        
        // === BOUTON 2 : GÉRER LES LIGUES ===
        BoutonPersonnalise bouton2 = new BoutonPersonnalise(
            "Gérer les ligues",
            "Administrer vos ligues",
            SECONDARY_COLOR,
            BUTTON_HOVER_2
        );
        bouton2.addActionListener(e -> fenetrePrincipale.afficherPanel("ligues"));
        boutonsPanel.add(bouton2);
        
        contenuPanel.add(boutonsPanel);
        
        // Ajouter le contenu au centre
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(contenuPanel, gbc);
    }
    
    /**
     * Classe interne pour créer des boutons personnalisés avec icônes et descriptions
     */
    private static class BoutonPersonnalise extends JButton {
        
        private String titre;
        private String description;
        private Color colorBase;
        private Color colorHover;
        private boolean isHovered = false;
        
        public BoutonPersonnalise(String titre, String description, Color colorBase, Color colorHover) {
            this.titre = titre;
            this.description = description;
            this.colorBase = colorBase;
            this.colorHover = colorHover;
            
            initialiserBouton();
        }
        
        private void initialiserBouton() {
            this.setContentAreaFilled(false);
            this.setBorderPainted(false);
            this.setFocusPainted(false);
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.setPreferredSize(new Dimension(350, 180));
            
            // Effet hover
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Couleur de fond (avec effet hover)
            Color couleur = isHovered ? colorHover : colorBase;
            g2.setColor(couleur);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            
            // Ombre subtile
            if (isHovered) {
                g2.setColor(new Color(0, 0, 0, 20));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
            
            // === TITRE ===
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
            FontMetrics fm = g2.getFontMetrics();
            int titreX = (getWidth() - fm.stringWidth(titre)) / 2;
            g2.drawString(titre, titreX, 60);
            
            // === DESCRIPTION ===
            g2.setColor(new Color(255, 255, 255, 200));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            fm = g2.getFontMetrics();
            int descX = (getWidth() - fm.stringWidth(description)) / 2;
            g2.drawString(description, descX, 100);
        }
    }
}