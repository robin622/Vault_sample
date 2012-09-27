<script type="text/javascript">
var globalRequests;
var firstSub=0;
function checkInterval() {
	var secondSub = new Date().getTime();
	var flag;
	if(secondSub-firstSub<1000) flag=false;
	else {
		flag = true;
	};
	firstSub = secondSub;
	return flag;
}
function checkCommentLength(comment) {
	if(comment.length>65500) {
		alert("Comment is too long.It should not have more than 65000 characters!");
		return false;
	}
	else return true;
}
if(${judgeDetailValue}){
	jQuery(function($){
		$("#requestid").val("${detailRequestid}");
		$("#versionid").val("${detailVersionid}");
		$("#judgeSignOff").val("0");
	});
}

//will be removed
function viewRequest(requestId,url) {
	jQuery(function($){
		var canViewUrl = "<%=request.getContextPath()%>/servlet/ShowRequestServlet";
		$.ajax({
			type: "POST",
			url: canViewUrl,
			data: "operation=canViewRequest"+"&requestId="+requestId+"&userName=${userName}",
			success: function(rtnData) {
				rtnData = eval("(" + rtnData + ")");
				var canView = rtnData.canView;
				if(canView) {
					location.href = url;
				}else {
					alert("You don't have the permit to view this request!");
				}
			}
		});
	});	
}

</script>
<input type="hidden" name="versionid" id="versionid" value="-1"/>
<table cellpadding="0" cellspacing="0" class="home_table margin_top" id="detail_name_table" style="display:none">
            	<tr>
                	<th class="tableheader">
                		<div class="tableheaderleft" id="requestname_value"><c:if test="${not empty detailRequest.requestname}">${detailRequest.requestid} ${detailRequest.requestname}</c:if>
                		</div>
                	</th>
                    <th align="right" class="tableheader">
                    		<div class="tableheaderright">
                    		<form action="<%=request.getContextPath()%>/rp" method="POST" name="formReport" id="formReport">
                            <a href="javascript:edit()" class="edit" id="edit_tr">Edit</a><a href="#" id="new_child_request" class="create_child">Create Child Request</a><a href="javascript:report()" class="report">Report This Request</a>
                           	<input type= "hidden" id ="requestNameUnescape" value="${requestName_unescape}" /> 
                                <script type="text/javascript">
                                    function report(){
                                         var form = document.formReport;
                                         form.target= "_blank";
                                         jQuery(function($){
                                             var action = $("#formReport").attr("action");
                                             var requestid = $("#requestid").val();
                                             //var str = "&requestid="+requestid;
                                             var str = "?id="+requestid;
                                             $("#formReport").attr("action",action+str);
                                             form.submit();
                                             $("#formReport").attr("action",action);
                                             return;
                                         });
                                    }
                                </script>
                            </form>
                    		</div>
                    </th>
                </tr>
                </table>
