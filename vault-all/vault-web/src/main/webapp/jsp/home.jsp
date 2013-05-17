<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="tran" uri="/wezhao/tran" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>vault</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/datatable.css" />
	 <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/eso-theme.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/vault.css" /> 
	
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/ui.core.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/ui.datepicker.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/jquery.ptTimeSelect.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/ui.theme.css" />
		
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/jquery.autocomplete.css" />
	
	<link href="<%=request.getContextPath()%>/images/favicon.ico" rel="shortcut icon" />
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.2.min.js" ></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ptTimeSelect.js" ></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/dropdown.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/modal.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/datatable.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/vault.js"></script>
	
	<script>
		var loc;
		var loc_timezoneOffset=new Date();
		var loc_ts=loc_timezoneOffset.toString();
		var loc_patern=/^(.+) (GMT.+)$/;
		var match=loc_ts.match(loc_patern);
		loc=match[2];
		function OutputLoc(){
			document.write(loc);
		}
		var currentMenu = "navHome";
		jQuery(function($){
			if("${is_search}" != "" || "${queryName}" != ""){
				$("table#allrequest").hide();
				$("table#myrequest").hide();
				$("table#waitrequest").hide();
				$("table#signedrequest").hide();
				$("table#canviewrequest").hide();
				$("table#cctomerequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#onbehalf_table").hide();
				$("table#searchrequest").attr("style","display");
			}
			else 
				if("${judgeDetailValue}" == "true"){
				$("table#searchrequest").hide();
				$("table#myrequest").hide();
				$("table#cctomerequest").hide();
				$("table#allrequest").hide();
				$("table#waitrequest").hide();
				$("table#signedrequest").hide();
				$("table#canviewrequest").hide();
				$("table#newrequest_tbl").hide();
				$("div#query-area").hide();
				$("div#advance_query").hide();
				$("fieldset#advance_set").hide();
				$("table#onbehalf_table").hide();
				$("table#detail_name_table").attr("style","display");
                $("table#reception_tbl").attr("style","display");
				$("table#detail_comment").attr("style","display");

				$("#subMenu").html("<a href=${pageContext.request.contextPath}/showRequest/${detailRequest.requestid}>Request Detail ${detailRequest.requestid}</a>");
				
			}
			else if("${operationstatus}" != "null" && "${operationstatus}" == "wait"){
				$("table#myrequest").hide();
				$("table#cctomerequest").hide();
				$("table#allrequest").hide();
				$("table#searchrequest").hide();
				$("table#signedrequest").hide();
				$("table#canviewrequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#onbehalf_table").hide();
				$("table#waitrequest").attr("style","display");

				$("#subMenu").html("<a href=${pageContext.request.contextPath}/listRequest?operation=WaitRequest>Needing My Approval</a>");
				$("#" + currentMenu).removeClass("active");
				$("#navWait").addClass("active");
				currentMenu = "navWait";
			}
			else if("${operationstatus}" != "null" && "${operationstatus}" == "signed"){
				$("table#cctomerequest").hide();
				$("table#myrequest").hide();
				$("table#allrequest").hide();
				$("table#searchrequest").hide();
				$("table#canviewrequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#waitrequest").hide();
				$("table#onbehalf_table").hide();
				$("table#signedrequest").attr("style","display");

				$("#subMenu").html("<a href=${pageContext.request.contextPath}/listRequest?operation=SignedRequest>Have Signed </a>");
				$("#" + currentMenu).removeClass("active");
				$("#navSign").addClass("active");
				currentMenu = "navSign";
			}
			else if("${operationstatus}" != "null" && "${operationstatus}" == "CanView"){
				$("table#cctomerequest").hide();
				$("table#myrequest").hide();
				$("table#allrequest").hide();
				$("table#searchrequest").hide();
				$("table#signedrequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#waitrequest").hide();
				$("table#onbehalf_table").hide();
				$("table#canviewrequest").attr("style","display");
				

				$("#subMenu").html("<a href=${pageContext.request.contextPath}/listRequest?operation=CanViewRequest>I Can View </a>");
				$("#" + currentMenu).removeClass("active");
				$("#navView").addClass("active");
				currentMenu = "navView";
			}
			else if("${operationstatus}" != "null" && "${operationstatus}" == "cctome"){
				$("table#canviewrequest").hide();
				$("table#myrequest").hide();
				$("table#allrequest").hide();
				$("table#searchrequest").hide();
				$("table#signedrequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#waitrequest").hide();
				$("table#onbehalf_table").hide();
				$("table#cctomerequest").attr("style","display");

				$("#subMenu").html("<a href=${pageContext.request.contextPath}/listRequest?operation=CCToMeRequest>CC Me </a>");
				$("#" + currentMenu).removeClass("active");
				$("#navCC").addClass("active");
				currentMenu = "navCC";
			}
			else if("${operationstatus}" != "null" && "${operationstatus}" == "myrequest"){
				$("table#canviewrequest").hide();
				$("table#cctomerequest").hide();
				$("table#allrequest").hide();
				$("table#searchrequest").hide();
				$("table#signedrequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#waitrequest").hide();
				$("table#onbehalf_table").hide();
				$("table#myrequest").attr("style","display");

				$("#subMenu").html("<a href=${pageContext.request.contextPath}/listRequest?operation=MyRequest>My Requests </a>");

				$("#" + currentMenu).removeClass("active");
				$("#navMy").addClass("active");
				currentMenu = "navMy";
			}
			else if("${operationstatus}" != "" && "${operationstatus}" == "newrequest"){
				$("table#canviewrequest").hide();
				$("table#myrequest").hide();
				$("table#allrequest").hide();
				$("table#searchrequest").hide();
				$("table#signedrequest").hide();
				$("table#cctomerequest").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#waitrequest").hide();
				$("div#query-area").hide();
				$("div#advance_query").hide();
				$("fieldset#advance_set").hide();
				$("table#onbehalf_table").hide();
				$("table#newrequest_tbl").attr("style","display");

				$("#subMenu").html("<a href=${pageContext.request.contextPath}/listRequest?operation=NewRequest >New Request </a>");
				$("#" + currentMenu).removeClass("active");
				$("#navNew").addClass("active");
				currentMenu = "navNew";
				
			}
			else if("${operationstatus}" != "" && "${operationstatus}" == "staticsrequest"){
				$("table#canviewrequest").hide();
				$("table#myrequest").hide();
				$("table#allrequest").hide();
				$("table#searchrequest").hide();
				$("table#signedrequest").hide();
				$("table#cctomerequest").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#waitrequest").hide();
				$("div#query-area").hide();
				$("div#advance_query").hide();
				$("fieldset#advance_set").hide();
				$("table#onbehalf_table").hide();
				$("table#newrequest_tbl").hide();
				$("div#statics").attr("style","display");
				
				$("#subMenu").html("<a href=${pageContext.request.contextPath}/listRequest?operation=StaticsRequest >Statics</a>");
				$("#" + currentMenu).removeClass("active");
				$("#navStatics").addClass("active");
				currentMenu = "navStatics";
				
			}
			else {
				$("table#canviewrequest").hide();
				$("table#cctomerequest").hide();
				$("table#searchrequest").hide();
				$("table#allrequest").hide();
				$("table#signedrequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#onbehalf_table").hide();
				$("table#myrequest").attr("style","display");
				$("table#waitrequest").attr("style","display");
			}
		});
		var selectRequest,selectUser;
		function setUserAndRequest()
		{
			jQuery(function($){
				var url = "${pageContext.request.contextPath}/FindRequest";
				$.ajax({
					type: "POST",
					url: url,
					async:false,
					dataType:'json',
					success: function(rtnData) {
						selectRequest = rtnData.rqsts;
					}
				});
				var url2 = "${pageContext.request.contextPath}/ShowAllEmails";
				$.ajax({
					type: "POST",
					url: url2,
					async:false,
					dataType:'json',
					success: function(rtnData) {
						selectUser = rtnData.emails;
					}
				});
			});
		}
		var loadingDiv = $('<div id="loadingDiv" style="position:fixed; left:'+window.screen.width/2+'px; top:'+window.screen.height/2+'px;"></div>').html("<img src='${pageContext.request.contextPath}/images/loading.gif' /> ");
		//console.info('loading div');
		$(document.body).append(loadingDiv);
		function addStyle(){
			var ccstyle=document.getElementById('ccstyle');
			if(ccstyle!=""){
				var ccstyle_val=ccstyle.innerHTML;
				if(ccstyle_val.trim()!=""){
					var arr=ccstyle_val.split(',');
					var all="";
					for(var i=0;i<arr.length;i++){
						if(i==0){
							all+=arr[i]+",";
						}else if(i==arr.length-1){
							all+="<span class='cc_style'>"+arr[i]+"</span>";
						}else{
							all+="<span class='cc_style'>"+arr[i]+",</span>";
						}
					}
					document.getElementById('ccstyle').innerHTML=all;
				}
			}
		}
	</script>
	<script type='text/javascript' src="<%=request.getContextPath()%>/js/vault_js.js"></script> 
	<script type='text/javascript' src="<%=request.getContextPath()%>/js/timezone.js"></script>
	<script type='text/javascript' src="<%=request.getContextPath()%>/js/browserdetector.js"></script>
	<script type="text/javascript">
		function setWidth(){
			var wid=Math.ceil(document.body.clientWidth*91/100);
			var comment=document.getElementById('addcomment_div');
			comment.setAttribute("style","width:"+wid+"px");
		}
		window.onresize=function(){
			setWidth();	
		}
	</script>
