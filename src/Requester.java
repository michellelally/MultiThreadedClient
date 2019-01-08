
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Requester {

	public static void main(String args[]) {
		Thread client = new Thread(new Client());
		client.start();
	}
}

class Client implements Runnable {
	Socket connect;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message = "";
	String ipaddress;
	int portNo;
	Scanner console;

	Client() {
	}

	public void run() {
		// creating a socket to connect to the server
		console = new Scanner(System.in);
		System.out.println("Please Enter your IP Address");
		ipaddress = console.next();
		System.out.println("Please enter the TCP Port Number");
		portNo = console.nextInt();

		try {
			connect = new Socket(ipaddress, portNo);
			out = new ObjectOutputStream(connect.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connect.getInputStream());

			// Client app logic
			
			//Connection status 
			recieveMessage();
			
			do {
				// Menu 
				recieveAndSendMessage();
				
				if (message.equalsIgnoreCase("1")) {
					System.out.println("===Register===");
					//Enter Name
					recieveAndSendMessage();
					// Enter ID
					recieveAndSendMessage();
					//Enter Email
					recieveAndSendMessage();
					// Enter Dept
					recieveAndSendMessage();
					//Enter Password
					recieveAndSendMessage();
					// Successfully registered
					recieveMessage();
			
				} else if (message.equalsIgnoreCase("2")) {
					System.out.println("===Login===");
					// Enter username
					recieveAndSendMessage();
					// Enter password
					recieveAndSendMessage();
					// Login successful or unsuccessful
					recieveMessage();
					
					// Calling bugReportMenu if login is successful
					if (message.equalsIgnoreCase("Login Successful")) {
						bugReportMenu();
					} 
				}

				// reading in message from the server: Would you like to continue? Y/N
				message = (String) in.readObject();
				// outputting message from the server
				System.out.println(message);
				message = console.next();
				sendMessage(message);

			} while (message.equalsIgnoreCase("y"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
				connect.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	void recieveAndSendMessage() {
		try {
			// reading in message from the server
			message = (String) in.readObject();
			// outputting message from the server
			System.out.println(message);
			message = console.next();
			sendMessage(message);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void recieveMessage() {
		// reading in message from the server
		try {
			message = (String) in.readObject();
			// outputting message from the server
			System.out.println(message);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void bugReportMenu() {
		//Recieving username of user logged in 
		recieveMessage();
		
		// Bug report menu 
		recieveAndSendMessage();
		
		int option = Integer.parseInt(message);
		System.out.println("option: " + option);
		
		switch (option) {
			case 1: 
				// Add a new bug report
				recieveMessage();
				// Enter Application Name
				recieveAndSendMessage();
				// Enter Platform
				recieveAndSendMessage();
				// Enter Problem Description
				recieveAndSendMessage();
				// Enter Status
				recieveAndSendMessage();
				// Enter Bug ID 
				recieveAndSendMessage();
				// Bug added
				recieveMessage();
				break;
			case 2:
				// Assign a bug report
				recieveMessage();
				// Enter Bug ID
				recieveAndSendMessage();
				// Bug found/not found
				recieveMessage();
				
				// If bug is found assign it to employee
				if (message.equalsIgnoreCase("Bug Found")) {
					//Enter Empployee ID 
					recieveAndSendMessage();
					// Successfully assigned bug
					recieveMessage();	
				}
	
				break;
					
		}
		
	} //bugReportMenu 

}