package movistar.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import movistar.bean.Category;
import movistar.bean.Client;
import movistar.bean.ClientAddress;
import movistar.bean.ClientType;
import movistar.bean.Company;
import movistar.bean.Currency;
import movistar.bean.Itinerary;
import movistar.bean.NoSale;
import movistar.bean.PriceList;
import movistar.bean.Product;
import movistar.bean.ProductPrice;
import movistar.bean.ProductUnity;
import movistar.bean.SaleMode;
import movistar.bean.SaleType;
import movistar.bean.User;
import movistar.bean.UserItinerary;
import movistar.bean.Warehouse;
import movistar.dbacces.ClientAccess;
import movistar.dbacces.CompanyAccess;
import movistar.dbacces.ItineraryAccess;
import movistar.dbacces.ProductAccess;
import movistar.dbacces.SaleAccess;
import movistar.dbacces.UserAccess;

public class FileInterface {
    
    public static int processFile(Company company,File file){
	try {
	    String name = file.getName().toLowerCase();
	    String extension = name.lastIndexOf('.')!=-1?name.substring(name.lastIndexOf('.')):"";
	    String singleName = name.substring(0,name.lastIndexOf('.'));
	    
	    if(".zip".equals(extension)){
		decompress(file);
	    }else{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		String datetime = sdf.format(new Date());
		if(name.indexOf(FileNames.CATEGORY)!=-1){
		    List<Category> categories = new ArrayList<Category>();
		    while((line = br.readLine() )!=null){
        		    String data [] = line.split("\\|");
        		    Category category = new Category();
        		    category.setCode(data[0]);
        		    category.setName(data[1]);
        		    categories.add(category);
		    }
		    ProductAccess.setCategories("MP0001",categories);
		}else if(name.indexOf(FileNames.CURRENCY)!=-1){
		    List<Currency> currencies= new ArrayList<Currency>();
		    while((line = br.readLine() )!=null){
        		    String data [] = line.split("\\|");
        		    Currency currency = new Currency();
        		    currency.setCode(data[0]);
        		    currency.setName(data[1]);
        		    currency.setSymbol(data[2]);
        		    currency.setLocal(data[3].equals("1"));
        		    currency.setExchange(Double.parseDouble(data[4]));
        		    currencies.add(currency);
		    }
		    ProductAccess.setCurrencies("MP0001",currencies);
		}else if(name.indexOf(FileNames.NO_SALE)!=-1){
		    List<NoSale> noSales= new ArrayList<NoSale>();
		    while((line = br.readLine() )!=null){
        		    String data [] = line.split("\\|");
        		    NoSale noSale = new NoSale();
        		    noSale.setCode(data[0]);
        		    noSale.setName(data[1]);
        		    noSales.add(noSale);
		    }
		    SaleAccess.setNoSales("MP0001",noSales);
		}else if(name.indexOf(FileNames.CLIENT_TYPE)!=-1){
		    List<ClientType> clientTypes= new ArrayList<ClientType>();
		    while((line = br.readLine() )!=null){
        		    String data [] = line.split("\\|");
        		    ClientType clientType = new ClientType();
        		    clientType.setCode(data[0]);
        		    clientType.setName(data[1]);
        		    clientTypes.add(clientType);
		    }
		    ClientAccess.setClientTypes("MP0001",clientTypes);
		}else if(name.indexOf(FileNames.SALE_TYPE)!=-1){
		    List<SaleType> saleTypes= new ArrayList<SaleType>();
		    while((line = br.readLine() )!=null){
        		    String data [] = line.split("\\|");
        		    SaleType saleType = new SaleType();
        		    saleType.setCode(data[0]);
        		    saleType.setName(data[1]);
        		    saleTypes.add(saleType);
		    }
		    SaleAccess.setSaleTypes("MP0001",saleTypes);
		}else if(name.indexOf(FileNames.SALE_MODE)!=-1){
		    List<SaleMode> saleModes= new ArrayList<SaleMode>();
		    while((line = br.readLine() )!=null){
        		    String data [] = line.split("\\|");
        		    SaleMode saleMode = new SaleMode();
        		    saleMode.setCode(data[0]);
        		    saleMode.setName(data[1]);
        		    saleMode.setType(data[3]);
        		    saleModes.add(saleMode);
		    }
		    SaleAccess.setSaleModes("MP0001",saleModes);
		}else if(name.indexOf(FileNames.PRICE_LIST)!=-1){
		    List<PriceList> priceLists= new ArrayList<PriceList>();
		    while((line = br.readLine() )!=null){
        		    String data [] = line.split("\\|");
        		    PriceList priceList = new PriceList();
        		    priceList.setCode(data[0]);
        		    priceList.setName(data[1]);
        		    priceLists.add(priceList);
		    }
		    ProductAccess.setPriceLists("MP0001",priceLists);
		}else if(name.indexOf(FileNames.WAREHOUSE)!=-1){
		    List<Warehouse> warehouses= new ArrayList<Warehouse>();
		    while((line = br.readLine() )!=null){
        		    String data [] = line.split("\\|");
        		    Warehouse warehouse = new Warehouse();
        		    warehouse.setCode(data[0]);
        		    warehouse.setName(data[1]);
        		    warehouses.add(warehouse);
		    }
		    CompanyAccess.setWarehouses("MP0001",warehouses);
		}else if(name.indexOf(FileNames.PRODUCT)!=-1){
		    List<Product> products = new ArrayList<Product>();
		    
		    while((line = br.readLine() )!=null){
        		    String data [] = line.split("\\|");
        		    int cont = 0;
        		    Product product = new Product();
        		    product.setCode(data[cont++]);
        		    product.setName(data[cont++]);
        		    product.setTradeMark(data[cont++]);
        		    product.setCategory(new Category());
        		    product.getCategory().setCode(data[cont++]);
        		    int unities = Integer.parseInt(data[cont++]);
        		    if(unities > 0){
        			
        			product.setProductUnities(new ArrayList<ProductUnity>());
        		    for(int i=0;i<unities;i++){
        			ProductUnity productUnity = new ProductUnity();
        			productUnity.setWarehouse(new Warehouse());
        			productUnity.getWarehouse().setCode(data[cont++]);
        			productUnity.setCode(data[cont++]);
        			productUnity.setName(data[cont++]);
        			productUnity.setStock(Double.parseDouble(data[cont++]));
        			int prices = Integer.parseInt(data[cont++]);
        			if(prices>0){
        			    productUnity.setProductPrices(new ArrayList<ProductPrice>());
        			    for(int j=0;j<prices;j++){
        				ProductPrice productPrice = new ProductPrice();
        				productPrice.setPriceList(new PriceList());
        				productPrice.getPriceList().setCode(data[cont++]);
        				productPrice.setCurrency(new Currency());
        				productPrice.getCurrency().setCode(data[cont++]);
        				productPrice.setPrice(Double.parseDouble(data[cont++]));
        				productUnity.getProductPrices().add(productPrice);
        			    }
        			}
        			product.getProductUnities().add(productUnity);
        		    }
        		    }
        		    products.add(product);
		    }
		   ProductAccess.setProducts(company,products);
		}else if(name.indexOf(FileNames.CLIENT)!=-1){
		    List<Client> clients = new ArrayList<Client>();
		    while((line = br.readLine() )!=null){
    		    	String data [] = line.split("\\|");
    		    	Client client = new Client();
    		    	client.setCode(data[0]);
    		    	client.setName(data[1]);
    		    	client.setRuc(data[2]);
    		    	client.setPhone(data[3]);
    		    	client.setClientType(new ClientType());
    		    	client.getClientType().setCode(data[4]);
    		    	int addresses = Integer.parseInt(data[5]);
    		    	if(addresses>0){
    		    	    client.setClientAddresses(new ArrayList<ClientAddress>());
    		    	    int c = 5;
    		    	    for(int i=0;i<addresses;i++){
    		    		ClientAddress clientAddress = new ClientAddress();
    		    		clientAddress.setCode(data[c++]);
    		    		clientAddress.setName(data[c++]);
    		    		clientAddress.setType(data[c++]);
    		    		clientAddress.setLatitude(data[c++]);
    		    		clientAddress.setLongitude(data[c++]);
    		    		client.getClientAddresses().add(clientAddress);
    		    	    }
    		    	}
    		    	clients.add(client);
		    }
		    ClientAccess.setClients(company,clients);
		}else if(name.indexOf(FileNames.ITINERARY)!=-1){
		    List<UserItinerary> userItineraries = new ArrayList<UserItinerary>();
		    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		    while((line = br.readLine() )!=null){
			String data [] = line.split("\\|");
			int c = 0;
			UserItinerary userItinerary = new UserItinerary();
			userItinerary.setUser(new User());
			userItinerary.setCode(data[c++]);
			userItinerary.getUser().setCode(data[c++]);
			userItinerary.setAssignedDate(data[c++]);
			int cont = Integer.parseInt(data[c++]);
			if(cont>0){
			    userItinerary.setItineraries(new ArrayList<Itinerary>());
			    for(int i=0;i<cont;i++){
				Itinerary itinerary = new Itinerary();
				itinerary.setClientAddress(new ClientAddress());
				itinerary.getClientAddress().setClient(new Client());
				itinerary.getClientAddress().getClient().setCode(data[c++]);
				itinerary.getClientAddress().setCode(data[c++]);
				itinerary.setDay(Short.parseShort(data[c++]));
				itinerary.setFrequency(data[c++]);
				itinerary.setDate(sdf1.parse(data[c++]));
				userItinerary.getItineraries().add(itinerary);
			    }
			}
			userItineraries.add(userItinerary);
		    }
		    ItineraryAccess.setItineraries(company,userItineraries);
		}else if(name.indexOf(FileNames.USER)!=-1){
		    List<User> users = new ArrayList<User>();
		    while((line = br.readLine() )!=null){
			String data [] = line.split("\\|");
			User user = new User();
			user.setCode(data[0]);
			user.setWarehouse(new Warehouse());
			user.getWarehouse().setCode(data[1]);
			user.setName(data[2]);
			user.setPhoneNumber(data[3]);
			user.setPassword(data[4]);
			users.add(user);
		    }
		    UserAccess.setUsers(company, users);
		}

		br.close();
		System.out.println(file.renameTo(new File(file.getParent()+File.separatorChar+"PROCESSED_"+singleName+"_"+datetime+extension)));;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
	return 0;
    }
    private static void decompress(File file) throws Exception{
	FileInputStream fis = new FileInputStream(file);
	ZipInputStream zis = new ZipInputStream(fis);
	ZipEntry fileZipped = null;
	while((fileZipped = zis.getNextEntry())!=null){
	    if(fileZipped.isDirectory()){
		File f = new File(file.getParent()+File.separatorChar+fileZipped.getName());
		f.mkdirs();
		continue;
	    }
	    FileOutputStream fos = new FileOutputStream(file.getParent()+File.separatorChar+fileZipped.getName());
	    int read = 0;
	    byte[] buffer = new byte[1024];
	    while((read = zis.read(buffer, 0, 1024))>0) {
		fos.write(buffer,0,read);
	    }
	    fos.close();
	    zis.closeEntry();
	}
	zis.close();
	fis.close();
	
    }
    private static void processFormatFromFile(){
	
    }
    private static class FileNames{
	private static final String SALE_MODE = "salemode";
	private static final String SALE_TYPE = "saletype";
	private static final String CLIENT_TYPE = "clienttype";
	private static final String CATEGORY = "category";
	private static final String NO_SALE = "nosale";
	private static final String CURRENCY = "currency";
	private static final String PRICE_LIST = "pricelist";
	private static final String WAREHOUSE = "warehouse";
	private static final String USER = "user";
	private static final String PRODUCT = "product";
	private static final String CLIENT = "client";
	private static final String ITINERARY = "itinerary";

    }
}
