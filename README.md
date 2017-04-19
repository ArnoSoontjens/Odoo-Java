# Odoo-Java
This library uses XML-RPC to connect to the Odoo ERP system, using Java. This library is still under construction. 
Depends on the <a href="https://github.com/gturri/aXMLRPC">aXMLRPC</a> library by Tim Roes. 

## Usage
Create a new instance of the OdooConnector (using the interface) and provide a URL.
```javascript   
IOdooConnector odooConnector = new OdooConnector("URL_to_connect_to");
```
This will create a new odooConnector object to connect to the given hostname. 

There also is a possibility to provide a protocol, hostname and port separately to form the URL:
```javascript   
IOdooConnector odooConnector = new OdooConnector("HTTP", "www.somehostname.com", 8080);
```
To connect to the Odoo database, you will need to provide a login, password and databasename. These parameters can be added by making use of the ```dbParams``` object and used as a parameter when calling the constructor:
```javascript
dbParams databaseParams = new dbParams("dbName", "dbLogin", "dbPassword");
IOdooConnector odooConnector = new OdooConnector("URL_to_connect_to", databaseParams);
```
Or can be added later:
```javascript
IOdooConnector odooConnector = new OdooConnector("URL_to_connect_to");
dbParams databaseParams = new dbParams("dbName", "dbLogin", "dbPassword");
odooConnector.setDbParams(databaseParams);
```
Or, alternatively, all parameters can be passed separately to the constructor:
```javascript   
IOdooConnector odooConnector = new OdooConnector("protocol", "hostName", "connectionPort","dbName", "dbLogin", "dbPassword"); 
```
If this connection uses SSL, a check for valid certificate and hostname will be run when calling methods on the odooConnector. If you are not using a valid or self-signed certificate, use false as a second parameter to the constructor to skip that check, like this (and can be done in the same way for all constructors):
```javascript   
IOdooConnector odooConnector = new OdooConnector("URL_to_connect_to", false);
```
### getVersion
Get information about the Odoo server you are connecting to. Getting version information means the connection to the server is ok.
This does NOT mean that you have access to the database.

### authenticate
Returns a user id that will be used in future calls to Odoo. Getting a user id means you have access to the Odoo database.

### search
Supply this method with the name of the model you are searching in, and a (optional) filter. Will return all of the id's in the 
model if used without filter, and all of the ids that match your query if used with a filter.

### read
Supply  this method with the name of the model you are trying to read from, the ids of the records you want to read and the 
fields you would like to read. The function will return an object with the searched fields of the requested records.

### searchAndRead
Performs a search and read at the same time. Supply this function with the model you would like to read from, the requested fields
and a (optional) filter/query.

### createRecord
Create new records in a specified model. Supply the model where the new records should be created and a HashMap with the data
that should be written. This should be a key-value pair, with the name of the field as key and the data as value. Returns the
id of the record that was created.
