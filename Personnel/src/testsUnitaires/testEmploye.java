package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import personnel.*;
import personnel.Employe.DateIncoherenteException;


class testEmploye {
	
	GestionPersonnel gp = GestionPersonnel.getGestionPersonnel();
		@Test
	    void datesCoherentes() throws SauvegardeImpossible, DateIncoherenteException
	    {        
	        Ligue ligue = gp.addLigue("Test");
	        Employe e = ligue.addEmploye(
	            "Dupont", "Jean", "j@j.fr", "abc",
	            LocalDate.of(2020, 1, 1),
	            LocalDate.of(2022, 1, 1)
	        );
	        
	        // vérifie que dateDepart > dateArrivee
	        assertTrue(e.getDateDepart().isAfter(e.getDateArrivee()),
	                   "La date de départ doit être après la date d'arrivée");
	    }
	
	    @Test
	    void datesIncoherentes() throws SauvegardeImpossible
	    {
	    	Ligue ligue = gp.addLigue("Test");
	    	assertThrows(Employe.DateIncoherenteException.class, () -> {
	            ligue.addEmploye(
	                "Martin", "Paul", "p@p.fr", "pwd",
	                LocalDate.of(2024, 1, 1),  
	                LocalDate.of(2023, 1, 1)   
	            );
	        });
	    }
	    
	    @Test
	    void removeEmploye() throws SauvegardeImpossible, DateIncoherenteException {
	        
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
		void addEmploye() throws SauvegardeImpossible, DateIncoherenteException
		{
			Ligue ligue = gp.addLigue("Fléchettes");
			Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty",LocalDate.of(2023, 1, 1), LocalDate.of(2024, 1, 1)); 
			assertEquals(employe, ligue.getEmployes().first());
		}
		

	

}
