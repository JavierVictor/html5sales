package movistar.bean;

import java.io.InputStream;

public class CreditClient extends Entity {

	
	private static final long serialVersionUID = 8035L;
	private Client client;
	private Currency currency;
	private double amount;
	
	
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

}
