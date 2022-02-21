package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CrearBBDD {
	
	
	/**
	 * El método crea una base de datos, crea un usuario y le da privilegios en el schema creado
	 */
	private static void crearUsuario() {
		String baseDatos = "CREATE DATABASE PRACTICA";
		String usuario = "CREATE USER 'practica'@'localhost' IDENTIFIED BY 'practica'";
		String privilegios = "GRANT ALL PRIVILEGES ON PRACTICA.* TO 'practica'@'localhost'";
		List<String> crearBBDD = new ArrayList<String>();
		crearBBDD.add(baseDatos);
		crearBBDD.add(usuario);
		crearBBDD.add(privilegios);
		
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?serverTimezone=UTC", "root", "root");
			Statement st  = conn.createStatement();
			for (String cadena : crearBBDD) {
				st.execute(cadena);
			}
			System.out.println("Creada base de datos y usuario");
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
	}
	
	/**
	 * El método crea las tablas necesarias para que funcione la persistencia del programa
	 */
	public static void crearTablas() {
		String usuarios = "CREATE TABLE USUARIOS (ID INT AUTO_INCREMENT, NOMBRE VARCHAR(30), CONTRASEÑA VARCHAR(12), PRIMARY KEY(ID))";
		String lugares = "CREATE TABLE LUGARES (ID INT AUTO_INCREMENT, NOMBRE VARCHAR(50), PRIMARY KEY(ID)) ";
		String opiniones =  "CREATE TABLE OPINIONES(ID INT AUTO_INCREMENT, IDLUGAR INT REFERENCES LUGARES(ID), IDUSUARIO INT REFERENCES USUARIOS(ID), CRITICA VARCHAR(800), VALORACION INT, PRIMARY KEY(ID)) ";
		String usuarioOpinion =	"CREATE TABLE USUARIO_OPINION(IDUSUARIO INT REFERENCES USUARIOS(ID), IDOPINION INT REFERENCES OPINIONES(ID))";
		String usuarioSiguiendo = "CREATE TABLE USUARIO_SIGUIENDO(IDUSUARIO INT REFERENCES USUARIOS(ID), SEGUIDO INT REFERENCES USUARIOS(ID)) ";
		String lugarOpinion = "CREATE TABLE LUGAR_OPINION(IDLUGAR INT REFERENCES LUGARES(ID), IDOPINION INT REFERENCES USUARIOS(ID))";
		List<String> crearTablas = new ArrayList<String>();
		crearTablas.add(usuarios);
		crearTablas.add(lugares);
		crearTablas.add(opiniones);
		crearTablas.add(usuarioOpinion);
		crearTablas.add(usuarioSiguiendo);
		crearTablas.add(lugarOpinion);
		
		try {
			Statement st  = ConexionBBDD.get().getConexion().createStatement();
			for (String cadena : crearTablas) {
				st.execute(cadena);
			}
			System.out.println("Tablas creadas");
			st.close();
			
		}catch (SQLSyntaxErrorException e) {
			System.out.println(e.getCause());
			System.out.println(e.getMessage());
		}catch (SQLException e2) {
			e2.getErrorCode();
			e2.getMessage();
		}
	}
			
	
	public static void main(String[] args) {
		crearUsuario();
		crearTablas();
	}


	
}
