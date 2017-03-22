# Odoo-Java
This library uses XML-RPC to connect to the Odoo ERP system, using Java. This library is still under construction. 
Depends on the <a href="https://github.com/gturri/aXMLRPC">aXMLRPC</a> library by Tim Roes. 

## Usage
Create a new instance of the OdooConnector and call methods from there. 
You can directly supply all parameters to the OdooConnector constructor, or create a DataBaseParamers object 
first and pass that to the OdooConnector constructor.

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
that should be written. This should be a key-value pair, with the name of the field as key and the data as value.
