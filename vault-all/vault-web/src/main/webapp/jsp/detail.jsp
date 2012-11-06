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
	if(comment.length>65000) {
		alert("Comment is too long.It should not be more than 65000 characters.");
		return false;
	}
	else return true;
}
function checkReplyLength(reply) {
	if(reply.length>65000) {
		alert("Reply is too long.It should not be more than 65000 characters.");
		return false;
	}
	else return true;
}
if("${judgeDetailValue}" == "true"){
	jQuery(function($){
		$("#requestid").val("${detailRequest.requestid}");
		$("#versionid").val("${detailRequest.versionid}");
		$("#judgeSignOff").val("0");
	});
}
function opstatus(status){
	var s="";
	//if(status=='Signed'){
	//	s=status;
	//}
	return s;
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
<input type="hidden" name="versionid" id="versionid" value="-1" />
<table class="eso-table tableWidth table-top detail-table" id="detail_name_table"
	style="display: none">
	<thead>
		<tr>
			<th colspan="5"><c:if
					test="${not empty detailRequest.requestname}"><span id="hiddenRequestName">${detailRequest.requestid}  ${detailRequest.requestname}</span></c:if>
				<form action="<%=request.getContextPath()%>/ReportServlet" method="POST"
					name="formReport" id="formReport" style="display: inline;">
					<a href="javascript:report()" class="report">Report This
						Request</a><a href="#" id="new_child_request" class="add">Create
						Child Request</a><a href="javascript:edit()" class="edite" id="edit_tr">Edit</a></th>
			<input type="hidden" id="requestNameUnescape"
				value="${requestName_unescape}" />
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
		</tr>
	</thead>
	<tbody>
		<tr>
			<td width="11%">Product:</td>
			<td width="32%">${detailRequest.productname}</td>
			<td width="7%" class="bg-gray font">Version:</td>
			<td width="50%">${detailRequest.versiondesc}</td>
		</tr>
		<tr>
			<td>Due Date:</td>
			<td id="requesttime_value">${tran:transformByFormat(detailRequest.requesttime,"yyyy-MM-dd
				HH:mm")}<script>OutputLoc();</script>
			</td>
			<td class="bg-gray font">Create Date:</td>
			<td>${tran:transformByFormat(detailRequest.createdtime,"yyyy-MM-dd
				HH:mm")}<script>OutputLoc();</script>
			</td>
		</tr>
		<tr>
			<td>Creator:</td>
			<td colspan="3">${detailRequest.createdby}</td>
		</tr>
		<tr>
			<td>CC:</td>
			<td colspan="3">${detailRequest.forward}</td>
		</tr>
		<tr>
			<td>Parent:</td>
			<td colspan="3">${tran:getParent(detailRequest.parent,pageContext.request.contextPath)}</td>
		</tr>
		<tr>
			<td>Child:</td>
			<td colspan="3">${tran:getChildren(detailRequest.children,pageContext.request.contextPath)}</td>
		</tr>
		<tr>
			<td>Description:</td>
			<td id="detail_value" colspan="3" class="wordwrap"><c:if test="${not empty detailRequest.detail}">
					<script type="text/javascript">
			       		jQuery(function($){
			       			var detail = "${detailRequest.detail}";
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
				</c:if></td>
		</tr>
		<tr>
			<td >Attachment:</td>
			<td>
				<div id="requestAttachment_value">
					<c:if test="${not empty detailRequest}">
	       				${tran:createFileInfo(detailRequest,pageContext.request.contextPath)}
	       			</c:if>
				</div>
			</td>
			<td class="bg-gray font">Public:</td>
			<td><input type="hidden" name="public_temp" id="public_temp"
				value="0" /> <c:if test="${not empty detailRequest.is_public}">
					<c:choose>
						<c:when test="${detailRequest.is_public eq 1}">
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
				</c:if></td>
		</tr>
	</tbody>
</table>
<table style="width:100%;max-width: 100%;" id="detail_sub_tbl">
	<tr>
		<td colspan="4" style="border: none; height: 30; padding: 0px;"
			id="detail_td">
			<table class="eso-table" id="reception_tbl" style="display: none">
				<c:if test="${not empty detailRequest.requestid}">
					<script type="text/javascript">
	                    	jQuery(function($){
	                    		var url = "<%=request.getContextPath()%>/RequestHistory";
	                    		$.ajax({
	    							type: "POST",
	    							url: url,
	    							data: "requestid=${detailRequest.requestid}&userEmail=${userEmail}&userName=${userName}",
	    							beforeSend:function(){	    										
										$(document.body).append(loadingDiv);
									},
	    							success: function(rtnData) {
	    								rtnData = eval("(" + rtnData + ")");
	    								var historys = rtnData.historys;
	    								var maps = rtnData.requests[0].maps;
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
	    										$("#addcomment_div").append("<div class='comment_list "+n.status+"'><strong class='ncolor'>"+n.editedby+"</strong><span class='time'>"+now+"</span><span class='blu' title='reply' onclick='javascript:showTextArea("+i+",-1,-1)'>Reply</span><div>"+opstatus(n.status)+comment_href+"</div><div id ='input"+i+"_"+"-1' class='comment_input'></div><ul id='reply"+i+"' style='display:none'></ul><input type= 'hidden' id ='historyid"+i+"' value='"+n.historyid+"'/></div></div>");
	    										showReply(i);
	    									});
	    								}
	    								$("#cc_value").html("${detailRequest.forward}");
	    								$("#maxcccount").val("${detailRequest.getCcCount()}");
	    								//$("#maxownercount").val("${detailRequest.getSignatoryCount()}");
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
	    										index++;
	    									
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
	    								
	    								if(maps != null && maps != ""){
	    									$("#signofftd").html('<input type="button" id="sign_addchild_btn" class ="btn off-margin span-top" onclick="javascript:request.req_addOwner()" value="Add More">');
	    									//var count = -1;
	    									var index=0;
	    									var edit_option1Str = "";
	    									var edit_option2Str = "";
	    									var edit_option3Str = "";
	    									$.each(maps, function(i, m){
	    										var tempvalue = m.mapvalue;
	    										if(tempvalue){
	    											var tempearray = tempvalue.split(":");
	    											var tempemail = $.trim(tempearray[0]);
	    											var tempoption = tempearray[1];
	    											
													if(tempoption == 1){
														edit_option1Str += tempemail + ', ';
													}else if(tempoption == 2){
														edit_option2Str += tempemail + ', ';
													}else if(tempoption == 3){
														edit_option3Str += tempemail + ', ';
													};
	    										}
	    										index++;
	    									
	        								});
	        								
	        								var optionIndex = 0;
	        								var firstOption =true;
	        								if(edit_option2Str != ""){
												if(!firstOption){
													request.req_addOwner(2,edit_option2Str,firstOption);
													optionIndex++;
												}else{ 
													request.req_addOwner(2,edit_option2Str,firstOption);
													firstOption = false;
												};
											}
											if(edit_option1Str != ""){
												if(!firstOption){
													request.req_addOwner(1,edit_option1Str,firstOption);
													optionIndex++;
												}else{ 
													request.req_addOwner(1,edit_option1Str,firstOption);
													firstOption = false;
												};
											}
											if(edit_option3Str != ""){
												if(!firstOption){
													request.req_addOwner(3,edit_option3Str,firstOption); 
													optionIndex++;
												}else{
													request.req_addOwner(3,edit_option3Str,firstOption); 
													firstOption = false;
												};
											}
											$("#maxownercount").val(optionIndex);
											$("#ownercount").val(optionIndex);
	    								}
	    								
	    								if (typeof iTable != 'undefined'){
	    									$("#detail_td").html("");
	    									$("#detail_td").append("<table cellpadding='0' cellspacing='0' class='eso-table' width='100%' id='reception_tbl'></table>");
	    								}
	    								iTable = $('#reception_tbl').dataTable({
	    								    "iDisplayLength": 100,
	    									"aaSorting": [[ 1, "desc" ]],
	    									"aaData": data2,
	    									"bAutoWidth": false,
	    									"bFilter": false,
	    									"bInfo": false,
	    									"bLengthChange": false,
	    									"bPaginate": false,
	    									"bSort": false,
	    									"bDestroy":false,
	    									"sPaginationType": "full_numbers",
	    									"aoColumns": [
	    									  { "sTitle": "ID",
	    										"bVisible":false },
	    									  { "sTitle": "Sign&nbsp;Off&nbsp;By",
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
	    										  var sReturn = "<span class='"+status+"m'>"+status+"</span>";
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
			</table>
		</td>
	</tr>
</table>
<table class="eso-table tableWidth" id="detail_comment"
	style="display: none">
	<thead>
		<tr>
			<th colspan="2" class="iconmean">Comment 
              <span class="Rejectedm icon-mean spanright">Rejected</span><span class="SignedByRequestorm icon-mean ">Signed by Requestor</span><span class="Waitingm icon-mean">Waiting</span><span class="Commentsm icon-mean">Comments</span><span class="Signedm icon-mean">Signed</span >
			</th>

		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="background-fff">
			
				<fieldset class="fieldset-vault wordwrap">
					<legend class="legend-vault img1" id="turn">Turn Off
						Comments </legend>
					<legend class="legend-vault img2 display-none" id="turn2">Turn
						On Comments </legend>
					<div class="comments" id="addcomment_div"></div>
				</fieldset>
			</td>
		</tr>
		<tr>
			<td class="new_bg_white" id="comment_td" colspan='2'><textarea
					name="comment" id="comment"></textarea></td>
		</tr>
		<!-- <tr>
			<td class="sing-off-bt"><a href="#"> <input type="hidden"
					name="requestid" id="requestid" value="" />
					<button class="btn btn-primary" onclick="javascript:addComment()">Comment</button>
			</a></td>
		</tr>-->
		<input type="hidden" name="requestid" id="requestid" value="" />
		<tr>
			<th colspan="3">
				<div class="tablefooter">
					<div class="tablefooterleft">
						<input type="hidden" name="judgeSignOff" id="judgeSignOff"
							value="0" /><input type="button" value="Comment" class="btn btn-primary btn-wide" onclick="javascript:addComment()"> <input type="button" value="Sign Off" class="btn btn-wide"
							id="sign_btn" onclick="javascript:commitSignOff('1','')" />
						<!-- <input type="button" value="Sign Off On Behalf Of"  class="btn"  id="sign_onbehalf_btn" onclick="javascript:commitSignOff('2')"/>  -->
						<input type="button" value="Sign Off On Behalf Of" class="btn btn-wide"
							id="sign_onbehalf_btn" onclick="javascript:behalf_click(this)" />
						<input type="button" value="Reject" class="btn btn-wide" id="reject_btn"
							onclick="javascript:commitReject()" /> <input type="button"
							value="Cancel" class="btn btn-wide" id="canceldetail" />
					</div>
					<div class="tablefooterright"></div>
				</div>
			</th>
		</tr>
</table>

<table cellpadding="0" cellspacing="0" class="home_table margin_top"
	id="onbehalf_table" style="display: none">

	<tr>
		<td class="new_bg"><span class="detail_title">On behalf
				of :</span></td>
		<td class="new_bg_white" colspan='2'>
			<div id="onbehalf_div" class="state" ></div> 
		</td>
		<td>
			<input type="button" class="state"
			name="Submit_onbehalf" value="Confirm"
			onclick="javascript:click_onbehalf_confirm()" /> <input
			type="button" name="Cancel_onbehalf" value="Cancel"
			onclick="javascript:click_onbehalf_cancel()" />
		</td>
	</tr>

</table>
<script type="text/javascript">
 	email="vault-dev-list@redhat.com";
           function behalf_click(obj){
        	document.getElementById("sign_onbehalf_btn").style.display="none";
           	//obj.hide();
           	//$("table#onbehalf_table").attr("style","display");
           	document.getElementById("onbehalf_table").style.display = "block";
           	//ajax, get all emails pending for sign off                
			//console.log("ajax start...");
           	jQuery(function($){
        		var url = "<%=request.getContextPath()%>/ListNoneSign";
        		$.ajax({
        			type: "POST",
        			url: url,
        			data: "requestId=${detailRequest.requestid}",
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
					data: "requestid="+requestid+"&userEmail=${userEmail}&userName=${userName}",
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
								$("#addcomment_div").append("<div class='comment_list "+n.status+"'><strong class='ncolor'>"+n.editedby+"</strong><span class='time'>"+now+"</span><span class='blu' title='reply' onclick='javascript:showTextArea("+i+",-1,-1)'>Reply</span><div>"+opstatus(n.status)+comment_href+"</div><div id ='input"+i+"_"+"-1' class='comment_input'></div><ul id='reply"+i+"' style='display:none'></ul><input type= 'hidden' id ='historyid"+i+"' value='"+n.historyid+"'/></div></div>");
								showReply(i);
								});
						}
						document.getElementById("comment").value = "";
						if (typeof iTable != 'undefined'){
							$("#detail_td").html("");
							$("#detail_td").append("<table cellpadding='0' cellspacing='0' class='eso-table' width='100%' id='reception_tbl'></table>");
							//iTable.fnClearTable( 0 );
							//iTable.fnDraw();
						}
						iTable = $('#reception_tbl').dataTable({
						    "iDisplayLength": 100,
							"aaSorting": [[ 1, "desc" ]],
							"aaData": data2,
							"bAutoWidth": false,
							"bDestroy":false,
							"bFilter": false,
							"bPaginate": false,
							"bInfo": false,
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
								  var sReturn = "<span class='"+status+"m'>"+status+"</span>";
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
           
           function showTextArea(i,baseid,j){
             jQuery(function($){
               if(document.getElementById("input"+i+"_"+j).innerHTML == ""){
                 $("#input"+i+"_"+j).append("<textarea name='replyComment"+i+"_"+j+"' id='replyComment"+i+"_"+j+"'></textarea></br><input id='comment_btn ' class='btn clear span-top span-bottom' type='button' value='Submit' onclick='javascript:addReply(&quot;"+i+"_"+j+"&quot;,"+baseid+")'>");
               }else{
                 $("#input"+i+"_"+j).html("");
               }
              });
            }
           
		   function addReply(m,baseid){
		     var replyComment = document.getElementById("replyComment"+m).value;
		     console.log("i ="+m);
		     console.log("replyComment="+replyComment);
		     document.getElementById("replyComment"+m).value = "";
		     i=m.split("_")[0];
		     var historyid = document.getElementById("historyid"+i).value;
		     if(replyComment == ""){
		       alert("Please enter Reply.");
		     }else{
		       if(!checkReplyLength(replyComment)) return false;
		       replyComment = replyComment.replaceAll("\n",'<br>');
		       replyComment = replyComment.replaceAll("\r",'<br>');
		       replyComment = replyComment.replaceAll("\r\n",'<br>');
		       replyComment = encodeURI(replyComment);
		       replyComment = replyComment.replace(/\+/g, "%2B");
		       replyComment = replyComment.replace(/\&/g, "%26");
               jQuery(function($){
                 $("#input"+m).html("");
                 var requestid = $("#requestid").val();
                 var url = "<%=request.getContextPath()%>/AddReply";
                 var addedReplyList = "";
                 var flag = "";
                 $.ajax({
                   type: "POST",
                   url: url,
                   data: "requestid="+requestid+"&editedby=${userName}&replyComment="+replyComment+"&historyid="+historyid+"&baseid="+baseid,
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
    		                 //$("#reply"+i).append("<li id='lireply"+n.replyid+"'><span>"+n.replycomment+"</span><div class='comment_pt'>"+n.editedtime+" "+n.editedby+" Reply<a title='reply' href='javascript:showTextArea("+i+","+n.replyid+")'></a></div></li>");
    		                 //$("#lireply"+n.replyid).append("<ul id='ulreply"+n.replyid+"' style='display:none'></ul>");
    		            	   $("#reply"+i).append("<li id='lireply"+n.replyid+"'><strong class='ncolor'>"+n.editedby+"</strong><span class='time'>"+n.editedtime+"</span><span class='blu' title='reply' onclick='javascript:showTextArea("+i+","+n.replyid+","+j+")'>Reply</span><div>"+opstatus(n.status)+n.replycomment+"</div><div id ='input"+i+"_"+j+"' class='comment_input'></div>");
    	    		           $("#lireply"+n.replyid).append("<ul id='ulreply"+n.replyid+"' style='display:none'></ul>");
    		               }else{
    		                 document.getElementById("ulreply"+n.baseid).style.display="block";
    		                 $("#ulreply"+n.baseid).append("<li id='lireply"+n.replyid+"'><strong class='ncolor'>"+n.editedby+"</strong><span class='time'>"+n.editedtime+"</span><span class='blu' title='reply' onclick='javascript:showTextArea("+i+","+n.replyid+","+j+")'>Reply</span><div>"+opstatus(n.status)+n.replycomment+"</div><div id ='input"+i+"_"+j+"' class='comment_input'></div>");
    		                 $("#lireply"+n.replyid).append("<ul id='ulreply"+n.replyid+"' style='display:none'></ul>");
    		               }
    		             });
    		             toreplace2("reply"+i);
    		           }
    		         }else{
    		        	 alert("There are some errors, please send eamil to "+email+".");
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
               var url = "<%=request.getContextPath()%>/ShowReplyComment";
               $.ajax({
                 type: "POST",
                 url: url,
                 data: "requestid="+requestid+"&historyid="+historyid,
				 success: function(rtnData) {
    			   rtnData = eval("(" + rtnData + ")");
    			   replyList = rtnData.replyList;
                   if(replyList != null && replyList != ""){
                   document.getElementById("reply"+i).style.display="block";
    		         $.each(replyList, function(j, n){
    		           if(n.baseid == -1){
    		             $("#reply"+i).append("<li id='lireply"+n.replyid+"'><strong class='ncolor'>"+n.editedby+"</strong><span class='time'>"+n.editedtime+"</span><span class='blu' title='reply' onclick='javascript:showTextArea("+i+",-1,"+j+")'>Reply</span><div>"+opstatus(n.status)+n.replycomment+"</div><div id ='input"+i+"_"+j+"' class='comment_input'></div>");
    		             $("#lireply"+n.replyid).append("<ul id='ulreply"+n.replyid+"' style='display:none'></ul>");
    		           }else{
    		             document.getElementById("ulreply"+n.baseid).style.display="block";
    		             $("#ulreply"+n.baseid).append("<li id='lireply"+n.replyid+"'><strong class='ncolor'>"+n.editedby+"</strong><span class='time'>"+n.editedtime+"</span><span class='blu' title='reply' onclick='javascript:showTextArea("+i+",-1,"+j+")'>Reply</span><div>"+opstatus(n.status)+n.replycomment+"</div><div id ='input"+i+"_"+j+"' class='comment_input'></div>");
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
        			$("#subMenu").html("Edit Request " + $("#requestid").val());

        			var url = "<%=request.getContextPath()%>/editVersion";
        			$("table#newrequest_tbl").attr("style","display");
        			$("table#myrequest").hide();
        			$("table#allrequest").hide();
        			$("table#waitrequest").hide();
        			$("table#signedrequest").hide();
        			$("table#canviewrequest").hide();
        			$("table#detail_name_table").hide();
        			$("table#detail_tbl").hide();
        			$("table#detail_sub_tbl").hide();
        			$("table#detail_comment").hide();
        			$("table#onbehalf_table").hide();
        			
        			$("#div_name").html("Edit Request");
        			
        			var requestid = $("#requestid").val();
        			
        			$("#newrequestid").val(requestid);
        			var request_name = document.getElementById("requestNameUnescape").value;
        			//document.getElementById("requestname").value = request_name.substring(request_name.indexOf("  ")+2);
        			//$("#requestname").attr("value",request_name.substring(request_name.indexOf("  ")+2));
        			$("#requestname").attr("value",request_name);
        			
        			var versionid = $("#versionid").val();
        			$("#selectversionid").attr("value",versionid);
        			if(versionid == "-1"){
        				$("#request_selectPro option[value=-1]").attr("selected", true);
        				$("#request_selectVersion").html("<option value='-1'>------------</option>");
        				$("#request_selectVersion option[value=-1]").attr("selected", true);
        			}else{
        				$.ajax({
        					type: "POST",
        					url: url,
        					data: "id="+versionid,
        					success: function(rtnData) {
        						rtnData = eval("(" + rtnData + ")");
        						var productid = rtnData.productid;
        						var sboption = rtnData.sboption;
        						$("#selectproductid").attr("value",productid);
        						$("#request_selectPro option[value="+productid+"]").attr("selected",true);
        						$("#request_selectVersion").html(sboption)
        						.fadeIn("slow");
        					}
        				});
        			}
        			var timeTemp = $("#requesttime_value").html().Trim();
        			timeTemp = timeTemp.substring(0,timeTemp.indexOf("<"));
        			//document.getElementById("requesttime").value = timeTemp.split(" ")[0];
        			document.getElementById("requesttime").value = timeTemp.substring(0,10);
        			//change to AM or PM
            		//var allhour=timeTemp.split(" ")[1];
            		var allhour=$.trim(timeTemp.substr(10));
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
        			url = "VaultFileUpload";
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

        			setUserAndRequest();
        			$("#cc_div").html("");
        			if(globalRequests != null && globalRequests != ""){
        				var cc_str = globalRequests[0].forward;
        				if(cc_str != null && cc_str != ""){
        					cc_str = cc_str.replaceAll('<br>','');
        					$("#input_cc").val(cc_str);
        				}
        			}
        			
        			if(globalRequests[0].parent == ""){
        				$("#input_parent").val("");
        			}else{
        				$("#input_parent").val(globalRequests[0].parent.split("##")[1]);
        			}								
        			
        			//child list
        			$("#childtd").html('<input type="button" id="addchild_btn" class ="btn off-margin span-top" onclick="javascript:request.req_addChild()" value="Add More">');
        			$("#maxchildcount").val(0);
        			if(globalRequests != null && globalRequests != ""){
        				var child_str = globalRequests[0].children;
        				if(child_str != null && child_str != ""){
        					var child_array = child_str.split(",");
        					for (var i=0;i<child_array.length;i++){								
        			            $('<input type="text" class="input-xlarge" style="margin-top:4px;" id="input_child_' + i + '" value="'+ child_array[i].split("##")[1] +'"><span id="input_child_' + i + '_del" class="delate-table" onclick=javascript:request.req_delChild("input_child_' + i + '")></span></br>').insertBefore($("#addchild_btn"));
        						$("#input_child_"+i).autocomplete(request.selectRequest,{ mustMatch:true,matchContains:1} );
        					}
        					$("#maxchildcount").val(child_array.length - 1);
        				}else{
							$('<input type="text" class="input-xlarge" id="input_child_0"><span class="delate-table"></span></br>').insertBefore($("#addchild_btn"));
        					$("#input_child_0").autocomplete(request.selectRequest,{ mustMatch:true,matchContains:1} );    										
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
			if(!checkCommentLength(comment)) return false;
			comment = comment.replaceAll("\n",'<br>');
			comment = comment.replaceAll("\r",'<br>');
			comment = comment.replaceAll("\r\n",'<br>');
			comment = encodeURI(comment);
			comment = comment.replace(/\+/g, "%2B");
			comment = comment.replace(/\&/g, "%26");
			var url = "<%=request.getContextPath()%>/Addcomment";
			jQuery(function($){
				var requestid = $("#requestid").val();
				$.ajax({
					type: "POST",
					url: url,
					data: "requestid="+requestid+"&username=${userName}&useremail=${userEmail}&comment="+comment + "&actionURL=",
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
							alert("There are some errors, please send eamil to "+email+".");
							return;
						}else{
							refresh_tables("<%=request.getContextPath()%>/RequestHistory",requestid);
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
                			if(!checkCommentLength(comment)) return false;
                 			comment = comment.replaceAll("\n",'<br>');
							comment = comment.replaceAll("\r",'<br>');
							comment = comment.replaceAll("\r\n",'<br>');
							comment = encodeURI(comment);
							comment = comment.replace(/\+/g, "%2B");
							comment = comment.replace(/\&/g, "%26");
                			var url = "<%=request.getContextPath()%>/SignedOrRject";
                			var requestid = $("#requestid").val();
                			$.ajax({
								type: "POST",
								url: url,
								data: "requestid="+requestid+"&onbehalf="+flag+"&username=${userName}&useremail=${userEmail}"+"&comment="+comment + "&type=${tran:getRequestSign('signed')}&actionURL=&onbehalfUsers="+onbehalf_users,
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
										alert("There are some errors, please send eamil to "+email+".");
										return;
									}else{
										if(rtnData.isSignatory){
											$("#sign_btn").show();
											$("#reject_btn").show();
										}else{
											$("#sign_btn").hide();
											$("#reject_btn").hide();
										}
										if(rtnData.displaySignoffOnBehalf){
											$("#sign_onbehalf_btn").show();
										}else{
											$("#sign_onbehalf_btn").hide();	
										}
										
										//document.getElementById("judgeSignOff").value = "1";
										refresh_tables("<%=request.getContextPath()%>/RequestHistory",requestid);    											
									}
									
									$('#loadingDiv').remove();
								}
						
                			});
                			//$("#sign_btn").attr("disable","false");	
							//$("#sign_onbehalf_btn").attr("disable","false");
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
                      			if(!checkCommentLength(comment)) return false;
                       			comment = comment.replaceAll("\n",'<br>');
								comment = comment.replaceAll("\r",'<br>');
								comment = comment.replaceAll("\r\n",'<br>');
								comment = encodeURI(comment);
								comment = comment.replace(/\+/g, "%2B");
								comment = comment.replace(/\&/g, "%26");
							
                      			var url = "<%=request.getContextPath()%>/SignedOrRject";
                      			var requestid = $("#requestid").val();
                      			$.ajax({
  									type: "POST",
  									url: url,
  									data: "requestid="+requestid+"&username=${userName}&useremail=${userEmail}"+"&comment="+comment + "&type=${tran:getRequestSign('reject')}&actionURL=",
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
  											alert("There are some errors, please send eamil to "+email+".");
  											return;
  										}else{
  											refresh_tables("<%=request.getContextPath()%>/RequestHistory",requestid);
  											
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
