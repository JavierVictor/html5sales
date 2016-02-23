package movistar.bean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

public class Itinerary extends Entity implements Serializable{
	private static final long serialVersionUID = 1L;
	private Company company;
	private ClientAddress clientAddress;
	private short day;// 1 domingo 7 sabado para diario | para quincenal 1 al 15
	private String frequency; //diaria semanal quincenal espcifica
	private Date date;
	
	public Itinerary(){
		super();
	}
	
	public Itinerary(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}
	
	public Itinerary(InputStream is) throws Exception {
		super(is);
		// TODO Auto-generated constructor stub
	}

	public Itinerary(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);
		// TODO Auto-generated constructor stub
	}

	public byte[] toByteArray() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream daos = new DataOutputStream(baos);
		daos.write(clientAddress.toByteArray());
		daos.writeUTF(getCode());
		daos.writeShort(getDay());
		daos.writeUTF(getFrequency());
		daos.writeLong(getDate()==null?0L:getDate().getTime());
		daos.close();
		
		return baos.toByteArray();
	}

	protected void makeObject(InputStream is) throws Exception {
		DataInputStream dis = new DataInputStream(is);
		setClientAddress(new ClientAddress(is, false));
		setCode(dis.readUTF());
		setDay(dis.readShort());
		setFrequency(dis.readUTF());
		setDate(new Date(dis.readLong()));
		
		if(closeStream)
			dis.close();

	}

	public ClientAddress getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(ClientAddress clientAddress) {
		this.clientAddress = clientAddress;
	}

	public short getDay() {
		return day;
	}

	public void setDay(short day) {
		this.day = day;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	
}
