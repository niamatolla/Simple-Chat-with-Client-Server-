package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
	final public static int DEFAULT_PORT = 5555;

  ChatIF serverUI; 
  String LogInIDSaved;
 
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port,ChatIF serverUI) 
  {
    super(port);
    this.serverUI=serverUI;
  }

  
  //Instance methods ************************************************
  





/**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object  msg, ConnectionToClient client)
  {
	  String message = msg.toString();

	    try {
	        // Check if the client has already logged in
	        Boolean loggedIn = (Boolean) client.getInfo("loggedIn");
	        if (loggedIn == null) {
	            // This is the first message from the client, initialize the login state
	            client.setInfo("loggedIn", false);
	            loggedIn = false;
	        }

	        if (message.startsWith("#login ")) {
	            // Handle #login command
	            if (loggedIn) {
	                // Client is already logged in; send error and disconnect
	                client.sendToClient("ERROR: You are already logged in.");
	                client.close();
	            } else {
	                // First-time login
	            	String[] splitmessage=message.split(" ");
	                String loginId = splitmessage[1]; // Extract loginId from the message
	                client.setInfo("loginId", loginId);    // Store loginId in client connection
	                client.setInfo("loggedIn", true);      // Mark the client as logged in
	                serverUI.display("Client logged in with ID: " + loginId);
	                client.sendToClient(loginId + " has logged on");
	            }
	        } else {
	            // If client has not logged in and sends any other command, send error and disconnect
	            if (!loggedIn) {
	                client.sendToClient("ERROR: You must log in first with the #login command.");
	                client.close();
	            } else {
	                // Handle regular message after login
	                String loginId = (String) client.getInfo("loginId");
	                serverUI.display("Message from " + loginId + ": " + message);
	                this.sendToAllClients(loginId+": "+ "Message from " +  ": " + msg);
	            }
	        }
	    } catch (IOException e) {
	        serverUI.display("Error: Could not send message to client or close connection.");
	    }
	  }
  

    
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromServerUI(String message) throws IOException
  {
	if(message.startsWith("#")) {
		handleCommand(message);
	}
	else{
		this.sendToAllClients(message);
	}
	
	}
  
  private void handleCommand(String command) {
	  
	  String[] commandSplit=command.split(" ");
	  
	  if(commandSplit[0].equals("#quit")) {
		  System.exit(0);
		  serverUI.display("The server has terminated");
		  
	  }
	  
	  else if(commandSplit[0].equals("#stop")) {
		  this.stopListening();
	  }
	  
	  else if(commandSplit[0].equals("#close")) {
		  
		  try {
			  this.close();
		  }
		  catch( IOException e) {
			  serverUI.display("Error can not close existing clients");
		  }
	  }
	  
	  else if (commandSplit[0].equals("#setport")) {
		  if(! this.isListening()) {
			  if(commandSplit.length>1) {
				  this.setPort(Integer.parseInt(commandSplit[1]));
			  }
			  else {
				  serverUI.display("The server has no port number");
			  }
		  }
		  else {
			  serverUI.display("The server is  listening to connections");
		  }
	  }
	  
	  else if(commandSplit[0].equals("#start")) {
		  
		  if(!this.isListening()) {
			  try {
				  this.listen();
			  }
			  catch(IOException e) {
				  serverUI.display("Error the server wont listen to clients");
			  }
		  }
		  else {
			  serverUI.display("The server is  listening to connections");
		  }
	  }
	  
	  else if(commandSplit[0].equals("#getport")) {
		  String port=String.valueOf(getPort());
		  serverUI.display(port);
	  }
	  
}
  
  
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println("Server has stopped listening for connections.");
  }
  /**
	 * implements the Hook method called each time a new client connection is
	 * accepted. The default implementation does nothing.
	 * @param client the connection connected to the client.
	 */
  @Override
	protected void clientConnected(ConnectionToClient client) {
	  System.out.println("Client has connected to Server with port "+ getPort());
  }
  
  /**
	 * implements Hook method called each time a client disconnects.
	 * The default implementation does nothing. The method
	 * may be overridden by subclasses but should remains synchronized.
	 *
	 * @param client the connection with the client.
	 */
  @Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("Client has disconnected from Server with port "+ getPort());
	
  }
  /**
	 *implements  Hook method called each time an exception is thrown in a
	 * ConnectionToClient thread.
	 * The method may be overridden by subclasses but should remains
	 * synchronized.
	 *
	 * @param client the client that raised the exception.
	 * @param Throwable the exception thrown.
	 */
  @Override
	synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
		System.out.println("Client unexpectedly disconnected from Server with port " + getPort());
	}








  
  //Class methods ***************************************************
  
 
  
}
//End of EchoServer class
