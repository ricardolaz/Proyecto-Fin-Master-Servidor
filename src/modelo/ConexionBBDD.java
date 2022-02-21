package modelo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * La clase simplemente sirve para recuperar la instancia �nica de ConexionBBDD protegido por el patr�n Singleton 
 * A trav�s de esa instancia se recupera la Connection
 * @author Ricardo L�zaro
 *
 */
public class ConexionBBDD {
	private static ConexionBBDD bbdd;
	private Connection conn;
	
	private ConexionBBDD() {
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/practica?serverTimezone=UTC", "practica", "practica");
			if(conn != null) System.out.println("Conectado a la base de datos");
			else System.out.println("Error al conectar a la base de datos");
		}catch(SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public synchronized static ConexionBBDD get() {
		if(bbdd==null) 
			bbdd=new ConexionBBDD();
		return bbdd; 
	}
	
	/**
	 * El m�todo para recuperar la conexi�n del objeto BBDD que est� protegido por el patr�n Singleton
	 * @return	devuelve la conexi�n 
	 */
	public Connection getConexion() {
		return conn;
	}
	
	

	
	
}
