package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import personnel.*;
import personnel.Employe.DateIncoherenteException;


class testGestionPersonnel {
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	

	
    @Test
    void changeAdmin() throws SauvegardeImpossible, DateIncoherenteException {
        Ligue ligue = gestionPersonnel.addLigue("Admins");
        
        Employe employe1 = ligue.addEmploye("Dupont", "Jean", "j@j.fr", "pwd1",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1));
        Employe employe2 = ligue.addEmploye("Martin", "Paul", "p@p.fr", "pwd2",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1));

        ligue.setAdministrateur(employe1);
        assertEquals(employe1, ligue.getAdministrateur());
        
        ligue.setAdministrateur(employe2);
        assertEquals(employe2, ligue.getAdministrateur()); // Vérifie le nouvel admin
        assertNotEquals(employe1, ligue.getAdministrateur()); // Vérifie que ce n'est plus l'ancien
    }
    
    
	@Test
	void testRemoveAdmin() throws SauvegardeImpossible, DateIncoherenteException {
		Ligue ligue = gestionPersonnel.addLigue("Test");
	    Employe employe = ligue.addEmploye("Dupont", "Jean", "j@j.fr", "pwd", LocalDate.of(2020,1,1), LocalDate.of(2022,1,1));
	    ligue.setAdministrateur(employe);
	    employe.remove();
	    assertEquals(gestionPersonnel.getRoot(), ligue.getAdministrateur());
	}
	
	
    @Test
    void getRoot() {
        GestionPersonnel gp = GestionPersonnel.getGestionPersonnel();
        Employe root = gp.getRoot();
        assertNotNull(root, "Le root doit exister");
        assertTrue(root.estRoot(), "Le root doit être identifié comme root");
    }
}
