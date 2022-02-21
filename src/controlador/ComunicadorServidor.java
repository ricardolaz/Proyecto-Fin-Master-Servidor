package controlador;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import modelo.BBDD;
import modelo.ConexionBBDD;
import modelo.Usuario;

public class ComunicadorServidor implements Runnable {
	private ServerSocket sv;
	BBDD bbdd = new BBDD();
	ControladorServidor cs = new ControladorServidor();
	
	public ComunicadorServidor() {
		try {
			sv=new ServerSocket(1001);
		}catch(IOException e) {
			System.out.println("Error al conectar al servidor en el puero 1001");
		}
	}
	
	/**
	 * Este método mantiene abierto el servidor
	 * Hay implementado un pequeño menú para hacer varias consultas o para cerrar el servidor
	 */
	void ejecutar() {
		new Thread(this).start();
		boolean salir = false;
		while(!salir) {
			System.out.println("\nMenú Servidor");
			System.out.println("Pulse 1 para registrar usuario");
			System.out.println("Pulse 2 listar todos los usuarios");
			System.out.println("Pulse 3 listar todos los lugares");
			System.out.println("Pulse 4 listar todas las opiniones");
			System.out.println("Pulse 0 o \"salir\" si quiere cerrar el servidor");
			String linea = new Scanner(System.in).nextLine();
			switch (linea) {
			case "0":
				salir = true;
				break;
			case "salir":
				salir = true;
				break;
			case "1":
				System.out.println("Eliga nombre de usuario");
				String usuario = new Scanner(System.in).nextLine();
				System.out.println("Eliga la contraseña");
				String contraseña = new Scanner(System.in).nextLine();
				if(cs.registrar(usuario, contraseña)) {
					System.out.println("Usuario creado");
				}else {
					System.out.println("El usuario no se ha podido crear");
				}
				break;
			case "2":
				List<Usuario> listaU = cs.listaUsuarios();
				for (Usuario us : listaU) {
					System.out.println(us);
				}
				break;
			case "3":
				List<String> listaL = cs.listaLugares();
				for (String lu : listaL) {
					System.out.println(lu);
				}
				break;
			case "4":
				List<String> listaO = cs.listaOpiniones();
				for (String op : listaO) {
					System.out.println(op);
				}
				break;
			default:
				System.out.println("No ha seleccionado una opción correcta");
				break;
			}
		}
		System.exit(0);
	}
	

	/**
	 *El método crea un hilo cuando un cliente conecta con el servidor
	 */
	@Override
	public void run() {
		while(true) {
			try {
				System.out.println("Servidor en escucha");
				Socket socket=sv.accept();
				new Hilo(socket).start();
				
			}catch(IOException e) {
				System.out.println(e.getMessage());
				
			}	
		}
	}
	
	public static void main(String[] args) {
		new ComunicadorServidor().ejecutar();
	}



}
