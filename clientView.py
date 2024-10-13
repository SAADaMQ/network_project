import socket
import random

# Function to calculate 16-bit one's complement checksum (in binary format)
def calculate_checksum(message):
    checksum = 0
    # Process the message in chunks of 2 bytes (16 bits)
    for i in range(0, len(message), 2):
        # Create a 16-bit word from two characters
        word = ord(message[i]) << 8  # Get first character (upper byte)
        if i + 1 < len(message):  # Check if there's a second character
            word += ord(message[i + 1])  # Add second character (lower byte)
        checksum += word  # Sum the 16-bit word
        if checksum & 0x10000:  # Handle carry (wrap around)
            checksum = (checksum & 0xFFFF) + 1
    checksum = ~checksum & 0xFFFF  # One's complement (invert all bits)
    return format(checksum, '016b')  # Return as a 16-bit binary string

# Function to introduce errors in the message with a given probability
def introduce_error(message, error_probability):
    if random.random() < error_probability:  # Check if we should introduce an error
        error_position = random.randint(0, len(message) - 1)  # Choose a random position to corrupt
        message = message[:error_position] + random.choice('abcdefghijklmnopqrstuvwxyz') + message[error_position + 1:]
    return message

def start_client():
    server_ip = '192.168.100.160'  # Server's IP address
    port = 8080  # Port to connect to

    try:
        client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        client_socket.connect((server_ip, port))  # Connect to the server
    except socket.error:
        print("Server is down, please try later.")
        return

    # Ask if the user wants error simulation
    error_simulation = input("Do you want error simulation (yes/no)? ").strip().lower()
    error_probability = 0.0  # Default value for error probability

    if error_simulation == 'yes':
        while True:
            try:
                error_percentage = float(input("Enter the error probability percentage (e.g., 30 for 30%): ").strip())
                if 0 <= error_percentage <= 100:
                    error_probability = error_percentage / 100  # Convert percentage to probability (0-1)
                    break
                else:
                    print("Error: Please enter a value between 0 and 100.")
            except ValueError:
                print("Error: Invalid input, please enter a numerical value.")

    while True:
        # Input from user
        message = input("Enter a message: ")

        # Validate input
        if not message:
            print("Error: Empty message is not valid.")
            continue

        if message.lower() == 'quit':
            client_socket.sendall('quit'.encode('utf-8'))  # Notify the server to quit
            break

        # Calculate checksum for the message
        checksum = calculate_checksum(message)

        # Simulate errors in the message if the user opted for error simulation
        if error_simulation == 'yes':
            message = introduce_error(message, error_probability)

        # Send the message and its checksum to the server
        client_socket.sendall(f"{message}:{checksum}".encode('utf-8'))

        # Receive and print the server response
        server_response = client_socket.recv(1024).decode('utf-8')
        print("Server: ", server_response)

    client_socket.close()  # Close the socket connection

if __name__ == "__main__":
    start_client()  # Start the client
