package modelo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * POJO que modeliza los objetos de tipo usuario
 * @author Ricardo Lázaro
 *
 */
public class Usuario {
	private int id;
	private String nombre;
	private String contraseña;
	private List<Integer> opiniones;
	private List<Integer> siguiendo;
	
	
	
	public Usuario() {
		super();
	}
	
	public Usuario(String nombre, String contraseña) {
		super();
		this.nombre = nombre;
		this.contraseña = contraseña;
		this.opiniones = new LinkedList<Integer>();
		this.siguiendo= new LinkedList<Integer>();
	}
	
	public Usuario(int id, String nombre, String contraseña) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.contraseña = contraseña;
		this.opiniones = new LinkedList<Integer>();
		this.siguiendo= new LinkedList<Integer>();
	}
	

	public Usuario(Integer id, String nombre, String contraseña, List<Integer> opiniones, List<Integer> siguiendo) {
		super();
		if (id==null) id=0;
		else this.id = id;
		this.nombre = nombre;
		this.contraseña = contraseña;
		this.opiniones = opiniones;
		this.siguiendo = siguiendo;
	}

	public void publicarOpinion(int idOpinion) {
		this.opiniones.add(idOpinion);
	}
	
	public boolean seguirUsuario(int idUsuario) {
		if(!(this.siguiendo.contains(idUsuario))) {
			this.siguiendo.add(idUsuario);
			return true;
		}else return false;
	}
	
	public boolean dejarDeSeguir(int idUsuario) {
		if(this.siguiendo.contains(idUsuario)) {
			this.siguiendo.remove(new Integer(idUsuario));
			return true;
		}else return false;
	}
	
	public int getId() {
		return id;
	}


	public String getNombre() {
		return nombre;
	}

	public String getContraseña() {
		return contraseña;
	}

	public List<Integer> getOpiniones() {
		return opiniones;
	}

	
	public List<Integer> getSiguiendo() {
		return siguiendo;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public void setOpiniones(List<Integer> opiniones) {
		this.opiniones = opiniones;
	}

	public void setSiguiendo(List<Integer> siguiendo) {
		this.siguiendo = siguiendo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	public JsonObject resumenJson() {
		JsonObject json = new JsonObject();
		json.addProperty("id", this.getId());
		json.addProperty("nombre", this.getNombre());
		return json;
	}

	public String convertiraJson() {
		String jsonAux= new Gson().toJson(this);
		JsonElement json = new JsonParser().parse(jsonAux);
		json.getAsJsonObject().remove("contraseña");
		return json.toString();
	}

	
	public static Usuario convertiraUsuario(String cadena) {
		int id=0;
		String nombre=null, contraseña=null;
		List<Integer> opiniones=new ArrayList<Integer>();
		List<Integer> siguiendo=new ArrayList<Integer>();
		
		JsonElement objeto = new JsonParser().parse(cadena);
		JsonObject json = objeto.getAsJsonObject();
		
		if(json.has("id")) id=Integer.parseInt(json.get("id").getAsString());
		if(json.has("nombre")) nombre=json.get("nombre").getAsString();
		if(json.has("contraseña")) contraseña=json.get("contraseña").getAsString();
		if(json.has("opiniones") && json.get("opiniones").isJsonArray()) 
			for (JsonElement ele : json.getAsJsonArray("opiniones")) 
				opiniones.add(ele.getAsInt());		
		if(json.has("siguiendo") && json.get("siguiendo").isJsonArray()) 
			for (JsonElement ele : json.getAsJsonArray("siguiendo"))
				siguiendo.add(ele.getAsInt());
		Usuario usuario=new Usuario (id,nombre,contraseña,opiniones,siguiendo);
		return usuario;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", contraseña="+ contraseña+", opiniones="+opiniones+", siguiendo="+siguiendo+ "]";
	}
	
	

}
