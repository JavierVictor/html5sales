package movistar.bean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

public class ClientAddress extends Entity {
	private Company company;
	private Client client;
	private String type;
	private String latitude;
	private String longitude;

	public ClientAddress(){
		super();
	}

	public ClientAddress(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);
		// TODO Auto-generated constructor stub
	}

	public ClientAddress(InputStream is) throws Exception {
		super(is);
		// TODO Auto-generated constructor stub
	}

	public ClientAddress(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}

	public byte[] toByteArray() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream daos = new DataOutputStream(baos);
		daos.write(getClient().toByteArray());
		daos.writeUTF(getCode());
		daos.writeUTF(getName());
		daos.writeUTF(getType());
		daos.writeUTF(getState());
		
		daos.close();
		
		return baos.toByteArray();
	}

	protected void makeObject(InputStream is) throws Exception {
		DataInputStream dis = new DataInputStream(is);
		setClient(new Client(is));
		setCode(dis.readUTF());
		setName(dis.readUTF());
		setType(dis.readUTF());
		setState(dis.readUTF());
		
		if(closeStream);
			dis.close();

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Company getCompany() {
		return company;
	}

	public String getLatitude() {
	    return latitude;
	}

	public void setLatitude(String latitude) {
	    this.latitude = latitude;
	}

	public String getLongitude() {
	    return longitude;
	}

	public void setLongitude(String longitude) {
	    this.longitude = longitude;
	}
}