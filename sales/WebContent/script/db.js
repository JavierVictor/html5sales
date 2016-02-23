var dbSales = openDatabase("html5FIVE","1.0","HTML5 DATABASE",10485760,firstTime,genericDBError); //10 MBytes
var user = null;
function firstTime(db){
	dbSales.transaction(function (tx){
		tx.executeSql('CREATE TABLE USER(COMPANYCODE,WAREHOUSECODE,CODE,NAME,PHONENUMBER,PASSWORD,LATITUDE,LONGITUDE,LASTLOGIN,STATE)');
		tx.executeSql('CREATE TABLE CLIENT(COMPANYCODE,CODE,NAME,RUC,PHONE,CLIENTTYPECODE)');
		tx.executeSql('CREATE TABLE CLIENTTYPE(COMPANYCODE,CODE,NAME)');
		tx.executeSql('CREATE TABLE CLIENTADDRESS(COMPANYCODE,CLIENTCODE,CODE,NAME,TYPE)');
		tx.executeSql('CREATE TABLE WAREHOUSE(COMPANYCODE,CODE,NAME)');
		tx.executeSql('CREATE TABLE NOSALE(COMPANYCODE,CODE,NAME)');
		tx.executeSql('CREATE TABLE SALETYPE(COMPANYCODE,CODE,NAME)');
		tx.executeSql('CREATE TABLE SALE(CODE,ITINERARYCODE,COMPANYCODE,CLIENTCODE,CLIENTADDRESSCODE,USERCODE,SALETYPECODE,CURRENCYCODE,SALEMODECODE,NOSALECODE,OBS,PRICELISTCODE,DELIVERYDATE,LATITUDE,LONGITUDE,STATE,REGISTEREDDATE)');
		tx.executeSql("CREATE TABLE SALEDETAIL(SALECODE,ITEM,PRODUCTCODE,WAREHOUSECODE,PRODUCTUNITYCODE,QUANTITY,PRICE,EDITEDPRICE,DISCOUNT,SUBTOTAL)");
		tx.executeSql('CREATE TABLE SALEMODE(COMPANYCODE,CODE,NAME,TYPE)');
		tx.executeSql('CREATE TABLE SALEMODECLIENT(COMPANYCODE,SALEMODECODE,CLIENTCODE)');
		tx.executeSql('CREATE TABLE CURRENCY(COMPANYCODE,CODE,NAME,SYMBOL,LOCAL)');
		tx.executeSql('CREATE TABLE CATEGORY(COMPANYCODE,CODE,NAME,PARENT)');
		tx.executeSql('CREATE TABLE PRICELIST(COMPANYCODE,CODE,NAME)');
		tx.executeSql('CREATE TABLE PRICELISTCLIENT(COMPANYCODE,PRICELISTCODE,CLIENTCODE)');
		tx.executeSql('CREATE TABLE PRODUCT(COMPANYCODE,CODE,NAME,TRADEMARK,CATEGORYCODE)');
		tx.executeSql('CREATE TABLE PRODUCTUNITY(COMPANYCODE,PRODUCTCODE,WAREHOUSECODE,CODE,NAME,STOCK)');
		tx.executeSql('CREATE TABLE PRODUCTPRICE(COMPANYCODE,PRICELISTCODE,PRODUCTUNITYCODE,PRODUCTCODE,WAREHOUSECODE,CURRENCYCODE,PRICE)');
		tx.executeSql('CREATE TABLE ITINERARY(COMPANYCODE,CODE,CLIENTCODE,CLIENTADDRESSCODE,DAY,FREQUENCY,DATE,STATE)');
		tx.executeSql('CREATE TABLE PRESALE(COMPANYCODE,ITINERARYCODE,CLIENTCODE,CLIENTADDRESSCODE,PRODUCTCODE,WAREHOUSECODE,PRODUCTUNITYCODE,QUANTITY,PRICE,EDITEDPRICE,DISCOUNT,SUBTOTAL)');
	});
}
function genericDBError(tx,e){
	console.log(tx);
	if(e)
		console.log(e);
}
function errorTransaction(tx,e){
	console.log(tx);
	if(e)
		console.log(e);
}

function cargaUsuario(kickSession,callBack,force){
	force = true & force;
	dbSales.transaction(function(tx){
		tx.executeSql("SELECT * FROM USER WHERE STATE = 'LOG'" + (force?" OR STATE = 'OUT'":""),[],function(tx,results){
			if(results.rows.length>0){
				user = {companyCode: results.rows.item(0).COMPANYCODE,
						warehouseCode: results.rows.item(0).WAREHOUSECODE,
						code: results.rows.item(0).CODE,
						name: results.rows.item(0).NAME,
						phoneNumber: results.rows.item(0).PHONENUMBER,
						password: results.rows.item(0).PASSWORD,
						lastLogin:results.rows.item(0).LASTLOGIN,
						state:results.rows.item(0).STATE};
				if(toStringDate(new Date(user.lastLogin), ONLY_DATE)!=toStringDate(new Date(), ONLY_DATE)){
					tx.executeSql("UPDATE USER SET LASTLOGIN= ?",[Date.now()]);
					tx.executeSql("UPDATE ITINERARY SET STATE = 'ACT'");
				}
				if(callBack)
					callBack();
			}else if(kickSession){
				browse("index.html");
			}else{
				if(callBack)
					callBack();
			}
	});
	},errorTransaction);
}
function deleteUserData(sucessCallback){
	dbSales.transaction(function(tx){
		tx.executeSql("delete from PRICELISTCLIENT");
		tx.executeSql("delete from SALEDETAIL");
		tx.executeSql("delete from SALE");
		tx.executeSql("delete from PRODUCTPRICE");
		tx.executeSql("delete from PRODUCTUNITY");
		tx.executeSql("delete from PRODUCT");
		tx.executeSql("delete from CATEGORY");
		tx.executeSql("delete from SALEMODECLIENT");
		tx.executeSql("delete from SALEMODE");
		tx.executeSql("delete from CLIENTADDRESS");
		tx.executeSql("delete from CLIENT");
		tx.executeSql("delete from CLIENTTYPE");
		tx.executeSql("delete from CURRENCY");
		tx.executeSql("delete from ITINERARY");
		tx.executeSql("delete from NOSALE");
		tx.executeSql("delete from PRESALE");
		tx.executeSql("delete from PRICELIST");
		tx.executeSql("delete from SALETYPE");
		tx.executeSql("delete from CATEGORY");
	},errorTransaction,sucessCallback);
}