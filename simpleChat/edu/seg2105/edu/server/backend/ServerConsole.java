package edu.seg2105.edu.server.backend;

import java.io.IOException;
import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;

public class ServerConsole implements ChatIF {

/**
* The default port to listen on.
*/
final public static int DEFAULT_PORT = 5555;
	
 EchoServer server;
 Scanner fromScanner;
 
 /**
  * Constructs an instance of the ServerConsole UI.
  *
  * @param port The port.
  */
	
 public ServerConsole(int port) throws IOException {
	 server=new EchoServer(port,this);
	 
	 fromScanner = new Scanner(System.in);
	 try {
         server.listen(); // Start the server
         System.out.println("Server is running on port " + port);
     } catch (IOException e) {
         System.out.println("Error: Could not start the server.");
     }
 }
 
	
	
	@Override
	public void display(String message) {
		  System.out.println("SERVER MSG>" + message);
 }
	 /**
     * Method to handle server-side user input.
     */
	public void accept() {
		try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromScanner.nextLine();
	        
	        server.handleMessageFromServerUI(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
    }

	
	/**
	   * This method is responsible for the creation of 
	   * the server instance (there 
	   *
	   * @param args[0] The port number to listen on.  Defaults to 5555 
	   *          if no argument is entered.
	   */
	  public static void main(String[] args) 
	  {
		  int port=0;

	        try {
	            port = Integer.parseInt(args[0]); // Get port from command line
	        } catch (ArrayIndexOutOfBoundsException e) {
	            port = DEFAULT_PORT; // Use default if no port provided
	        }

	        try {
	            ServerConsole serverConsole = new ServerConsole(port); // Create ServerConsole instance
	            serverConsole.accept(); // Start accepting console input
	        } catch (IOException e) {
	            System.out.println("Error: Could not initialize server on port " + port);
	        }
	    }
	  }


