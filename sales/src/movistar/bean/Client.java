package movistar.bean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.List;

public class Client extends Entity {
	private Company company;
	private String ruc;
	private ClientType clientType;
	private List<ClientAddress> clientAddresses;
	private String phone;
	private List<SaleMode> saleModes;
	private List<PriceList> priceLists;
	
	public Client(byte[] bytes) throws Exception {
		super(bytes);
	}
	
	public Client(){
	    this.ruc = "";
	    this.phone = "";
	}

	public Client(InputStream is) throws Exception {
	    super(is);
	}

	public Client(InputStream is, boolean closeStream) throws Exception {
	    super(is, closeStream);
	}

	public byte[] toByteArray() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream daos = new DataOutputStream(baos);
		daos.write(getClientType().toByteArray());
		daos.writeShort(getSaleModes().size());
		for(SaleMode saleMode : saleModes){
			daos.write(saleMode.toByteArray());
		}
		daos.writeShort(getPriceLists().size());
		for(PriceList priceList:priceLists){
			daos.write(priceList.toByteArray());
		}
		daos.writeUTF(getCode());
		daos.writeUTF(getName());
		daos.writeUTF(getRuc());
		daos.writeUTF(getPhone());
		daos.writeUTF(getState());
		daos.close();
		
		return baos.toByteArray();
	}

	protected void makeObject(InputStream is) throws Exception {
		DataInputStream dis = new DataInputStream(is);
		setClientType(new ClientType(is, false));
		SaleMode[] saleModeTemp = new SaleMode[dis.readShort()];
		for(int i=0;i<saleModeTemp.length;i++){
			saleModeTemp[i]=new SaleMode(is,false);
		}
		PriceList[] priceListTemp = new PriceList[dis.readShort()];
		for(int i = 0; i < priceListTemp.length; i++){
			priceListTemp[i]=new PriceList(is,false);
		}
		
		setCode(dis.readUTF());
		setName(dis.readUTF());
		setRuc(dis.readUTF());
		setPhone(dis.readUTF());
		setState(dis.readUTF());
		
		if(closeStream)
		    dis.close();

	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public ClientType getClientType() {
		return clientType;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}

	public List<ClientAddress> getClientAddresses() {
		return clientAddresses;
	}

	public void setClientAddresses(List<ClientAddress> clientAddresses) {
		this.clientAddresses = clientAddresses;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<SaleMode> getSaleModes() {
		return saleModes;
	}

	public void setSaleModes(List<SaleMode> saleModes) {
		this.saleModes = saleModes;
	}

	public List<PriceList> getPriceLists() {
		return priceLists;
	}

	public void setPriceLists(List<PriceList> priceLists) {
		this.priceLists = priceLists;
	}
}