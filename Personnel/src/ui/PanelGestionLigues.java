package ui;
 
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import personnel.Ligue;
import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.SauvegardeImpossible;
import personnel.DroitsInsuffisants;
 
/**
 * Panel pour la gestion des ligues.
 * ✅ Root peut gérer TOUTES les ligues
 * ✅ Admin de ligue peut gérer SA ligue uniquement
 * ✅ Employé simple voit seulement (pas d'écriture)
 */
public class PanelGestionLigues extends JPanel {
    
    private GestionPersonnel gestionPersonnel;
    private FenetrePrincipale fenetre;
    private JList<Ligue> ligueList;
    private DefaultListModel<Ligue> ligueModel;
    private JButton btnAjouter;
    
    public PanelGestionLigues(GestionPersonnel gestionPersonnel, FenetrePrincipale fenetre) {
        this.gestionPersonnel = gestionPersonnel;
        this.fenetre = fenetre;
        
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(new Color(248, 249, 251));
        
        // === TITRE ===
        JLabel titre = new JLabel("Gérer les Ligues");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(33, 33, 33));
        this.add(titre, BorderLayout.NORTH);
        
        // === LISTE DES LIGUES ===
        ligueModel = new DefaultListModel<>();
        ligueList = new JList<>(ligueModel);
        ligueList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ligueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ligueList.setBackground(Color.WHITE);
        ligueList.setBorder(new LineBorder(new Color(200, 210, 220), 1));
        
        // Charger les ligues
        chargerLigues();
        
