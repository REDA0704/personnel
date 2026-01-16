package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import personnel.*;


public class testGestionPersonnel {
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	

	
    @Test
    void changeAdmin() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("Admins");
        Employe e = ligue.addEmploye(
                "Martin", "Paul", "p@p.fr", "pwd",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1)
        );
        ligue.setAdministrateur(e);
        assertEquals(e, ligue.getAdministrateur());
    }
    
    
	@Test
	void testRemoveAdmin() throws SauvegardeImpossible {
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
