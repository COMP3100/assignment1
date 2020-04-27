import java.io.*;
import java.net.*;
import java.util.*;
	
public class Client {
	
	public static void writeToServer(Socket s, String message) {
		try {
			PrintWriter out = new PrintWriter(s.getOutputStream());
			out.println(message);
			out.flush();
		} catch (IOException e) {
			System.out.println("IOException in writeToServer " + e);
		}
	}
	
	public static String readFromServer(Socket s) {
		String input = "";
		try {
			InputStreamReader in = new InputStreamReader(s.getInputStream());
			BufferedReader bufIn = new BufferedReader(in);
			input = bufIn.readLine();
		} catch (IOException e) {
			System.out.println("IOException in readFromServer " + e);
		}
		return input;
	}
	
	public static String findLargest(List<Server> serverList) {
		int largest = 0; 
		for (int i = 1; i < serverList.size(); i++) {
			if (serverList.get(largest).coreCount < serverList.get(i).coreCount) {
				largest = i;
			}
		}
		
		return " " + serverList.get(largest).type + " ";
	}
	
	public static void main(String[] args) {
		try {
			Socket s = new Socket("127.0.0.1", 50000);
			String fromServer = "";
			String[] jobDetails = null;
			String schedulingDecision = "";

			writeToServer(s, "HELO");
			fromServer = readFromServer(s);

			writeToServer(s, "AUTH " + System.getProperty("user.name"));
			fromServer = readFromServer(s);
			
			List<Server> serverList = ReadXMLFile.parseSystemXML("system.xml");
			String largestServerType = findLargest(serverList);
			writeToServer(s, "REDY");
			fromServer = readFromServer(s);			

			while (!fromServer.equals("NONE")) {

				jobDetails = fromServer.split(" ");
				schedulingDecision = "SCHD " + jobDetails[2] + largestServerType + "0";

				writeToServer(s, schedulingDecision);
				fromServer = readFromServer(s);

				writeToServer(s, "REDY");
				fromServer = readFromServer(s);
			}

			writeToServer(s, "QUIT");
			fromServer = readFromServer(s);
			s.close();

		} catch (IOException e) {
			System.out.println("IO Exception in main " + e);
		}

	}
}
