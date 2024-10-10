import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerView {

        public static void main(String[] args) throws IOException {
         
             int port = 9998; //Define the port on which server listen to clients
             ServerSocket serverSocket = new ServerSocket(port);
             System.out.println("Server is running and listening to port" + port);
             //Accept client connection 
             Socket clientSocket = serverSocket.accept(); //Server is looking for a client to connect
             BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputSream()));
             DataOutputStream 

             


              


        }

        
    }
