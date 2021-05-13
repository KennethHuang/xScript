# xScript.database

**xScript.database**, the database elements of xScript. You can use xmlns attribute to specify the package of database elements.

	xmlns:d="kenh.xscript.database.elements"
	xmlns:f.d="kenh.xscript.database.functions"

**Now, only hsqldb's driver is available, if the other JDBC driver is required, xScript's module.xml (\xScript\modules\kenh\xscript\main\module.xml) need to update.**  

***
***

## &lt;datasource&gt;

It define a data source. This element is a sub class of &lt;set&gt;, so it can be a sub-element of &lt;script&gt;.

### Attribute

_var_ -- the name of data source.  

### Sub-elements

&lt;param&gt;

### Example
	<d:datasource var="localdb"> 
		<param name="driverClassName" value="org.hsqldb.jdbcDriver"/>
		<param name="url" value="jdbc:hsqldb:hsql://localhost"/>
		<param name="username" value="sa"/>
		<param name="password" value=""/>
	</d:datasource>
***

## &lt;connection&gt;

Establish the connection with data source. This element is a sub class of &lt;set&gt;, so it can be a sub-element of &lt;script&gt;.

### Attribute

_var_ -- the name of connection.  
_source_ -- the name of data source which the connection establish with.  
_auto-commit_ -- sets this connection's auto-commit mode.  

### Example
	<d:connection var="conn" source="localdb"/>

***

## &lt;sql&gt;

It define a SQL. This element is a sub class of &lt;set&gt;, so it can be a sub-element of &lt;script&gt;.

### Attribute

_var_ -- the name of SQL.  

### Sub-elements

&lt;param&gt;

### Example
	<d:sql var="updateTable"><![CDATA[ update TABLE1 set STR_COL = ?, NUM_COL = ?  ]]> 
		<param name="STR" value="KENH"/>
		<param name="NUM" value="123"/>
	</d:sql>

***

## &lt;execute&gt;

Execute the sql. If sql is failure to execute, this process method return EXCEPTION, it can be catch by &lt;catch&gt; element.

### Attribute

_sql_ -- the name of sql.  
_var_ -- the variable to store the result.  
_ref_ -- the name of datasource or connection.  

### Sub-elements

&lt;param&gt;

### Variable

_@exception_ -- the exception if sql is failure to execute. This variable can only visit in &lt;catch&gt; 

### Example
	<d:execute var="result" ref="conn">
		<![CDATA[ INSERT INTO TABLE1 (str_col,num_col,bo_col,date_col) VALUES(NULL, NULL, NULL, NULL) ]]>
	</d:execute>
	<d:execute sql="updateTable" ref="conn">
		<param name="STR" value="K"/>
	</d:execute>
	<catch> 
		<d:rollback ref="conn"/> 
	</catch> 


***

## &lt;commit&gt;

Commit the connection. 

### Attribute

_ref_ -- the name of connection.  


***

## &lt;rollback&gt;

Rollback the connection, undoes all changes.

### Attribute

_ref_ -- the name of connection.  
_save-point_ -- the name of save point.  

***

## &lt;savepoint&gt;

Creates a savepoint.

### Attribute

_ref_ -- the name of connection.  
_var_ -- the name of save point.  

***
***

## Function:

execute({connection|datasource},{sql}) -- execute the sql. If sql is failure to execute, function will throw UnsupportedExpressionException, it can't catch by  &lt;catch&gt; element.

getInt({connection|datasource},{sql}) -- Retrieves the integer value of first column of first row. 

isEmpty({connection|datasource},{sql}) -- to check whether result set is empty or not.

### Example
	<set var="count" value="{#d:getInt(localdb,select count(*) from TABLE1)}"/>
	<set var="rs" value="{#d:execute(conn,select * from TABLE1)}"/>

