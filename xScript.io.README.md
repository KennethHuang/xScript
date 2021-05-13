# xScript.io

**xScript.io**, the io elements of xScript, to support text/excel reading.

	xmlns:i="kenh.xscript.io.elements"

***
***

## &lt;excel&gt;

To create a reference pointing to Excel file(only support .xls).

### Attribute

_file_ -- the path of file.
_var_ -- the name store the reference.
_append_ -- if false, it will cover the file if it exist. Default is false.
_read-only_ -- read only flag. Default is false.

### Example
    <i:excel var="xls" file="ONE.xls"/>

***

## &lt;text&gt;

To create a reference pointing to Text file.

### Attribute

_file_ -- the path of file.
_var_ -- the name store the reference.
_append_ -- if false, it will cover the file if it exist. Default is false.
_read-only_ -- read only flag. Default is false.

### Example
    <i:text var="txt" file="TWO.txt" read-only="true"/>

***

## &lt;read&gt;

To read the context for the reference.

### Attribute

_ref_ -- the reference which read the context from.  
_var_ -- the variable store the context.

for Excel
_sheet_ -- the sheet name. 
_row_ -- the row, start with 0.  
_col_ -- the column, start with 0.

for Text
_line_ -- the line no.

### Example
    <i:read ref="xls" sheet="sheet2" row="2" col="2" var="C1"/>
    <i:read ref="txt" var="B1" line="6"/>

***

## &lt;write&gt;

To write the context.

### Attribute

_ref_ -- the reference which write the context into.  

for Excel
_sheet_ -- the sheet name. 
_row_ -- the row, start with 0.  
_col_ -- the column, start with 0.

for Text
_line_ -- the line no.
_opt_ -- the operation. 
    _insert_, _i_ : insert
    _delete_, _d_ : delete the whole line
    _append_, _a_ : add the context to the end of line, default value
    _update_, _u_ : update the whole line
    _new-line_, _n_ : add the context to the end of line, then add the empty line

### Example
    <i:write ref="xls" sheet="sheet2" row="2" col="2">ABCDEFG</i:write> 
	<i:write ref="txt" opt="n">BB</i:write>
	<i:write ref="txt">CC</i:write>


