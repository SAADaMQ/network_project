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

                 // Compare calculated checksum with received checksum

                 if (!calculateChecksum.equals(receivedChecksum))  { //if checksum don't match
                     outToClient.writeBytes("Error: The Received Message is not correct\n"); // Send error message to the client
                  } else {
                     outToClient.writeBytes("Confirmation: Message received correctly \n");

                  }

          

               } // End the loop 

               // Close the connection and server socket

               clientSocket.close();
               serverSocket.close();


          } // End main 

             // checksum calculating


          private static String calculateChecksum(String message) {
               int checksum = 0;
                

               //loop through the message 16-bits at a time
               for (int i = 0; i < message.length(); i += 2) {
                  int word = 0;

                  // Get the first charachter (8 bits)
                  word = message.charAt(i) << 8; // Shift the first charcter 8 bits to the left (upper byte)

                  // Get the second charcter if it exists
                  if (i + 1 < message.length()) {
                     word += message.charAt(i + 1); // Add the second charcter (lower byte)

                  }

                  // Add the 16-bit word to the checksum
                  checksum += word;

                  // Handle carry (wrap around) if the sum exceeds 16 bits

                  if ((checksum & 0xFFFF0000) > 0) { // If ther's a carry beyond 16 bits 

                  checksum &= 0xFFFF; // Keep only the lower 16 bits
                  checksum++; // Add the carry

                  }
                                     
               } // End loop 

               // One's complement
               checksum = ~checksum;

               // Print the checksum as a binary string and hexdecimal value
               System.out.println("Checksum (binary):" + String.format("%16s", Integer.toBinaryString(checksum)).replace(' ', '0'));
               System.out.println("Checksum (hexdecimal): " + Integer.toBinaryString(checksum).toUpperCase());


               // Return the checksum as a hexdecimal string
               return  Integer.toHexString(checksum).toUpperCase();


 
            }     
          
     

             
             

              


} // End the class

        
    

    
