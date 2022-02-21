package modelo;

public class UsuarioException extends LoguinException{
	
	public UsuarioException(){
		super("usuario no encontrado");
	}

}
