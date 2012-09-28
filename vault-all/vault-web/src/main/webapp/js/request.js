window.request = {
		
	selectRequest : null,
	selectUser : null,
	
	req_init : function(){
		if(request.selectRequest == null){
        	var url = "FindRequest";
            $.ajax({
                type: "POST",
                url: url,
                data: "operation=FindRequest",
                success: function(rtnData) {
                    rtnData = eval("(" + rtnData + ")");
                    var childstr = rtnData.rqsts;
                    $("#input_parent").autocomplete(childstr,{mustMatch:true,matchContains:1});
                    $("#input_child_0").autocomplete(childstr,{ mustMatch:true,matchContains:1} );
                    request.selectRequest = childstr;
                }
            });
        }else{
        	$("#input_parent").autocomplete(childstr,{mustMatch:true,matchContains:1});
        	$("#input_child_0").autocomplete(request.selectRequest,{ mustMatch:true,matchContains:1} );
        }
		
		if(this.selectUser == null){
			var url = "ShowAllEmails";
			$.ajax({
				type: "POST",
				url: url,
				data: "operation=ShowAllEmails",
				dataType : "json",
				success: function(rtnData) {
					var emails = rtnData.emails;
					$("#input_owner_0").autocomplete(emails,{multiple:true,mustMatch:true,matchContains:1});
					$("#input_cc").autocomplete(emails,{multiple:true,mustMatch:true,matchContains:1});
					request.selectUser = emails;
				}
			});
		}else{
			$("#input_owner_0").autocomplete(request.selectUser,{multiple:true,mustMatch:true,matchContains:1});
			$("#input_cc").autocomplete(request.selectUser,{multiple:true,mustMatch:true,matchContains:1});
		}
		
		$("#requesttime").datepicker({
			dateFormat: 'yy-mm-dd',
			numberOfMonths: 3,
			showButtonPanel: true,
			clickInput:true
        });
		$('#requestdatetime').ptTimeSelect();
		
		$("#requestAttachment").change(function () {
			var url = "VaultFileUpload?operation=upload";
			$(this).upload(
				url,
				{operation:"upload"}
				,function(data){
				$("#Attachment_div_request").html(data);
			});
		});
		
		$("#requestname").focus();
	},
	
	req_addOwner : function(){
		var ownercount = $("#maxownercount").val();
		if(parseInt(ownercount) == 2){
			alert("The item can't more than 3 !");
			return;
		}
		var email = $("#input_owner_"+ownercount).val();
		ocId = "input_owner_"+ownercount;
		if(email != ""){
			/*if (!checkEmail(email)) {
				alert("Enter any valid redhat user id !");
				document.getElementById(ocId).value="";
				return;
			}else {
				if(checkOwner(email,ocId)||checkCc(email,ocId)){
					alert("Two sign off or cc  fields have same user email!");
					document.getElementById(ocId).value="";
					return;
				}
			}*/
		}
		
		var newownercount = parseInt(ownercount)+parseInt(1);
		$('<select id="notifyoption' + newownercount + '" data-placeholder=""  class="chzn-select creat-request-select" style="margin-top:4px;margin-right:4px;" tabindex="6"> '
			      +  '<option value="1">Reset and require new sign off</option>'
			      +  '  <option value="2">Send email notification</option>'
			      +  '  <option value="3">Do not notify</option>'
			      +  '</select>'
			      +  '<input type="text" class="input-xlarge add-width" id="input_owner_' + newownercount + '" value="">'
			      +  '<span id="input_owner_' + newownercount + '_del" class="delate-table" onclick=javascript:request.req_delOwner("input_owner_' + newownercount + '",' + newownercount + ')></span></br>').insertBefore($('#sign_addchild_btn'));
		
		$("#input_owner_"+newownercount).focus();
		$("#maxownercount").val(newownercount);
		
		if(request.selectUser == null){
			var url = "ShowAllEmails";
			$.ajax({
				type: "POST",
				url: url,
				data: "operation=ShowAllEmails",
				dataType : "json",
				success: function(rtnData) {
					var emails = rtnData.emails;
					$("#input_owner_"+newownercount).focus().autocomplete(emails,{multiple:true,mustMatch:true,matchContains:1});
					request.selectUser = emails;
				}
			});
		}else{
			$("#input_owner_"+newownercount).focus().autocomplete(request.selectUser,{multiple:true,mustMatch:true,matchContains:1});
		}
	},
	
	req_delOwner : function(inputownerid,index){
		var ownercount = $("#maxownercount").val();
		$("#notifyoption"+index).val("");
		$("#notifyoption"+index).remove();
		$("#"+inputownerid).val("");
        $("#"+inputownerid).remove();
        $("#"+ inputownerid + "_del").next("br").remove();
        $("#"+ inputownerid + "_del").remove();
        $("#maxownercount").val(ownercount-parseInt(1));
	},
	
	req_addChild : function(){
        if(request.req_checkParentAndChild()) {
    		var childcount = $("#maxchildcount").val();
            var newchildcount = parseInt(childcount)+parseInt(1);
           
            $('<input type="text" class="input-xlarge" style="margin-top:4px;" id="input_child_' + newchildcount + '"><span id="input_child_' + newchildcount + '_del" class="delate-table" onclick=javascript:request.req_delChild("input_child_' + newchildcount + '")></span></br>').insertBefore($("#addchild_btn"));
            $("#input_child_"+newchildcount).focus();
            $("#maxchildcount").val(newchildcount);

            if(request.selectRequest == null){
            	var url = "FindRequest";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: "operation=FindRequest",
                    success: function(rtnData) {
                        rtnData = eval("(" + rtnData + ")");
                        var childstr = rtnData.rqsts;
                        $("#input_child_"+newchildcount).focus().autocomplete(childstr,{ mustMatch:true,matchContains:1} );
                        request.selectRequest = childstr;
                    }
                });
            }else{
            	$("#input_child_"+newchildcount).focus().autocomplete(request.selectRequest,{ mustMatch:true,matchContains:1} );
            }
        }else {

        }
	},
	
	req_delChild : function(inputchildid){
		var childcount = parseInt($("#maxchildcount").val());
        if(inputchildid == "input_child_0" && childcount==0){
            
        }else{
            $("#"+inputchildid).val("");
            $("#"+inputchildid).remove();
            $("#"+ inputchildid + "_del").next("br").remove();
            $("#"+ inputchildid + "_del").remove();
            $("#maxchildcount").val(childcount-parseInt(1));
        }
	},
	//parent request and child request cannot be same
	req_checkParentAndChild : function(){
		var intchildcount = parseInt($("#maxchildcount").val());
		var parent = $("#input_parent").val();
		var childstr = "";
		if(parent!="") {
	        for(var i=0;i<=intchildcount;i++){
				var child = $("#input_child_"+i).val();
				if(typeof child  != "undefined"){
					if(child != ""){
						if(request.req_compare(parent,child)) {
							alert("Parent field and Child field have same request!");
							return false;
						}
					}
				}
	        }
		}
		for(var i=0;i<=intchildcount;i++) {
			var child1 = $("#input_child_"+i).val();
			if(typeof child1  != "undefined"){
				if(child1 != ""){
					for(var j=i+1;j<=intchildcount;j++) {
						var child2 = $("#input_child_"+j).val();
						if(typeof child2  != "undefined"){
							if(child2 != ""){
								if(request.req_compare(child1,child2)) {
									alert("Two Child fields have same request!");
									return false;
								}
							}
						}
					}
					childstr += child1 + ",";
				}
			}
		}
		document.getElementById("childrenStr").value = childstr;
		document.getElementById("parentStr").value = document.getElementById("input_parent").value;
		return true;
	},
	//sign off and cc cannot be same
	req_checkSignOffAndCC : function(){
		var ownercount = $("#maxownercount").val();	
        var intownercount = parseInt(ownercount);
        
		for ( var i = 0; i <= intownercount; i++) {
			var owner = $("#input_owner_"+i).val();
			if(typeof owner  != "undefined"){
				if(owner != ""){
					if(!this.req_checkMail(owner)) {
						alert("Please enter valid Red Hat email address.");
						return false;
					}
				}
			}
			if(i==-1) return false;
        }
        
		var cc = $("#input_cc").val();
		if(!this.req_checkMail(cc)) {
			alert("Please enter valid Red Hat email address.");
			return false;
		}

        var ownerstr = "";
		var notifystr = "";
        for(var i=0;i<=intownercount;i++){
			var owner1 = document.getElementById("input_owner_"+i).value;
			if(typeof owner1  != "undefined"){
				if(owner1 != ""){
					var ownerArray1 = owner1.split(',');
					ownerArray1.sort();
					for(var r=0;r<ownerArray1.length-1;r++){
						if($.trim(ownerArray1[r])=="" || $.trim(ownerArray1[r+1])==""){
							continue;
						}
						if (ownerArray1[r]==ownerArray1[r+1]){
							alert("Sign off field have same email：" + ownerArray1[r]);
							return false;
						}
					}
					
					for(var r=0;r<ownerArray1.length;r++){
						for(var j=i+1;j<=intownercount;j++) {
							var owner2 = document.getElementById("input_owner_"+j).value;
							if(typeof owner2  != "undefined"){
								if(owner2 != ""){
									var ownerArray2 = owner2.split(',');
									for(var k=0;k<ownerArray2.length;k++){
										if($.trim(ownerArray1[r])=="" || $.trim(ownerArray2[k])==""){
											continue;
										}
										if(ownerArray1[r] == ownerArray2[k]){
											alert("Two sign off fields have same email:" + ownerArray1[r]);
											return false;
										}
									}
								}
							}
						}
					}
					
					for(var r=0;r<ownerArray1.length;r++){
						var cc2 = document.getElementById("input_cc").value;
						if(typeof cc2  != "undefined"){
							if(cc2 != ""){
								var ccArray = cc2.split(',');
								for(var k=0;k<ccArray.length;k++){
									if($.trim(ownerArray1[r])=="" || $.trim(ccArray[k])==""){
										continue;
									}
									if(ownerArray1[r] == ccArray[k]){
										alert("Sign off and Cc have same email:" + ownerArray1[r]);
										return false;
									}
								}
							}
						}
					}

					var notifyObj = document.getElementById("notifyoption"+i);
					var notifyVal = notifyObj.options[notifyObj.selectedIndex].value;
					for(var r=0;r<ownerArray1.length;r++){
						if($.trim(ownerArray1[r])==""){
							continue;
						}
						ownerstr += ownerArray1[r] + ",";
						notifystr += ownerArray1[r] +":" + notifyVal+ ",";
					}
				}
			}
        }
		document.getElementById("owner").value = ownerstr;
	    document.getElementById("notifyOption").value = notifystr;	


		var cc_str = document.getElementById("input_cc").value;
		if(typeof cc_str  != "undefined"){
			if(cc_str != ""){
				var ccArray = cc_str.split(',');
				ccArray.sort();
				for(var r=0;r<ccArray.length-1;r++){
					if (ccArray[r]==ccArray[r+1]){
						alert("Cc field have same email：" + ccArray[r]);
						return false;
					}
				}
			}
		}
		document.getElementById("cc").value = cc_str;
		return true;
	},
	/**
	 * check mail 
	 * @param mailList  (the user mails separated with ",")
	 */
	req_checkMail : function(mailList){
		var flag = true;
		if(typeof mailList != "undefined"){
			var mailArray = mailList.split(',');
			$.each(mailArray,function(index,item){
				if(typeof item != "undefined"){
					if(item != "" && jQuery.trim(item) != ""){
						if(item.indexOf("redhat.com") != -1 || item.indexOf("REDHAT.COM") != -1){
							
						}else{
							flag = false;
							return;
						}
					}
				}
			});
		}
		return flag;
	},

	req_ckDate : function(datestr) {
		if(!datestr.match(/^\d{4}\-\d{2}\-\d{2}$/)){
    		return false;
		}
		var vYear = datestr.substr(0, 4) - 0;
		var vMonth = datestr.substr(5, 2) - 1;
		var vDay = datestr.substr(8, 2) - 0;
		if(vMonth >= 0 && vMonth <= 11 && vDay >= 1 && vDay <= 31){
			var vDt = new Date(vYear, vMonth, vDay);
			var nowDate = new Date();
			var nowNewDate  = new Date(nowDate.getFullYear(), nowDate.getMonth(), nowDate.getDate());
			if(isNaN(vDt)){
				return false;
			}else if(vDt < nowNewDate){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	},
	/**
	 * compare two requests (the param is requestId & requestName)
	 * @param req1 
	 * @param req2
	 * @returns {Boolean}
	 */
	req_compare : function(req1,req2){
		if(req1==""||req2=="") {
			return false;
		}else {
			var temp1 = req1.split(" ")[0];
			var temp2 = req2.split(" ")[0];
			if(temp1==temp2) return true;
			else return false;
		}
	},
	
	req_checkRequest : function(){
		var requestname = $("#requestname").val();
		var requesttime = $("#requesttime").val();
		var detail = $("#detail").val();
		
        if(requestname == "") {
            alert("Please enter request name.");
            return false;
        }
        
        if(typeof requesttime != "undefined"){
        	if(requesttime != "") {
        		 if(!this.req_ckDate(requesttime.Trim())){
                 	alert("Due Date must be greater than today");
                 	return false;
                 }
            }
        }
        if(detail == "") {
            alert("Please enter Detail Description.");
            return false;
        }        
        
        detail = detail.replaceAll("\r\n",'<br>');
        detail = detail.replaceAll("\n\r",'<br>');
        detail = detail.replaceAll("\n",'<br>');
        detail = detail.replaceAll("\r",'<br>');
        detail = detail.replaceAll('"',"&quot;");
        detail = detail.replaceAll(' ',"&nbsp;");
        detail = detail.replaceAll('©',"&copy;");
        detail = detail.replaceAll("&",'&amp;');
        
        
		document.getElementById("detailStr").value = detail;
		return true;
	},
	
	req_commit : function(){
		
		var requestname = $("#requestname").val();
		var childstr = "";
		if(this.req_checkParentAndChild()) {
			childstr = $("#childrenStr").val() ;
		}else return;
		if(this.req_checkSignOffAndCC()) {

		}else return;
        if(childstr == ""){
        	if(this.req_checkRequest()){
        		var form = document.newrequest_form;
    	        form.submit();		        
    	    }
	    }else{		    	
        	jQuery(function($){
    	    	//check if child has parent, if yes, prompt the message box.
    		    var url = "Checkrequest";
    	        $.ajax({	        	
    	            type: "POST",
    	            url: url,
    	            data: "operation=CheckChild&child="+childstr,
    	            dataType:"json",
    	            success: function(rtnData) {
    	                var result_str = rtnData.result;
    	                if(result_str != ""){
	    	                if(result_str.indexOf(",") > -1){			    	                
	    	                	var result_array = result_str.split(",");
	    		                if(result_array!=null && result_array.length>0){
	    			                var msg="";
	    			                for(var i=0;i<result_array.length;i++){
	    				                var each = result_array[i];
	    				                var childInfo = each.split("##")[0];
	    				                var parentInfo = each.split("##")[1];
	    				                msg+="Request "+childInfo+" already has one parent "+parentInfo+", are you sure you want to assign "+requestname+" as "+childInfo+"'s new parent?\n";
	    				            }
	    				            if(confirm(msg)){		
	    				            	if(request.req_checkRequest()){
	    					        		var form = document.newrequest_form;
	    					    	        form.submit();	
	    				            	}			            
	    					        }else{
										return;
		    					    }	    				            
	    			            }
		    	            }else{
			    	            var index = result_str.indexOf("##");
			    	            if(index > -1){				    	            	
			    	            	var childInfo = result_str.substring(0,index);
	    				            var parentInfo = result_str.substring(index+2,result_str.length);
	    				            var msg="Request "+childInfo+" already has one parent "+parentInfo+", are you sure you want to assign "+requestname+" as "+childInfo+"'s new parent?";
	    				            if(confirm(msg)){
	    				            	if(request.req_checkRequest()){
		    				            	var form = document.newrequest_form;
		    				    	        form.submit();		
	    				            	}			            
	    					        }else{
										return;
		    					    }
				    	        }
				    	         
			    	            else {
				    	            return;
				    	        }
		    	            }
    		             }else{
    		            	 if(request.req_checkRequest()){
					        		var form = document.newrequest_form;
					    	        form.submit();	
				             }
	    		         }
    	            }
    	        });
        	});
	    }
	},
	
	req_initProduct : function(){
		$("#request_selectPro").append("<option value='-1'>------------</option>");
		$("#request_selectVersion").html("<option value='-1'>Please select product</option>");
		$("#request_selectVersion option[value=-1]").attr("selected", true);
		var url = "SavequeryServlet";
		$.ajax({
			type: "POST",
			url: url,
			data: "operation=getAllProduct",
			dataType:'json',
			success: function(rtnData) {
				$.each(rtnData,function(index,item){
					$("#request_selectPro").append("<option value='" + item.productId + "'>"+ item.productName +"</option>");
				});
				
				$('#request_selectPro').change(function(){ 
					$("#request_selectVersion").html('');
					//$("#request_selectVersion").append("<option value='-1'>-----------</option>");
					jQuery('option:selected', this).each(function(){ 
						$('#selectproductid').attr('value',this.value);
					});
					var selectproductid = $('#selectproductid').attr('value');
					if(selectproductid != '-1'){
						request.req_initVersion(selectproductid);
					}else{
						$("#request_selectVersion").html("<option value='-1'>Please select product</option>");
						$("#request_selectVersion option[value=-1]").attr("selected", true);
						$("#selectversionid").val("-1");
					}
				});
				
				$('#request_selectVersion').change(function(){
					jQuery('option:selected', this).each(function(){ 
						$('#selectversionid').attr('value',this.value);
					});
				});
			}
		});
	},
	/**
	 * get versions of the product by id , if the param versionId is not null , make the option whose value is versionId selected
	 * @param productId
	 * @param versionId
	 */
	req_initVersion : function(productId,versionId){
		var url = "SavequeryServlet";
		$.ajax({
			type: 'POST',
			url: url,
			data: "operation=getProdVersion&productId="+productId,
			dataType:'json',
			success: function(rtnData) {
				if(rtnData == null){
					$("#request_selectVersion").html("<option value='-1'>Please select product</option>");
					$("#request_selectVersion option[value=-1]").attr("selected", true);
					$("#selectversionid").val("-1");
					return;
				}
				$.each(rtnData,function(index,item){
					$("#request_selectVersion").append("<option value='" + item.versionId + "'>" + item.versionValue + "</option>");
				});
				if(versionId){
					$("#request_selectVersion option[value=" + versionId + "]").attr("selected", true);
				}
			}
		});
	},
	
	req_delAttachment : function(file) {
		var url = "VaultFileUpload";
		var r=confirm("Are you sure you want to delete this attachment?");
		if(r==true){
			jQuery(function($){
				$.post(url,{
					operation:"delete",
					type:"request",
					ref:file
				},function(data){
					$("#requestAttachment").val("");
					$("#Attachment_div_request").html(data);
				});
			});
			
		}
	}
};

request.req_init();
request.req_initProduct();
request.req_initVersion();