import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;
public class client {
    private static int requestCount = 0;
    private static int errorRequestsCount = 0;
    public static void main(String[] args) throws Exception {
        String serverIP = "127.0.0.1"; 
        int port = 9999;
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(serverIP, port);
            
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Server is Down , Please try again later ");
            return; //exit or break 
        } // end catch 
        // create i/o  streams for connection ; 
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); // getting user input 
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); //sending msg to server
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // loop for sending messages from user to server 
        while (true) {
            System.out.println("Enter A message: ");
            String message = inFromUser.readLine();

            // message validation .
            if (message.isEmpty()){
                System.out.println("Error: Empty message is not valid ");
                continue;
            } //

            if(message.equalsIgnoreCase("Quit")){
                outToServer.writeBytes(message + "\n");
                break;
            } // check for quitting  command 
        
            String checksum = calculateChecksum(message);

            message = simulateError(message,0.3);



            outToServer.writeBytes(message + ":" + checksum +"\n");
            String serverResponse = inFromServer.readLine();
            System.out.println("Server: " + serverResponse);
        } // end while 
            clientSocket.close(); // close socket connection to server 
        } // end main 
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
         private static String simulateError(String message, double ErrorProbability ) {
            requestCount++;
            int expectedErrors = (int) (requestCount * ErrorProbability);
            if (errorRequestsCount < expectedErrors ){
                errorRequestsCount++;
                return introduceRandomError(message);
            }
            return message;

         }
         private static String introduceRandomError(String message){
            Random random = new Random();
            StringBuilder CorruptedMessage = new StringBuilder(message);

            int errorPosition = random.nextInt(message.length());
            CorruptedMessage.setCharAt(errorPosition, (char) (random.nextInt(26)+ 'a'));
            return CorruptedMessage.toString();
         }



    }

