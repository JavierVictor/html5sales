package movistar.bean;

import java.io.InputStream;

public class SaleDetail extends Entity {
	
	private static final long serialVersionUID = 1L;
	private String saleCode;
	private int item;
	private double price;
	private ProductUnity productUnity;
	private double editedPrice;
	private int quantity;
	private double discount;
	private double subtotal;

	public SaleDetail(){
		super();
	}
	
	public SaleDetail(InputStream is) throws Exception {
		super(is);
	}

	public SaleDetail(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);
		// TODO Auto-generated constructor stub
	}

	public SaleDetail(byte[] bytes) throws Exception {
		super(bytes);
	}

	public byte[] toByteArray() throws Exception {
		return null;
	}

	protected void makeObject(InputStream is) throws Exception {

	}

	public String getSaleCode() {
		return saleCode;
	}

	public void setSaleCode(String saleCode) {
		this.saleCode = saleCode;
	}

	public void setQuantity(short quantity) {
		this.quantity = quantity;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public ProductUnity getProductUnity() {
		return productUnity;
	}

	public void setProductUnity(ProductUnity productUnity) {
		this.productUnity = productUnity;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getEditedPrice() {
		return editedPrice;
	}

	public void setEditedPrice(double editedPrice) {
		this.editedPrice = editedPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getSubtotal() {
	    return subtotal;
	}

	public void setSubtotal(double subtotal) {
	    this.subtotal = subtotal;
	}
}