</head>
<body id='eso-body' onload="rmloading();BrowserDetection.init();addStyle();setWidth();">
<div class="eso-inner">
<a href="<%=request.getContextPath()%>/jsp/warn.jsp" class="warn-index" style="display: none" id="warning">Your browser is not supported</a>
<%@ include file="header.jsp" %>
<div class="navbar">
  <ul class="nav">
    <li id="navHome" class="active"><a href=${pageContext.request.contextPath}/HomeServlet>Home</a></li>
    <li id="navNew" class=""><a href=${pageContext.request.contextPath}/listRequest?operation=NewRequest >New Request </a></li>
    <li id="navWait" class=""><a href=${pageContext.request.contextPath}/listRequest?operation=WaitRequest>Needing My Approval<span class="number"> ( ${reqCounts.waiting} )</span></a></li>
    <li id="navMy" class=""><a href=${pageContext.request.contextPath}/listRequest?operation=MyRequest>My Requests<span class="number"> ( ${reqCounts.myrequest} )</span></a></li>
    <li id="navSign" class=""><a href=${pageContext.request.contextPath}/listRequest?operation=SignedRequest>Have Signed <span class="number">( ${reqCounts.signed} )</span></a></li>
    <li id="navCC" class=""><a href=${pageContext.request.contextPath}/listRequest?operation=CCToMeRequest>CC Me<span class="number"> ( ${reqCounts.cc} )</span></a></li>
    <li id="navView" class=""><a href=${pageContext.request.contextPath}/listRequest?operation=CanViewRequest>I Can View <span class="number">( ${reqCounts.canview} )</span></a></li>
    <li id="navStatics" class=""><a href=${pageContext.request.contextPath}/listRequest?operation=StaticsRequest>Statics </a></li>
  </ul>
</div>
<div class="content">
   
<div class="breadcrumb">
    <ul id="breadcrumbul" class="pull-left">
        <li><a href=${pageContext.request.contextPath}/HomeServlet>Home</a> <span class="divider">/</span> <span id="subMenu"></span></li>
    </ul>
    <span class="pull-right time-zone"> TimeZone: <script>OutputLoc();</script></span>   
    <div class="clear"></div>
</div>
	<%@ include file="detail.jsp"%>  
        <%@ include file="search.jsp"%>
	<%@ include file="needApproval.jsp"%>
	<%@ include file="signed.jsp" %>
	<%@ include file="viewrequest.jsp" %>
	<%@ include file="searchrequest.jsp" %>
	<%@ include file="cctomerequest.jsp" %>
	<%@ include file="myrequest.jsp" %>
	<%@ include file="newrequest.jsp" %>
	<%@ include file="statics.jsp" %>
</div>
<%@ include file="footer.jsp" %>
</div>
<script>
	//$(".chzn-select").chosen(); 
	//$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
	adaptTimeZone();
	javascript:scroll(0,0);
</script>
    </div>
 </body>
</html>
