import socket
import random

# Function to calculate 16-bit one's complement checksum
def calculate_checksum(message):
    checksum = 0
    
    for i in range(0, len(message), 2):# Process the message in chunks of 2 bytes (16 bits)
        # Create a 16-bit word from two characters
        word = ord(message[i]) << 8  # Get first character (upper byte) ,
        #The ord() function returns the number of unicode code of a specified character.
        if i + 1 < len(message):  # Check if there's a second character
            word += ord(message[i + 1])  # Add second character (lower byte)
        checksum += word  # Sum the 16-bit word
        if checksum & 0x10000:  # Handles any carry that occurs if the sum exceeds 16 bits.
            checksum = (checksum & 0xFFFF) + 1  # Handle carry (wrap around)
    checksum = ~checksum & 0xFFFF  # One's complement (invert all bits)
    return format(checksum, '016b')  # Return as a 16-bit binary string

def start_server():
    server_address = '192.168.100.160'  #TEST Server IP address
    port = 8080  # the port of the server 

    # Create the server socket
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((server_address, port))  # Bind to the address and port
    server_socket.listen(1)  #  The server will allow one connection to wait in the queue(for simple server)
    
    print(f"The Server is Available and wait for Client on port {port}")

    # if you want the server is ALWAYS ON HOST use here infnite while true without break
    # this will make the server run even the client disconnect :) 
    # but we dont have the best resourcses for infnite loops :(  
    
    # Accept client connection
    client_socket, client_address = server_socket.accept()
    print(f"Client connected from {client_address}")

    while True:
        client_message = client_socket.recv(1024).decode('utf-8')  # Receive message

        if client_message.lower() == 'quit':  # Check if the client wants to quit
            print("Client has exited.")
            break

        # Split message into data and checksum
        message_parts = client_message.split(':')
        if len(message_parts) != 2:  # Ensure message is in the correct format
            client_socket.sendall("Error: Invalid message format\n".encode('utf-8'))
            continue

        message= message_parts[0]
        received_checksum =message_parts[1]
        print(f"Received Message: {message} with Checksum: {received_checksum}")

        # Calculate checksum for the received message
        calculated_checksum = calculate_checksum(message)







        # Check if calculated checksum matches the received checksum
        if calculated_checksum != received_checksum:
            client_socket.sendall(f"Error: The Received Message is not correct with server checksum: {calculated_checksum} client checksum: {received_checksum}\n".encode('utf-8'))
        else:
            client_socket.sendall(f"Acknowledged: Message received correctly checksum: {calculated_checksum} client checksum: {received_checksum}\n".encode('utf-8'))
#the\n helps ensure that when this error message is displayed on the client side, it’s printed correctly.
    
    client_socket.close()  # Close client connection
    server_socket.close()  # Close server

if __name__ == "__main__":
    start_server()  # Start the server