        // ✅ DOUBLE-CLIC pour éditer
        ligueList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = ligueList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Ligue ligueSelectionnee = ligueModel.getElementAt(index);
                        editerLigue(ligueSelectionnee);
                    }
                }
            }
        });
        
        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(ligueList);
        scrollPane.setBorder(new LineBorder(new Color(200, 210, 220), 1));
        this.add(scrollPane, BorderLayout.CENTER);
        
        // === BOUTONS ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBoutons.setBackground(new Color(248, 249, 251));
        
        btnAjouter = new JButton("Ajouter");
        btnAjouter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAjouter.setBackground(new Color(100, 150, 100));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.setFocusPainted(false);
        btnAjouter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAjouter.addActionListener(e -> ajouterLigue());
        panelBoutons.add(btnAjouter);
        
        JButton btnBack = new JButton("Retour");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setBackground(new Color(150, 150, 150));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> fenetre.afficherPanel("accueil"));
        panelBoutons.add(btnBack);
        
        this.add(panelBoutons, BorderLayout.SOUTH);
        
        // ✅ Vérifier les droits et désactiver le bouton si nécessaire
        verifierDroits();
    }
    
    /**
     * Vérifie les droits et désactive le bouton "Ajouter" si pas de droits
     */
    private void verifierDroits() {
        try {
            fenetre.getPermissionChecker().checkCanManageLigues();
            btnAjouter.setEnabled(true);
        } catch (DroitsInsuffisants e) {
            btnAjouter.setEnabled(false);
            btnAjouter.setToolTipText("Seul le super-administrateur peut ajouter des ligues");
        }
    }
    
    /**
     * Charge les ligues depuis GestionPersonnel
     */
    private void chargerLigues() {
        ligueModel.clear();
        for (Ligue ligue : gestionPersonnel.getLigues()) {
            ligueModel.addElement(ligue);
        }
    }
    
    /**
     * Ajoute une nouvelle ligue (ROOT ONLY)
     */
    private void ajouterLigue() {
        try {
            fenetre.getPermissionChecker().checkCanManageLigues();
            
            String nom = JOptionPane.showInputDialog(this, "Nom de la ligue:");
            if (nom != null && !nom.trim().isEmpty()) {
                gestionPersonnel.addLigue(nom.trim());
                chargerLigues();
                JOptionPane.showMessageDialog(this, "Ligue ajoutée !");
            }
        } catch (DroitsInsuffisants e) {
            JOptionPane.showMessageDialog(this,
                fenetre.getPermissionChecker().getErrorMessage(),
                "❌ Accès refusé",
                JOptionPane.WARNING_MESSAGE);
        } catch (SauvegardeImpossible e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Menu d'édition d'une ligue
     */
    private void editerLigue(Ligue ligue) {
        if (ligue == null) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une ligue.",
                "Erreur",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        String[] options = {
            "Afficher",
            "Gérer employés",
            "Changer administrateur",
            "Changer nom",
            "Supprimer",
            "Retour"
        };
        
        int choix = JOptionPane.showOptionDialog(this,
            "Éditer: " + ligue.getNom(),
            "Menu Édition",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[5]);
        
        switch(choix) {
            case 0:
                afficher(ligue);
                break;
            case 1:
                gererEmployes(ligue);
                break;
            case 2:
                changerAdministrateur(ligue);
                break;
            case 3:
                changerNom(ligue);
                break;
            case 4:
                supprimerLigue(ligue);
                break;
            case 5:
                break;
        }
    }
    
    /**
     * Affiche les détails d'une ligue
     */
    private void afficher(Ligue ligue) {
        String admin = ligue.getAdministrateur() != null 
            ? ligue.getAdministrateur().toString() 
            : "Aucun administrateur";
        
        JOptionPane.showMessageDialog(this,
            ligue.toString() + "\nAdmin: " + admin,
            "Afficher Ligue",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Change le nom - Root ou Admin de la ligue
     */
    private void changerNom(Ligue ligue) {
        try {
            fenetre.getPermissionChecker().checkCanWrite(ligue);
            
            String nom = JOptionPane.showInputDialog(this, 
                "Nouveau nom:",
                ligue.getNom());
            
            if (nom != null && !nom.trim().isEmpty()) {
                ligue.setNom(nom.trim());
                chargerLigues();
                JOptionPane.showMessageDialog(this, "Nom modifié !");
            }
        } catch (DroitsInsuffisants e) {
            JOptionPane.showMessageDialog(this,
                fenetre.getPermissionChecker().getErrorMessage(),
                "❌ Accès refusé",
                JOptionPane.WARNING_MESSAGE);
        } catch (SauvegardeImpossible e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Change l'administrateur - Root ou Admin de la ligue
     */
    private void changerAdministrateur(Ligue ligue) {
        try {
            fenetre.getPermissionChecker().checkCanWrite(ligue);
            
            Object[] employes = ligue.getEmployes().toArray();
            
            if (employes.length == 0) {
                JOptionPane.showMessageDialog(this,
                    "Aucun employé dans cette ligue.",
                    "Erreur",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
 
            Object choix = JOptionPane.showInputDialog(
                this,
                "Choisir administrateur:",
                "Administrateur",
                JOptionPane.QUESTION_MESSAGE,
                null,
                employes,
                employes[0]
            );
 
            if (choix != null) {
                ligue.setAdministrateur((Employe) choix);
                JOptionPane.showMessageDialog(this, "Administrateur modifié !");
            }
        } catch (DroitsInsuffisants e) {
            JOptionPane.showMessageDialog(this,
                fenetre.getPermissionChecker().getErrorMessage(),
                "❌ Accès refusé",
                JOptionPane.WARNING_MESSAGE);
        } catch (SauvegardeImpossible e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Supprime une ligue - Root ou Admin de la ligue
     */
    private void supprimerLigue(Ligue ligue) {
        try {
            fenetre.getPermissionChecker().checkCanWrite(ligue);
            
            int confirmation = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer cette ligue ?\n" + ligue.getNom(),
                "Confirmation suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirmation == JOptionPane.YES_OPTION) {
                ligue.remove();
                chargerLigues();
                JOptionPane.showMessageDialog(this, "Ligue supprimée !");
            }
        } catch (DroitsInsuffisants e) {
            JOptionPane.showMessageDialog(this,
                fenetre.getPermissionChecker().getErrorMessage(),
                "❌ Accès refusé",
                JOptionPane.WARNING_MESSAGE);
        } catch (SauvegardeImpossible e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Gère les employés d'une ligue
     */
    private void gererEmployes(Ligue ligue) {
        try {
            fenetre.getPermissionChecker().checkCanManageEmployes(ligue);
            
            fenetre.setLiguePersonnels(ligue);
            fenetre.afficherPanel("personnels");
            
        } catch (DroitsInsuffisants e) {
            JOptionPane.showMessageDialog(this,
                fenetre.getPermissionChecker().getErrorMessage(),
                "Accès refusé",
                JOptionPane.WARNING_MESSAGE);
        }
    }
}