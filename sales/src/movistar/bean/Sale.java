package movistar.bean;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class Sale extends Entity {

    private static final long serialVersionUID = 1L;
    private String comment;
    private String itinerary;
    private String latitude;
    private String longitude;
    private String mobileDate;
    private double total;
    private User user;
    private SaleMode saleMode;
    private NoSale noSale;
    private ClientAddress clientAddress;
    private List<SaleDetail> saleDetails;
    private SaleType saleType;
    private Currency currency;
    private Date deliveryDate;
    private PriceList priceList;

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public PriceList getPriceList() {
	return priceList;
    }

    public void setPriceList(PriceList priceList) {
	this.priceList = priceList;
    }

    public Sale() {
	super();
    }

    public Sale(byte[] bytes) throws Exception {
	super(bytes);
    }

    public Sale(InputStream is) throws Exception {
	super(is);
    }

    public Sale(InputStream is, boolean closeStream) throws Exception {
	super(is, closeStream);
    }

    public byte[] toByteArray() throws Exception {
	return null;
    }

    protected void makeObject(InputStream is) throws Exception {

    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public SaleMode getSaleMode() {
	return saleMode;
    }

    public void setSaleMode(SaleMode saleMode) {
	this.saleMode = saleMode;
    }

    public NoSale getNoSale() {
	return noSale;
    }

    public void setNoSale(NoSale noSale) {
	this.noSale = noSale;
    }

    public ClientAddress getClientAddress() {
	return clientAddress;
    }

    public void setClientAddress(ClientAddress clientAddress) {
	this.clientAddress = clientAddress;
    }

    public List<SaleDetail> getSaleDetails() {
	return saleDetails;
    }

    public void setSaleDetails(List<SaleDetail> saleDetails) {
	this.saleDetails = saleDetails;
    }

    public SaleType getSaleType() {
	return saleType;
    }

    public void setSaleType(SaleType saleType) {
	this.saleType = saleType;
    }

    public Currency getCurrency() {
	return currency;
    }

    public void setCurrency(Currency currency) {
	this.currency = currency;
    }

    public Date getDeliveryDate() {
	return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
	this.deliveryDate = deliveryDate;
    }

    public String getItinerary() {
	return itinerary;
    }

    public void setItinerary(String itinerary) {
	this.itinerary = itinerary;
    }

    public String getLatitude() {
	return latitude;
    }

    public void setLatitude(String latitude) {
	this.latitude = latitude;
    }

    public String getLongitude() {
	return longitude;
    }

    public void setLongitude(String longitude) {
	this.longitude = longitude;
    }

    public String getMobileDate() {
	return mobileDate;
    }

    public void setMobileDate(String mobileDate) {
	this.mobileDate = mobileDate;
    }

    public double getTotal() {
	return total;
    }

    public void setTotal(double total) {
	this.total = total;
    }

}