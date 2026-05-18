package ui;
 
import javax.swing.*;
import javax.swing.border.*;
import personnel.Employe;
import util.Password;
import java.awt.*;
 
/**
 * Panel pour la gestion du compte utilisateur.
 * Permet de changer le nom, prénom, email, mot de passe, etc.
 * ✅ Vérification sécurisée du mot de passe avec BCrypt
 * ✅ Sauvegarde en BD après modification
 */
public class PanelCompte extends JPanel {
    
    private FenetrePrincipale fenetre;
    private Employe employe;
    
    // Déclaration des champs AVANT le constructeur
    private JTextField tfNom;
    private JTextField tfPrenom;
    private JTextField tfEmail;
    private JPasswordField pfAncien;
    private JPasswordField pfNouveau;
    
    public PanelCompte(FenetrePrincipale fenetre) {
        this.fenetre = fenetre;
        
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setBackground(new Color(248, 249, 251));
        
        // Titre
        JLabel titre = new JLabel("Gérer le Compte");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setForeground(new Color(33, 33, 33));
        this.add(titre, BorderLayout.NORTH);
        
        // Formulaire
        JPanel formulaire = new JPanel();
        formulaire.setLayout(new GridLayout(5, 2, 10, 10));
        formulaire.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formulaire.setBackground(new Color(248, 249, 251));
        
        // === NOM ===
        JLabel labelNom = new JLabel("Nom:");
        labelNom.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formulaire.add(labelNom);
        tfNom = new JTextField();
        tfNom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfNom.setBorder(new LineBorder(new Color(200, 210, 220), 1));
        formulaire.add(tfNom);
        
        // === PRÉNOM ===
        JLabel labelPrenom = new JLabel("Prénom:");
        labelPrenom.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formulaire.add(labelPrenom);
        tfPrenom = new JTextField();
        tfPrenom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfPrenom.setBorder(new LineBorder(new Color(200, 210, 220), 1));
        formulaire.add(tfPrenom);
        
        // === EMAIL ===
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formulaire.add(labelEmail);
        tfEmail = new JTextField();
        tfEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfEmail.setBorder(new LineBorder(new Color(200, 210, 220), 1));
        formulaire.add(tfEmail);
        
        // === ANCIEN MOT DE PASSE ===
        JLabel labelAncien = new JLabel("Ancien mot de passe:");
        labelAncien.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formulaire.add(labelAncien);
        pfAncien = new JPasswordField();
        pfAncien.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pfAncien.setBorder(new LineBorder(new Color(200, 210, 220), 1));
        formulaire.add(pfAncien);
        
        // === NOUVEAU MOT DE PASSE ===
        JLabel labelNouveau = new JLabel("Nouveau mot de passe:");
        labelNouveau.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formulaire.add(labelNouveau);
        pfNouveau = new JPasswordField();
        pfNouveau.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pfNouveau.setBorder(new LineBorder(new Color(200, 210, 220), 1));
        formulaire.add(pfNouveau);
        
        this.add(formulaire, BorderLayout.CENTER);
        
        // === BOUTONS ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBoutons.setBackground(new Color(248, 249, 251));
        
        JButton btnSauvegarder = new JButton("Sauvegarder");
        btnSauvegarder.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSauvegarder.setBackground(new Color(41, 98, 255));
        btnSauvegarder.setForeground(Color.WHITE);
        btnSauvegarder.setFocusPainted(false);
        btnSauvegarder.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSauvegarder.addActionListener(e -> sauvegarder());
        panelBoutons.add(btnSauvegarder);
        
        JButton btnReinitialiser = new JButton("Réinitialiser");
        btnReinitialiser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnReinitialiser.setBackground(new Color(100, 150, 100));
        btnReinitialiser.setForeground(Color.WHITE);
        btnReinitialiser.setFocusPainted(false);
        btnReinitialiser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReinitialiser.addActionListener(e -> remplirChamps());
        panelBoutons.add(btnReinitialiser);
        
        JButton btnBack = new JButton("Retour");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setBackground(new Color(150, 150, 150));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> fenetre.afficherPanel("accueil"));
        panelBoutons.add(btnBack);
        
        this.add(panelBoutons, BorderLayout.SOUTH);
        
        // ✅ IMPORTANT : Récupère l'employé connecté APRÈS avoir créé tous les champs
        this.employe = fenetre.getEmployeConnecte();
        
        // ✅ PUIS remplit les champs avec les données de l'employé
        remplirChamps();
    }
    
    /**
     * Remplit les champs texte avec les données de l'employé
     */
    private void remplirChamps() {
        if (employe != null) {
            tfNom.setText(employe.getNom());
            tfPrenom.setText(employe.getPrenom());
            tfEmail.setText(employe.getMail());
            pfAncien.setText("");
            pfNouveau.setText("");
        }
    }
    
    /**
     * Sauvegarde les modifications (avec vérification sécurisée du mot de passe)
     */
    private void sauvegarder() {
        try {
 
            if (employe == null) {
                JOptionPane.showMessageDialog(this,
                    "Aucun employé sélectionné");
                return;
            }
 
            String nomSaisi = tfNom.getText().trim();
            String prenomSaisi = tfPrenom.getText().trim();
            String emailSaisi = tfEmail.getText().trim();
 
            String ancienMdp = new String(pfAncien.getPassword()).trim();
            String nouveauMdp = new String(pfNouveau.getPassword()).trim();
 
            // Sauvegarder le hash actuel
            String ancienHash = employe.getPassword();
 
            employe.setNom(nomSaisi);
            employe.setPrenom(prenomSaisi);
            employe.setMail(emailSaisi);
 
            // Seulement si l'utilisateur veut changer le mdp
            if (!nouveauMdp.isEmpty()) {
 
                if (!Password.verify(ancienMdp, ancienHash)) {
                    JOptionPane.showMessageDialog(this,
                        "Ancien mot de passe incorrect");
                    return;
                }
 
                employe.setPassword(
                    Password.hash(nouveauMdp)
                );
            } else {
                // ✅ GARDE le mot de passe actuel (NE PAS LE CHANGER)
                employe.setPassword(ancienHash);
            }
 
            // ✅ SAUVEGARDER EN BD
            try {
                fenetre.getGestionPersonnel().update(employe);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la sauvegarde en BD: " + e.getMessage(),
                    "Erreur BD",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
 
            JOptionPane.showMessageDialog(this,
                "✅ Modifications sauvegardées en BD");
 
            // Réinitialiser les champs de mot de passe
            pfAncien.setText("");
            pfNouveau.setText("");
 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}