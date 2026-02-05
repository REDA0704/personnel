package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.util.ArrayList;

import commandLineMenus.List;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import personnel.*;

public class LigueConsole 
{
	private GestionPersonnel gestionPersonnel;
	private EmployeConsole employeConsole;

	public LigueConsole(GestionPersonnel gestionPersonnel, EmployeConsole employeConsole)
	{
		this.gestionPersonnel = gestionPersonnel;
		this.employeConsole = employeConsole;
	}

	Menu menuLigues()
	{
		Menu menu = new Menu("Gérer les ligues", "l");
		menu.add(afficherLigues());
		menu.add(ajouterLigue());
		menu.add(selectionnerLigue());
		menu.addBack("q");
		return menu;
	}

	private Option afficherLigues()
	{
		return new Option("Afficher les ligues", "l", () -> {System.out.println(gestionPersonnel.getLigues());});
	}

	private Option afficher(final Ligue ligue)
	{
		return new Option("Afficher la ligue", "l", 
				() -> 
				{
					System.out.println(ligue);
					System.out.println("administrée par " + ligue.getAdministrateur());
				}
		);
	}
	private Option afficherEmployes(final Ligue ligue)
	{
		return new Option("Afficher les employes", "l", () -> {System.out.println(ligue.getEmployes());});
	}

	private Option ajouterLigue()
	{
		return new Option("Ajouter une ligue", "a", () -> 
		{
			try
			{
				gestionPersonnel.addLigue(getString("nom : "));
			}
			catch(SauvegardeImpossible exception)
			{
				System.err.println("Impossible de sauvegarder cette ligue");
			}
		});
	}
	
	private Menu editerLigue(Ligue ligue)
	{
		Menu menu = new Menu("Editer " + ligue.getNom());
		menu.add(afficher(ligue));
		menu.add(gererEmployes(ligue));
		menu.add(changerAdministrateur(ligue));
		menu.add(changerNom(ligue));
		menu.add(supprimer(ligue));
		menu.addBack("q");
		return menu;
	}

	private Option changerNom(final Ligue ligue)
	{
		return new Option("Renommer", "r", 
				() -> {ligue.setNom(getString("Nouveau nom : "));});
	}

	private List<Ligue> selectionnerLigue()
	{
		return new List<Ligue>("Sélectionner une ligue", "e", 
				() -> new ArrayList<>(gestionPersonnel.getLigues()),
				(element) -> editerLigue(element)
				);
	}
	
	
	private List<Employe> selectionnerEmploye(Ligue ligue)
	{
		return new List<Employe>("Sélectionner un employé", "r", 
				() -> new ArrayList<>(ligue.getEmployes()),
				(employe) -> menuEmploye(ligue, employe)
				);
	}
	
	
	private Menu menuEmploye(Ligue ligue, Employe employe)
	{
		Menu menu = new Menu("Editer " + employe.getNom());
		menu.add(employeConsole.editerEmploye(employe)); 
	    menu.add(supprimerEmploye(ligue));
		menu.addBack("q");
		return menu;
	}

	
	private Option ajouterEmploye(final Ligue ligue)
	{
		return new Option("ajouter un employé", "a",
				() -> 
				{
		            try
		            {
		                String nom = getString("nom : ");
		                String prenom = getString("prenom : ");
		                String mail = getString("mail : ");
		                String password = getString("password : ");

		                LocalDate dateArrivee =
		                    LocalDate.parse(getString("date d'arrivée (AAAA-MM-JJ) : "));
		                LocalDate dateDepart =
		                    LocalDate.parse(getString("date de départ (AAAA-MM-JJ) : "));

		                ligue.addEmploye(
		                    nom,
		                    prenom,
		                    mail,
		                    password,
		                    dateArrivee,
		                    dateDepart
		                );
		            }
		            catch (DateTimeParseException e)
		            {
		                System.err.println("Format de date invalide (AAAA-MM-JJ attendu)");
		            }
		            catch (Employe.DateIncoherenteException e)
		            {
		                System.err.println("Erreur : " + e.getMessage());
		            }
		        }
		    );
	}
	
	private Menu gererEmployes(Ligue ligue)
	{
		Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
		menu.add(afficherEmployes(ligue));
		menu.add(ajouterEmploye(ligue));
		//menu.add(modifierEmploye(ligue));
		//menu.add(supprimerEmploye(ligue));
		menu.add(selectionnerEmploye(ligue));
		menu.addBack("q");
		return menu;
	}

	private List<Employe> supprimerEmploye(final Ligue ligue)
	{
		return new List<>("Supprimer un employé", "s", 
				() -> new ArrayList<>(ligue.getEmployes()),
				(index, element) -> {element.remove();}
				);
	}
	
	private List<Employe> changerAdministrateur(final Ligue ligue)
	{
	    return new List<>(
	            "Changer l'administrateur",
	            "c",
	            () -> new ArrayList<>(ligue.getEmployes()),
	            (index, employe) ->
	            {
	                try
	                {
	                    ligue.setAdministrateur(employe);
	                }
	                catch (DroitsInsuffisants e)
	                {
	                    System.err.println("Droits insuffisants pour définir cet administrateur");
	                }
	            }
	        );
	}		

	private List<Employe> modifierEmploye(final Ligue ligue)
	{
		return new List<>("Modifier un employé", "e", 
				() -> new ArrayList<>(ligue.getEmployes()),
				employeConsole.editerEmploye()
				);
	}
	
	private Option supprimer(Ligue ligue)
	{
		return new Option("Supprimer", "d", () -> {ligue.remove();});
	}
	
}
