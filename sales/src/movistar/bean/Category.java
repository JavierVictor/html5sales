package movistar.bean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

public class Category extends Entity {
	private Category parent = null;
	private Category[] categories = null;
	private Company company;

	public Category() {
		super();
	}

	public Category(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}

	public Category(InputStream is) throws Exception {
		super(is);
	}

	public Category(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);
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

	protected void makeObject(InputStream is) throws Exception {
		DataInputStream dis = new DataInputStream(is);
		setCode(dis.readUTF());
		setName(dis.readUTF());
		setState(dis.readUTF());
		if(closeStream)
			dis.close();

	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Category[] getCategories() {
		return categories;
	}

	public void setCategories(Category[] categories) {
		this.categories = categories;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
