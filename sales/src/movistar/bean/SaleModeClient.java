package movistar.bean;

import java.io.InputStream;

public class SaleModeClient extends Entity {

	private Client client;
	private SaleMode saleMode;

	public SaleModeClient() {
		super();
	}
	public SaleModeClient(byte[] bytes) throws Exception {
		super(bytes);
		// TODO Auto-generated constructor stub
	}

	public SaleModeClient(InputStream is) throws Exception {
		super(is);
		// TODO Auto-generated constructor stub
	}

	public SaleModeClient(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);
		// TODO Auto-generated constructor stub
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public SaleMode getSaleMode() {
		return saleMode;
	}

	public void setSaleMode(SaleMode saleMode) {
		this.saleMode = saleMode;
	}

}
