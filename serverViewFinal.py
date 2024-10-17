import socket
import random

# calculate one's complement checksum (in binary format)
def calculate_checksum(msg):
    checksum = 0
    # Process the msg in chunks of 2 bytes (16 bits)
    for i in range(0, len(msg), 2):
    #Create a variable word with 16-bit from two characters 8 bit each 
        word = ord(msg[i]) << 8  #The ord()  function returns the number of unicode (ASCII) code of a specified character.
        #'A' the ord is 65 ,65*256=16,640 ,in binary: 01000001 00000000
        #so we Got first character (upper byte)  
        if i + 1 < len(msg):  # Check if there is a second character
            word += ord(msg[i + 1])  # Add second character (lower byte)
        checksum += word  # Sum the 16-bit 
        if checksum & 0x10000:  # check if there is a carry 
            checksum = (checksum & 0xFFFF) + 1
    checksum = ~checksum & 0xFFFF  # One's complement by using ~
    return format(checksum, '016b')  # Return as a 16-bit binary form as a string

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
        client_msg = client_socket.recv(1024).decode('utf-8')  # Receive msg

        if client_msg.lower() == 'quit':  # Check if the client wants to quit
            print("Client has exited.")
            break

        # Split msg into data and checksum with : 
        msg_parts = client_msg.split(':')
        if len(msg_parts) != 2:  # Ensure msg is in the correct format
            client_socket.sendall("Error: Invalid msg format\n".encode('utf-8'))
            continue

        msg= msg_parts[0]
        received_checksum =msg_parts[1]
        print(f"Received msg: {msg} with Checksum: {received_checksum}")

        # Calculate checksum for the received msg
        calculated_checksum = calculate_checksum(msg)







        # Check if calculated checksum == the received checksum
        if calculated_checksum != received_checksum:
            client_socket.sendall(f"Error: The Received msg is not correct with server checksum: {calculated_checksum} client checksum: {received_checksum}\n".encode('utf-8'))
        else:
            client_socket.sendall(f"Acknowledged: msg received correctly checksum: {calculated_checksum} client checksum: {received_checksum}\n".encode('utf-8'))
#the\n helps ensure that when this error msg is displayed on the client side, it’s printed correctly.
    
    client_socket.close()  # Close client connection
    server_socket.close()  # Close server

if __name__ == "__main__":
    start_server()  # Start the server
