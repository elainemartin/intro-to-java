public class Passenger {
	
	private String name;
	private Flight flight;

	public Passenger (String inputName, Flight inputFlight) //Constructor
	{
		name = inputName;
		flight = inputFlight;
	}
	
	public String getName() {
		return name;
	}
	public int getFlightID() {
		return flight.getFlightID();
	}
	public String getOrigin() {
		return flight.getOrigin();
	}
	public String getDestination() {
		return flight.getDestination();
	}
	public String getFlightString() {
		return getOrigin() + " ; " + getDestination() + " ; " + getFlightID() + " ; ";
	}
}