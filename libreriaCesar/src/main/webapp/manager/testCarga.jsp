<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="mx.com.libreria.factory.ObjectFactory" %>
<%@ page import="mx.com.libreria.manager.ProductMGR" %>
<%@ page import="mx.com.libreria.model.Producto" %>
<%@ page import="java.util.List" %>

<jsp:useBean id = "infoProducto"
			     scope = "page"
			     class = "mx.com.libreria.model.Producto" />

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		
		<script src="dhtmlxCombo/codebase/dhtmlxcommon.js"></script>
		<script src="dhtmlxCombo/codebase/dhtmlxcombo.js"></script>
		<script>
      		window.dhx_globalImgPath="dhtmlxCombo/codebase/imgs/";
    	</script>
		
		<link rel="STYLESHEET" type="text/css" href="dhtmlxCombo/codebase/dhtmlxcombo.css">	
		
		<script language=JavaScript>
			function DisplayEvent(eventName) {
   				var myMessage = window.document.form1.textarea2.value;
   				myMessage = myMessage + eventName;
   				window.document.form1.textarea2.value = myMessage;
			}
		</script>
		
		<title>Some samples</title>
	</head>
	
	<body>
		
		 <select style='width:200px;'  id="combo_zone1" name="alfa1">
        	<option value="1">Uno</option>
        	<option value="2">Dos</option>
			<option value="3">Tres</option>
			<option value="4">Cuatro</option>
			<option value="5">Cinco</option>
			<option value="6">Seis</option>
			<option value="7">Siete</option>
			<option value="8">Ocho</option>
			<option value="9">Nueve</option>
			<option value="10">Diez</option>
    	</select>
	
	
		 <script>
    	  	var z=dhtmlXComboFromSelect("combo_zone1");			
    	 </script>
	
	
		
		  <div id="combo_zone2" style="width:200px; height:30px;"></div>
		  	
		   
		  <%  
		  
		  ProductMGR productMGR = (ProductMGR) ObjectFactory.getBean("productMGR");
		  
		  List<Producto> listaProductos = productMGR.getAllNameArticulos();
		  		  
		  %>
		  <script>
      	  	var combo2 = new dhtmlXCombo("combo_zone2","alfa2",200);
			combo2.enableFilteringMode(true);
			<% 			
				for (int i=0; i < listaProductos.size(); i++) {
					Producto elemento  = (Producto) listaProductos.get(i);
					out.println("combo2.addOption(" + elemento.getProductoId() + ", '" + elemento.getNombreProducto() + "');");	
				}				 			
			%>			
			//combo2.attachEvent("onKeyPressed", function(keyCode){ alert("A la vista");});  							
    	  </script>
	
		 	
	
	<FORM NAME=form1>
   		<TEXTAREA ROWS=15 COLS=40 NAME=textarea1 
			onchange="DisplayEvent('onchange\n');" 
      		onkeydown="DisplayEvent('onkeydown\n');" 
      		onkeypress="DisplayEvent('onkeypress\n');" 
      		onkeyup="DisplayEvent('onkeyup\n\n');">
   		</TEXTAREA>
   		
		<TEXTAREA ROWS=15 COLS=40 NAME=textarea2>
   		</TEXTAREA>
   		<BR><BR>
   		<INPUT TYPE="button" VALUE="Limpiar la Ventana de Eventos" NAME=button1 onclick="window.document.form1.textarea2.value=''">
		 
	</FORM>



<!-- desde aqui empieza el codigo -->
<h3>From select box</h3>
<form target="bframe" action="php/dumpPost.php" method="POST" onsubmit="confirmComboValues()">
            <select style='width:200px;'  id="combo_zone1" name="alfa1">
                <option value="1">a00</option>
                <option value="2">a01</option>
                <option value="3">a02</option>
                <option value="4">a10</option>
                <option value="5">a11</option>
                <option value="6">a12</option>
                <option value="7">b00</option>
                <option value="8">b01</option>
                <option value="9">b02</option>
                <option value="10">b10</option>
                <option value="11">b11</option>
                <option value="12">b12</option>
                <option value="13">a22</option>
                <option value="14">a31</option>
                <option value="15">a04</option>
            </select>


<h3>From XML</h3>
	<div id="combo_zone2" style="width:200px; height:30px;"></div>

<h3>From Javasript</h3>
    <div id="combo_zone3" style="width:200px; height:30px;"></div>
	
<input type="submit" />

</form>

<script>

var combos = [];
combos[0] = dhtmlXComboFromSelect("combo_zone1");
combos[1] = new dhtmlXCombo("combo_zone2", "alfa2", 200);
combos[1].loadXML("../common/data.xml");
combos[2] = new dhtmlXCombo("combo_zone3", "alfa3", 200);
combos[2].addOption([[1, 1111], [2, 2222], [3, 3333], [4, 4444], [5, 5555]]);
/*we recommend to use confirmValue on form submit*/
;
function confirmComboValues() {
    for (var i = 0; i < combos.length; i++);
    combos[i].confirmValue();
}

</script>

<iframe name="bframe" id="bframe" style="width:1005; height:200px;"></iframe>

</html>