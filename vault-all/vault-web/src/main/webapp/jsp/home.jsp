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
		jQuery(function($){
			if("${is_search}" != "" || "${queryName}" != ""){
				$("table#searchrequest").attr("style","display");
				$("table#allrequest").hide();
				$("table#myrequest").hide();
				$("table#waitrequest").hide();
				$("table#signedrequest").hide();
				$("table#canviewrequest").hide();
				$("table#cctomerequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
			}
			else 
				if("${judgeDetailValue}" == "true"){
				$("table#detail_name_table").attr("style","display");
                $("table#reception_tbl").attr("style","display");
				$("table#detail_comment").attr("style","display");
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
			}
			else if("${operationstatus}" != "null" && "${operationstatus}" == "wait"){
				$("table#waitrequest").attr("style","display");
				$("table#myrequest").hide();
				$("table#cctomerequest").hide();
				$("table#allrequest").hide();
				$("table#searchrequest").hide();
				$("table#signedrequest").hide();
				$("table#canviewrequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
			}
			else if("${operationstatus}" != "null" && "${operationstatus}" == "signed"){
				$("table#signedrequest").attr("style","display");
				$("table#cctomerequest").hide();
				$("table#myrequest").hide();
				$("table#allrequest").hide();
				$("table#searchrequest").hide();
				$("table#canviewrequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#waitrequest").hide();
			}
			else if("${operationstatus}" != "null" && "${operationstatus}" == "CanView"){
				$("table#canviewrequest").attr("style","display");
				$("table#cctomerequest").hide();
				$("table#myrequest").hide();
				$("table#allrequest").hide();
				$("table#searchrequest").hide();
				$("table#signedrequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#waitrequest").hide();
			}
			else if("${operationstatus}" != "null" && "${operationstatus}" == "cctome"){
				$("table#cctomerequest").attr("style","display");
				$("table#canviewrequest").hide();
				$("table#myrequest").hide();
				$("table#allrequest").hide();
				$("table#searchrequest").hide();
				$("table#signedrequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#waitrequest").hide();
			}
			else if("${operationstatus}" != "null" && "${operationstatus}" == "myrequest"){
				$("table#myrequest").attr("style","display");
				$("table#canviewrequest").hide();
				$("table#cctomerequest").hide();
				$("table#allrequest").hide();
				$("table#searchrequest").hide();
				$("table#signedrequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
				$("table#waitrequest").hide();
			}
			else if("${operationstatus}" != "" && "${operationstatus}" == "newrequest"){
				$("table#newrequest_tbl").attr("style","display");
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
			}
			else {
				$("table#myrequest").attr("style","display");
				$("table#waitrequest").attr("style","display");
				$("table#canviewrequest").hide();
				$("table#cctomerequest").hide();
				$("table#searchrequest").hide();
				$("table#allrequest").hide();
				$("table#signedrequest").hide();
				$("table#newrequest_tbl").hide();
				$("table#detail_name_table").hide();
				$("table#detail_tbl").hide();
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
					success: function(rtnData) {
						rtnData = eval("(" + rtnData + ")");
						selectRequest = rtnData.rqsts;
					}
				});
				var url2 = "${pageContext.request.contextPath}/ShowAllEmails";
				$.ajax({
					type: "POST",
					url: url2,
					async:false,
					success: function(rtnData) {
						rtnData = eval("(" + rtnData + ")");
						selectUser = rtnData.emails;
					}
				});
			});
		}
		var loadingDiv = $('<div id="loadingDiv" style="position:fixed; left:'+window.screen.width/2+'px; top:'+window.screen.height/2+'px;"></div>').html("<img src='${pageContext.request.contextPath}/images/loading.gif' /> ");
		console.info('loading div');
		$(document.body).append(loadingDiv);
	</script>
	<script type='text/javascript' src="<%=request.getContextPath()%>/js/vault_js.js"></script> 
	<script type='text/javascript' src="<%=request.getContextPath()%>/js/timezone.js"></script>
</head>
<body id='eso-body' onload="rmloading();">
<div class="eso-inner">
<header id='eso-topbar'>
  <a href="index.html" title="go back home" class="logo">Vault</a>
  <a href="https://engineering.redhat.com/hss-portal" class="eso-logo"><img src="<%=request.getContextPath()%>/images/header-logo-eso-developed.png" alt="Developed by HSS"></a>
  <ul class="quick-menu unstyled">
  <li class="dropdown">
    <a href="https://engineering.redhat.com/hss-portal/products/" title="Engineering Services">Engineering Services</a>
  </li>
  <li class="dropdown header-help">
    <a class="dropdown-toggle" data-toggle="dropdown" href="#" title="User Guide">Help<b class="caret"></b></a>
      <ul class="dropdown-menu">
        <li><a href="aboutus.html">User guide</a></li>
        <li class="divider"></li>
        <li><a href="mailto:hss-eip@redhat.com">EIP Request</a></li>
        <li><a href="mailto:eng-ops@redhat.com">Eng-ops Request</a></li>
        <li><a href="mailto:hss-ied-list@redhat.com">Application Request</a></li>
        <li class="divider"></li>
        <li><a href="mailto:hss-ied-list@redhat.com">Contact developers</a></li>
      </ul>
    </li>
 <li class="dropdown header-user">
   <a class="dropdown-toggle" data-toggle="dropdown" href="#" title="User Info">erousseau<b class="caret"></b></a>
    <ul class="dropdown-menu">
        <li><a href="#">Profile</a></li>
    </ul>
    </li>
 </ul>

</header>

<div class="navbar">
  <ul class="nav">
    <li class="active"><a href="index.html">Home</a></li>
   <li class=""><a href=${pageContext.request.contextPath}/listRequest?operation=NewRequest >New Request </a></li>
    <li class=""><a href=${pageContext.request.contextPath}/listRequest?operation=WaitRequest>Needing My Approval<span class="number"> ( 3 )</span></a></li>
    <li class=""><a href=${pageContext.request.contextPath}/listRequest?operation=MyRequest>My Requests<span class="number"> ( 10 )</span></a></li>
    <li class=""><a href=${pageContext.request.contextPath}/listRequest?operation=SignedRequest>Have Signed <span class="number">( 8 )</span></a></li>
    <li class=""><a href=${pageContext.request.contextPath}/listRequest?operation=CCToMeRequest>CC Me<span class="number"> ( 100 )</span></a></li>
    <li class=""><a href=${pageContext.request.contextPath}/listRequest?operation=CanViewRequest>I Can View <span class="number">( 568 )</span></a></li>
  </ul>
</div>
<div class="content">
   
<div class="breadcrumb">
    <ul class="pull-left">
        <li><a href="index.html">Home</a> <span class="divider">/</span></li>
    </ul>
        
    
     <div class="clear"></div>
</div>
	<%@ include file="detail.jsp"%>  
        <%@ include file="search.jsp"%>
	<%@ include file="needApproval.jsp"%>
	<%@ include file="signed.jsp" %>
	<%@ include file="viewrequest.jsp" %>
	<%@ include file="newrequest.jsp" %>
	<%@ include file="searchrequest.jsp" %>
	<%@ include file="cctomerequest.jsp" %>
	<%@ include file="myrequest.jsp" %>
</div>
<footer class="footer">
    <div class="hss-logo"></div>
    <div class='copyright'>
    <p>Vault 3.0 <a href="#"> Report an Issue</a></p>
    <p>Copyright Â© 2012 Red Hat, Inc. All rights reserved.</p>
    <p>INTERNAL USE ONLY</p>
    <!--when the application use some tech supported by opensource, then please mark it here><p>Powered by <a href='http://www.redhat.com/products/jbossenterprisemiddleware/portal/'>@JBoss EAP</a></p><-->
    </div>
</footer>
</div>
<script>
	//$(".chzn-select").chosen(); 
	//$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
</script>	
    </div>
 </body>
</html>
