package modelo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * POJO que modeliza los objetos de tipo lugar
 * @author Ricardo Lázaro
 *
 */
public class Lugar{
	private int id;
	private String nombre;
	private List<Integer> opiniones;
	
	
	public Lugar() {
		super();
	}

	public Lugar(Integer id,String nombre) {
		super();
		this.id=id;
		this.nombre = nombre;
		if(this.opiniones==null) this.opiniones=new LinkedList<Integer>();
	}


	public Lugar(Integer id, String nombre, List<Integer> criticas) {
		super();
		if (id==null) id=0;
		else this.id=id;
		this.nombre = nombre;
		this.opiniones = criticas;
	}
	
	public Lugar(String nombre) {
		this.nombre=nombre;
	}

	public void publicarOpinion(int idOpinion) {
		this.opiniones.add(idOpinion);
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setOpiniones(List<Integer> criticas) {
		this.opiniones = criticas;
	}

	public String getNombre() {
		return nombre;
	}


	public List<Integer> getOpiniones() {
		return opiniones;
	}

	
	public Lugar convertiraLugar(String cadena) {
		int id=0;
		String nombre=null;
		List<Integer> criticas=new ArrayList<Integer>();
		JsonElement objeto = new JsonParser().parse(cadena);
		JsonObject json = objeto.getAsJsonObject();
		
		if(json.has("id")) id=Integer.parseInt(json.get("id").getAsString());
		if(json.has("nombre"))nombre=json.get("nombre").getAsString();
		if(json.has("criticas") && json.get("criticas").isJsonArray())
			for(JsonElement ele:json.getAsJsonArray("criticas"))
				criticas.add(ele.getAsInt());
		Lugar lugar = new Lugar(id,nombre,criticas);
		return lugar;
	}

	public String convertiraJson() {
		String json= new Gson().toJson(this);
		return json;
	}


	
	
}
