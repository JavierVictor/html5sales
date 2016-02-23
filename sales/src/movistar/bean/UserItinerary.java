package movistar.bean;

import java.io.InputStream;
import java.util.List;

public class UserItinerary extends Entity {
    private static final long serialVersionUID = 15246332731L;
	private User user;
	private List<Itinerary> itineraries;
	private String assignedDate;

	public UserItinerary() {
		super();
	}
	public UserItinerary(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}
	public UserItinerary(InputStream is) throws Exception {
		super(is);
		// TODO Auto-generated constructor stub
	}
	public UserItinerary(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);
		// TODO Auto-generated constructor stub
	}
	public byte[] toByteArray() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	protected void makeObject(InputStream is) throws Exception {
		// TODO Auto-generated method stub
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Itinerary> getItineraries() {
		return itineraries;
	}
	public void setItineraries(List<Itinerary> itineraries) {
		this.itineraries = itineraries;
	}
	public String getAssignedDate() {
	    return assignedDate;
	}
	public void setAssignedDate(String assignedDate) {
	    this.assignedDate = assignedDate;
	}
}
