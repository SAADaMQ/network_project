import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class serverView { 


          public static void main(String[] args) throws IOException {
         
             int port = 9998; //Define the port on which server listen to clients
             ServerSocket serverSocket = new ServerSocket(port);
             System.out.println("Server is running and listening to port" + port);
             //Accept client connection 
             Socket clientSocket = serverSocket.accept(); //Server is looking for a client to connect
             BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Create a reader to receive maessages from the clients
             DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream()); // Create a stream to send responses to the clients

             // Create a loop to continuously receive message from clients 
             while (true) { 
                 String clientMessage = inFromClient.readLine(); // Read the clients' message 

                 // If the client sends "Quit", break the loop and cut the connection 
                 if (clientMessage.equalsIgnoreCase("Quit")) { // check if client wants to quit  

                    System.out.println("Client has exited");
                    break; // Break the loop and stop the server
                 } // End First if 

                 // Split message and checksum in format (meesage:checksum)
                 String[] messageParts = clientMessage.split(":");

                 if (messageParts.length != 2) { // Check of message format validity
                    outToClient.writeBytes(("Error: Invalid message format\n")); // Send error if the format is wrong
                    continue; // Skip this iteration and wait for the message


                 } // End second if 

                 String message = messageParts[0];
                 String receivedChecksum = messageParts[1]; // Extract the received checkusm
                 System.out.println("Received Message: " + message + "With checkum" + receivedChecksum); // Print out received message from the client

                 // Caluclate checksum of the received message
                 String calculateChecksum = calculateChecksum(message);

          

               } // End the loop 

               


          } // End main 

             // checksum calculating


          private static String calculateChecksum(String message) {
               int checksum = 0;
                

               //loop through the message 16-bits at a time
               for (int i = 0; i < messagelength(); i+=2) {

                   
               }

           }     
          
     

             
             

              


} // End the class

        
    

    
