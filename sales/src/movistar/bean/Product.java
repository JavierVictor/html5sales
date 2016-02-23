package movistar.bean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.List;

public class Product extends Entity {
	private Company company;
	private Category category;
	private String tradeMark;
	
	private transient List<ProductUnity> productUnities;
	
	public List<ProductUnity> getProductUnities() {
	    return productUnities;
	}

	public void setProductUnities(List<ProductUnity> productUnities) {
	    this.productUnities = productUnities;
	}

	public Product(){
		super();
		tradeMark="";
	}

	public Product(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);
		// TODO Auto-generated constructor stub
	}

	public Product(InputStream is) throws Exception {
		super(is);
		// TODO Auto-generated constructor stub
	}

	public Product(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}

	public byte[] toByteArray() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream daos = new DataOutputStream(baos);
		
		daos.writeUTF(getCode());
		daos.writeUTF(getName());
		daos.writeUTF(getTradeMark());
		daos.write(getCategory().toByteArray());
		daos.writeUTF(getState());
		
		daos.close();
		return baos.toByteArray();
	}

	protected void makeObject(InputStream is) throws Exception {
		DataInputStream dis = new DataInputStream(is);
		setCode(dis.readUTF());
		setName(dis.readUTF());
		setTradeMark(dis.readUTF());
		setCategory(new Category(is,false));
		setState(dis.readUTF());
		if(closeStream)
			dis.close();
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTradeMark() {
		return tradeMark;
	}

	public void setTradeMark(String tradeMark) {
		this.tradeMark = tradeMark;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	

}
