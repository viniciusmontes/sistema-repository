package repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Datasource {

	private static final String URL;
	private static final String DRIVER;
	private static final String USER;
	private static final String PASSWD;

	static {
		URL = "jdbc:mysql://127.0.0.1/cliente-db";
		DRIVER = "com.mysql.jdbc.Driver";
		USER = "root";
		PASSWD = "root";
	}

	public static Connection openConnection() throws DataBaseException {
		try {
			// Carregar o driver
			Class.forName(DRIVER); // Reflection Framework
			
			// Abrir a conex�o
			return DriverManager.getConnection(URL, USER, PASSWD);
		} catch (ClassNotFoundException cause) {
			throw new DataBaseException(
					"PROBLEMAS AO CARREGAR O DRIVER!", 
					cause);
		} catch (SQLException cause) {
			throw new DataBaseException(
					"PROBLEMAS AO ABRIR CONEX�O!", 
					cause);
		}
	}

	public static void close(Connection c) throws DataBaseException {
		try {
			// Fechar a conex�o
			if (c != null) {
				c.close();
			}
			return;
		} catch (SQLException cause) {
			throw new DataBaseException(
					"PROBLEMAS AO FECHAR CONEX�O!", 
					cause);
		}
	}
}

class DataBaseException extends Exception {

	private static final long serialVersionUID = -1377422332439479605L;

	public DataBaseException(String message, Throwable cause) {
		super(message, cause);
	}
}

