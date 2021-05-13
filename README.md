# xScript

**xScript**, xml script, it defines the basic logical xml tag, like &lt;if&gt;, &lt;for&gt;, &lt;while&gt;, &lt;method&gt; ect. 

**I think xScript can link one application with the other. For me, I often need to operate the Excel doc and Oracle, so I use it to read the data from Excel or Oracle or both of them, and finally output the result. Futhermore, it's easy to [define your own xml tag](https://github.com/KennethHuang/xScript/wiki/Define-your-own-element) or [ExpL function](https://github.com/KennethHuang/ExpL/wiki#3-define-a-function).**  
* xScript use [ExpL](https://github.com/KennethHuang/ExpL) to parse expressions.
* [xScript.database](https://github.com/KennethHuang/xScript.database) is released. 
* [xScript.os](https://github.com/KennethHuang/xScript.os) is released.
* [xScript.io] in progress.

**To avoid class verion conflict, I use JBoss Modules. All jar files that xScript reply on, is in _modules_ folder.**   

Set XSCRIPT_HOME where xScript be located at, then use xScript.bat in _bin_ folder, it can start xScript through JBoss Modules.  

	xScript -f:{xScript file}
	xScript -file:{xScript file}


***


Example:

	<?xml version="1.0"?>
	<script> 
		<method name="main">  
			<call name="listArray" ref="{[1,2,3,5,7,11]}"/> 
		</method>  
		<method name="listArray" param="ref">
			<for ref="{ref}">
				<println value="{___ref___}"/>
			</for>
		</method>	
	</script>


The output:

	1
	2
	3
	5
	7
	11



***
***

## &lt;script&gt;

Every xScript doc should start with &lt;script&gt; tag. 

### Attribute

_name_ -- the name of this script, this name will store to variable _\_\_\_xScript\_\_\__, you can use {\_\_\_xScript\_\_\_} to get the value of this attribute.  
_main-method_ -- xScript will find the method with this name to invoke. The default vaue is _main_, so if this tag do not have this attribute, the method with the name _'main'_ will be invoked.

### Sub-elements

&lt;set&gt;, &lt;method&gt;, &lt;include&gt;

### Variable

_\_\_\_xScript\_\_\__ -- the name of script.  
_\_\_\_home\_\_\__ -- if you use xml file, the value is the path of the folder that the file place, otherwise, the value is use's directory.

### Example
	<?xml version="1.0"?>
	<script name="example" main-method="main"> 
		<method name="main">  
			<println>Name: {___xScript___}</println>
			<println>Home: {___home___}</println>
		</method>  
	</script>

***

## _xmlns_ attribute 

You can use namespace attribute to specify your own xScript or ExpL extension dynamically.   
_xmlns:&lt;namespace&gt;="&lt;xScript extension package&gt;"_  
_xmlns:f.&lt;namespace&gt;="&lt;ExpL extension package&gt;"_  
_xmlns:func.&lt;namespace&gt;="&lt;ExpL extension package&gt;"_  
_xmlns:function.&lt;namespace&gt;="&lt;ExpL extension package&gt;"_  

### Example

	<?xml version="1.0"?>
	<script name="example" xmlns:yours="your.xscript.package" xmlns:f.yours="your.expl.package"> 
		<method name="main">  
			<yours:yourelement attribute="{#yours:yourfunction()}"/>
		</method>  
	</script>

***

## &lt;set&gt;

Initial a variable with specify value.

### Attribute

_var_ -- the name of variable.  
_value_ -- the value.  
_default_ -- the default value when attribute _value_ is empty.

### Sub-elements

&lt;condition&gt;

### Modifier

&lt;set&gt; support two modifiers: _public_, _final_.   

_public_ -- means this variable is publics, it can be used anywhere. The &lt;set&gt; under &lt;script&gt; is publics automatically.  
_final_ -- means this variable can't be changed anymore.

### Example

	<set var="result" value="1">
		<condition cond="{result}==0" value="2"/>
		<condition cond="{result}==1" value="3"/>
		<condition cond="{result}==2" value="4"/>
	</set>
	<set var="public final my_var" value="1" />

***

## &lt;condition&gt;

Sub-element of _Set_, the condition check for &lt;set&gt;.

### Attribute

_cond_ -- the condition.  
_value_ -- the value.

***

## &lt;method&gt;

A method tag.

### Attribute

_name_ -- the name of method.  
_param_ -- the parameters.

### Sub-elements

All except for &lt;method&gt;, &lt;script&gt;, &lt;include&gt;

### Modifier

The attribute _param_ suport 3 modifiers: _final_, _required_, _default_.

_final_ -- means this parameter can't be changed anymore.  
_required_ -- means this parameter is required, when this method is called.  
_default_ -- means default value when this parameter is blank.

### Example

	<method name="method1" param="required var1, final required var2, final var3, default({'abc'}) var4">
		...
	</method>

***

## &lt;call&gt;

invoke the method.

### Attribute

_name_ -- the name of method.  
_var_ -- the variable store the value of method's return value.  
other attribute, the parameter of method.

### Example

	<method name="method1" param="required var1, final required var2, final var3, default({'abc'}) var4">
		<return value="1" />
	</method>
	<method name="main">
		<call name="method1" var="returnValue" var1="1" var2="2" var4="a"/>
	</method>

***

## &lt;return&gt;

Return the value of method. This element will skip the elements after it.

### Attribute

_value_ -- the return value.


***

## &lt;if&gt;

Condition element. 

### Attribute

_cond_ -- the condition of &lt;if&gt;. If the result is true, use &lt;then&gt; else, use &lt;else&gt;.

### Sub-elements

&lt;then&gt;, &lt;else&gt;

### Example

	<if cond="{a}==0">
		<then>
			<set var="b" value="1"/>
		</then>
		<else>
			<set var="b" value="2"/>
		</else>
	</if>

***

## &lt;then&gt;

Sub element of &lt;if&gt;. 

### Sub-elements

All except for &lt;method&gt;, &lt;script&gt;, &lt;include&gt;

***

## &lt;else&gt;

Sub element of &lt;if&gt;. 

### Sub-elements

All except for &lt;method&gt;, &lt;script&gt;, &lt;include&gt;

***

## &lt;ifthen&gt;

The simple style of &lt;if&gt;-&lt;then&gt;. 

### Attribute

_cond_ -- the condition.

### Sub-elements

All except for &lt;method&gt;, &lt;script&gt;, &lt;include&gt;

### Example

	<ifthen cond="{a}==0">
		<set var="a" value="1"/>
	</ifthen>

***

## &lt;while&gt;

Loops, just like _while_ keyword in Java. 

### Attribute

_cond_ -- the condition.  
_after_ -- if this attribute is true, it works like do...while.

### Sub-elements

All except for &lt;method&gt;, &lt;script&gt;, &lt;include&gt;

### Variable

_\_\_\_index\_\_\__ -- the loops time.  

### Example

		<set var="i" value="0"/>
		<while cond="{i} &lt; 10">
			<set var="i" value="{{i} + 1}"/>
			<println>{___index___}</println>
		</while>


***

## &lt;for&gt;

Loops. 

### Attribute

_from_, _to_ -- use number to loop.  
_step_ -- each step. 

_ref_, _sub-ref_ -- use reference/array to loop.   
_skip_ -- skip previous ref. When you want to skip first reference, just let _skip="1"_.


### Sub-elements

All except for &lt;method&gt;, &lt;script&gt;, &lt;include&gt;

### Variable

_\_\_\_index\_\_\__ -- when use number to loop, the value is the current number; when use reference to loop, the value is the loop time.   
_\_\_\_ref\_\_\__ -- the default variable name of sub reference when _sub-ref_ is blank.

### Example

	<!--use reference to loop-->
	<set var="ref" value="{[1,2,3,5,7,11]}"/>
	<for ref="{ref}">
		<println value="{___ref___}"/> <!--do not specify attribute sub-ref, so use ___ref___.-->
	</for>
	
	<!--use number to loop-->
	<for from="0" to="10" step="2">
		<println value="{___index___}"/>
	</for>
	<for from="0" to="10">
		<println value="{___index___}"/>
	</for>

***

## &lt;continue&gt;

Just like keyword continue in Java, skip the current loop and start the next. 

***

## &lt;break&gt;

Just like keyword break in Java, jump out of the loops. 

***

## &lt;accum&gt;

Accumulator. 

### Attribute

_var_ -- the name of variable.  
_step_ -- each step.

### Example

	<set var="a1" value="3"/>
	<set var="a2" value="3"/>
	<accum var="a1"/> <!--a1 == 4-->
	<accum var="a2" step="2"/>  <!--a2 == 5-->
	<accum var="public a3"/>  <!--if a3 do not define, a3 == 1, and a3 is a public variable-->

***

## &lt;clear&gt;

Remove the variable. 

### Attribute

_var_ -- the name of variable.

***

## &lt;include&gt;

Include another xScript doc. 

### Attribute

_file_ -- the path of file.  
_url_ -- the url.  
_load-methods_ -- load method in the doc.  
_methods_ -- list the methos need to load.  
_load-publics_ -- load variable under &lt;script&gt; in the doc.  


***

## &lt;debug&gt;

Show debug dialog, list all variables and allow you to test ExpL expression.

### Attribute 

_cond_ -- the condition.   

***

## &lt;print&gt;, &lt;println&gt;

System standard output. &lt;println&gt; is with link break.

### Attribute 

_value_ -- the value you want to output.   
_trim_ -- omit the leading and trailing whitespace.   

### Text

These two elements allow text, text is the context you want to output. Same as attribute _value_.

### Example

	<print value="{1+1}"/>
	<println/>
	<println>{1+1}</println>

***

## &lt;param&gt;

Provide the parameter for parent element. 

### Attribute

_name_ -- the name of this parameter.  
_value_ -- the value of this parameter.  
_default_ -- the default value of this parameter.  
_link_ -- link another parameter. When use link parameter, it means use another parameter's value.  

***

## &lt;catch&gt;

When EXCEPTION is return (process metod return EXCEPTION), the sub-elements of this element will be invoked. 
`kenh.xscript.database.elements.Execute` is the example of EXCEPTION.

### Example

	<!--the example use xScript.database-->
	<d:execute var="result" ref="conn">
		<![CDATA[ INSERT INTO TABLE1 (str_col,num_col,bo_col,date_col) VALUES(NULL, NULL, NULL, NULL, NULL) ]]>
	</d:execute>
	<catch> 
		<d:rollback ref="conn"/> 
	</catch> 

### Variable

_\_\_\_exception\_\_\__ - The exception.

***
***

## The scope of variable

The variable define by &lt;set&gt; that under &lt;script&gt; is public variable.  
The variable define by &lt;set&gt; use _public_ modifier is public variable.  
Public variable can visit everywhere in doc.

The variable define by &lt;set&gt; in the &lt;method&gt; is non-public variable. The variable will be remove when jump out of method.

When a method have parameter that the name same as public variable, in the method, parameter take effect.

	<script>
		<set var="var1" value="1"/> <!--public variable, define under script-->
		<method name="main">
			<set var="public var2" value="2"/> <!--public variable, use public modifier-->
			<call name="method1" var1="{{var2}+1}"/>
		</method>
		<method name="method1" param="var1">
			<set var="var3" value="1"/>  <!--non-public variable-->
			<println value="{var1}"/> <!-- 3   display parameter var1 here-->
			<println value="{var2}"/> <!-- 2   display public variable var2 here-->
		</method>
	</script>



