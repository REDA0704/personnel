package ui;
 
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import util.Password;
import personnel.Employe;
import personnel.GestionPersonnel;
 
/**
 * Panel de connexion - Première page de l'application
 * Vérifie les identifiants contre les objets en mémoire (GestionPersonnel)
 * Design moderne et professionnel
 */
public class LoginPanel extends JPanel {
    
    private JTextField mailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private LoginCallback loginCallback;
    private GestionPersonnel gestionPersonnel;
    
    // Couleurs
    private static final Color PRIMARY_COLOR = new Color(41, 98, 255);      // Bleu moderne
    private static final Color SECONDARY_COLOR = new Color(30, 41, 82);     // Bleu foncé
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 251); // Gris très clair
    private static final Color TEXT_COLOR = new Color(33, 33, 33);          // Gris foncé
    private static final Color ERROR_COLOR = new Color(220, 53, 69);        // Rouge
    private static final Color INPUT_BORDER = new Color(200, 210, 220);     // Gris bleu
    
    // Interface pour notifier quand la connexion réussit
    public interface LoginCallback {
        void onLoginSuccess(Employe employe);
    }
    
    public LoginPanel(GestionPersonnel gestionPersonnel, LoginCallback callback) {
        this.gestionPersonnel = gestionPersonnel;
        this.loginCallback = callback;
        initialiserUI();
    }
    
    private void initialiserUI() {
        this.setLayout(new GridBagLayout());
        this.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        
        // Panel blanc arrondi pour le formulaire
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fond blanc avec ombre
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
                
                super.paintComponent(g);
            }
        };
        
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        formPanel.setPreferredSize(new Dimension(400, 480));
        formPanel.setMaximumSize(new Dimension(400, 480));
        
        // === TITRE ===
        JLabel titleLabel = new JLabel("Connexion");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Sous-titre
        JLabel subtitleLabel = new JLabel("Accédez à votre compte");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(120, 130, 140));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(subtitleLabel);
        formPanel.add(Box.createVerticalStrut(30));
        
        // === MESSAGE D'ERREUR ===
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setForeground(ERROR_COLOR);
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(messageLabel);
        formPanel.add(Box.createVerticalStrut(10));
        
        // === CHAMP EMAIL ===
        formPanel.add(creerLabelChamp("Email"));
        mailField = creerChampsTexte();
        mailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        formPanel.add(mailField);
        formPanel.add(Box.createVerticalStrut(20));
        
        // === CHAMP MOT DE PASSE ===
        formPanel.add(creerLabelChamp("Mot de passe"));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        passwordField.setBorder(new LineBorder(INPUT_BORDER, 1));
        passwordField.setBackground(new Color(255, 255, 255));
        passwordField.setForeground(TEXT_COLOR);
        passwordField.setCaretColor(PRIMARY_COLOR);
        passwordField.setMargin(new Insets(8, 12, 8, 12));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(25));
        
        // === BOUTON CONNEXION ===
        loginButton = new JButton("Se connecter") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Couleur bleu
                g2.setColor(PRIMARY_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                super.paintComponent(g);
            }
        };
        
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setBorder(null);
        loginButton.setFocusPainted(false);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setOpaque(false);
        
        // Effet hover
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(30, 85, 220));
                loginButton.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(PRIMARY_COLOR);
                loginButton.repaint();
            }
        });
        
        loginButton.addActionListener(e -> verifierConnexion());
        formPanel.add(loginButton);
        
        formPanel.add(Box.createVerticalStrut(15));
        
        // === TEXTE INFORMATIF ===
        JLabel infoLabel = new JLabel("<html><center>Utilisez vos identifiants<br>pour accéder à l'application</center></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(150, 160, 170));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(infoLabel);
        
        // Ajouter le panel au centre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        this.add(formPanel, gbc);
        
        // Permettre la connexion avec Entrée
        KeyListener enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    verifierConnexion();
                }
            }
        };
        mailField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);
    }
    
    private JLabel creerLabelChamp(String texte) {
        JLabel label = new JLabel(texte);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JTextField creerChampsTexte() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new LineBorder(INPUT_BORDER, 1));
        field.setBackground(new Color(255, 255, 255));
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(PRIMARY_COLOR);
        field.setMargin(new Insets(8, 12, 8, 12));
        return field;
    }
    
    private void verifierConnexion() {
        String mail = mailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validation
        if (mail.isEmpty() || password.isEmpty()) {
            afficherErreur("Veuillez remplir tous les champs");
            return;
        }
        
        if (!mail.contains("@")) {
            afficherErreur("Email invalide");
            return;
        }
        
        // Chercher l'employé par mail dans GestionPersonnel
        Employe employe = gestionPersonnel.getEmployeParMail(mail);
        
        if (employe != null) {
            // Vérifier le mot de passe avec BCrypt
            if (Password.verify(password, employe.getPassword())) {
                messageLabel.setText("");
                loginCallback.onLoginSuccess(employe);
            } else {
                afficherErreur("❌ Email ou mot de passe incorrect");
                passwordField.setText("");
            }
        } else {
            afficherErreur("❌ Email ou mot de passe incorrect");
            passwordField.setText("");
        }
    }
    
    private void afficherErreur(String message) {
        messageLabel.setText(message);
    }
    
    public void effacerChamps() {
        mailField.setText("");
        passwordField.setText("");
        messageLabel.setText("");
    }
}