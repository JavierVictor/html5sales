package movistar.bean;

import java.io.InputStream;

public class SaleType extends Entity {

	public SaleType(InputStream is, boolean closeStream) throws Exception {
		super(is, closeStream);
		// TODO Auto-generated constructor stub
	}
	public SaleType(){
		super();
	}
	public SaleType(InputStream is) throws Exception {
		super(is);
		// TODO Auto-generated constructor stub
	}
	
	public SaleType(byte[] bytes)throws Exception{
		super(bytes);
	}

	public byte[] toByteArray() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	protected void makeObject(InputStream is) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	

}
