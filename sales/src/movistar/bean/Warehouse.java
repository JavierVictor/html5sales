package movistar.bean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

public class Warehouse extends Entity {
	private Company company;
	
	public Warehouse() {
		super();
	}
	
	public Warehouse(InputStream is) throws Exception {
		super(is);

	}

	public Warehouse(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);

	}

	public Warehouse(byte[] bytes) throws Exception {
		super(bytes);

	}

	protected void makeObject(InputStream is) throws Exception {
		DataInputStream dis = new DataInputStream(is);
		setCode(dis.readUTF());
		setName(dis.readUTF());
		setState(dis.readUTF());
		if(closeStream)
			is.close();
	}

	public byte[] toByteArray() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream daos = new DataOutputStream(baos);
		daos.writeUTF(getCode());
		daos.writeUTF(getName());
		daos.writeUTF(getState());
		daos.close();
		return baos.toByteArray();
		
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
