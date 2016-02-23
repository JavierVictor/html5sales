package movistar.bean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

public class Currency extends Entity {
	private String symbol;
	private boolean local;
	private double exchange;
	
	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public Currency(){
		super();
	}
	
	public Currency(InputStream is) throws Exception {
		super(is);

	}

	public Currency(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);

	}

	public Currency(byte[] bytes) throws Exception {
		super(bytes);

	}

	protected void makeObject(InputStream is) throws Exception {
		DataInputStream dis = new DataInputStream(is);
		setCode(dis.readUTF());
		setName(dis.readUTF());
		setSymbol(dis.readUTF());
		setState(dis.readUTF());
		if(closeStream)
			dis.close();

	}

	public byte[] toByteArray() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream daos = new DataOutputStream(baos);
		
		daos.writeUTF(getCode());
		daos.writeUTF(getName());
		daos.writeUTF(getSymbol());
		daos.writeUTF(getState());
		daos.close();
		
		return baos.toByteArray();
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getExchange() {
	    return exchange;
	}

	public void setExchange(double exchange) {
	    this.exchange = exchange;
	}
}
