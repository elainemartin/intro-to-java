/**
Authors:		Elaine Martin, Sari Nahmad, Anthony Tockar
Major:			Master of Science in Analytics
Purpose:		To allow users to add passengers to flights and output flight information in a variety of ways.
General design: User is presented with a variety of options (N,S,U,B,Q), each of which lead them to a different
				part of the program which executes those options. The program reads from and writes to a file
				that includes the flight itineraries for all passengers, and also uses a file to match origins
				and destinations to flight numbers.
*/

import java.io.*;
import java.util.*;

public class Project4 {

	public static void main(String[] args) throws FileNotFoundException {

		final String USERFILE = "userItineraries.txt";
		final String FLIGHTFILE = "allFlights.txt";

		boolean done = false;
		
		//Initialize USERFILE for first time user
		try {
			getPassengers(USERFILE);
		}
		catch (FileNotFoundException except) {
			PrintWriter out = new PrintWriter(USERFILE);
			out.close();
		}
		
		try {
			new Flight(FLIGHTFILE);
		}
		catch (FileNotFoundException except) {
			System.out.println(FLIGHTFILE+" does not exist. Please ensure " +FLIGHTFILE+" is in the proper directory.");
			System.exit(1);
		}

		System.out.println("Welcome to the Flight Planner");

		while (!done) {

			printOptions(); // Print menu of options available to the user

			char userSelection = menuInput(); // Read in user selection from menu options

			if (userSelection == 'Q') {
				done = true;
			}
			else
			{
				ArrayList<Passenger> passengers = new ArrayList<Passenger>();
				passengers = getPassengers(USERFILE);

				if (userSelection == 'N') {
					Nmethod(passengers, FLIGHTFILE, USERFILE);
				}
				else if (userSelection == 'S') {
					Smethod(USERFILE);
				}
				else if (userSelection == 'U') {
					Umethod(USERFILE);
				}
				else if (userSelection == 'B') {
					Bmethod(USERFILE,FLIGHTFILE);
				}

				System.out.print("\nWhat would you like to do next?\n\n");
			}
		}
	}

	/**
	 Prints the menu of options available to the user
	 */
	public static void printOptions() {
		System.out.println("Please Select From One of the Following:");
		System.out.println("N - in order to create a new flight reservation");
		System.out.println("S - in order to show all the data for the existing reservations for all the users");
		System.out.println("U - in order to show all the reservations for a particular user");
		System.out.println("B - in order to show the names of the customers who have bookings for a particular flights");
		System.out.println("Q - in order to quit this application");
	}

	public static void Nmethod(ArrayList<Passenger> passengers, String flightsFile, String userFile) throws FileNotFoundException {
		
		Scanner option = new Scanner(System.in);
	
		String name;
		String origin;
		String destination;
		
		do {
			name = nameInput();
			origin = originInput();
			destination = destinationInput();
			System.out.println("You have input the following information:");
			System.out.println("Name: " + name);
			System.out.println("Origin: " + origin);
			System.out.println("Destination: " + destination);
			System.out.print("Is this correct? (Y/N) "); 
		} while (!option.next().toUpperCase().equals("Y"));
		
		Flight flight = new Flight(origin, destination);
		while(flight.getFlightID() == -1) {
			System.out.println("There are no flights from "+origin+" to "+destination+".");
			origin = originInput();
			destination = destinationInput();
			flight = new Flight(origin, destination);
		}
		
		boolean existingRecord = checkRecords(name, flight, passengers);

		if(!existingRecord) //Check if user's flight already exists
		{	Passenger newPassenger = new Passenger(name, flight);
			passengers.add(newPassenger);
			writeFile(userFile, passengers);
		}	
		else {
			System.out.println();
			System.out.println("Error: This record already exists.");
		}
	}

	/**
	 Reads in user selection and ensures it is correct
	 */
	public static char menuInput() {
		Scanner in = new Scanner(System.in);
		char userInput = ' ';
		boolean done = false;
		while (!done) {
			userInput = in.nextLine().toUpperCase().charAt(0);
			if (userInput == 'N' || userInput == 'S' || userInput == 'U' || userInput == 'B' || userInput == 'Q') {
				done = true;
			}
			else {
				System.out.println("Please enter N, S, U, B or Q");
			}
		}
		return userInput;
	}

	/**
	 Asks for the user to input the new passenger's first and last name
	 @return a string with the passenger's full name
	 */
	public static String nameInput() {
		Scanner in = new Scanner(System.in);

		System.out.print("Please enter the passenger's first name: ");
		String firstName = in.nextLine().trim();

		System.out.print("Now please enter the passenger's last name: ");
		String lastName = in.nextLine().trim();

		return (firstName + " " + lastName);
	}

	/**
	 Asks the user for the origin of the flight for the new entry
	 @return the text input by the user
	 */
	public static String originInput() {
		Scanner in = new Scanner(System.in);
		System.out.print("Please enter the flight's origin: ");
		return in.nextLine().trim();
	}

	/**
	 Asks the user for the destination of the flight for the new entry
	 @return the text input by the user
	 */
	public static String destinationInput() {
		Scanner in = new Scanner(System.in);
		System.out.print("Please enter the flight's destination: ");
		return in.nextLine().trim();
	}

