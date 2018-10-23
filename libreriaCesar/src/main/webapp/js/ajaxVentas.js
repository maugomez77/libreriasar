            /* 
             * Function to send information via POST http
             */
            function validateBoxVentas(sessionid, event) {
                
                if (event.keyCode != 13) {
                   return;
                }
                
                target = document.getElementsByName("myinputid");
                if (target[0].value == null || target[0].value.length == 0) {                 
                    //setMessageUsingDOM2("userNumberMessage", "green", "");
                    //setMessageUsingDOM2("userIdMessage", "green", "");                    
                    disableSubmitBtn();                    
                    //here we have to stopped the following, in order to don't do again a call
                    return;
                }
                                
                //var url = "validate?id=" + escape(target.value); 
                var url = "/RostiCesar/scanVentas";
                var postdata = "myinputid=" + escape(target[0].value) + "&" + "sessionid=" + escape(sessionid);

                // Invoke initRequest(url) to create XMLHttpRequest object
                initRequest(url);

                // The "processRequest" function is set as a callback function.
                req.onreadystatechange = processRequest_ventas;
                //req.open("GET", url, true);
                //req.send(null);
                req.open("POST", url, true);
                req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                req.send(postdata);
            }
               
           

            // (4) Callback function that gets invoked asynchronously by the browser
            // when the data has been successfully returned from the server.
            // (Actually this callback function gets called every time the value
            // of "readyState" field of the XMLHttpRequest object gets changed.)
            // This callback function needs to be set to the "onreadystatechange"
            // field of the XMLHttpRequest.
            //
            function processRequest_ventas() {
                                
                if (req.readyState == 4) {
                  
                  if (req.status == 200) {
                    
                    var doc_el = req.responseXML.documentElement;
                    var important = get_element(doc_el, "important", 0);
                    var url = get_element(doc_el, "url", 0);
                    var message = get_element(doc_el, "foundMatch", 0);
                    var messagePrev = get_element(doc_el, "foundPrevious", 0);
                    //alert("responseText\n>" + req.responseText + "<");
                    //alert(url);
                    url = replaceCharacteres(url, '-', '&');                    
                    
                    if (important == 'si') {
                        window.location.href = url;                        
                    } else {
	                    if (messagePrev == 'true') {
	                        alert('Esta caja ya ha sido agregada a una venta anterior o no se encuentra en inventario.');                        
	                        focusText();
	                    } else {
	                        if (message == 'false') {
		                        alert('No se pudo agregar la caja debido a que no hacen match.');
		                        focusText();
		                    } else {
		                        location.reload();
		                    }            
	                    }
	                }
                    
                    //var message = req.responseXML.getElementsByTagName("valid")[0].childNodes[0].nodeValue;                    
                    //setTable("tableContent");
                    // If the user entered value is not valid, do not allow the user to
                    // click submit button.
                    var submitBtn = document.getElementById("submit_btn");
                    if (message == "false") {
                        submitBtn.disabled = true;
                    } else {
                        submitBtn.disabled = false;
                    }
                  }
                }
            }            
              