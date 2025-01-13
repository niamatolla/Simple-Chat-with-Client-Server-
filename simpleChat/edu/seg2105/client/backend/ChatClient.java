// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String Id;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String Id,String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.Id=Id;
    this.clientUI = clientUI;
    openConnection();
    clientUI.display("Connected with login ID: " + Id);
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if(message.startsWith("#")) {
    		handleCommand(message);
    	}
    	else{
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  private void handleCommand(String command) {
	  
	  String[] commandSplit=command.split(" ");
	  
	  if(commandSplit[0].equals("#quit")) {
		  quit();
	  }
	  
	  else if(commandSplit[0].equals("#logoff")) {
		  try {
			  this.closeConnection();
		  }
		  catch(IOException e) {
			  clientUI.display("Error closing connection");
		  }
	  }
	  
	  else if(commandSplit[0].equals("#sethost")) {
		  
		  if(isConnected()) {
			  
			  clientUI.display("The client is already connected");
		  }
		  else if(commandSplit.length>1){
			  this.setHost(commandSplit[1]);
		  }
		  else {
			  clientUI.display("No host name was specified");
		  }
	  }
	  
	  else if(commandSplit[0].equals("#setport")) {
		  
		  
		  if(isConnected()) {
			  clientUI.display("The client is already connected");
		  }
		  else if(commandSplit.length>1) {
			  this.setPort(Integer.parseInt(commandSplit[1]));
		  }
		  else {
			  clientUI.display("No port number was specified");
		  }
	  }
	  
	  else if(commandSplit[0].equals("#login")) {
	  
		  if(!isConnected()) {
			  try {
				  this.openConnection();
			  }
			  catch(IOException e) {
				  clientUI.display("Server wont open");
			  }
		  }
		  else {
			  clientUI.display("The client is already connected");
		  }
	  
	  }
	  
	  else if(commandSplit[0].equals("#gethost")) {
		  
		  
		  clientUI.display(this.getHost());
		  
	  }
	  
	  else if(commandSplit[0].equals("#getport")) {
		   
		  
		  String port=String.valueOf(getPort());
		  
		  clientUI.display(port);
	  }
	  
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  /**
	 * implements the Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  @Override 
	protected void connectionException(Exception exception) {
	  
	  clientUI.display("The server has shutdown");
	  quit();
	  
	}
  /**
	 * implements the Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overridden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	protected void connectionClosed() {
		clientUI.display("Connection closed");
	}
	protected String getLogInId() {
		return Id;
	}
	/**
	 * Hook method called after a connection has been established. The default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
	 */
	protected void connectionEstablished() {
		try {
			sendToServer("#login "+getLogInId());
		}
		catch (IOException e) {
            clientUI.display("Error: Unable to send login message to server.");
        }
	}
}
//End of ChatClient class
