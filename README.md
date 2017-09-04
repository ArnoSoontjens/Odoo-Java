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
If this connection uses SSL, a check for valid certificate and hostname will be run when calling methods on the odooConnector. If you are not using a valid or are using a self-signed certificate, use ```false``` as a second parameter to the constructor to skip that check, like this (and can be done in the same way for all constructors):
```javascript   
IOdooConnector odooConnector = new OdooConnector("URL_to_connect_to", false);
```
### getVersion
Get information about the Odoo server you are connecting to. Getting version information means the connection to the server is ok.
This does NOT mean that you have access to the database.

### authenticate
Returns a user id that will be used in future calls to Odoo. Getting a user id means you have access to the Odoo database.

### search
Supply this method with the name of the model you are searching in, and a (optional) query. 
#### Queries
Queries can be constructed using the ```QueryBuilder```, in the following way (constructs a query that searches field 'id' for a value equal to ```value```:
```javascript
OdooQueryBuilder builder = new OdooQueryBuilder;
OdooQuery query = builder.searchField("id").forValueEqualTo(value).build();
```
This ```OdooQuery``` can than be passed to the ```search``` or ```searchAndRead``` functions. Note: This is true for queries that search only one field. Queries that define more parameters (like the one in the example for the ```search``` function) should be done by constructing an ```Object```.
Using the search function will return all of the ids in the model if used with an empty query, and all of the ids that match your query in the case a query is passed to the function.
Returns all id's in the "sale.order" model:
```javascript
Object[] query = {}; //OR: builder.buildEmptyQuery();
int[] ids = odooConnector.search("sale.order",query);
```
Returns only the id's of the records that match the provided user id and where the field "active" is set to true:
```javascript
Object[] query = {
    asList("id", "=", userId),
    asList("active", "=", true)
};
int[] ids = odooConnector.search("sale.order",query);
```
### read
Supply  this method with the name of the model you are trying to read from, the ids of the records you want to read and the 
fields you would like to read. The function will return an object with the searched fields of the requested records.
Returns all fields for the records with the requested id's:
```javascript
int[] ids = {1,2,3,4,5};
Object[] result = odooConnector.read("sale.order",ids);
```
Returns only the requested fields(id, name and project ID) for the records with requested id's:
```javascript
int[] ids = {1,2,3,4,5};
Object[] requestedFields = {
      "id",
      "name",
      "project_id"
};
Object[] result = odooConnector.read("sale.order",ids, requestedFields);
```
### searchAndRead
Performs a search and read at the same time. Supply this function with the model you would like to read from, the requested fields and a (optional) filter/query.
This method combines the search and read functions that were used earlier: 
```javascript
Object[] requestedFields = {
    "id"
    "name",
    "project_id"
};

Object[] filter = {
    asList("id", "=", userId),
    asList("active", "=", true)
};

Object[] result = odooConnector.searchAndRead("sale.order", filter, requestedFields);
```
### createRecord
Create new records in a specified model. Supply the model where the new records should be created and a HashMap with the data
that should be written. This should be a key-value pair, with the name of the field as key and the data as value. Returns the
id of the record that was created.
