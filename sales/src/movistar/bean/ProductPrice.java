package movistar.bean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

public class ProductPrice extends Entity {
	private Company company;
	private PriceList priceList;
	private ProductUnity productUnity;
	private double price;
	private Currency currency;

	public ProductPrice(){
		super();
		
	}
	
	public ProductPrice(byte[] bytes) throws Exception {
		super(bytes);
	}

	public ProductPrice(InputStream is) throws Exception {
		super(is);
	}

	public ProductPrice(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);
	}

	public byte[] toByteArray() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream daos = new DataOutputStream(baos);
		
		daos.write(getPriceList().toByteArray());
		daos.write(getProductUnity().toByteArray());
		daos.write(getCurrency().toByteArray());
		daos.writeDouble(getPrice());
		daos.writeUTF(getState());
		
		daos.close();
		
		return baos.toByteArray();
	}

	protected void makeObject(InputStream is) throws Exception {
		DataInputStream dis = new DataInputStream(is);
		setPriceList(new PriceList(is,false));
		setProductUnity(new ProductUnity(is,false));
		setCurrency(new Currency(is, false));
		setPrice(dis.readDouble());
		setState(dis.readUTF());
		
		if(closeStream)
			dis.close();

	}

	public PriceList getPriceList() {
		return priceList;
	}

	public void setPriceList(PriceList priceList) {
		this.priceList = priceList;
	}

	public ProductUnity getProductUnity() {
		return productUnity;
	}

	public void setProductUnity(ProductUnity productUnity) {
		this.productUnity = productUnity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	
}
