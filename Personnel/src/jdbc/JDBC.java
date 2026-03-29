package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;

import personnel.*;

public class JDBC implements Passerelle 
{
	Connection connection;

	public JDBC()
	{
		try
		{
			Class.forName(Credentials.getDriverClassName());
			connection = DriverManager.getConnection(Credentials.getUrl(), Credentials.getUser(), Credentials.getPassword());
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Pilote JDBC non installé.");
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	@Override
	public GestionPersonnel getGestionPersonnel() 
	{
		GestionPersonnel gestionPersonnel = new GestionPersonnel();
		try 
		{
			// Charger le ROOT
			String requeteRoot = "SELECT * FROM employe WHERE num_ligue IS NULL LIMIT 1";
	        Statement stmtRoot = connection.createStatement();
	        ResultSet rsRoot = stmtRoot.executeQuery(requeteRoot);
	        
	        if (rsRoot.next()) {
                gestionPersonnel.addRoot(
                    rsRoot.getInt("num_employe"),
                    rsRoot.getString("nom"),
                    rsRoot.getString("prenom"),
                    rsRoot.getString("mail"),
                    rsRoot.getString("password"),
                    rsRoot.getDate("date_arrivee").toLocalDate(),
                    rsRoot.getDate("date_depart").toLocalDate()
                );
	        }

	        // Charger les ligues
			String requete = "SELECT * FROM ligue";
			Statement instruction = connection.createStatement();
			ResultSet ligues = instruction.executeQuery(requete);
			
			while (ligues.next()) {
				int idLigue = ligues.getInt("num_ligue");
	            String nomLigue = ligues.getString("nom");

	            // Ajouter la ligue à gestionPersonnel
	            Ligue ligue = gestionPersonnel.addLigue(idLigue, nomLigue);

	            // Charger les employés de cette ligue
	            String requeteEmp = "SELECT * FROM employe WHERE num_ligue = ?";
	            PreparedStatement stmtEmp = connection.prepareStatement(requeteEmp);
	            stmtEmp.setInt(1, idLigue);
	            ResultSet rsEmp = stmtEmp.executeQuery();

	            while (rsEmp.next()) {
		            String nomEmp = rsEmp.getString("nom");
		            String prenomEmp = rsEmp.getString("prenom");
		            String mailEmp = rsEmp.getString("mail");
		            String passwordEmp = rsEmp.getString("password");
		            LocalDate dateArr  = rsEmp.getDate("date_arrivee").toLocalDate();
		            LocalDate dateDep = rsEmp.getDate("date_depart").toLocalDate();
	                ligue.addEmploye(nomEmp, prenomEmp, mailEmp, passwordEmp, dateArr, dateDep);
	            }
	            
	            // Charger l'administrateur de la ligue
	            String requeteAdmin = "SELECT num_employe FROM ADMINISTRER WHERE num_ligue = ?";
	            PreparedStatement stmtAdmin = connection.prepareStatement(requeteAdmin);
	            stmtAdmin.setInt(1, idLigue);
	            ResultSet rsAdmin = stmtAdmin.executeQuery();

	            if (rsAdmin.next()) {
	                int idAdmin = rsAdmin.getInt("num_employe");

	                // retrouver l'employé correspondant
	                for (Employe emp : ligue.getEmployes()) {
	                    if (emp.getId() == idAdmin) {
	                        ligue.setAdministrateur(emp);
	                        break;
	                    }
	                }
	            }
	        }
		}
		
		catch (SQLException | SauvegardeImpossible | Employe.DateIncoherenteException e)
		{
	        e.printStackTrace();
	        throw new RuntimeException("Erreur lors du chargement de la base", e);
		}
		return gestionPersonnel;
	}

	@Override
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible 
	{
		close();
	}
	
	public void close() throws SauvegardeImpossible
	{
		try
		{
			if (connection != null)
				connection.close();
		}
		catch (SQLException e)
		{
			throw new SauvegardeImpossible(e);
		}
	}
	
	@Override
	public int insert(Ligue ligue) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into ligue (nom) values(?)", Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, ligue.getNom());		
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
	
	@Override
	public int insert(Employe employe) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into employe (nom, prenom, mail, password, date_arrivee, date_depart, num_ligue) values(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
	        
			instruction.setString(1, employe.getNom());
			instruction.setString(2, employe.getPrenom());
			instruction.setString(3, employe.getMail());
			instruction.setString(4, employe.getPassword());
			instruction.setDate(5, java.sql.Date.valueOf(employe.getDateArrivee()));

			// dateDepart peut être null
			if (employe.getDateDepart() != null) {
			    instruction.setDate(6, java.sql.Date.valueOf(employe.getDateDepart()));
			} else {
			    instruction.setNull(6, java.sql.Types.DATE);
			}
			
	        if (employe.getLigue() != null) {
	            instruction.setInt(7, employe.getLigue().getId());
	        } else {
	            instruction.setNull(7, java.sql.Types.INTEGER); // root
	        }
			
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
	
	@Override
	public void update(Ligue ligue) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("UPDATE ligue SET nom = ? WHERE num_ligue = ?");
			instruction.setString(1, ligue.getNom());
			instruction.setInt(2, ligue.getId());  
			instruction.executeUpdate();
		} 
		
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
	
	
	@Override
	public void update(Employe employe) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("UPDATE employe SET nom = ?, prenom = ?, mail = ?, password = ?, date_arrivee = ?, date_depart = ?, num_ligue = ? WHERE num_employe = ?");
			instruction.setString(1, employe.getNom());
	        instruction.setString(2, employe.getPrenom());
	        instruction.setString(3, employe.getMail());
	        instruction.setString(4, employe.getPassword());
	        instruction.setDate(5, java.sql.Date.valueOf(employe.getDateArrivee()));

	        if (employe.getDateDepart() != null)
	            instruction.setDate(6, java.sql.Date.valueOf(employe.getDateDepart()));
	        else
	            instruction.setNull(6, java.sql.Types.DATE);

	        if (employe.getLigue() != null)
	            instruction.setInt(7, employe.getLigue().getId());
	        else
	            instruction.setNull(7, java.sql.Types.INTEGER);

	        instruction.setInt(8, employe.getId()); 
			instruction.executeUpdate();
		} 
		
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
	
	
	@Override
	public void delete(Employe employe) throws SauvegardeImpossible {
	    try {
	        PreparedStatement instruction = connection.prepareStatement(
	            "DELETE FROM employe WHERE num_employe = ?"
	        );
	        instruction.setInt(1, employe.getId());
	        instruction.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new SauvegardeImpossible(e);
	    }
	}
	
	
	@Override
	public void delete(Ligue ligue) throws SauvegardeImpossible {
	    try {
	        PreparedStatement deleteEmployes = connection.prepareStatement(
	            "DELETE FROM employe WHERE num_ligue = ?"
	        );
	        deleteEmployes.setInt(1, ligue.getId());
	        deleteEmployes.executeUpdate();

	        PreparedStatement deleteLigue = connection.prepareStatement(
	            "DELETE FROM ligue WHERE num_ligue = ?"
	        );
	        deleteLigue.setInt(1, ligue.getId());
	        deleteLigue.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new SauvegardeImpossible(e);
	    }
	}
}
