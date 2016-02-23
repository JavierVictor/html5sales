package movistar.bean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

public class SaleMode extends Entity{
	private String type; //contado credito, ambos
	private Company company;
	public SaleMode(){
		super();
	}
	
	public SaleMode(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);
		// TODO Auto-generated constructor stub
	}

	public SaleMode(InputStream is) throws Exception {
		super(is);
		// TODO Auto-generated constructor stub
	}
	
	public SaleMode(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}

	protected void makeObject(InputStream is) throws Exception {
		DataInputStream dis = new DataInputStream(is);
		setCode(dis.readUTF());
		setName(dis.readUTF());
		setState(dis.readUTF());
		if(closeStream)
			dis.close();

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	
}
