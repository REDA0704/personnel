package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

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

			String requete = "select * from ligue";
			Statement instruction = connection.createStatement();
			ResultSet ligues = instruction.executeQuery(requete);
			while (ligues.next())
				gestionPersonnel.addLigue(ligues.getInt(1), ligues.getString(2));
		}
		catch (SQLException e)
		{
			System.out.println(e);
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
			instruction = connection.prepareStatement("insert into employe (nom, prenom, mail, password, date_arrivee, date_depart, num_ligue) values(?)", Statement.RETURN_GENERATED_KEYS);

			instruction.setString(1, employe.getNom());
			instruction.setString(2, employe.getPrenom());
			instruction.setString(3, employe.getMail());
			instruction.setString(4, employe.getPassword());
			instruction.setDate(5, Date.valueOf(employe.getDateArrivee()));
			instruction.setDate(6, Date.valueOf(employe.getDateDepart()));
			instruction.setInt(7, employe.getLigue().getId());
			
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
	        instruction.setDate(6, java.sql.Date.valueOf(employe.getDateDepart()));

	        // gestion ligue null (root)
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
}
