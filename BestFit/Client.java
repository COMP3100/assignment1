import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	// Declare global variables
	static Socket s = null;
	static String fromServer = "";
	static String[] jobDetails = null;
	static String schedulingDecision = "";
	static List<Server> allServers = null;

	public static void main(String[] args) {
		try {
			// Create socket connection
			s = new Socket("127.0.0.1", 50000);

			// Exchanging with the Server
			writeToServer(s, "HELO");
			fromServer = readFromServer(s);

			writeToServer(s, "AUTH " + System.getProperty("user.name"));
			fromServer = readFromServer(s);

			// create a list of servers from system.xml
			allServers = ReadXMLFile.parseSystemXML("system.xml");

			// Continue communication with Server
			writeToServer(s, "REDY");
			fromServer = readFromServer(s);

			// loop through all jobs to be scheduled
			while (!fromServer.equals("NONE")) {
				jobDetails = fromServer.split(" ");

				if (args.length > 0) {
					if (args[1].equals("ff")) {
						firstFit();
					} else if (args[1].contentEquals("bf")) {
						bestFit();
					}
				} else {
					allToLargest();
				}

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

	// Function to write to the server
	public static void writeToServer(Socket s, String message) {
		try {
			PrintWriter out = new PrintWriter(s.getOutputStream());
			out.println(message);
			out.flush();
		} catch (IOException e) {
			System.out.println("IOException in writeToServer " + e);
		}
	}

	// Function to read from the server
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

	// Function to implement the AllToLargest algorithm
	public static void allToLargest() {
		int largest = 0;
		for (int i = 1; i < allServers.size(); i++) {
			// compare the size of the servers
			if (allServers.get(largest).coreCount < allServers.get(i).coreCount) {
				largest = i;
			}
		}
		schedulingDecision = "SCHD " + jobDetails[2] + " " + allServers.get(largest).type + " " + "0";
	}

	// Function to implement the FirstFit algorithm
	public static void firstFit() {
		String[] serverDetails = null;

		writeToServer(s, "RESC Avail " + jobDetails[4] + " " + jobDetails[5] + " " + jobDetails[6]);
		fromServer = readFromServer(s);

		// loop through the server details
		while (!fromServer.equals(".")) {
			if (fromServer.contentEquals("DATA")) {
				writeToServer(s, "OK");
				fromServer = readFromServer(s);
				// Handle if the server returns no available servers
				if (fromServer.contentEquals(".")) {
					allToLargest();
					break;
				} else {
					serverDetails = fromServer.split(" ");
					schedulingDecision = "SCHD " + jobDetails[2] + " " + serverDetails[0] + " " + serverDetails[1];
				}
			}
			writeToServer(s, "OK");
			fromServer = readFromServer(s);
		}
	}

	// Function to implement BestFit algorithm
	public static void bestFit() {
		String[] serverDetails = null;
		String[] bestServer = null;
		int bestFitVal = 100;
		int bestAvailTime = 1000;

		writeToServer(s, "RESC Capable " + jobDetails[4] + " " + jobDetails[5] + " " + jobDetails[6]);
		fromServer = readFromServer(s);

		writeToServer(s, "OK");
		fromServer = readFromServer(s);

		// loop through server details
		while (!fromServer.equals(".")) {
			serverDetails = fromServer.split(" ");

			// if this server has the availabe resources to run the job, calculate fitness value
			if (Integer.parseInt(serverDetails[4]) >= Integer.parseInt(jobDetails[4])) {
				int fitVal = Integer.parseInt(serverDetails[4]) - Integer.parseInt(jobDetails[4]);
				int availTime;
				if (serverDetails[3].equals("-1")) {
					availTime = 0;
				} else {
					availTime = Integer.parseInt(serverDetails[3]);
				}
				if (fitVal < bestFitVal || fitVal == bestFitVal && availTime < bestAvailTime) {
					bestFitVal = fitVal;
					bestAvailTime = availTime;
					bestServer = serverDetails;
				}
			}
			writeToServer(s, "OK");
			fromServer = readFromServer(s);
		}
		// if no server had sufficient available resources
		if (bestServer == null) {
			for (int i = 0; i < allServers.size(); i++) {
				if (allServers.get(i).getCoreCount() >= Integer.parseInt(jobDetails[4])) {
					String temp = allServers.get(i).getType() + " 0";
					bestServer = temp.split(" ");
				}
			}
		}
		schedulingDecision = "SCHD " + jobDetails[2] + " " + bestServer[0] + " " + bestServer[1];
	}
}