<table cellpadding="0" cellspacing="0" class="home_table" id="detail_tbl" style="display:none">
            	
                <tr>
                    <td class="new_bg"><span class="detail_title">Product:</span></td>
                    <td class="new_bg_white" id="product_value">
                    	${detailRequest.productname}
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="new_bg"><span class="detail_title">Version:</span></td>
                    <td class="new_bg_white" id="version_value">
                    	${detailRequest.versiondesc}
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="new_bg"><span class="detail_title">Creator:</span></td>
                    <td class="new_bg_white" id="creator_value">
                    	${detailRequest.createdby}
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="new_bg"><span class="detail_title">Create date:</span></td>
                    <td class="new_bg_white" id="createdtime_value">
                    	${tran:transformByFormat(detailRequest.createdtime,"yyyy-MM-dd HH:mm")}
                    </td>
                    <td><script>OutputLoc();</script></td>
                </tr>
                <tr>
                    <td class="new_bg"><span class="detail_title">Due date:</span></td>
                    <td class="new_bg_white" id="requesttime_value">${tran:transformByFormat(detailRequest.requesttime,"yyyy-MM-dd HH:mm")}
                    </td>
                    <td><script>OutputLoc();</script></td>
                </tr>
                <tr>
                    <td class="new_bg"><span class="detail_title">Parent:</span></td>
                    <td class="new_bg_white" id="parent_val">                    						
						${tran:getParent(detailRequest.parent,pageContext.request.contextPath)}       
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td class="new_bg"><span class="detail_title">Child:</span></td>
                    <td class="new_bg_white" id="child_val">
                    	${tran:getChildren(detailRequest.parent,pageContext.request.contextPath)}
                    </td>
                    <td></td>
                </tr>
                 <tr>
                    <td class="new_bg"><span class="detail_title">Detailed description:</span></td>
                    <td class="new_bg_white" colspan="2">
                    <div class="description" id="detail_value">
                    	
                    </div>
                    <c:if test="not empty detailDetailed">
                   		<script type="text/javascript">
                   		jQuery(function($){
                   			var detail = "${detailRequest.detailDetailed}";
                   			var editdetail = detail; 
                   			//var reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|&|-)+)/g;
                   			var reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|-)+)/g;
                   			detail = detail.replace(reg, '<a href="$1$2" target="_blank">$1$2</a>');
                   			detail = detail.replaceAll(' ','&nbsp;');
                   			detail = detail.replaceAll("&nbsp</a>;","</a>&nbsp;");
                   			$("#detail_value").html("");
                   			$("#detail_value").html(detail);
                   			editdetail = editdetail.replaceAll("<br>",'\n');                   			
                   			editdetail = editdetail.replaceAll('&quot;','"');
                   			editdetail = editdetail.replaceAll('&nbsp;',' ');
                   			editdetail = editdetail.replaceAll('&copy;','�');
                   			editdetail = editdetail.replaceAll("&amp;amp;",'&');
                   			editdetail = editdetail.replaceAll('&amp;','&');
                   			editdetail = editdetail.replaceAll('&lt;','<');
                   			editdetail = editdetail.replaceAll('&gt;','>');
                   			$("#detail").val("");
    						$("#detail").val(editdetail);
                       	});
                   		</script>
                   	</c:if>
                    </td>
                </tr>
                <tr>
                    <td  class="new_bg"><span class="detail_title">Attachment:</span></td>
                    <td class="new_bg_white" colspan="2">
                    	<div id="requestAttachment_value">
                    		<c:if test="${not empty detailRequest}">
                    			${tran:createFileInfo(detailRequest,pageContext.request.contextPath)}
                    		</c:if>
                    	</div>
                    </td>
                </tr>
                <tr>
					<td class="new_bg"><span class="detail_title">Public:</span>
					<input type="hidden" name="public_temp" id="public_temp" value="0"/>
					</td>
					<td class="new_bg_white" id="public_value" colspan="2">
						<c:if test="${not empty detailRequest.is_public}">
							<c:choose>
								 <c:when test="${detailPublic eq 1}">
								 	Yes
									<script type="text/javascript">
									jQuery(function($){
										$("#public_temp").attr("value","1");
									});
									</script>
								 </c:when>
								 <c:otherwise>
								 	No
									<script type="text/javascript">
									jQuery(function($){
										$("#public_temp").attr("value","0");
									});
									</script>
								 </c:otherwise>
							</c:choose>
						</c:if>
					</td>
				</tr>
				<tr>
                    <td class="new_bg"><span class="detail_title">CC:</span></td>
                    <td class="new_bg_white" id="cc_value" colspan="2">
                    	${detailRequest.forward}
                    </td>
                </tr>
				
                <tr>
                    <td colspan="3" style=" border:none; height:30'" id="detail_td">
                    <table cellpadding="0" cellspacing="0" class="list_table" width="100%" id="reception_tbl"></table>
                    	<c:if test="${not empty detailRequest.requestid}">
	                    	<script type="text/javascript">
		                    	jQuery(function($){
		                    		var url = "<%=request.getContextPath()%>/servlet/ShowRequestServlet";
		                    		$.ajax({
		    							type: "POST",
		    							url: url,
		    							data: "operation=RequestHistory&requestid=${detailRequest.requestid}&userEmail=${userEmail}&userName=${userName}",
		    							beforeSend:function(){	    										
											$(document.body).append(loadingDiv);
										},
		    							success: function(rtnData) {
		    								rtnData = eval("(" + rtnData + ")");
		    								var historys = rtnData.historys;
		    								globalRequests = rtnData.requests;
		    								var comments = rtnData.comments;
		    								var data1 = null;
		    								var data2 = new Array();
		    								$("#addcomment_div").html("");
		    								$("#judgeSignOff").val("0");
		    								if(comments != null && comments != ""){
		    									$.each(comments, function(i, n){
		    										var editedtime = "";
		    										var now = "";
		    										if(n.editedtime != null){    											
		    											now = n.editDate;    		
		    											now = now.substring(0,now.lastIndexOf(":"));							
		    										}
		    										//var reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|&|-)+)/g;
		    										var reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|-)+)/g;
		    			                   			var comment_href = n.comment.replace(reg, '<a href="$1$2" target="_blank">$1$2</a>');
		    										comment_href = comment_href.replaceAll("&amp;",'&');
		    										$("#addcomment_div").append("<div class='comment_list "+n.status+"'><strong>"+comment_href+"</strong><div class='comment_pt'>"+now+" "+n.editedby+" "+n.status+"<a title='reply' href='javascript:showTextArea("+i+",-1)'></a></div><ul id='reply"+i+"' style='display:none'></ul><div id ='input"+i+"' class='comment_input'></div><input type= 'hidden' id ='historyid"+i+"' value='"+n.historyid+"'/></div>");
		    										showReply(i);
		    									});
		    								}
		    								
		    								
		    								$("#cc_value").html("${detailRequest.forward}");
		    								$("#maxcccount").val("${detailRequest.getCcCount()}");
		    								$("#maxownercount").val("${detailRequest.getSignatoryCount()}");
		    								$("#maxchildcount").val("${detailRequest.getChildCount()}");
		    								
		    								
		    								
		    								if(historys != null && historys != ""){
		    									$("#reception_div").html("");
		    									//var count = -1;
		    									var index=0;
		    									$.each(historys, function(i, n){
		    										//var editedtime = "";
		    										var now = "";
		    										data1 = new Array();
		    										data1[0] = n.historyid;
		    										data1[1] = n.useremail;
		    										data1[2] = "${tran:transformByFormat(detailRequest.requesttime,"yyyy-MM-dd HH:mm")}";
		    										if(n.editedtime != null){
		    											now = n.editDate;
		    											now = now.substring(0,now.lastIndexOf(":"));
		    										}
		    										data1[3] = now;
		    										data1[4] = n.status;
		    										data2[i] = data1;
		    										if(n.displayFlag == "0"){
		    											$("#reception_div").append("<div class='ower' id='owner"+index+"'><select id='notifyoption"+index+"' name='notifyoption"+index+"' ><option value='1' title='Any change of the Vault request will reset the sign off state to Waiting and require a new sign off'>Reset and require new sign off</option><option value='2' title='Any change of the Vault request generates an email to the signatory, The sign off state will not change'>Send email notification</option><option value='3' title='Changes of the Vault request produce no notifications'>Do not notify</option></select> &nbsp;<input type='text' name='inputowner"+index+"' id='inputowner"+index+"' value='"+n.useremail+"' /><input type='button' class='delete' onclick='javascript:deleteReception("+'"'+"owner"+index+""+'"'+","+ '"'+"inputowner"+index+""+'"' +")'/></div>");
		    											//$("#reception_div").append("<div class='ower' id='owner"+index+"'><select id='notifyoption"+index+"' name='notifyoption"+index+"' ><option value='0'>------</option><option value='1'>Request for sign off</option><option value='2'>Notify the change</option><option value='3'>Don't send an email</option></select> &nbsp;<input type='text' name='inputowner"+index+"' id='inputowner"+index+"' value='"+n.useremail+"'  onblur='javascript:checkOwnerAndCc("+'"'+"inputowner"+index+'"'+");'  /><input type='button' class='delete' onclick='javascript:deleteReception("+'"'+"owner"+index+""+'"'+","+ '"'+"inputowner"+index+""+'"' +")'/></div>");
		    											$("#notifyoption"+index+" option[value="+n.notifiyOptionValue+"]").attr("selected",true);
		    											index++;
		    											}
		    									
		        								});
		    									
		    									if(globalRequests != null && globalRequests != ""){
													if (${isEditable}){
														$("#edit_tr").show();
													}else{
														$("#edit_tr").hide();
													}
													if (${isSignatory}){	
														$("#sign_btn").show();
														$("#reject_btn").show();
													}else{
														$("#sign_btn").hide();
														$("#reject_btn").hide();
													}
												}
		
		    									if (${displaySignoffOnBehalf}){
													$("#sign_onbehalf_btn").show();											
												}else{
													$("#sign_onbehalf_btn").hide();
												}
		        							}
		    								if (typeof iTable != 'undefined'){
		    									$("#detail_td").html("");
		    									$("#detail_td").append("<table cellpadding='0' cellspacing='0' class='list_table' width='100%' id='reception_tbl'></table>");
		    								}
		    								iTable = $('#reception_tbl').dataTable({
		    								    "iDisplayLength": 100,
		    									"aaSorting": [[ 1, "desc" ]],
		    									"aaData": data2,
		    									"bAutoWidth": false,
		    									"sPaginationType": "full_numbers",
		    									"aoColumns": [
		    									  { "sTitle": "ID",
		    										"bVisible":false },
		    									  { "sTitle": "Sign&nbsp;off&nbsp;by",
		    										"fnRender": function(obj){
		    											var reception = obj.aData[1];
		    							            	var	sReturn = "<a href='#'>"+reception+"</a>"
		    											return sReturn;
		    						              	}
		    									  },
		    									  { "sTitle": "Due&nbsp;date"},
		    									  { "sTitle": "Last&nbsp;modified"},
		    									  { "sTitle": "Status",
		    										"fnRender":function(obj){
		    						            	  var status = obj.aData[4];
		    										  var sReturn = "<span class='"+status+"'>"+status+"</span>";
		    										  return sReturn;
		    					              		}
		    									  }
		    							        ]
		    								});
		    								$('#loadingDiv').remove();
		    								toreplace("reception_tbl");
		    								toreplace2("addcomment_div");
		    								//console.info("load finished.......");    								
		        						}
		                    		});
								});
                    		</script>
                    </c:if>
                    </td>
				</tr>
				
                <tr>
                    <td  class="new_bg"><span class="detail_title">Comment:</span></td>
                    <td class="new_bg_white" id="comment_td" colspan='2'>
                    	<input type="hidden" name="requestid" id="requestid" value=""/>
                    	<div id="addcomment_div">
	                    	
                    	</div>
                    	<textarea name="comment" id="comment"></textarea>
                    	<div class="ower"><input type="button" value="Add Comment" id="comment_btn"  class="btn" style="margin:5px 0" onclick="javascript:addComment()"/>
                    	</div>
                    		
                    </td>
                    
                </tr>
                <tr>
                <th colspan="3">
                	<div class="tablefooter"> 
                    	<div class="tablefooterleft">
                    	<input type="hidden" name="judgeSignOff" id="judgeSignOff" value="0"/>
                    	<input type="button" value="Sign Off"  class="btn"  id="sign_btn" onclick="javascript:commitSignOff('1','')"/>
                    	<!-- <input type="button" value="Sign Off On Behalf Of"  class="btn"  id="sign_onbehalf_btn" onclick="javascript:commitSignOff('2')"/>  -->
                    	<input type="button" value="Sign Off On Behalf Of"  class="btn"  id="sign_onbehalf_btn" onclick="javascript:behalf_click(this)"/>                    	
                    	<input type="button" value="Reject"  class="btn" id="reject_btn" onclick="javascript:commitReject()"/>                    	
                    	<input type="button" value="Cancel" class="btn" id="canceldetail"/>
                    	</div>
                        <div class="tablefooterright"></div>
                    </div>
                </th>
            	</tr>
            	
