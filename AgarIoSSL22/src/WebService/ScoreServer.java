package WebService;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

import ServerModel.ServerModel;



public class ScoreServer extends Thread{
	
	private Socket socket;
	private WebServer server;
	
	public ScoreServer(Socket socket, WebServer server) {
		
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
					String mensajeObtenido = "listo ya esta :v";
					String[] lista = mensajeObtenido.split("\n");
					StringBuilder responseBuffer =  new StringBuilder();
					responseBuffer
					.append("<html>")
					.append("<head>")
					.append("<style>")
					.append("body{")
					.append("	background-image: url(\"https://drive.google.com/file/d/1xZfWxNobQfVBSg9o5VXQASWIWyIeXaGY/view\");")
					.append("}")
					.append("</style>")
					.append("<title>Records del jugador</title>")
					.append("</head>")
					.append("<body>")
					.append("<h1>Listado de Carreras Realizadas</h1>")
					.append("<table>")
					.append("<tr>")
					.append("<td><strong>USERNAME</strong></td>")
					.append("<td><strong>SCORE</strong></td>")
					.append("<td><strong>PUNTAJE</strong></td>")
					.append("<td><strong>FECHA</strong></td>")
					.append("<td><strong>JUGADORES ADVERSARIOS</strong></td>")
					.append("<td><strong>GANÓ?</strong></td>")
					.append("<td><strong>"+mensajeObtenido+"</td>");;
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
	private void agregarlista(String[] lista , StringBuilder responseBuffer, String usr) {
		for (int i = 0; i < lista.length; i++) {
			System.out.println("CHEQUEO LISTA :" + lista[i]);
			if(lista[i].split(",")[0].compareToIgnoreCase(usr)==0) {
				responseBuffer.append("<tr>");
				responseBuffer.append("<td>"+lista[i].split(",")[0]+"</td>");
				responseBuffer.append("<td>"+lista[i].split(",")[1]+"</td>");
				responseBuffer.append("<td>"+lista[i].split(",")[2]+"</td>");
				responseBuffer.append("<td>"+lista[i].split(",")[3]+"</td>");
				responseBuffer.append("<td>"+lista[i].split(",")[4]+"</td>");
				responseBuffer.append("<td>"+lista[i].split(",")[5]+"</td>");
				responseBuffer.append("<tr>");
			}
			
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

