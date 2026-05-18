package ui;
 
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import personnel.*;
import util.Password;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
 
/**
 * Panel pour la gestion des employés d'une ligue.
 * Affiche la liste des employés avec double-clic pour éditer
 */
public class PanelGestionPersonnels extends JPanel {
    
    private GestionPersonnel gestionPersonnel;
    private FenetrePrincipale fenetre;
    private JList<Employe> personnelList;
    private DefaultListModel<Employe> listModel;
    private Ligue ligue;
    
    public PanelGestionPersonnels(GestionPersonnel gestionPersonnel, FenetrePrincipale fenetre) {
        this.gestionPersonnel = gestionPersonnel;
        this.fenetre = fenetre;
        
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(new Color(248, 249, 251));
        
        // === TITRE ===
        JLabel titre = new JLabel("Gérer les Employés");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(33, 33, 33));
        this.add(titre, BorderLayout.NORTH);
        
        // === LISTE DES EMPLOYÉS ===
        listModel = new DefaultListModel<>();
        personnelList = new JList<>(listModel);
        personnelList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        personnelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        personnelList.setBackground(Color.WHITE);
        personnelList.setBorder(new LineBorder(new Color(200, 210, 220), 1));
        
        // ✅ DOUBLE-CLIC pour éditer
        personnelList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = personnelList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Employe employe = listModel.getElementAt(index);
                        editerPersonnel(employe);
                    }
                }
            }
        });
        
        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(personnelList);
        scrollPane.setBorder(new LineBorder(new Color(200, 210, 220), 1));
        this.add(scrollPane, BorderLayout.CENTER);
        
        // === BOUTONS ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBoutons.setBackground(new Color(248, 249, 251));
        
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAjouter.setBackground(new Color(100, 150, 100));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.setFocusPainted(false);
        btnAjouter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAjouter.addActionListener(e -> ajouterPersonnel());
        panelBoutons.add(btnAjouter);
        
        JButton btnBack = new JButton("Retour");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setBackground(new Color(150, 150, 150));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> fenetre.afficherPanel("ligues"));
        panelBoutons.add(btnBack);
        
        this.add(panelBoutons, BorderLayout.SOUTH);
    }
    
    /**
     * Définit la ligue dont gérer les employés
     */
    public void setLigue(Ligue ligue) {
        this.ligue = ligue;
        chargerEmployes();
    }
    
    /**
     * Charge les employés de la ligue
     */
    private void chargerEmployes() {
        listModel.clear();
        if (ligue != null) {
            for (Employe emp : ligue.getEmployes()) {
                listModel.addElement(emp);
            }
        }
    }
    
    /**
     * Ajoute un nouvel employé à la ligue
     */
    private void ajouterPersonnel() {
        if (ligue == null) {
            JOptionPane.showMessageDialog(this,
                "Veuillez d'abord sélectionner une ligue.",
                "Erreur",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String nom = JOptionPane.showInputDialog(this, "Nom:");
            if (nom == null || nom.trim().isEmpty()) return;
 
            String prenom = JOptionPane.showInputDialog(this, "Prénom:");
            if (prenom == null || prenom.trim().isEmpty()) return;
 
            String mail = JOptionPane.showInputDialog(this, "Email:");
            if (mail == null || mail.trim().isEmpty()) return;
 
            String password = JOptionPane.showInputDialog(this, "Mot de passe:");
            if (password == null || password.trim().isEmpty()) return;
 
            String dateArriveeStr = JOptionPane.showInputDialog(this, "Date d'arrivée (AAAA-MM-JJ):");
            if (dateArriveeStr == null || dateArriveeStr.trim().isEmpty()) return;
            LocalDate dateArrivee = LocalDate.parse(dateArriveeStr);
 
            String dateDepartStr = JOptionPane.showInputDialog(this, "Date de départ (AAAA-MM-JJ) ou laisser vide:");
            LocalDate dateDepart = null;
            if (dateDepartStr != null && !dateDepartStr.trim().isEmpty()) {
                dateDepart = LocalDate.parse(dateDepartStr);
            }
 
            // Ajouter l'employé
            ligue.addEmploye(nom.trim(), prenom.trim(), mail.trim(), password.trim(), dateArrivee, dateDepart);
            chargerEmployes();
            JOptionPane.showMessageDialog(this, "Employé ajouté !");
 
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Format de date invalide (utilisez AAAA-MM-JJ)",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Édite un employé (double-clic ou bouton)
     */
    private void editerPersonnel(Employe employe) {
        if (employe == null) {
            employe = personnelList.getSelectedValue();
        }
        
        if (employe == null) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un employé.",
                "Erreur",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        String[] options = {
            "Afficher",
            "Changer nom",
            "Changer prénom",
            "Changer email",
            "Changer mot de passe",
            "Changer date d'arrivée",
            "Changer date de départ",
            "Supprimer",
            "Retour"
        };
        
        int choix = JOptionPane.showOptionDialog(this,
            "Éditer: " + employe.getPrenom() + " " + employe.getNom(),
            "Menu Édition",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[8]); // Retour par défaut
        
        switch(choix) {
            case 0: // Afficher
                afficherPersonnel(employe);
                break;
            case 1: // Changer nom
                changerNom(employe);
                break;
            case 2: // Changer prénom
                changerPrenom(employe);
                break;
            case 3: // Changer email
                changerEmail(employe);
                break;
            case 4: // Changer mot de passe
                changerPassword(employe);
                break;
            case 5: // Changer date d'arrivée
                changerDateArrivee(employe);
                break;
            case 6: // Changer date de départ
                changerDateDepart(employe);
                break;
            case 7: // Supprimer
                supprimerPersonnel(employe);
                break;
            case 8: // Retour
                // Rien à faire
                break;
        }
    }
    
    /**
     * Affiche les détails d'un employé
     */
    private void afficherPersonnel(Employe emp) {
        JOptionPane.showMessageDialog(this,
            emp.toString(),
            "Afficher Employé",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Change le nom d'un employé
     */
    private void changerNom(Employe emp) {
        String nouveauNom = JOptionPane.showInputDialog(this,
            "Nouveau nom:",
            emp.getNom());
        
        if (nouveauNom != null && !nouveauNom.trim().isEmpty()) {
            try {
                emp.setNom(nouveauNom.trim());
                gestionPersonnel.update(emp);
                chargerEmployes();
                JOptionPane.showMessageDialog(this, "Nom modifié !");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Change le prénom d'un employé
     */
    private void changerPrenom(Employe emp) {
        String nouveauPrenom = JOptionPane.showInputDialog(this,
            "Nouveau prénom:",
            emp.getPrenom());
        
        if (nouveauPrenom != null && !nouveauPrenom.trim().isEmpty()) {
            try {
                emp.setPrenom(nouveauPrenom.trim());
                gestionPersonnel.update(emp);
                chargerEmployes();
                JOptionPane.showMessageDialog(this, "Prénom modifié !");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Change l'email d'un employé
     */
    private void changerEmail(Employe emp) {
        String nouvelEmail = JOptionPane.showInputDialog(this,
            "Nouvel email:",
            emp.getMail());
        
        if (nouvelEmail != null && !nouvelEmail.trim().isEmpty()) {
            try {
                emp.setMail(nouvelEmail.trim());
                gestionPersonnel.update(emp);
                chargerEmployes();
                JOptionPane.showMessageDialog(this, "Email modifié !");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Change le mot de passe d'un employé
     */
    private void changerPassword(Employe emp) {
        String nouveauPassword = JOptionPane.showInputDialog(this,
            "Nouveau mot de passe:");
        
        if (nouveauPassword != null && !nouveauPassword.trim().isEmpty()) {
            try {
                if (nouveauPassword.length() < 6) {
                    JOptionPane.showMessageDialog(this,
                        "Le mot de passe doit contenir au moins 6 caractères",
                        "Erreur",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Hacher le mot de passe
                String mdpHash = Password.hash(nouveauPassword.trim());
                emp.setPassword(mdpHash);
                gestionPersonnel.update(emp);
                chargerEmployes();
                JOptionPane.showMessageDialog(this, "Mot de passe modifié !");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Change la date d'arrivée d'un employé
     */
    private void changerDateArrivee(Employe emp) {
        String nouvelleDateStr = JOptionPane.showInputDialog(this,
            "Nouvelle date d'arrivée (AAAA-MM-JJ):",
            emp.getDateArrivee().toString());
        
        if (nouvelleDateStr != null && !nouvelleDateStr.trim().isEmpty()) {
            try {
                LocalDate nouvelleDate = LocalDate.parse(nouvelleDateStr);
                emp.setDateArrivee(nouvelleDate);
                gestionPersonnel.update(emp);
                chargerEmployes();
                JOptionPane.showMessageDialog(this, "Date d'arrivée modifiée !");
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this,
                    "Format de date invalide (utilisez AAAA-MM-JJ)",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Change la date de départ d'un employé
     */
    private void changerDateDepart(Employe emp) {
        String dateDepartActuelle = emp.getDateDepart() != null 
            ? emp.getDateDepart().toString() 
            : "";
        
        String nouvelleDateStr = JOptionPane.showInputDialog(this,
            "Nouvelle date de départ (AAAA-MM-JJ) ou laisser vide:",
            dateDepartActuelle);
        
        if (nouvelleDateStr != null) {
            try {
                LocalDate nouvelleDate = null;
                if (!nouvelleDateStr.trim().isEmpty()) {
                    nouvelleDate = LocalDate.parse(nouvelleDateStr);
                }
                emp.setDateDepart(nouvelleDate);
                gestionPersonnel.update(emp);
                chargerEmployes();
                JOptionPane.showMessageDialog(this, "Date de départ modifiée !");
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this,
                    "Format de date invalide (utilisez AAAA-MM-JJ)",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Supprime un employé
     */
    private void supprimerPersonnel(Employe emp) {
        int confirmation = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer ?\n" + emp.getPrenom() + " " + emp.getNom(),
            "Confirmation suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                emp.remove();
                chargerEmployes();
                JOptionPane.showMessageDialog(this, "Employé supprimé !");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}