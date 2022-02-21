package controlador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import modelo.Contrase�aException;
import modelo.Lugar;
import modelo.Opinion;
import modelo.Usuario;
import modelo.UsuarioException;

public class Hilo extends Thread{
	private Socket socket;
	private ControladorServidor controlador= new ControladorServidor();
	private boolean bandera;
	
	public Hilo(Socket socket) {
		super();
		this.socket = socket;
		this.bandera = true;
	}
	
	
	/**
	 *El m�todo que se queda a la escucha de las peticiones del cliente. Se queda en bucle infinito hasta que se desconecta el cliente.
	 */
	@Override
	public void run() {
		
		try(BufferedReader entrada=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter salida=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))){
			System.out.println("Hilo a la escucha");
			String protocolo = protocolo();
			salida.write(protocolo);
			salida.newLine();
			salida.flush();
			String peticion = "";
			while(bandera) {
				peticion = entrada.readLine();
				salida.write(leerPeticion(peticion)); 
				salida.newLine();
				salida.flush();
			}
			
		}catch(IOException e){
			System.out.println("Error en la comunicaci�n con el cliente");
		} 
		
	}
	
	/**
	 * Cuando el cliente se conecta por primera vez le devuelve el protocolo al cliente.
	 * @return	Protocolo en formato JSON
	 */
	private String protocolo() {
		JsonObject json = new JsonObject();
		json.addProperty("logado", "");
		json.addProperty("registrar", "");
		json.addProperty("crear lugar", "");
		json.addProperty("publicar opini�n", "");
		json.addProperty("seguir usuario", "");
		json.addProperty("dejar de seguir", "");
		json.addProperty("listar usuarios seguidos", "");
		json.addProperty("listar usuarios", "");
		json.addProperty("listar opiniones", "");
		json.addProperty("listar lugares", "");
		json.addProperty("buscar lugares", "");
		json.addProperty("buscar usuario", "");
		json.addProperty("buscar opinion", "");
		json.addProperty("salir", "");
		return json.toString();
	}

	/**
	 * Este es el m�todo principal, el que recibe las peticiones del cliente
	 * seg�n el tipo de petici�n invoca a un m�todo del controlador del servidor
	 * @param peticion 	El cliente le env�a una petici�n en formato JSON
	 * @return			Le devuelve al cliente el resultado de la petici�n con formato JSON
	 */
	private String leerPeticion(String peticion) {
		JsonObject json = new JsonParser().parse(peticion).getAsJsonObject();
		String aux = "";
		String usuario = "";
		String contrase�a = "";
		String lugar = "";
		String critica = "";
		int id = 0;
		int idLugar = 0;
		int idUsuario = 0;
		Integer valoracion = null;
		JsonObject jsonAux = new JsonObject();
		JsonArray jarray = new JsonArray();
		List<Usuario> listaUsuarios = new ArrayList<Usuario>();
		List<String> lista = null;
		if(json.has("tipo")) aux = json.get("tipo").getAsString();
		jsonAux.addProperty("tipo", aux);
		switch (aux) {
		
		case "logado":
			if(json.has("usuario")) usuario = json.get("usuario").getAsString();
			if(json.has("contrase�a")) contrase�a = json.get("contrase�a").getAsString();
			try {
				controlador.loguearse(usuario, contrase�a);
				jsonAux.addProperty("usuario", usuario);
				jsonAux.addProperty("id", controlador.buscarIdUsuario(usuario));
				return jsonAux.toString(); 
			}catch(UsuarioException e) {
				jsonAux.addProperty("usuario incorrecto", "usuario incorrecto");
				return jsonAux.toString(); 
			}catch(Contrase�aException e) {
				jsonAux.addProperty("contrase�a incorrecta", "contrase�a incorrecta");
				return jsonAux.toString(); 
			}
			
		case "registrar":
			if(json.has("usuario")) usuario=json.get("usuario").getAsString();
			if(json.has("contrase�a")) contrase�a=json.get("contrase�a").getAsString();
			if(controlador.registrar(usuario, contrase�a)) {
				jsonAux.addProperty("usuario", "creado");
				System.out.println("usuario creado");
				return jsonAux.toString();
			}else {
				jsonAux.addProperty("usuario existente", "usuario existente");
				return jsonAux.toString();
			}
			
		case "crear lugar":
			if(json.has("nombre")) lugar=json.get("nombre").getAsString();
			if(controlador.registrarLugar(lugar)) {
				Lugar auxLugar = controlador.buscarLugar(controlador.buscarIdLugar(lugar));
				jsonAux.addProperty("id", auxLugar.getId());
				jsonAux.addProperty("nombre", auxLugar.getNombre());
				return jsonAux.toString();
			}else return jsonAux.toString();
				
		case "publicar opinion":
			if(json.has("lugar")) idLugar = json.get("lugar").getAsInt();
			if(json.has("valoracion")) valoracion = json.get("valoracion").getAsInt();
			if(json.has("critica")) critica = json.get("critica").getAsString();
			if(json.has("usuario")) idUsuario = json.get("usuario").getAsInt();
			controlador.publicarOpinion(idLugar, valoracion, critica, idUsuario); 
			jsonAux.addProperty("opinion publicada", "opinion publicada");
			return jsonAux.toString();
			
		case "seguir usuario":
			if(json.has("usuario")) id = json.get("usuario").getAsInt();
			if(json.has("seguir")) idUsuario = json.get("seguir").getAsInt();
			if(controlador.seguirUsuario(id, idUsuario)) {
				jsonAux.addProperty("seguido", "seguido");
			}
			else jsonAux.addProperty("no seguido", "no seguido");
			return jsonAux.toString();
			
		case "dejar de seguir":
			if(json.has("usuario")) id = json.get("usuario").getAsInt();
			if(json.has("dejar de seguir")) idUsuario = json.get("dejar de seguir").getAsInt();
			if(controlador.dejarDeSeguir(id, idUsuario)) {
				jsonAux.addProperty("ok", "dejado de seguir ok");
			}
			else jsonAux.addProperty("no ok", "");
			return jsonAux.toString();
			
		case "listar usuarios seguidos":
			if(json.has("id"))id = json.get("id").getAsInt();
			listaUsuarios=controlador.listarUsuariosSeguidos(id);
			for (Usuario ele : listaUsuarios) jarray.add(ele.resumenJson());
			jsonAux.add("usuarios", jarray);
			return jsonAux.toString();
			
		case "listar usuarios":
			listaUsuarios = controlador.listaUsuarios();
			for (Usuario ele : listaUsuarios) jarray.add(ele.resumenJson());
			jsonAux.add("usuarios", jarray);
			return jsonAux.toString();
			
		case "listar opiniones":
			lista = controlador.listaOpiniones();
			for (String ele : lista) {
				jarray.add(new JsonParser().parse(ele).getAsJsonObject());
			}
			jsonAux.add("opiniones",jarray);
			return jsonAux.toString();
	
		case "listar lugares":
			lista = controlador.listaLugares();
			for (String ele : lista) {
				jarray.add(new JsonParser().parse(ele).getAsJsonObject());
			}
			jsonAux.add("lugares", jarray);
			return jsonAux.toString();	
			
		case "buscar lugar":
			if(json.has("id")) id=json.get("id").getAsInt();
			Lugar auxLugar= controlador.buscarLugar(id);
			aux=auxLugar.convertiraJson();
			return aux;
			
		case "buscar usuario":
			if(json.has("id")) id=json.get("id").getAsInt();
			Usuario auxUsuario= controlador.buscarUsuario(id);
			aux=auxUsuario.convertiraJson();
			return aux;
		case "buscar opinion":
			if(json.has("id")) id=json.get("id").getAsInt();
			Opinion auxOpinion= controlador.buscarOpinion(id);
			aux=auxOpinion.convertiraJson();
			return aux;	
			
		case "salir":
			bandera=false;
			jsonAux.addProperty("salir", "programa");
			jsonAux.toString();
			
		default:
			jsonAux.addProperty("peticion", "incorrecta");
			return jsonAux.toString();
		}	
	}

		
}
