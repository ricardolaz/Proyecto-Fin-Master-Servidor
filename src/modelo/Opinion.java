package modelo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * POJO que modeliza los objetos de tipo opinion
 * @author Ricardo Lázaro
 *
 */
public class Opinion{
	private int id;
	private int idLugar;
	private int valoracion;
	private String critica;
	private int idUsuario; 
	
	public Opinion() {
		super();
	}

	public Opinion(Integer idLugar, int valoracion, String critica, Integer idUsuario) {
		super();
		this.idLugar=idLugar;
		this.valoracion = valoracion;
		this.critica = critica;
		this.idUsuario=idUsuario;
	}
	
	
	public Opinion(Integer id, Integer idLugar, Integer idUsuario, String critica, Integer valoracion) {
		super();
		this.id = id;
		if(idLugar == null) idLugar=0;
		else this.idLugar=idLugar;
		if(valoracion==null) valoracion=0;
		else this.valoracion = valoracion;
		if(critica==null) critica="";
		else this.critica = critica;
		if(idUsuario == null) idUsuario=0;
		else this.idUsuario=idUsuario;
	}

	public int getId() {
		return id;
	}

	public int getIdLugar() {
		return idLugar;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public int getValoracion() {
		return valoracion;
	}
	public String getCritica() {
		return critica;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setIdLugar(int idLugar) {
		this.idLugar = idLugar;
	}

	public void setValoracion(int valoracion) {
		this.valoracion = valoracion;
	}

	public void setCritica(String critica) {
		this.critica = critica;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	
	public static Opinion convertiraOpinion(String cadena) {
		int id=0;
		int idLugar=0;
		int valoracion=0;
		String critica="";
		int idUsuario=0;
		JsonElement objeto = new JsonParser().parse(cadena);
		JsonObject json = objeto.getAsJsonObject();
		if(json.has("id"))id=Integer.parseInt(json.get("id").getAsString());
		if(json.has("lugar"))idLugar=Integer.parseInt(json.get("lugar").getAsString());
		if(json.has("valoracion"))valoracion=Integer.parseInt(json.get("valoracion").getAsString());
		if(json.has("critica"))critica=json.get("critica").getAsString();
		if(json.has("usuario"))idUsuario=Integer.parseInt(json.get("usuario").getAsString());
		Opinion opinion=new Opinion(id,idLugar,valoracion,critica,idUsuario);
		return opinion;
	}


	public String convertiraJson() {
		String json= new Gson().toJson(this);
		return json;
	}

	
}
