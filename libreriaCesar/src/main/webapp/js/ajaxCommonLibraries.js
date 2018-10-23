            var req;
            var target;
            var isIE;                        
            
            function replaceCharacteres(str, old, news) {
                //alert(str);
                var strTemp = str;
                //alert(strTemp);
                while (strTemp.indexOf(old) != -1) {
                    strTemp = strTemp.replace(old, news);
                    //alert('nuevo: ' + strTemp); 
                }                
                return strTemp;
            }
                    
            // (3) JavaScript function in which XMLHttpRequest JavaScript object is created.
            // Please note that depending on a browser type, you create
            // XMLHttpRequest JavaScript object differently.  Also the "url" parameter is not
            // used in this code (just in case you are wondering why it is
            // passed as a parameter).
            //
            function initRequest(url) {
            
                //window.alert("initRequest(" + url + ") is called");
                //console.log("initRequest(%s) is called", url);
             
                if (window.XMLHttpRequest) {
                    req = new XMLHttpRequest();                    
                } else if (window.ActiveXObject) {
                    isIE = true;
                    req = new ActiveXObject("Microsoft.XMLHTTP");
                }
            }
            
             /**
             * Function to get elements from a XML document, having this one 
             * a root element and whatever leafs accesing through the index number
             */
            function get_element(doc_el, name, idx) {
                var element = doc_el.getElementsByTagName(name);
                return element[idx].firstChild.data;
            }
            
            function focusText() {
                var textBox = forma.myinputid;
                textBox.value = '';
                textBox.focus();
            }            
            /****
             * Function to disable the submit button
             */
            function disableSubmitBtn() {
                var submitBtn = document.getElementById("submit_btn");
                submitBtn.disabled = true;
            }            
            