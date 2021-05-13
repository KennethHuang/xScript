# xScript.os

**xScript.os**, the os elements of xScript. You can use xmlns attribute to specify the package of os elements.

	xmlns:o="kenh.xscript.os.elements"
	xmlns:dia="kenh.xscript.os.elements.dialog"
	xmlns:f.o="kenh.xscript.os.functions"

***
***

## &lt;cd&gt;

Change the current work folder for xScript, it will update [@home](https://github.com/KennethHuang/xScript#variable).

### Attribute

_path_ -- the path, it will change the current work folder.  
_var_ -- the name hold current work folder, it equals @home.  
_parent_, _child_ -- parent and child should be appeared together, it just work like path.  

### Example
    <o:cd path="C:\test" var="user_hone"/>
    <o:cd parent="C:\test" child="sub"/> <!--C:\test\sub-->

***

## &lt;copy&gt;

Copy the file or folder.

### Attribute

_path_ -- the path of file or folder. If the path is absolute, it will use current work folder as parent folder.  
_dest_ -- the destination.  
_cut_ -- true/false, cut or nor.  

### Example
    <o:copy path="abc.txt" dest="ONE\abc.txt"/> <!--copy file-->
    <o:copy path="ONE" dest="FIVE" cut="true"/> <!--copy folder-->

***

## &lt;dir&gt;  &lt;ls&gt;

List the files or folders in the folder.

### Attribute

_path_ -- the path of file or folder. If the path is absolute, it will use current work folder as parent folder.  
_var_ -- the ref name store the result, the result is an array.  
_dir-only_ -- true/false, list the folders only.  
_file-only_ -- true/false, list the files only.  
_type_ -- the files with specify types, for example, 'txt', 'xls,xlsx'.  

### Example
    <o:ls path="." var="folder1s"/> 
    <o:ls path="." var="folder2s" file-only="true"/> 
    <o:ls path="TWO" var="folder3s" type="txt,xls"/> 

***

## &lt;md&gt;  &lt;mkdir&gt;

Creates the directory.

### Attribute

_path_ -- the path of file or folder. If the path is absolute, it will use current work folder as parent folder.  
_parent_, _child_ -- parent and child should be appeared together, it just work like path.  

### Example
    <o:md parent="C:\test" child="ONE" /> 
    <o:md path=".\TWO" />" + 

***

## &lt;ren&gt;  &lt;rename&gt;

Renames the file or folder.

### Attribute

_path_ -- the path of file or folder. If the path is absolute, it will use current work folder as parent folder.  
_parent_, _child_ -- parent and child should be appeared together, it just work like path.  
_dest_ -- the destination.  

### Example
    <o:ren path="FIVE" dest="SIX" />
    <o:ren parent="TWO" child="abc.txt" dest="xyz.txt" />

***

## &lt;file&gt;

Initial a file bean, it's the reference of _kenh.xscript.os.beans.File_.
This is an element of "kenh.xscript.os.elements", so it uses _xmlns:o="kenh.xscript.os.elements"_.

### Attribute

_path_ -- the path of file or folder. If the path is absolute, it will use current work folder as parent folder.  
_parent_, _child_ -- parent and child should be appeared together, it just work like path.  
_var_ -- the name store the reference.  

### Example
    <o:file path="C:\test" var="file1" /> 
    <o:file parent="." child="ONE"  var="file2" /> 

***

## &lt;confirm&gt;

Show the confirm dialog. 

### Attribute

_message_ -- the message.  
_title_ -- the title.  

### Sub-elements

This element works like &lt;ifthen&gt;.

### Example
    <for from="1" to="10">
        <dia:confirm message="Print {@index}?" title="xScript">
            <println>{@index}</println>
        </dia:confirm>
    </for>

***

## &lt;file&gt;

Show the file chooser dialog. 
This is an element of "kenh.xscript.os.elements.dialog", so it uses _xmlns:dia="kenh.xscript.os.elements.dialog"_.

### Attribute

_path_ -- the path.  
_var_ -- the reference name store the result.  
_type_ -- specify the type of files.  
_title_ -- the title.  
_multi-select_ -- sets the file chooser to allow multiple file selections. If true, the result is an array of _kenh.xscript.os.beans.File_, if false, the result is a reference of _kenh.xscript.os.beans.File_.

### Example
    <dia:file var="multi_files" multi-select="true" type="xls,xlsx,cvs"/>
    <dia:file var="single_file" title="Text File" type="txt"/>

***

## &lt;folder&gt;

Show the folder chooser dialog. 

### Attribute

_path_ -- the path.  
_var_ -- the reference name store the result.  
_title_ -- the title.  
_multi-select_ -- sets the file chooser to allow multiple file selections. If true, the result is an array of _kenh.xscript.os.beans.File_, if false, the result is a reference of _kenh.xscript.os.beans.File_.

### Example
    <dia:folder var="multi_dirs" multi-select="true"/>
    <dia:folder var="single_dir" title="Open..." />

***

## &lt;input&gt;

Shows a dialog requesting input.

### Attribute

_message_ -- the message.  
_var_ -- the reference name store the result.  
_title_ -- the title.  
_value_ -- the value used to initialize the input field  
_values_ -- an array that gives the possible selections  

### Example
    <dia:input title="xScript" var="one" message="Option 1:" />
    <dia:input title="xScript" var="two" message="Option 2:" />
    <dia:input title="xScript" var="three" message="Option 3:" />
    <dia:input title="xScript" values="{['{one}','{two}','{three}']}" value="{one}" var="result">Choose one of them: [{one}, {two}, {three}]</d:input>

***

## &lt;message&gt;

Shows a message dialog.

### Attribute

_message_ -- the message.  
_title_ -- the title.  

### Example
    <dia:message>Show the message.</d:message>

***
***

## Function:

getFile({folder},...)  
getFile({the ref of kenh.xscript.os.beans.File}, {folder},...)  
 -- get the reference of _kenh.xscript.os.beans.File_.

***
***

## kenh.xscript.os.beans.File

the method of _kenh.xscript.os.beans.File_:  
getParent()|_kenh.xscript.os.beans.File_ -- get the parent folder.  
getName()|_String_ -- get the name of file.  
getPath()|_String_ -- get the full path.  
isFile()|_boolean_ -- check whether the bean is a file.  
isDirectory()|_boolean_ -- check whether the bean is a folder.  
exists()|_boolean_ -- check whether file is exists.  
list()|_String[]_ -- list the children.  
list(boolean)|_String[]_ -- list the folder or file.  
listFiles(String)|_String[]_ -- list the files with specify type.  

