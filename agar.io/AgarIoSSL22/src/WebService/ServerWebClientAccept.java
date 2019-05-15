package WebService;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import ServerModel.ServerModel;



public class ServerWebClientAccept extends Thread{
	
	private Socket socket;
	private WebServer server;
	
	public ServerWebClientAccept(Socket socket, WebServer server) {
		
		this.socket = socket;
		this.server = server;
	}
	
	public void run() {

		//le quite el while true
			handleRequest(this.socket);
		
		
	}
	private void handleRequest(Socket socket) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String headerLine = in.readLine();
			// A tokenizer is a process that splits text into a series of tokens
			StringTokenizer tokenizer =  new StringTokenizer(headerLine);
			//The nextToken method will return the next available token
			String httpMethod = tokenizer.nextToken();
			// The next code sequence handles the GET method. A message is displayed on the
			// server side to indicate that a GET method is being processed
			if(httpMethod.equals("GET"))
			{
				System.out.println("Get method processed");
				String httpQueryString = tokenizer.nextToken();
				System.out.println(httpQueryString);
				if(httpQueryString.equals("/"))
				{
					StringBuilder responseBuffer =  new StringBuilder();
					String str="";
					BufferedReader buf = new BufferedReader(new FileReader("./html/login.html"));			
					while ((str = buf.readLine()) != null) {
						responseBuffer.append(str);
				    }
					sendResponse(socket, 200, responseBuffer.toString());		
				    buf.close();
				}
				//permite obtener el dato ingresado en el submit
				if(httpQueryString.contains("/?usr="))
				{
					System.out.println("Get method processed");
					String[] response =  httpQueryString.split("=");
					
					ArrayList<String> list=server.mensajeSalida(response[1].split("&")[0],response[2]);
					Object[] lista = list.toArray();
					//String mensajeObtenido = ;
					//String[] lista = mensajeObtenido.split("\n");
					
					StringBuilder responseBuffer =  new StringBuilder();
					responseBuffer
					.append("<html>")
					.append("<head>")
					.append("<style>")
					.append("body{")
					.append("background-image: url(\"http://agar.io/img/1200x630.png\");")
					.append("margin: 0;")
					.append("background-size: cover;")
					.append("margin: 0;")
					.append("background-attachment: fixed;")
					.append("font-family: Verdana;")
					.append("}")
					
					.append("h1{")
					.append("text-align: center;")
					.append("}")
					
					.append("table{")
					.append("border: 1px solid #000;")
					.append("border-collapse: collapse;")
					.append("margin: auto;")
					.append("width: 100%;")
					.append("}")
					
					.append("table th{")
					.append("border: 1px solid #000;")
					.append("border-collapse: collapse;")
					.append("padding: 5px;")
					.append("background-color: #55B8DE;")
					.append("text-align: center;")
					.append("}")
					
					.append("table td{")
					.append("border: 1px solid #000;")
					.append("padding: 15px;")
					.append("background-color: #AF8CF0;")
					.append("text-align: center;")
					.append("}")
					
					.append("table tr:hover{")
					.append("background: #D6D6D6;")
					.append("}")
					
					.append("table td:hover{")
					.append("background: #FBABF4;")
					.append("color:white;")
					.append("	}")					
					.append("</style>")					
					.append("<title>Record del jugador</title>")
					.append("</head>")
					
					.append("<body>")
					.append("<h1>Listado  de records</h1>")
					
					.append("<table>")
					.append("<tr>")
					.append("<th><strong>USERNAME</strong></th>")
					.append("<th><strong>PUNTAJE</strong></th>")
					.append("<th><strong>FECHA</strong></th>")
					.append("<th><strong>PARTIDA</strong></th>")
					.append("<th><strong>JUGADORES ADVERSARIOS</strong></th>");
					agregarlista(lista,responseBuffer, response[1].trim());
					responseBuffer.append("<body>")
					.append("<table>")
					.append("<body>")
					.append("</html>");
					
					sendResponse(socket, 200, responseBuffer.toString());		
				    
				}
			}
			else
			{
				System.out.println("The HTTP method is not recognized");
				sendResponse(socket, 405, "Method Not Allowed");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error1");
		}	
		
	}
	private void agregarlista(Object[] lista , StringBuilder responseBuffer, String usr) {
		for (int i = 0; i < lista.length; i++) {
			System.out.println("CHEQUEO LISTA :" + lista[i]);			
			responseBuffer.append("<tr>");
			responseBuffer.append("<td>"+((String) lista[i]).split(";")[0]+"</td>");
			responseBuffer.append("<td>"+((String) lista[i]).split(";")[1]+"</td>");
			responseBuffer.append("<td>"+((String) lista[i]).split(";")[2]+"</td>");
			responseBuffer.append("<td>"+((String) lista[i]).split(";")[3]+"</td>");
			responseBuffer.append("<td>"+((String) lista[i]).split(";")[4]+"</td>");
			responseBuffer.append("<tr>");			
		}		
	}
	
	public void sendResponse(Socket socket, int statusCode, String responseString)
	{
		String statusLine;
		String serverHeader = "Server: WebServer\r\n";
		String contentTypeHeader = "Content-Type: text/html\r\n";
		
		try {
			DataOutputStream out =  new DataOutputStream(socket.getOutputStream());
			if (statusCode == 200) 
			{
				statusLine = "HTTP/1.0 200 OK" + "\r\n";
				String contentLengthHeader = "Content-Length: "
				+ responseString.length() + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes(serverHeader);
				out.writeBytes(contentTypeHeader);
				out.writeBytes(contentLengthHeader);
				out.writeBytes("\r\n");
				out.writeBytes(responseString);
				} 
			else if (statusCode == 405) 
			{
				statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			} 
			else 
			{
				statusLine = "HTTP/1.0 404 Not Found" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
