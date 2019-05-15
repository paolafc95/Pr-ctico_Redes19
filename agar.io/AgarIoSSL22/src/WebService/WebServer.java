package WebService;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class WebServer {
	public static final String LOCAL_HOST = "localhost";
	public static final int PORT_BD = 6500;
	public boolean webService ;
	private HiloDespliegueAppWeb DespliegueAppWeb;
	private ArrayList<Socket> socketsActivos;
	public ArrayList<String> jugadoresPersistir;
	public ServerSocket serverSocketWebService;
	public static final int PORT_WEB_SERVICE = 7000;
	
	
	
public WebServer() throws InterruptedException, IOException {
	
	
	socketsActivos = new ArrayList<>();
	jugadoresPersistir = new ArrayList<>();
	
	try {
		serverSocketWebService = new ServerSocket(PORT_WEB_SERVICE);
	} catch (Exception e) {
		System.out.println(e.getMessage());
	}

	webService = true;
	DespliegueAppWeb = new HiloDespliegueAppWeb(this);
	DespliegueAppWeb.start();
		

}
	
	public ArrayList<String> getjugadoresPersistir() {
		return jugadoresPersistir;
	}
	public void setjugadoresPersistir(ArrayList<String> juagadoresPersistir) {
		this.jugadoresPersistir = juagadoresPersistir;
	}
	public boolean isWebService() {
		return webService;
	}
	public void setWebService(boolean webService) {
		this.webService = webService;
	}
	public ServerSocket getServerSocketWebService() {
		return serverSocketWebService;
	}
	public void setServerSocketWebService(ServerSocket serverSocketWebService) {
		this.serverSocketWebService = serverSocketWebService;
	}

	//arrayList
	public ArrayList<String> mensajeSalida(String id, String pass) throws IOException {
		System.out.println("archivo");
		ArrayList<String> lista=new ArrayList<>();
		File archivo = null;
	    FileReader fr = null;
	    BufferedReader br = null;
	    
	    // Apertura del fichero y creacion de BufferedReader para poder hacer una lectura comoda (disponer del metodo readLine()).
	      
	    archivo = new File ("./data/archivo.txt"); //RUTA DONDE SE SUPONE SE VA A GUARDAR EL TXT EN EL SERVIDOR
        fr = new FileReader (archivo);
        br = new BufferedReader(fr);
        
     // Lectura del fichero
         String linea;
         linea = br.readLine();
         boolean seEncontroUsuario = false;
         boolean errorPassword=false;
        
         while(linea!=null&&!seEncontroUsuario&&!errorPassword) {
        	 //Vamos buscando usuario y contraseña
        	 String[] datos = linea.split(";");
        	
        	 if(datos[2].equals(id)) {
        		 if(datos[1].equals(pass)) {
        			 System.out.println("encontrado");
        			 //AQUI VA EL CODIGO PARA INICIAR EL JUEGO (Abrir la pestaña del juego)
        			seEncontroUsuario = true;
        			File archivo2 = null;
        		    FileReader fr2 = null;
        		    BufferedReader br2 = null;
        		    
        		    // Apertura del fichero y creacion de BufferedReader para poder
        	        // hacer una lectura comoda (disponer del metodo readLine()).
        		      
        		    archivo2 = new File ("./RegistrosDB/"+datos[0]); //donde se guardarán los registro en el server
        	        fr2 = new FileReader (archivo2);
        	        br2 = new BufferedReader(fr2);
        	        
        	     // Lectura del fichero
        	         String linea2;
        	         linea2 = br2.readLine();
        	        
        	        
        	         while(linea2!=null) {
        	        	 //Vamos buscando usuario y contraseña
        	        	lista.add(linea2);
        	        	linea2=br2.readLine();
        	         }
        			br2.close();
        		}
        		
        	 }
        	 linea=br.readLine();
         }
         br.close();
         return lista;
	}
	

	public static String pedirDatosAlServerBD(String user) {
		String datosObtenidos = "";
		try {
			Socket socket = new Socket(LOCAL_HOST, PORT_BD);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			String mensaje = "PEDIR_DATOS;" + user;
			System.out.println("Chequeo Method pedir Datos Al Server BD");	
			out.writeUTF(mensaje);
			String mensajeObtenido = in.readUTF();
			datosObtenidos = mensajeObtenido;
			System.out.println("Mensaje Obtenido por el Servidor BD al PEDIR DATOS : " + mensajeObtenido);
			socket.close();	
		} catch (IOException e) {
			System.out.println("Exception in ConexServerBD");
		}	
		return datosObtenidos;
	}

}
