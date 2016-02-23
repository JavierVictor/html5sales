package movistar.bean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

public class User extends Entity {
	private String phoneNumber;
	private Company company;
	private String password;
	private Warehouse warehouse;
	private String latitude;
	private String longitude;
	
	
	public User(){
		super();
		phoneNumber="";
	}

	public User(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}

	public User(InputStream is) throws Exception {
		super(is);
		// TODO Auto-generated constructor stub
	}

	public User(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);
		// TODO Auto-generated constructor stub
	}

	public synchronized byte[] toByteArray() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream daos = new DataOutputStream(baos);
		daos.writeUTF(getCode());
		daos.writeUTF(getName());
		daos.writeUTF(getPhoneNumber());
		daos.writeUTF(getPassword());
		daos.writeUTF(getState());
		daos.write(getCompany().toByteArray());
		daos.write(getWarehouse().toByteArray());
		daos.close();
		baos.close();
		return baos.toByteArray();
	}

	protected void makeObject(InputStream is) throws Exception {
		DataInputStream dis = new DataInputStream(is);
		setCode(dis.readUTF());
		setName(dis.readUTF());
		setPhoneNumber(dis.readUTF());
		setPassword(dis.readUTF());
		setState(dis.readUTF());
		if(closeStream){
			dis.close();
		}
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
		if(this.warehouse!=null)
			this.warehouse.setCompany(getCompany());
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
