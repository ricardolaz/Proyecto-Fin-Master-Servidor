package modelo;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * La clase simplemente sirve para recuperar la instancia única de ConexionBBDD protegido por el patrón Singleton 
 * A través de esa instancia se recupera la Connection
 * @author Ricardo Lázaro
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
	 * El método para recuperar la conexión del objeto BBDD que está protegido por el patrón Singleton
	 * @return	devuelve la conexión 
	 */
	public Connection getConexion() {
		return conn;
	}
	
	

	
	
}
