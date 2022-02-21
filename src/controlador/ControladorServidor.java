package controlador;

import java.util.ArrayList;
import java.util.List;
import modelo.ContraseñaException;
import modelo.BBDD;
import modelo.Lugar;
import modelo.Opinion;
import modelo.Usuario;
import modelo.UsuarioException;

/**
 * Esta clase invoca a los métodos de la clase BBDD y devuelve la información a la clase Hilo y clase ComunicadorServidor
 * @author Ricardo Lázaro
 *
 */
public class ControladorServidor {
	BBDD bbdd = new BBDD();
	
	public boolean registrar(String nombre, String contraseña) {
		Usuario aux = new Usuario(nombre,contraseña);
		return bbdd.guardarUsuario(aux);
	}
	
	public boolean loguearse(String nombre,String contraseña) throws UsuarioException, ContraseñaException {
		Usuario aux = bbdd.buscarNombreUsuario(nombre);
		if(aux==null) {
			System.out.println("El usuario no existe");
			throw new UsuarioException();
		}
		if(!aux.getNombre().equals(nombre)) {
			System.out.println("El usuario no existe");
			throw new UsuarioException();
		}
		
		if(!aux.getContraseña().equals(contraseña)) {
			System.out.println("La contraseña es incorrecta");
			throw new ContraseñaException();
		}
		return true;
	}
	
	public boolean registrarLugar(String nombre) {
		Lugar aux = new Lugar(nombre);
		if(bbdd.guardarLugar(aux)) {
			aux = bbdd.buscarNombreLugar(nombre);
			return true;
		}else return false;
		
	}
	
	public void publicarOpinion(int lugar, int valoracion, String critica, int usuario) {
		Opinion aux = new Opinion (lugar,valoracion, critica, usuario);
		bbdd.guardarOpinion(aux);
		bbdd.actualizarTablas(usuario, lugar);
	}
	
	public boolean seguirUsuario(int usuario, int seguir) { 
		return bbdd.seguir(usuario,seguir);
		
	}
	
	public boolean dejarDeSeguir(int usuario, int seguido) { 
		return bbdd.dejarDeSeguir(usuario, seguido);		
	}
	
	public List<Usuario> listaUsuarios(){
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios = bbdd.buscarTodosUsuarios();
		return usuarios;
	}
	
	public List<String> listaOpiniones(){
		List<String> lista = new ArrayList<String>();
		List<Opinion> opiniones = new ArrayList<Opinion>();
		opiniones = bbdd.buscarTodosOpiniones();
		for (Opinion ele : opiniones) 
			lista.add(ele.convertiraJson());
		return lista;
	}

	public List<String> listaLugares(){
		List<String> lista = new ArrayList<String>();
		List<Lugar> lugares = new ArrayList<Lugar>();
		lugares = bbdd.buscarTodosLugares();
		for (Lugar ele : lugares) 
			lista.add(ele.convertiraJson());
		return lista;
	}

	public int buscarIdUsuario(String nombre) {
		Usuario aux=bbdd.buscarNombreUsuario(nombre);
		return aux.getId();
	}
	
	public int buscarIdLugar(String nombre) {
		Lugar aux=bbdd.buscarNombreLugar(nombre);
		return aux.getId();
	}
	
	public Lugar buscarLugar(int id) {
		return bbdd.buscarIdLugar(id);
	}
	
	public Opinion buscarOpinion(int id) {
		return bbdd.buscarIdOpinion(id);
	}
	
	public Usuario buscarUsuario(int id) {
		return bbdd.buscarIdUsuario(id);
	}

	public List<Usuario> listarUsuariosSeguidos(int id) {
		List<Usuario> lista= new ArrayList<Usuario>();
		List<Integer> listaInt;
		Usuario muestra = bbdd.buscarIdUsuario(id);
		listaInt=muestra.getSiguiendo();
		if(!listaInt.isEmpty()) 
			for (Integer ele : listaInt) 
				lista.add(bbdd.buscarIdUsuario(ele));
		return lista;
	}
		 
	
}
