package movistar.bean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.List;

public class ProductUnity extends Entity {
	private Product product;
	private double stock;
	private Warehouse warehouse;
	private Company company;
	private transient List<ProductPrice> productPrices;
	
	public List<ProductPrice> getProductPrices() {
	    return productPrices;
	}

	public void setProductPrices(List<ProductPrice> productPrices) {
	    this.productPrices = productPrices;
	}

	public ProductUnity(){
		super();
	}

	public ProductUnity(byte[] bytes) throws Exception {
		super(bytes);
	}

	public ProductUnity(InputStream is) throws Exception {
		super(is);
	}

	public ProductUnity(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);
	}

	public byte[] toByteArray() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream daos = new DataOutputStream(baos);
		daos.write(getProduct().toByteArray());
		daos.write(getWarehouse().toByteArray());
		daos.writeUTF(getCode());
		daos.writeUTF(getName());
		daos.writeDouble(getStock());
		daos.writeUTF(getState());
		daos.close();
		baos.close();
		return baos.toByteArray();
	}

	protected void makeObject(InputStream is) throws Exception {
		DataInputStream dis = new DataInputStream(is);
		setProduct(new Product(is,false));
		setWarehouse(new Warehouse(is, false));
		setCode(dis.readUTF());
		setName(dis.readUTF());
		setStock(dis.readDouble());
		setState(dis.readUTF());
		if(closeStream)
			dis.close();

	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getStock() {
		return stock;
	}

	public void setStock(double stock) {
		this.stock = stock;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Company getCompany() {
		return company;
	}

}
