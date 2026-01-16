package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import personnel.*;


public class testEmploye {
	
	GestionPersonnel gp = GestionPersonnel.getGestionPersonnel();
		@Test
	    void datesCoherentes() throws SauvegardeImpossible
	    {
	        
	        Ligue ligue = gp.addLigue("Test");
	        Employe e = ligue.addEmploye(
	            "Dupont", "Jean", "j@j.fr", "pwd",
	            LocalDate.of(2020, 1, 1),
	            LocalDate.of(2022, 1, 1)
	        );
	        
		    e.setNom("Dupuis");
		    e.setPrenom("Marie");
		    e.setMail("m@m.fr");
		    e.setPassword("abcd");
		    e.setDateArrivee(LocalDate.of(2020,1,1));
		    e.setDateDepart(LocalDate.of(2023,1,1));
		    
	        assertEquals("Dupont", e.getNom());
	        assertEquals("Jean", e.getPrenom());
	        assertEquals("j@j.fr", e.getMail());
		    assertTrue(e.checkPassword("abcd"));
	        assertEquals(LocalDate.of(2020, 1, 1), e.getDateArrivee());
	        assertEquals(LocalDate.of(2022, 1, 1), e.getDateDepart());
	    }
	
	    @Test
	    void datesIncoherentes() throws SauvegardeImpossible
	    {
	        
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
	    
	    @Test
	    void removeEmploye() throws SauvegardeImpossible {
	        
	        Ligue ligue = gp.addLigue("Test3");
	        Employe e = ligue.addEmploye(
	                "Leroy", "Anne", "a@a.fr", "pwd",
	                LocalDate.of(2021, 1, 1),
	                LocalDate.of(2023, 1, 1)
	        );

	        e.remove();
	        assertFalse(ligue.getEmployes().contains(e));
	    }
	    
		@Test
		void addEmploye() throws SauvegardeImpossible
		{
			Ligue ligue = gp.addLigue("Fléchettes");
			Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty",LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1)); 
			assertEquals(employe, ligue.getEmployes().first());
		}
		

	

}
