package movistar.bean;

import java.io.InputStream;

public class Configuration extends Entity {
    private String value;
    private String description;
    private Company company;
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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