</table>

<table cellpadding="0" cellspacing="0" class="home_table margin_top" id="onbehalf_table" style="display:none">

            	<tr>
                    <td  class="new_bg"><span class="detail_title">On behalf of:</span></td>
                    <td  class="new_bg_white" colspan='2'>
                        <div id="onbehalf_div"></div>
					    <input type="button" name="Submit_onbehalf" value="Confirm" onclick="javascript:click_onbehalf_confirm()"/>                    
					    <input type="button" name="Cancel_onbehalf" value="Cancel" onclick="javascript:click_onbehalf_cancel()" />
                    </td>
                </tr>
               
</table>
 <script type="text/javascript">
 	email="vault-dev-list@redhat.com";
           function behalf_click(obj){
           	obj.hide();
           	//$("table#onbehalf_table").attr("style","display");
           	document.getElementById("onbehalf_table").style.display = "block";
           	//ajax, get all emails pending for sign off                
			//console.log("ajax start...");
           	jQuery(function($){
        		var url = "<%=request.getContextPath()%>/servlet/ShowRequestServlet";
        		$.ajax({
        			type: "POST",
        			url: url,
        			data: "operation=listNoneSign&requestId=${detailRequest.requestid}",
        			success: function(rtnData) {
        				rtnData = eval("(" + rtnData + ")");
        				var users = rtnData.noneSignUsers;
        				//console.log("users = "+users);
        				$("#onbehalf_div").html("");
        				$.each(users, function(i, n){
        					$("#onbehalf_div").append("<input name='behalfEmails' type='checkbox' value='"+n+"' />   "+n+"@redhat.com<br>");						
						});        				
        			}
        		});
        	});		
           	

           }
           function click_onbehalf_confirm(){
	           	var all_behalf_users = document.getElementsByName("behalfEmails");
	            var selected_behalf_users = "";
	            for (i=0; i<all_behalf_users.length; i++){
					if(all_behalf_users[i].checked){
						selected_behalf_users += all_behalf_users[i].value + ",";
					}
	            }
				//console.log(selected_behalf_users);
				if(selected_behalf_users != ""){
					var comment = document.getElementById("comment").value;
					commitSignOff('2',selected_behalf_users);
					document.getElementById("onbehalf_table").style.display = "none";	
				}else{

					alert("Please select at least one.");
					}
           }
           function click_onbehalf_cancel(){
	           	document.getElementById("sign_onbehalf_btn").show();
	           	document.getElementById("onbehalf_table").style.display = "none";               
           }

           function refresh_tables(url,requestid){
        	   jQuery(function($){
        	   $.ajax({
					type: "POST",
					url: url,
					data: "operation=RequestHistory&requestid="+requestid+"&userEmail=${userEmail}&userName=${userName}",
					success: function(rtnData1) {
						rtnData1 = eval("(" + rtnData1 + ")");
						var comments = rtnData1.comments;
						var historys = rtnData1.historys;
						globalRequests = rtnData1.requests;
						//var editedtimeHistorys = rtnData1.editedtimeHistorys;
						var data1 = null;
						var data2 = new Array();
						if(historys != null && historys != ""){
							$.each(historys, function(i, n){

								var now = "";
								//var editedtime = "";
								data1 = new Array();
								data1[0] = n.historyid;
								data1[1] = n.useremail;
								data1[2] = $("#requesttime_value").html();
								if(n.editedtime != null){
									now = n.editDate;
									now = now.substring(0,now.lastIndexOf(":"));
									now=calculateTime(now);
								}
								data1[3] = now;
								data1[4] = n.status;
								data2[i] = data1;
							});
						}

						if(comments != null && comments != ""){
							$("#addcomment_div").html("");
							$.each(comments, function(i, n){
								//var editedtime = "";
								var now = "";
								if(n.editedtime != null){
									now = n.editDate;
									now = now.substring(0,now.lastIndexOf(":"));
								}
								//var reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|&|-)+)/g;
								var reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|-)+)/g;
	                   			var comment_href = n.comment.replace(reg, '<a href="$1$2" target="_blank">$1$2</a>');
								comment_href = comment_href.replaceAll("&amp;",'&');
								$("#addcomment_div").append("<div class='comment_list "+n.status+"'><strong>"+comment_href+"</strong><div class='comment_pt'>"+now+" "+n.editedby+" "+n.status+"<a title='reply' href='javascript:showTextArea("+i+",-1)'></a></div><ul id='reply"+i+"' style='display:none'></ul><div id ='input"+i+"' class='comment_input'></div><input type= 'hidden' id ='historyid"+i+"' value='"+n.historyid+"'/></div>");
								showReply(i);
								});
						}
						document.getElementById("comment").value = "";
						if (typeof iTable != 'undefined'){
							$("#detail_td").html("");
							$("#detail_td").append("<table cellpadding='0' cellspacing='0' class='list_table' width='100%' id='reception_tbl'></table>");
							//iTable.fnClearTable( 0 );
							//iTable.fnDraw();
						}
						iTable = $('#reception_tbl').dataTable({
						    "iDisplayLength": 100,
							"aaSorting": [[ 1, "desc" ]],
							"aaData": data2,
							"bAutoWidth": false,
							"sPaginationType": "full_numbers",
							"aoColumns": [
							  { "sTitle": "ID",
								"bVisible":false },
							  { "sTitle": "Sign&nbsp;off&nbsp;by",
								"fnRender": function(obj){
									var reception = obj.aData[1];
					            	var	sReturn = "<a href='#'>"+reception+"</a>"
									return sReturn;
				              	}
							  },
							  { "sTitle": "Due&nbsp;Date"},
							  { "sTitle": "Last&nbsp;Modified"},
							  { "sTitle": "Status",
								"fnRender":function(obj){
				            	  var status = obj.aData[4];
								  var sReturn = "<span class='"+status+"'>"+status+"</span>";
								  return sReturn;
			              		}
							  }
					        ]
						});
						toreplace2("addcomment_div");
					}
				});
        	   });
        	   
           }
           
           function showTextArea(i,baseid){
             jQuery(function($){
               if(document.getElementById("input"+i).innerHTML == ""){
                 $("#input"+i).append("<textarea name='replyComment"+i+"' id='replyComment"+i+"'></textarea><div class='ower'><input id='comment_btn' class='btn' type='button' value='Add Reply' onclick='javascript:addReply("+i+","+baseid+")'></div>");
               }else{
                 $("#input"+i).html("");
               }
              });
            }
           
		   function addReply(i,baseid){
		     var replyComment = document.getElementById("replyComment"+i).value;
		     document.getElementById("replyComment"+i).value = "";
		     var historyid = document.getElementById("historyid"+i).value;
		     if(replyComment == ""){
		       alert("Please enter Reply.");
		     }else{
		       replyComment = replyComment.replaceAll("\n",'<br>');
		       replyComment = replyComment.replaceAll("\r",'<br>');
		       replyComment = replyComment.replaceAll("\r\n",'<br>');
		       replyComment = encodeURI(replyComment);
		       replyComment = replyComment.replace(/\+/g, "%2B");
		       replyComment = replyComment.replace(/\&/g, "%26");
               jQuery(function($){
                 $("#input"+i).html("");
                 var requestid = $("#requestid").val();
                 var url = "<%=request.getContextPath()%>/servlet/ShowRequestServlet";
                 var addedReplyList = "";
                 var flag = "";
                 $.ajax({
                   type: "POST",
                   url: url,
                   data: "operation=AddReply&requestid="+requestid+"&editedby=${userName}&replyComment="+replyComment+"&historyid="+historyid+"&baseid="+baseid,
				   success: function(rtnData) {
				     rtnData = eval("(" + rtnData + ")");
    		         addedReplyList = rtnData.addedReplyList;
    		         flag = rtnData.flag;
    		         if(flag == "success"){
    		           $("#reply"+i).html("");
    		           if(addedReplyList != null && addedReplyList != ""){
    		           document.getElementById("reply"+i).style.display="block";
    		             $.each(addedReplyList, function(j, n){
    		               if(n.baseid == -1){
    		                 $("#reply"+i).append("<li id='lireply"+n.replyid+"'><span>"+n.replycomment+"</span><div class='comment_pt'>"+n.editedtime+" "+n.editedby+" Reply<a title='reply' href='javascript:showTextArea("+i+","+n.replyid+")'></a></div></li>");
    		                 $("#lireply"+n.replyid).append("<ul id='ulreply"+n.replyid+"' style='display:none'></ul>");
    		               }else{
    		                 document.getElementById("ulreply"+n.baseid).style.display="block";
    		                 $("#ulreply"+n.baseid).append("<li id='lireply"+n.replyid+"'><span>"+n.replycomment+"</span><div class='comment_pt'>"+n.editedtime+" "+n.editedby+" Reply<a title='reply' href='javascript:showTextArea("+i+","+n.replyid+")'></a></div></li>");
    		                 $("#lireply"+n.replyid).append("<ul id='ulreply"+n.replyid+"' style='display:none'></ul>");
    		               }
    		             });
    		             toreplace2("reply"+i);
    		           }
    		         }else{
    		           alert("There have some errors! Please send eamil to "+email+".");
    		         }
    		       }                   
                 });
               });
             }
		   }
		   
		   function showReply(i){
		     var historyid = document.getElementById("historyid"+i).value;
             var replyList = "";
             jQuery(function($){
               var requestid = $("#requestid").val();
               var url = "<%=request.getContextPath()%>/servlet/ShowRequestServlet";
               $.ajax({
                 type: "POST",
                 url: url,
                 data: "operation=ShowReplyComment&requestid="+requestid+"&historyid="+historyid,
				 success: function(rtnData) {
    			   rtnData = eval("(" + rtnData + ")");
    			   replyList = rtnData.replyList;
                   if(replyList != null && replyList != ""){
                   document.getElementById("reply"+i).style.display="block";
    		         $.each(replyList, function(j, n){
    		           if(n.baseid == -1){
    		             $("#reply"+i).append("<li id='lireply"+n.replyid+"'><span>"+n.replycomment+"</span><div class='comment_pt'>"+n.editedtime+" "+n.editedby+" Reply<a title='reply' href='javascript:showTextArea("+i+","+n.replyid+")'></a></div></li>");
    		             $("#lireply"+n.replyid).append("<ul id='ulreply"+n.replyid+"' style='display:none'></ul>");
    		           }else{
    		             document.getElementById("ulreply"+n.baseid).style.display="block";
    		             $("#ulreply"+n.baseid).append("<li id='lireply"+n.replyid+"'><span>"+n.replycomment+"</span><div class='comment_pt'>"+n.editedtime+" "+n.editedby+" Reply<a title='reply' href='javascript:showTextArea("+i+","+n.replyid+")'></a></div></li>");
    		             $("#lireply"+n.replyid).append("<ul id='ulreply"+n.replyid+"' style='display:none'></ul>");
    		           }
    		         });
    		         toreplace2("reply"+i);
    		       }
    			 }
               });
    		 });
		   }
		   
           function edit(){
        		jQuery(function($){
        			var url = "<%=request.getContextPath()%>/servlet/ShowRequestServlet";
        			$("table#newrequest_tbl").attr("style","display");
        			$("table#myrequest").hide();
        			$("table#allrequest").hide();
        			$("table#waitrequest").hide();
        			$("table#signedrequest").hide();
        			$("table#canviewrequest").hide();
        			$("table#detail_name_table").hide();
        			$("table#detail_tbl").hide();

        			$("#div_name").html("Edit Request")
        			
        			var requestid = $("#requestid").val();
        			
        			$("#newrequestid").val(requestid);
        			var request_name = document.getElementById("requestNameUnescape").value;
        			//document.getElementById("requestname").value = request_name.substring(request_name.indexOf("  ")+2);
        			//$("#requestname").attr("value",request_name.substring(request_name.indexOf("  ")+2));
        			$("#requestname").attr("value",request_name);
        			
        			var versionid = $("#versionid").val();
        			$("#selectversionid").attr("value",versionid);
        			if(versionid == "-1"){
        				$("#productoption option[value=-1]").attr("selected", true);
        				$("#versionoption").html("<option value='-1'>------</option>");
        				$("#versionoption option[value=-1]").attr("selected", true);
        			}else{
        				$.ajax({
        					type: "POST",
        					url: url,
        					data: "operation=editVersion"+"&id="+versionid,
        					success: function(rtnData) {
        						rtnData = eval("(" + rtnData + ")");
        						var productid = rtnData.productid;
        						var sboption = rtnData.sboption;
        						$("#selectproductid").attr("value",productid);
        						$("#productoption option[value="+productid+"]").attr("selected",true);
        						$("#versionoption").html(sboption)
        						.fadeIn("slow");
        					}
        				});
        			}
        			document.getElementById("requesttime").value = $("#requesttime_value").html().Trim().split(" ")[0];
        			//change to AM or PM
            		var allhour=$("#requesttime_value").html().Trim().split(" ")[1];
            		var firsthour=allhour.split(":")[0];
            		var secondhour=allhour.split(":")[1];
            		if(parseInt(firsthour)>12){
                		allhour=(parseInt(firsthour)-12) + ":" +secondhour + " PM";
                	}else if(parseInt(firsthour)==12){
                		allhour+= " PM";
                    }else if(parseInt(firsthour)==24){
                    	allhour+= " AM";
                    }
                    else{
                		allhour=firsthour + ":" + secondhour+" AM";
                    } 
        			document.getElementById("requestdatetime").value = allhour;
            		
        			//$("#detail").html($("#detail_value").html());
        			$("#Attachment_div_request").html("");
        			document.getElementById("requestAttachment").value = "";
        			url = "<%=request.getContextPath()%>/servlet/VaultFileUploadServlet";
        			$.post(url,{
        				operation:"list",
        				id:requestid
        			},function(data){
        				$("#Attachment_div_request").html(data);
        			});
        			var public_value = $("#public_temp").attr("value");
        			if(public_value == "1"){
        				$("#is_public").attr("checked","checked");
        			}else{
        				$("#is_public").removeAttr("checked");
        			} 
        			$("#create_btn").attr("value", "Change");


        			////
        			setUserAndRequest();
        			$("#cc_div").html("");
        			if(globalRequests != null && globalRequests != ""){
        				var cc_str = globalRequests[0].forward;
        				if(cc_str != null && cc_str != ""){
        					cc_str = cc_str.replaceAll('<br>','');
        					var cc_array = cc_str.split(",");
        					for (var i=0;i<cc_array.length;i++){								
        						$("#cc_div").append("<div class='ower' id='cc"+i+"'><input type='text' name='inputcc"+i+"' id='inputcc"+i+"' value='"+cc_array[i]+"'/><input type='button' class='delete' onclick='javascript:deleteCC("+'"'+"cc"+i+""+'"'+","+ '"'+"inputcc"+i+""+'"' +")'/></div>");    											
        						$("#inputcc"+i).autocomplete(selectUser);
        					}
        				}else{
        					$("#cc_div").html("<div class='ower' id='cc0'><input type='text' name='inputcc0' id='inputcc0' value=''/><input type='button' class='delete' onclick='javascript:deleteCC("+'"'+"cc0"+'"'+","+'"'+"inputcc0"+'"'+")'/></div>");
        					$("#inputcc0").autocomplete(selectUser);    										
        				}
        				
        			}
        			
        			//$("#inputparent").val(requests[0].parent.split("##")[1]);    								  
        			if(globalRequests[0].parent == ""){
        				$("#inputparent").val("");
        			}else{
        				$("#inputparent").val(globalRequests[0].parent.split("##")[1]);
        			}								
        			$("#inputparent").autocomplete(selectRequest,{ mustMatch:true,matchContains:1} );
        			
        			//child list
        			$("#child_div").html("");
        			if(globalRequests != null && globalRequests != ""){
        				var child_str = globalRequests[0].children;
        				if(child_str != null && child_str != ""){
        					var child_array = child_str.split(",");
        					for (var i=0;i<child_array.length;i++){								
        						//$("#child_div").append("<div class='ower' id='child"+i+"'><input  onblur='javascript:checkChildParent("+'"'+"inputchild"+i+'"'+");' type='text' name='inputchild"+i+"' id='inputchild"+i+"' value='"+child_array[i].split("##")[1]+"'/><input type='button' class='delete' onclick='javascript:deleteChild("+'"'+"child"+i+""+'"'+","+ '"'+"inputchild"+i+""+'"' +")'/></div>");    											
        						$("#child_div").append("<div class='ower' id='child"+i+"'><input  type='text' name='inputchild"+i+"' id='inputchild"+i+"' value='"+child_array[i].split("##")[1]+"'/><input type='button' class='delete' onclick='javascript:deleteChild("+'"'+"child"+i+""+'"'+","+ '"'+"inputchild"+i+""+'"' +")'/></div>");    											
        						$("#inputchild"+i).autocomplete(selectRequest,{ mustMatch:true,matchContains:1} );
        						
        					}
        				}else{
        					//$("#child_div").html("<div class='ower' id='child0'><input  onblur='javascript:checkChildParent("+'"'+"inputchild0"+'"'+");' type='text' name='inputchild0' id='inputchild0' value=''/><input type='button' class='delete' onclick='javascript:deleteChild("+'"'+"child0"+'"'+","+'"'+"inputchild0"+'"'+")'/></div>");
        					$("#child_div").html("<div class='ower' id='child0'><input type='text' name='inputchild0' id='inputchild0' value=''/><input type='button' class='delete' onclick='javascript:deleteChild("+'"'+"child0"+'"'+","+'"'+"inputchild0"+'"'+")'/></div>");
        					$("#inputchild0").autocomplete(selectRequest,{ mustMatch:true,matchContains:1} );    										
        				}
        				
        			} 

        			
        		});
        	}

	function addComment() {
		if(checkInterval()) {
			var comment = document.getElementById("comment").value;
			if(comment == ""){
				 alert("Please enter Comment.");
		         return false;
			}
			comment = comment.replaceAll("\n",'<br>');
			comment = comment.replaceAll("\r",'<br>');
			comment = comment.replaceAll("\r\n",'<br>');
			comment = encodeURI(comment);
			comment = comment.replace(/\+/g, "%2B");
			comment = comment.replace(/\&/g, "%26");
			if(!checkCommentLength(comment)) return false;
			var url = "<%=request.getContextPath()%>/servlet/ShowRequestServlet";
			jQuery(function($){
				var requestid = $("#requestid").val();
				$.ajax({
					type: "POST",
					url: url,
					data: "operation=Addcomment&requestid="+requestid+"&username=${userName}&useremail=${userEmail}&comment="+comment + "&actionURL=",
					beforeSend:function(){
						$("#comment_btn").attr("disable","true");	    										
						$(document.body).append(loadingDiv);
					},
					success: function(rtnData) {
						rtnData = eval("(" + rtnData + ")");
						var message = rtnData.message;
						//var historyid = rtnData.historyid;
						if(message != "Add comment success!"){
							$('#loadingDiv').remove(); 
							alert("There have some errors! Please send eamil to "+email+".");
							return;
						}else{
							refresh_tables(url,requestid);
						}
						//toreplace2("addcomment_div");
						$('#loadingDiv').remove(); 
					}											
				});
				$("#comment_btn").attr("disable","true");
			});
	        return;
		}else return;
	}
                 	
           	function commitSignOff(flag,onbehalf_users) {       
               	if(checkInterval()) {                 	
            		jQuery(function($){                        		
                		var judgeSignOff = $("#judgeSignOff").val();
                		if(judgeSignOff == "0"){
                			var comment = document.getElementById("comment").value;
                 		comment = comment.replaceAll("\n",'<br>');
						comment = comment.replaceAll("\r",'<br>');
						comment = comment.replaceAll("\r\n",'<br>');
						comment = encodeURI(comment);
						comment = comment.replace(/\+/g, "%2B");
						comment = comment.replace(/\&/g, "%26");
						if(!checkCommentLength(comment)) return false;
                			var url = "<%=request.getContextPath()%>/servlet/ShowRequestServlet";
                			var requestid = $("#requestid").val();
                			$.ajax({
								type: "POST",
								url: url,
								data: "operation=SignedOrRject"+"&requestid="+requestid+"&onbehalf="+flag+"&username=${userName}&useremail=${userEmail}"+"&comment="+comment + "&type=${tran:getRequestSign('signed')}&actionURL=&onbehalfUsers="+onbehalf_users,
								beforeSend:function(){	    				
									$("#sign_btn").attr("disable","true");		
									$("#sign_onbehalf_btn").attr("disable","true");				
									$(document.body).append(loadingDiv);
								},	    									
								success: function(rtnData) {	    										    										
									rtnData = eval("(" + rtnData + ")");
									var message = rtnData.message;
									if(message != "Sign Off success!"){
										$('#loadingDiv').remove();
										$("#sign_btn").attr("disable","false");
										alert("There have some errors! Please send eamil to "+email+".");
										return;
									}else{
										$("#sign_btn").hide();
										$("#sign_onbehalf_btn").hide();
										$("#reject_btn").hide();
										document.getElementById("judgeSignOff").value = "1";
										refresh_tables(url,requestid);	    											
									}
									
									$('#loadingDiv').remove();
								}
						
                			});
                			$("#sign_btn").attr("disable","false");	
							$("#sign_onbehalf_btn").attr("disable","false");
                     }else if(judgeSignOff == "1"){
						alert('You have already signed this request.');
						return;
                     }
            		});
               	}else return;
           	}
           
                 	function commitReject() {
                     	if(checkInterval()) {
                  		jQuery(function($){
                      		var judgeSignOff = $("#judgeSignOff").val();
                      		if(judgeSignOff == "0"){
                      			var comment = document.getElementById("comment").value;
                       		comment = comment.replaceAll("\n",'<br>');
							comment = comment.replaceAll("\r",'<br>');
							comment = comment.replaceAll("\r\n",'<br>');
							comment = encodeURI(comment);
							comment = comment.replace(/\+/g, "%2B");
							comment = comment.replace(/\&/g, "%26");
							if(!checkCommentLength(comment)) return false;
                      			var url = "<%=request.getContextPath()%>/servlet/ShowRequestServlet";
                      			var requestid = $("#requestid").val();
                      			$.ajax({
  									type: "POST",
  									url: url,
  									data: "operation=SignedOrRject"+"&requestid="+requestid+"&username=${userName}&useremail=${userEmail}"+"&comment="+comment + "&type=${tran:getRequestSign('reject')}&actionURL=",
  									beforeSend:function(){	  
  										$("#reject_btn").attr("disable","true");	  										
  										$(document.body).append(loadingDiv);
  									},	
  									success: function(rtnData) {
  										$("#sign_btn").hide();
 										$("#sign_onbehalf_btn").hide();
										$("#reject_btn").hide();
  										rtnData = eval("(" + rtnData + ")");
  										var message = rtnData.message;
  										//var historyid = rtnData.historyid;
  										if(message != "Reject success!"){
  											$('#loadingDiv').remove();
  											alert("There have some errors! Please send eamil to "+email+".");
  											return;
  										}else{
										refresh_tables(url,requestid);
  											
      									}
  										$('#loadingDiv').remove();
  									}
  									
                      			});
                      			$("#reject_btn").attr("disable","false");
                      			
                           }else if(judgeSignOff == "1"){		                            	
							alert('You have already signed this request.');
							return;
                           }
                  		});
                     	}else return;
                 	}
                 	
                   	jQuery(function($){
                   		$("#canceldetail")
                   		.click(function () {
                   			$("table#detail_name_table").fadeOut("slow");
                   			$("#detail_tbl").fadeOut("slow");
                   			location.href = "";						                   			
                   		})
                   	});
  </script>