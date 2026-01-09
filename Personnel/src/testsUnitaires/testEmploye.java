package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import personnel.*;


public class testEmploye {
		@Test
	    void datesCoherentes() throws SauvegardeImpossible
	    {
	        GestionPersonnel gp = GestionPersonnel.getGestionPersonnel();
	        Ligue ligue = gp.addLigue("Test");
	        Employe e = ligue.addEmploye(
	            "Dupont", "Jean", "j@j.fr", "pwd",
	            LocalDate.of(2020, 1, 1),
	            LocalDate.of(2022, 1, 1)
	        );
	
	        assertEquals(LocalDate.of(2020, 1, 1), e.getDateArrivee());
	        assertEquals(LocalDate.of(2022, 1, 1), e.getDateDepart());
	    }
	
	    @Test
	    void datesIncoherentes() throws SauvegardeImpossible
	    {
	        GestionPersonnel gp = GestionPersonnel.getGestionPersonnel();
	        Ligue ligue = gp.addLigue("Test2");
	
	        try
	        {
	            ligue.addEmploye(
	                "Martin", "Paul", "p@p.fr", "pwd",
	                LocalDate.of(2024, 1, 1),
	                LocalDate.of(2023, 1, 1)
	            );
	            fail("Une DateIncoherenteException était attendue");
	        }
	        catch (Employe.DateIncoherenteException e)
	        {
	            assertTrue(true);
	        }
	    }
	

}
