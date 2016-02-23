package movistar.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

public abstract class Entity implements Serializable{

	private static final long serialVersionUID = -9732L;
	private String code;
	private String name;
	private transient String state;
	
	protected transient boolean closeStream = true;
	public  abstract byte[] toByteArray() throws Exception;
	public Entity(byte[] bytes)throws Exception{
		if( bytes != null && bytes.length != 0){
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			makeObject(bais);
		}
	}
	public Entity(){
		code ="";
		name ="";
		state = "ACT";
	}
	public Entity(InputStream is) throws Exception {
		makeObject(is);
	}
	public Entity(InputStream is, boolean closeStream) throws Exception {
		this.closeStream = closeStream;
		makeObject(is);
	}
	protected abstract void makeObject(InputStream is)throws Exception;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isCloseStream() {
		return closeStream;
	}
	public void setCloseStream(boolean closeStream) {
		this.closeStream = closeStream;
	}
	
	public static Entity findEntity(List<Entity> list, String code) {
		for (Entity entity : list) {
			if (entity.getCode().equals(code)) {
				return entity;
			}
		}
		return null;
	}
}