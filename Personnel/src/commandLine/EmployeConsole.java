package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import commandLineMenus.ListOption;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import personnel.Employe;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import personnel.Employe.DateIncoherenteException;
import personnel.SauvegardeImpossible;

public class EmployeConsole 
{
	private Option afficher(final Employe employe)
	{
		return new Option("Afficher l'employé", "l", () -> {System.out.println(employe);});
	}

	ListOption<Employe> editerEmploye()
	{
		return (employe) -> editerEmploye(employe);		
	}

	Option editerEmploye(Employe employe)
	{
			Menu menu = new Menu("Gérer le compte " + employe.getNom(), "c");
			menu.add(afficher(employe));
			menu.add(changerNom(employe));
			menu.add(changerPrenom(employe));
			menu.add(changerMail(employe));
			menu.add(changerPassword(employe));
			menu.add(changerDateArrivee(employe));
			menu.add(changerDateDepart(employe));
			menu.addBack("q");
			return menu;
	}

	private Option changerNom(final Employe employe)
	{
		return new Option("Changer le nom", "n", 
				() -> {try {
					employe.setNom(getString("Nouveau nom : "));
				} catch (SauvegardeImpossible e) {
					e.printStackTrace();
				}}
			);
	}
	
	private Option changerPrenom(final Employe employe)
	{
		return new Option("Changer le prénom", "p", () -> {try {
			employe.setPrenom(getString("Nouveau prénom : "));
		} catch (SauvegardeImpossible e) {
			e.printStackTrace();
		}});
	}
	
	private Option changerMail(final Employe employe)
	{
		return new Option("Changer le mail", "e", () -> {try {
			employe.setMail(getString("Nouveau mail : "));
		} catch (SauvegardeImpossible e) {
			e.printStackTrace();
		}});
	}
	
	private Option changerPassword(final Employe employe)
	{
		return new Option("Changer le password", "x", () -> {try {
			employe.setPassword(getString("Nouveau password : "));
		} catch (SauvegardeImpossible e) {
			e.printStackTrace();
		}});
	}
	
	private Option changerDateArrivee(final Employe employe)
	{
	    return new Option("Changer la date d'arrivée", "d",
	        () -> {
	            try
	            {
						employe.setDateArrivee(
						    LocalDate.parse(getString("Date d'arrivée (YYYY-MM-DD) : "))
						);
				} 
	            catch (DateTimeParseException e)
	            {
	                System.out.println("Format de date invalide");
	            }
	            catch (DateIncoherenteException e)
	            {
	                System.out.println(e.getMessage());
	            } catch (SauvegardeImpossible e) {
					e.printStackTrace();
				}

	        }
	    );
	}
	
	private Option changerDateDepart(final Employe employe)
	{
	    return new Option("Changer la date de départ", "f",
	        () -> {
	            try
	            {
	                employe.setDateDepart(
	                    LocalDate.parse(getString("Date de départ (YYYY-MM-DD) : "))
	                );
	            }
	            catch (DateTimeParseException e)
	            {
	                System.out.println("Format de date invalide");
	            }
	            catch (DateIncoherenteException e)
	            {
	                System.out.println(e.getMessage());
	            } catch (SauvegardeImpossible e) {
					e.printStackTrace();
				}
	        }
	    );
	}
}
