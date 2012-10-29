window.request = {
		
	selectRequest : null,
	selectUser : null,
	option : new Object(),
	
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
					//{multiple:true,mustMatch:true,matchContains:1}
					$("#input_owner_0").autocomplete(emails,{multiple:true,matchContains:1});
					$("#input_cc").autocomplete(emails,{multiple:true,matchContains:1});
					request.selectUser = emails;
				}
			});
		}else{
			$("#input_owner_0").autocomplete(request.selectUser,{multiple:true,matchContains:1});
			$("#input_cc").autocomplete(request.selectUser,{multiple:true,matchContains:1});
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
		
		var req_d = new Date();
		var gmtHours = req_d.getTimezoneOffset()/60;
		$("#gap").val(gmtHours);
		
		this.option['notifyoption0'] = $('#notifyoption0').val();
		$('#notifyoption0').change(function(){
			if(!request.req_checkOption(0,$('#notifyoption0').val())){
				$(this).val(request.option['notifyoption0']); 
				return false;
			}
			request.option['notifyoption0'] = $('#notifyoption0').val();
		});
		
		
		$("#new_child_request").click(function(){
			$("#subMenu").html("Creat Child Request");
			
			$("table#newrequest_tbl").attr("style","display");
			$("table#canviewrequest").hide();
			$("table#myrequest").hide();
			$("table#allrequest").hide();
			$("table#searchrequest").hide();
			$("table#signedrequest").hide();
			$("table#cctomerequest").hide();
			$("table#waitrequest").hide();
			$("div#query-area").hide();
			$("div#advance_query").hide();
			$("fieldset#advance_set").hide();
			$("table#detail_name_table").hide();
			$("table#detail_tbl").hide();
			$("table#detail_sub_tbl").hide();
			$("table#detail_comment").hide();
			$("table#onbehalf_table").hide();
			$("#div_name").html("Create Request");
			
			document.getElementById("selectproductid").value = "-1";
			document.getElementById("selectversionid").value = "-1";
			document.getElementById("notifyOption").value = "0";
			document.getElementById("maxownercount").value = "0";
			document.getElementById("ownercount").value = "0";
			document.getElementById("maxcccount").value = "0";
			document.getElementById("maxchildcount").value = "-1";
			document.getElementById("requestname").value = "";
			document.getElementById("newrequestid").value = "";
			request.req_initProduct();
			request.req_initVersion();

			$("#signofftd").html('<input type="button" id="sign_addchild_btn" class ="btn off-margin span-top" onclick="javascript:request.req_addOwner()" value="Add More">');
			request.req_addOwner(2,"",true);
			$("#childtd").html('<input type="button" id="addchild_btn" class ="btn off-margin span-top" onclick="javascript:request.req_addChild()" value="Add More">');
			request.req_addChild();

			document.getElementById("requesttime").value = "";
			document.getElementById("detail").value = "";
			$("#Attachment_div_request").html("");
			document.getElementById("requestAttachment").value = "";
			$("#is_public").attr("checked","checked");
			document.getElementById("create_btn").value = "Create";
			$("#requestname").focus();
			$("#input_parent").val($('#hiddenRequestName').html().trim());
			
		});	
	},
	
	req_addOwner : function(selectedIndex,defaultValue,firstOption){
		if(!defaultValue) defaultValue="";
		var ownercount = $("#maxownercount").val();
		var ownercount1 = $("#ownercount").val();
		
		if(parseInt(ownercount1) == 2){
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
		if(firstOption){
			newownercount = parseInt(0);
		}
		var optionStr = "";
		if(this.req_checkOption(newownercount,1)){
			optionStr += '<option value="1">Reset and require new sign off</option>';
		}
		if(this.req_checkOption(newownercount,2)){
			optionStr += '<option value="2">Send email notification</option>';
		}
		if(this.req_checkOption(newownercount,3)){
			optionStr += '<option value="3">Do not notify</option>';
		}
		var link = '';
		var tip = '';
		if(firstOption){
			link = '<a title="1. Reset and require new sign off: Any change to the Vault request will reset the sign off state to \'Waiting\' and require a new sign off. 2. Send email notification: Any change to the Vault request generates an email to the signatory. The sign off state is not changed. 3. Do not notify: Changes to the Vault request produce no notifications." href="javascript:avoid(0);">For any change,</a>';
			tip = 'Tips: can type multiple emails.';
		}
		$(link + '<select id="notifyoption' + newownercount + '" data-placeholder=""  class="chzn-select creat-request-select creat-request-select-sign-off" style="margin-top:4px;margin-right:4px;" tabindex="6"> '
				  +  optionStr
			      +  '</select>'
			      +  '<input type="text" class="input-xlarge add-width creat-request-select-sign-off" style="margin-top:4px;margin-right:4px;" id="input_owner_' + newownercount + '" value="' + defaultValue + '">'
			      +  '<span id="input_owner_' + newownercount + '_del" class="delate-table" onclick=javascript:request.req_delOwner("input_owner_' + newownercount + '",' + newownercount + ')></span>' + tip + '</br>').insertBefore($('#sign_addchild_btn'));
		if(selectedIndex){
			$("#notifyoption"+newownercount+" option[value=" + selectedIndex + "]").attr("selected",true);
		}

		$("#input_owner_"+newownercount).focus();
		$("#maxownercount").val(newownercount);
		if(!firstOption){
			$("#ownercount").val(parseInt(ownercount1) + parseInt(1));
		}
		
		request.option['notifyoption'+newownercount] = $('#notifyoption'+newownercount).val();
		$('#notifyoption'+newownercount).change(function(){
			if(!request.req_checkOption(newownercount,$('#notifyoption'+newownercount).val())){
				$(this).val(request.option['notifyoption'+newownercount]); 
				return false;
			}
			request.option['notifyoption'+newownercount] = $('#notifyoption'+newownercount).val();
		});
		
		if(request.selectUser == null){
			var url = "ShowAllEmails";
			$.ajax({
				type: "POST",
				url: url,
				data: "operation=ShowAllEmails",
				dataType : "json",
				success: function(rtnData) {
					var emails = rtnData.emails;
					$("#input_owner_"+newownercount).focus().autocomplete(emails,{multiple:true,matchContains:1});
					request.selectUser = emails;
				}
			});
		}else{
			$("#input_owner_"+newownercount).focus().autocomplete(request.selectUser,{multiple:true,matchContains:1});
		}
		
		ownercount1 = $("#ownercount").val();
		if(parseInt(ownercount1) == 2){
			$('#sign_addchild_btn').hide();
		}
	},
	
	req_delOwner : function(inputownerid,index){
		var ownercount = $("#ownercount").val();
		if(inputownerid == 'input_owner_0' && index == 0){
			$("#"+inputownerid).val("");
			return;
		}
		$("#notifyoption"+index).val("");
		$("#notifyoption"+index).remove();
		$("#"+inputownerid).val("");
        $("#"+inputownerid).remove();
        $("#"+ inputownerid + "_del").next("br").remove();
        $("#"+ inputownerid + "_del").remove();
        $("#ownercount").val(ownercount-parseInt(1));
        request.option['notifyoption'+index] = -1;
        
        ownercount1 = $("#ownercount").val();
		if(parseInt(ownercount1) == 1){
			$('#sign_addchild_btn').show();
		}
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
        if(inputchildid == "input_child_0"){
        	$("#"+inputchildid).val("");
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
		if(this.req_checkIsSelf()){
			return false;
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
			var owner1 = $("#input_owner_"+i).val();
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
							var owner2 = $("#input_owner_"+j).val();
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
        
        if($('#owner').val() == ''){
        	alert('Please enter signoff user.');
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
        if(detail.length > 27000){
        	alert("Description is too long.It should not be more than 27000 characters");
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
    	            data: "operation=CheckChild&child="+childstr + "&id=" + $('#newrequestid').val(),
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
		$('#request_selectPro').html('');
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
					if(index == 0){
						$("#selectversionid").val(item.versionId);
					}
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
	},
	
	req_delRequest : function(reqId,reqName){
		var r=confirm("Are you sure you want to delete the request " + reqName + "?");
		if(r){
			var action = "deleteRequest?requestid=" + reqId;
	    	window.location.href = action;
		}
	},
	
	req_sumReport : function(table){
		 /*var arr = $("#myrequest input[type=checkbox]:checked").map(function() { 
             var obj = $(this).parent().next();
             //obj.text(), obj.next().text(), obj.text(), obj.parent().next().text()
             return [obj.text()];
         }).get();
        if (arr == null || arr.length == 0){
            alert("Please select requests by clicking on check box.");
            return false;
        }
		var selectedRpts = arr.join("_");
        */
		var sData = "";
		if(table == "search"){
			sData = $('input', searchoTable.fnGetNodes()).serialize();
		}else{
			sData = $('input', myrequestTable.fnGetNodes()).serialize();
		}
        if (sData == ""){
            alert("Please select requests by clicking on check box.");
            return false;
        }
		var selectedRpts;
		selectedRpts = "";
		var sDataArray = sData.split("&");
		for(var i=0;i<sDataArray.length;i++){
			if(i<sDataArray.length-1){
				selectedRpts+=sDataArray[i].split("=")[1]+"_";
			}else{
				selectedRpts+=sDataArray[i].split("=")[1];
			}
		}
		window.open("vaultSumReport?op=mr&tp=mine&selectedRpts="+selectedRpts);
	},
	/**
	 * check if there has been the option 
	 * @param currentIndex
	 * @param currentValue
	 * @returns {Boolean}
	 */
	req_checkOption : function(currentIndex,currentValue){
		var ownercount = $("#maxownercount").val();
		var count = parseInt(ownercount);
		for(var i = 0;i<=count;i++){
			if(request.option['notifyoption' + i] != -1 && request.option['notifyoption' + i] == currentValue && i != currentIndex){
				return false;
			}
		}
		return true;
	},
	
	req_checkRequestEqual : function(request,requestid) {
		return request.split(" ")[0]==requestid;
	},
	
	req_checkIsSelf : function() {
		var newrequestid = document.getElementById("newrequestid").value;
		if(newrequestid!="") {
			var parent = document.getElementById("input_parent").value;
			if(parent!=""&&this.req_checkRequestEqual(parent,newrequestid)) {
				alert("Parent can't be itself.");
				return true;
			}
			var intchildcount = parseInt(document.getElementById("maxchildcount").value);
			for(var i=0;i<=intchildcount;i++){
				var child = $("#input_child_"+i).val();
				if(typeof child  != "undefined"){
					if(child != ""&&this.req_checkRequestEqual(child,newrequestid)){
						alert("Child can't be itself.");
						return true;
					}
				}
			}
		}
		return false;
	},
	req_updateProducts : function() {
		var url = "ProductUpdate";
		var flag = "";
		var awccc = "";
		jQuery (function($) {
			$.ajax ({
				type: "POST",
                url: url,
                dataType:"json",
				success: function (rtnData) {
//					rtnData = eval("(" + rtnData + ")");
					flag = rtnData.flag;
					if (flag == "success") {
//						request.req_initProduct();
//						request.req_initVersion();
						location.reload();
					} else {
						alert("Sorry, update failed! Please contact the administrator.");
					}
				}
			});
		});
	}
};

request.req_init();
request.req_initProduct();
request.req_initVersion();