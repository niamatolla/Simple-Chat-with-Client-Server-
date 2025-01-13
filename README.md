This project builds on the SimpleChat Application, enhancing its features to improve client-server communication. 
The implementation follows the guidelines provided in the assignment and includes several new functionalities for both the client and server sides.

Client-Side Enhancements:
Graceful Shutdown Handling: Detects server shutdown and exits gracefully.
Dynamic Port Configuration: Accepts port numbers via the command line with a fallback to default if omitted.
Command-Based Actions:
#quit: Graceful client termination.
#logoff: Disconnect from the server without quitting.
#sethost <host>: Change the host (only when logged off).
#setport <port>: Change the port (only when logged off).
#login: Connect to the server.
#gethost: Display the current host.
#getport: Display the current port.

Server-Side Enhancements:
Client Connection and Disconnection Alerts: Prints a message when clients connect or disconnect.
Command-Based Management:
#quit: Gracefully shuts down the server.
#stop: Stops accepting new clients.
#close: Stops new clients and disconnects existing ones.
#setport <port>: Updates the port (only when stopped).
#start: Resumes accepting new clients.
#getport: Displays the current port.
Broadcast Messages: Allows the server user to send messages prefixed with SERVER MSG>.

User Identification:
Login ID: Clients must provide a unique login ID on startup.
Message Attribution: All messages are prefixed with the sender's login ID for better traceability.

Test Cases:
The implementation has been validated against all provided test cases, ensuring robustness and correctness. Test results are included in the repository as Testcases.pdf.
