package movistar.bean;

import java.io.InputStream;

public class PriceListClient extends Entity {
	private PriceList priceList;
	private Client client;

	public PriceListClient(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}

	public PriceListClient(InputStream is) throws Exception {
		super(is);
		// TODO Auto-generated constructor stub
	}
	
	public PriceListClient(){
		super();
	}

	public PriceListClient(InputStream is, boolean closeStream)
			throws Exception {
		super(is, closeStream);
	}

	@Override
	public byte[] toByteArray() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void makeObject(InputStream is) throws Exception {
		// TODO Auto-generated method stub

	}

	public PriceList getPriceList() {
		return priceList;
	}

	public void setPriceList(PriceList priceList) {
		this.priceList = priceList;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
