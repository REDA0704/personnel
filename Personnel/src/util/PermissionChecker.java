package util;
 
import personnel.Employe;
import personnel.Ligue;
import personnel.DroitsInsuffisants;
 
/**
 * Vérifie les permissions des utilisateurs
 * Utilise l'exception DroitsInsuffisants existante
 */
public class PermissionChecker {
    
    private Employe utilisateur;
    
    public PermissionChecker(Employe utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    /**
     * Vérifie si l'utilisateur est le root (super-admin)
     */
    public boolean isRoot() {
        return utilisateur != null && utilisateur.getLigue() == null;
    }
    
    /**
     * Vérifie si l'utilisateur est administrateur d'une ligue
     */
    public boolean isAdminOfLigue(Ligue ligue) {
        if (ligue == null) return false;
        if (isRoot()) return true;
        
        Employe admin = ligue.getAdministrateur();
        return admin != null && admin.getId() == utilisateur.getId();
    }
    
    /**
     * Vérife si l'utilisateur peut écrire dans une ligue
     * Lève DroitsInsuffisants si non
     */
    public void checkCanWrite(Ligue ligue) throws DroitsInsuffisants {
        // Root peut tout faire
        if (isRoot()) return;
        
        // Admin de la ligue peut écrire
        if (isAdminOfLigue(ligue)) return;
        
        // Sinon, erreur
        throw new DroitsInsuffisants();
    }
    
    /**
     * Vérifie si l'utilisateur peut gérer les ligues
     * Seul le root peut le faire
     */
    public void checkCanManageLigues() throws DroitsInsuffisants {
        if (!isRoot()) {
            throw new DroitsInsuffisants();
        }
    }
    
    /**
     * Vérifie si l'utilisateur peut gérer les employés d'une ligue
     */
    public void checkCanManageEmployes(Ligue ligue) throws DroitsInsuffisants {
        checkCanWrite(ligue);
    }
    
    /**
     * Message d'erreur personnalisé selon le rôle
     */
    public String getErrorMessage() {
        if (utilisateur == null) {
            return "Erreur: Utilisateur non authentifié";
        }
        
        if (isRoot()) {
            return "Erreur inattendue pour l'administrateur";
        }
        
        if (utilisateur.getLigue() != null) {
            return "❌ Vous n'avez pas les droits pour effectuer cette opération.\n" +
                   "Seul l'administrateur de votre ligue ou le super-administrateur peut le faire.";
        }
        
        return "❌ Vous n'avez pas les droits suffisants.";
    }
}