	/**
	Makes array of all details in user itinerary file 
	@param NA
	@return getPassengers
	 */
	public static ArrayList<Passenger> getPassengers(String userFile) throws FileNotFoundException
	{
		File inputFile = new File(userFile);
		Scanner in = new Scanner(inputFile);

		ArrayList<Passenger> passengers = new ArrayList<Passenger>(); 

		while(in.hasNextLine())
		{
			String line = in.nextLine(); 
			String[] temp = line.split("\\s*;\\s*");
	
			Flight flight = new Flight(temp[1],temp[2]);
			Passenger person = new Passenger(temp[0], flight);
			passengers.add(person);
		}

		in.close();
		return passengers; 
	}

	/**
	Makes array of all details in sample flights text and prints all existing flights 
	@param NA
	@return getPassengers
	 */
	public static String [][] printFlights(String flightsFile) throws FileNotFoundException
	{
		File inputFile = new File(flightsFile); 
		Scanner in = new Scanner(inputFile); 

		int counter = 0; 
		while(in.hasNextLine())
		{
			counter = counter + 1;
			in.nextLine();
		}

		final int ITEMS = 3; 
		String[][] getPassengers = new String [counter][ITEMS];

		System.out.println("The following are the current flight options:");
		System.out.println("Origin:                  Destination:             Flight Number:");

		int x = 0;

		Scanner in2 = new Scanner(inputFile); 
		while(in2.hasNextLine())
		{
			String line = in2.nextLine(); 
			String[] temp = line.split("\\s*;\\s*");

			getPassengers[x][0] = temp[0];
			getPassengers[x][1] = temp[1];
			getPassengers[x][2] = temp[2];
			System.out.printf("%-25s%-25s%-25s", temp[0], temp[1],temp[2]);
			System.out.println();
			x=x+1;
		}
		System.out.println();

		in.close();
		in2.close();
		return getPassengers; 
	}

	/**
	S method: shows all data for all the existing reservations for all the users
	@param NA
	@return NA
	 */
	public static void Smethod(String userFile) throws FileNotFoundException
	{	
		ArrayList<Passenger> passengers = getPassengers(userFile);

		System.out.println("Passenger Name:          Origin:                  Destination:             Flight Number:");
		for(int i=0; i<passengers.size(); i++)
		{
			System.out.printf("%-25s%-25s%-25s%-25s", passengers.get(i).getName(), passengers.get(i).getOrigin(), passengers.get(i).getDestination(), passengers.get(i).getFlightID());
			System.out.println();
		}
	}

	/**
	U method: shows all the reservations for a particular user
	@param NA
	@return NA
	 */
	public static void Umethod(String userFile) throws FileNotFoundException
	{
		ArrayList<Passenger> passengers = getPassengers(userFile);

		Scanner in = new Scanner(System.in);
		System.out.println();
		System.out.print("Please input passenger name: ");
		String matchName = in.nextLine();

		int checkPassenger = 0; 

		for(int i=0; i<passengers.size(); i++)
		{
			String name = passengers.get(i).getName(); 
			if(matchName.equals(name))
			{
				checkPassenger++; 
				if (checkPassenger==1)
				{
					System.out.println();
					System.out.println("Passenger Name:          Origin:                  Destination:             Flight Number:");
				}
				System.out.printf("%-25s%-25s%-25s%-25s", passengers.get(i).getName(), passengers.get(i).getOrigin(), passengers.get(i).getDestination(), passengers.get(i).getFlightID());
				System.out.println();
			}
		}
		if (checkPassenger==0)
			System.out.printf("Passenger does not have a reservation.");
		System.out.println();
	}

	/**
	B method: shows the names of the customers who have bookings for a particular flight
	@param NA
	@return NA
	 */
	public static void Bmethod(String userFile, String flightsFile) throws FileNotFoundException {
		String [][] flights = printFlights(flightsFile);

		boolean realFlight = false;
		String matchFlight = "";

		do //checks validity of user input flight number
		{
			Scanner in = new Scanner(System.in);
			System.out.print("Please input valid Flight Number: ");
			matchFlight = in.next();
			realFlight = Flight.validFlightNumber(matchFlight);
		} while(!realFlight);

		ArrayList<Passenger> passengers = getPassengers(userFile);

		int passengerCount = 0; 

		for(int i=0; i<passengers.size(); i++)
		{
			int flight = passengers.get(i).getFlightID(); 
			if(Integer.parseInt(matchFlight) == flight)
			{
				passengerCount++; 
				if (passengerCount==1)
				{
					System.out.printf("The following passengers are on Flight Number %s:", matchFlight);
					System.out.println();
				}
				System.out.printf("%-20s", passengers.get(i).getName());
				System.out.println();
			}
		}
		if (passengerCount==0)
		{
			System.out.println();
			System.out.println("There are no passengers on this flight.");
		}
	}

	/**
	Saves current arrays
	@param 4 arrays of names, origins, destinations, & flightIDs; length of arrays
	 */
	public static void writeFile(String userFile, ArrayList<Passenger> passengers) throws FileNotFoundException {

		File itineraryFile = new File(userFile);
		PrintWriter out = new PrintWriter(itineraryFile);

		for(int i = 0; i < passengers.size(); i++) {
				out.print(passengers.get(i).getName() + " ; " + passengers.get(i).getFlightString());
			out.println();
		}

		out.close();
	}

	/**
	Checks if user's input already exists
	@param userfile, name, flight#
	@return true or false
	 */
	public static boolean checkRecords(String matchName, Flight flight, ArrayList<Passenger> passengers) throws FileNotFoundException {
		for(int i = 0; i < passengers.size(); i++)
		{
			String name = passengers.get(i).getName();
			int flightID = passengers.get(i).getFlightID();
			if(matchName.equals(name) && flight.getFlightID() == flightID)
				return true;
		}
		return false;
	}

}
