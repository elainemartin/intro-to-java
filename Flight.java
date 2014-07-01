import java.util.ArrayList; 
import java.util.Scanner;
import java.io.*;

public class Flight {
	
	private static ArrayList<String[]> allFlights = new ArrayList<String[]>();
	private int flightID;
	private String origin;
	private String destination;
	
	public Flight(String FLIGHTFILE) throws FileNotFoundException {
		allFlights = readFlights(FLIGHTFILE);	
	}
	
	public Flight (String inputOrigin, String inputDestination) {
		flightID = findFlight(inputOrigin, inputDestination);
		origin = inputOrigin;
		destination = inputDestination;
	}
	
	/**
	Gets flightID for single origin & destination pair
	@return array list string array of flight details
	 */
	private ArrayList<String[]> readFlights(String flightsFile) throws FileNotFoundException {
		File flightFile = new File(flightsFile);
		Scanner in = new Scanner(flightFile).useDelimiter("[;]+");
		ArrayList<String[]> allflights = new ArrayList<String[]>();
//		allflights.add(new String[3]);
		
		while(in.hasNextLine()) {
			String[] flightDetails = new String[3];
			flightDetails[0] = in.next().trim();
			flightDetails[1] = in.next().trim();
			flightDetails[2] = in.next().trim();
			in.nextLine();
			allflights.add(flightDetails);
		}
		in.close();
		return allflights;
	}
	
	private int findFlight(String origin, String destination) {
		int position = 0;
		while(position < allFlights.size()) { //Find origin & destination
			if (origin.equals(allFlights.get(position)[0]) && destination.equals(allFlights.get(position)[1])) {
				return Integer.parseInt(allFlights.get(position)[2]);
			}
			else position++;
		}
		return -1;
	}
	public int getFlightID() {
		return flightID;
	}
	public String getOrigin() {
		return origin;
	}
	public String getDestination() {
		return destination;
	}	

	public static boolean validFlightNumber(String input) {
		for(int i = 0; i < allFlights.size(); i++) {
			if(input.equals(allFlights.get(i)[2]))
				return true;
		}
		return false;
	}
}