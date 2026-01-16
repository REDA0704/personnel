package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


import personnel.*;

class testLigue 
{
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	
	@Test
	void createLigue() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		assertEquals("Fléchettes", ligue.getNom());
	}

	

    
    @Test
    void removeLigue() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("Volleyball");
        gestionPersonnel.remove(ligue); 
        assertFalse(gestionPersonnel.getLigues().contains(ligue), "La ligue doit être supprimée");
    }
}
