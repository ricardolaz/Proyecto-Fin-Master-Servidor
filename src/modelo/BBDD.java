package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase se encarga de todas las consultas en la base de datos.
 * Tanto de SELECT, INSERT y DELETE.
 * @author Ricardo Lázaro
 *
 */
public class BBDD {

	public void actualizar() {
		String actualizar = "COMMIT";
		try {
			Statement st = ConexionBBDD.get().getConexion().createStatement();
			st.execute(actualizar);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean guardarUsuario(Usuario nuevo) {
		if(buscarNombreUsuario(nuevo.getNombre())==null) {
			String insert = "INSERT INTO USUARIOS(NOMBRE, CONTRASEÑA) VALUES(?,?)";
			try {
				PreparedStatement ps = ConexionBBDD.get().getConexion().prepareStatement(insert);
				ps.setString(1, nuevo.getNombre());
				ps.setString(2, nuevo.getContraseña());
				ps.executeUpdate();
				return true;
			}catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}else return false;
	}
	
	public boolean guardarOpinion(Opinion nuevo) {
		String insert = "INSERT INTO OPINIONES(IDLUGAR,IDUSUARIO,CRITICA,VALORACION) VALUES(?,?,?,?)";
		try {
			PreparedStatement ps = ConexionBBDD.get().getConexion().prepareStatement(insert);
			ps.setInt(1, nuevo.getIdLugar());
			ps.setInt(2, nuevo.getIdUsuario());
			ps.setString(3, nuevo.getCritica());
			ps.setInt(4, nuevo.getValoracion());
			ps.executeUpdate();
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean actualizarTablas(int idUsuario, int idLugar) {
		String SQLConsulta = "SELECT * FROM OPINIONES ORDER BY ID DESC";
		Statement st;
		try {
			st = ConexionBBDD.get().getConexion().createStatement();
			ResultSet rs = st.executeQuery(SQLConsulta);
			while(rs.next()) {
				int idOpinion = rs.getInt("ID");
				guardarUsuarioOpinion(idUsuario,idOpinion);
				guardarLugarOpinion(idLugar, idOpinion);
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void guardarUsuarioOpinion(int idUsuario, int idOpinion) {
		String insert = "INSERT INTO USUARIO_OPINION (IDUSUARIO,IDOPINION) VALUES(?,?)";
		PreparedStatement ps;
		try {
			ps = ConexionBBDD.get().getConexion().prepareStatement(insert);
			ps.setInt(1, idUsuario);
			ps.setInt(2, idOpinion);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void guardarLugarOpinion(int idLugar, int idOpinion) {
		String insert = "INSERT INTO LUGAR_OPINION (IDLUGAR,IDOPINION) VALUES(?,?)";
		PreparedStatement ps;
		try {
			ps = ConexionBBDD.get().getConexion().prepareStatement(insert);
			ps.setInt(1, idLugar);
			ps.setInt(2, idOpinion);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean guardarLugar(Lugar nuevo) {
		if(buscarNombreLugar(nuevo.getNombre())==null){ 
			String insert = "INSERT INTO LUGARES (NOMBRE) VALUES(?)";
			try {
				PreparedStatement ps = ConexionBBDD.get().getConexion().prepareStatement(insert);
				ps.setString(1, nuevo.getNombre());
				ps.executeUpdate();
				return true;
			}catch (SQLException e) {
				e.printStackTrace();
				return false;
			}		
		}else return false;
		
	}
	
	public boolean seguir(int id, int seguido) {
		String insert = "INSERT INTO USUARIO_SIGUIENDO(IDUSUARIO,SEGUIDO) VALUES (?, ?)";
		try {
			PreparedStatement ps = ConexionBBDD.get().getConexion().prepareStatement(insert);
			ps.setInt(1, id);
			ps.setInt(2, seguido);
			ps.executeUpdate();
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	public boolean dejarDeSeguir(int id, int seguido) {
		String delete = "DELETE FROM USUARIO_SIGUIENDO WHERE IDUSUARIO=? AND SEGUIDO=?";
		try {
			PreparedStatement ps = ConexionBBDD.get().getConexion().prepareStatement(delete);
			ps.setInt(1, id);
			ps.setInt(2, seguido);
			ps.executeUpdate();
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}		
	}
	
	public Usuario buscarIdUsuario(int id) {
		String SQLConsulta = "SELECT * FROM USUARIOS WHERE ID=?";
		PreparedStatement ps;
		Usuario aux = new Usuario();
		try {
			ps = ConexionBBDD.get().getConexion().prepareStatement(SQLConsulta);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String nombre = rs.getString("NOMBRE");
				String contraseña = rs.getString("CONTRASEÑA");
				aux = new Usuario(id,nombre,contraseña);
				aux.setOpiniones(buscarOpinionesUsuario(id));
				aux.setSiguiendo(buscarSeguidosUsuario(id));
				return aux;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public Opinion buscarIdOpinion(int id) {
		String SQLConsulta = "SELECT * FROM OPINIONES WHERE ID=?";
		PreparedStatement ps;
		Opinion aux = new Opinion();
		try {
			ps = ConexionBBDD.get().getConexion().prepareStatement(SQLConsulta);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int idLugar = rs.getInt("IDLUGAR");
				int idUsuario = rs.getInt("IDUSUARIO");
				String critica = rs.getString("CRITICA");
				int valoracion = rs.getInt("VALORACION");
				aux = new Opinion(id,idLugar,idUsuario,critica,valoracion);
				return aux;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public Lugar buscarIdLugar(int id) {
		String SQLConsulta = "SELECT * FROM LUGARES WHERE ID=?";
		PreparedStatement ps;
		Lugar aux = new Lugar();
		try {
			ps = ConexionBBDD.get().getConexion().prepareStatement(SQLConsulta);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String nombre = rs.getString("NOMBRE");
				aux = new Lugar(id,nombre);
				aux.setOpiniones(buscarOpinionLugar(id));
				return aux;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return aux;
		}
		return aux;
	}
	
	public Usuario buscarNombreUsuario(String nombre) {
		String SQLConsulta = "SELECT * FROM USUARIOS WHERE NOMBRE=?";
		Usuario aux = new Usuario();
		try {
			PreparedStatement ps= ConexionBBDD.get().getConexion().prepareStatement(SQLConsulta);
			ps.setString(1, nombre);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("ID");
				String contraseña = rs.getString("CONTRASEÑA");
				aux = new Usuario(id,nombre,contraseña);
				aux.setOpiniones(buscarOpinionesUsuario(id));
				aux.setSiguiendo(buscarSeguidosUsuario(id));
				return aux;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public Lugar buscarNombreLugar(String nombre) {
		String SQLConsulta = "SELECT * FROM LUGARES WHERE NOMBRE=?";
		PreparedStatement ps;
		Lugar aux = new Lugar();
		try {
			ps = ConexionBBDD.get().getConexion().prepareStatement(SQLConsulta);
			ps.setString(1, nombre);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("ID");
				aux = new Lugar(id,nombre);
				aux.setOpiniones(buscarOpinionLugar(id));
				return aux;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	
	public List<Integer> buscarOpinionesUsuario(int id){
		List<Integer> lista = new ArrayList<Integer>();
		String SQLConsulta = "SELECT * FROM USUARIO_OPINION WHERE IDUSUARIO=?";
		try {
			PreparedStatement ps = ConexionBBDD.get().getConexion().prepareStatement(SQLConsulta);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				lista.add(rs.getInt("IDOPINION"));
			}
			return lista;
		} catch (SQLException e) {
			e.printStackTrace();
			return lista;
		}
	}
	
	public List<Integer> buscarSeguidosUsuario(int id){
		List<Integer> lista = new ArrayList<Integer>();
		String SQLConsulta = "SELECT * FROM USUARIO_SIGUIENDO WHERE IDUSUARIO=?";
		try {
			PreparedStatement ps = ConexionBBDD.get().getConexion().prepareStatement(SQLConsulta);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				lista.add(rs.getInt("SEGUIDO"));
			}
			return lista;
		} catch (SQLException e) {
			e.printStackTrace();
			return lista;
		}
	}
	
	public List<Usuario> buscarTodosUsuarios(){
		List<Usuario> lista = new ArrayList<Usuario>();
		String SQLConsulta = "SELECT * FROM USUARIOS";
		Statement st;
		try {
			st = ConexionBBDD.get().getConexion().createStatement();
			ResultSet rs = st.executeQuery(SQLConsulta);
			while(rs.next()) {
				int id = rs.getInt("ID");
				String nombre = rs.getString("NOMBRE");
				String contraseña = rs.getString("CONTRASEÑA");
				Usuario aux = new Usuario(id,nombre, contraseña);
				aux.setOpiniones(buscarOpinionesUsuario(id));
				aux.setSiguiendo(buscarSeguidosUsuario(id));
				lista.add(aux);
			}
			return lista;
		} catch (SQLException e) {
			e.printStackTrace();
			return lista;
		}
	}
	
	public List<Opinion> buscarTodosOpiniones(){
		List<Opinion> lista = new ArrayList<Opinion>();
		String SQLConsulta = "SELECT * FROM OPINIONES";
		Statement st;
		try {
			st = ConexionBBDD.get().getConexion().createStatement();
			ResultSet rs = st.executeQuery(SQLConsulta);
			while(rs.next()) {
				int id = rs.getInt("ID");
				int idLugar = rs.getInt("IDLUGAR");
				int idUsuario = rs.getInt("IDUSUARIO");
				String critica = rs.getString("CRITICA");
				int valoracion = rs.getInt("VALORACION");
				Opinion aux = new Opinion(id,idLugar,idUsuario,critica, valoracion);
				lista.add(aux);
			}
			return lista;
		} catch (SQLException e) {
			e.printStackTrace();
			return lista;
		}
	}
	
	public List<Integer> buscarOpinionLugar(int id) {
		List<Integer> lista = new ArrayList<Integer>();
		String SQLConsulta = "SELECT * FROM LUGAR_OPINION WHERE IDLUGAR=?";
		try {
			PreparedStatement ps = ConexionBBDD.get().getConexion().prepareStatement(SQLConsulta);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				lista.add(rs.getInt("IDOPINION"));
			}
			return lista;
		} catch (SQLException e) {
			e.printStackTrace();
			return lista;
		}
	}
	
	public List<Lugar> buscarTodosLugares(){
		List<Lugar> lista = new ArrayList<Lugar>();
		String SQLConsulta = "SELECT * FROM LUGARES";
		Statement st;
		try {
			st = ConexionBBDD.get().getConexion().createStatement();
			ResultSet rs = st.executeQuery(SQLConsulta);
			while(rs.next()) {
				int id = rs.getInt("ID");
				String nombre = rs.getString("NOMBRE");
				Lugar aux = new Lugar(id,nombre);
				aux.setOpiniones(buscarOpinionLugar(id));
				lista.add(aux);
			}
			return lista;
		} catch (SQLException e) {
			e.printStackTrace();
			return lista;
		}
	}
}
