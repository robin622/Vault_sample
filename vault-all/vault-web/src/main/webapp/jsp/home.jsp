<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>vault</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/datatable.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/eso-theme.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/vault.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/jquery-ui-1.8.20.custom.css" />
	<link href="<%=request.getContextPath()%>/images/favicon.ico" rel="shortcut icon" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.2.min.js" ></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/dropdown.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/modal.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/datatable.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/vault.js"></script>
</head>
<body id='eso-body'>
<div class="server"><div>stage server</div></div>
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
   <li class=""><a href="new request.html" >New Request </a></li>
    <li class=""><a href="needing my approval.html">Needing My Approval<span class="number"> ( 3 )</span></a></li>
    <li class=""><a href="my request.html">My Requests<span class="number"> ( 10 )</span></a></li>
    <li class=""><a href="have signed.html">Have Signed <span class="number">( 8 )</span></a></li>
    <li class=""><a href="cc me.html">CC Me<span class="number"> ( 100 )</span></a></li>
    <li class=""><a href="i can view.html">I Can View <span class="number">( 568 )</span></a></li>
  </ul>
    
</div>
<div class="content">
   
<div class="breadcrumb">
    <ul class="pull-left">
        <li><a href="index.html">Home</a> <span class="divider">/</span></li>
    </ul>
        
    
     <div class="clear"></div>
</div>
	
    <div>
			<form class="form-horizontal form-horizontal-vault ">
            <div class="control-group">
               
                <div class="controls search-vault">
                  <input type="text" class="input-xlarge" id="input">
                  <button class="btn">Search</button>
                  <a href="#" class="quary">newquery<img src=" <%=request.getContextPath()%>/images/delete.gif"  alt=""></a>
                  <a href="#" class="quary">newquery<img src=" <%=request.getContextPath()%>/images/delete.gif"  alt=""></a>
                  <a href="#" class="quary">newquery<img src=" <%=request.getContextPath()%>/images/delete.gif"  alt=""></a>
                  <a href="#" class="quary">newquery<img src=" <%=request.getContextPath()%>/images/delete.gif"  alt=""></a>
                  <span class="quary-more"  id="quary-js">
                  		<a href="#" class="quary">newquery<img src=" <%=request.getContextPath()%>/images/delete.gif"  alt=""></a>
                  		<a href="#" class="quary">newquery<img src=" <%=request.getContextPath()%>/images/delete.gif"  alt=""></a>
                  </span>
                  <a href="#" class="quary" id="more">more...</a>
                  <a href="#" class="quary display-none" id="hide">hide...</a>
                </div>
            </div>
        </form>
        <div class="clear"></div>
	</div>
    <fieldset class="fieldset-vault fieldset-search">
    <legend class="legend-vault img2" id="search1">Advanced Search </legend>
    <legend class="legend-vault img1 display-none" id="search2" >Advanced Search </legend>
    </fieldset>
	<div class="form-area display-none">
    <div class="form-area-left">
        <form class="form-horizontal form-horizontal-vault">
            <div class="control-group">
                <label class="control-label" for="input01">Request Name:</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="input">
                </div>
            </div>
        </form>
         <form class="form-horizontal form-horizontal-vault">
            <div class="control-group">
                <label class="control-label" for="input01">Product:</label>
                <div class="controls">
                <select data-placeholder="Your Favorite Football Team"  class="chzn-select" tabindex="6">
             <option value=""></option>
               <option>Dallas Cowboys</option>
               <option>New York Giants</option>
               <option>Philadelphia Eagles</option>
               <option>Washington Redskins</option>
           </select>
                </div>
            </div>
        </form>
        <form class="form-horizontal form-horizontal-vault">
            <div class="control-group">
                <label class="control-label" for="input01">Creator:</label>
                  <div class="controls">
             
           <input type="text" class="input-xlarge" id="input01">
           </div>
            </div>
        </form>
       
    </div>
    <div class="form-area-right">
        <form class="form-horizontal">
              <div class="control-group">
                <label class="control-label">Status: </label>
                <div class="controls">
                <select data-placeholder="RHEL6"  class="chzn-select select-width" tabindex="6">
                <option value=""></option>
                <option>RHEL6</option>
                <option>RHEL7</option>
                <option>JBoss</option>
                <option>Openshift</option>
                </select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">Sign off by: </label>
                <div class="controls">
               <input type="text" class="input-xlarge" id="input01">
                </div>
            </div>
          
        
        </form>
    </div>
    <div class="bottom">
    <button type="submit" class="btn btn-primary">Search</button>
    <a class="btn" data-toggle="modal" href="#myModal" >Save Query</a>
  
	<div class="page-header">
    </div>
    <div class="row-fluid">
    	<div class="span8">
            <div class="modal hide fade" id="myModal">
                <div class="modal-header">
                  <button data-dismiss="modal" class="close">Ã</button>
                  <h3>Please input query name</h3>
                </div>
                <div class="modal-body">
                	<p><input type="text" class="input-xlarge" id="input01"></p>
                </div>
                <div class="modal-footer">
                  <a data-dismiss="modal" class="btn" href="#">Close</a>
                  <a class="btn btn-primary" href="#">Save</a>
                </div>
          </div>
           
      </div>
     </div>
    </div>
<div class="clear"></div>  
</div>
	<table class="eso-table" id="table1">
	  <thead>
	    <tr>
      		<th>Requests Needing My Approval</th>
        </tr>
      </thead>
	  <tbody>
	    <tr>
	      <td class="clear-padding">
	        
	        
	        <div class="list-margin-top">
	          <table id="example" class="eso-table table-striped eso-table-inner ">
<thead>
    <tr>        
        <th>ID</th>
        <th>Request Name</th>
         <th>Product</th>
        <th>Vertion</th>
        <th>Creator</th>
        <th>Created date</th>
        <th>	Due date</th>
        <th>Last modified</th>
         <th>Modified date</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
</thead>
<tbody>
    <tr>        
    <td><a href="request_details.html">6427</a></td>
    <td><a href="request_details.html">test child</a></td>
    <td>Unspecified</td>
    <td>Unspecified</td>
    <td> 	xiaowang</td>
    <td>2012-08-23 10:43</td>
    <td>2012-08-23 10:43</td>
    <td>xiaowang</td>
    <td>2012-08-23 13:48</a></td>
    <td> 	Approved</td>
    <td><a href="#" id="delete" ></a></td>
    </tr>
    <tr>        
    <td><a href="request_details.html">6427</a></td>
    <td><a href="request_details.html">test child</a></td>
    <td>Unspecified</td>
    <td>Unspecified</td>
    <td> 	xiaowang</td>
    <td>2012-08-23 10:43</td>
    <td>2012-08-23 10:43</td>
    <td>xiaowang</td>
    <td>2012-08-23 13:48</a></td>
    <td> 	Approved</td>
    <td><a href="#" id="delete" ></a></td>
    </tr>
    <tr>        
    <td><a href="request_details.html">6427</a></td>
    <td><a href="request_details.html">test child</a></td>
    <td>Unspecified</td>
    <td>Unspecified</td>
    <td> 	xiaowang</td>
    <td>2012-08-23 10:43</td>
    <td>2012-08-23 10:43</td>
    <td>xiaowang</td>
    <td>2012-08-23 13:48</a></td>
    <td> 	Approved</td>
    <td><a href="#" id="delete" ></a></td>
    </tr>
    <tr>        
    <td><a href="request_details.html">6427</a></td>
    <td><a href="request_details.html">test child</a></td>
    <td>Unspecified</td>
    <td>Unspecified</td>
    <td> 	xiaowang</td>
    <td>2012-08-23 10:43</td>
    <td>2012-08-23 10:43</td>
    <td>xiaowang</td>
    <td>2012-08-23 13:48</a></td>
    <td> 	Approved</td>
    <td><a href="#" id="delete" ></a></td>
    </tr>
    <tr>        
    <td><a href="request_details.html">6427</a></td>
    <td><a href="request_details.html">test child</a></td>
    <td>Unspecified</td>
    <td>Unspecified</td>
    <td> 	xiaowang</td>
    <td>2012-08-23 10:43</td>
    <td>2012-08-23 10:43</td>
    <td>xiaowang</td>
    <td>2012-08-23 13:48</a></td>
    <td> 	Approved</td>
    <td><a href="#" id="delete" ></a></td>
    </tr>
    <tr>        
    <td><a href="request_details.html">6427</a></td>
    <td><a href="request_details.html">test child</a></td>
    <td>Unspecified</td>
    <td>Unspecified</td>
    <td> 	xiaowang</td>
    <td>2012-08-23 10:43</td>
    <td>2012-08-23 10:43</td>
    <td>xiaowang</td>
    <td>2012-08-23 13:48</a></td>
    <td> 	Approved</td>
    <td><a href="#" id="delete" ></a></td>
    </tr>
    <tr>        
    <td><a href="request_details.html">6427</a></td>
    <td><a href="request_details.html">test child</a></td>
    <td>Unspecified</td>
    <td>Unspecified</td>
    <td> 	xiaowang</td>
    <td>2012-08-23 10:43</td>
    <td>2012-08-23 10:43</td>
    <td>xiaowang</td>
    <td>2012-08-23 13:48</a></td>
    <td> 	Approved</td>
    <td><a href="#" id="delete" ></a></td>
    </tr>
    <tr>        
    <td><a href="request_details.html">6427</a></td>
    <td><a href="request_details.html">test child</a></td>
    <td>Unspecified</td>
    <td>Unspecified</td>
    <td> 	xiaowang</td>
    <td>2012-08-23 10:43</td>
    <td>2012-08-23 10:43</td>
    <td>xiaowang</td>
    <td>2012-08-23 13:48</a></td>
    <td> 	Approved</td>
    <td><a href="#" id="delete" ></a></td>
    </tr>
    <tr>        
    <td><a href="request_details.html">6427</a></td>
    <td><a href="request_details.html">test child</a></td>
    <td>Unspecified</td>
    <td>Unspecified</td>
    <td> 	xiaowang</td>
    <td>2012-08-23 10:43</td>
    <td>2012-08-23 10:43</td>
    <td>xiaowang</td>
    <td>2012-08-23 13:48</a></td>
    <td> 	Approved</td>
    <td><a href="#" id="delete" ></a></td>
    </tr>
    <tr>        
    <td><a href="request_details.html">6427</a></td>
    <td><a href="request_details.html">test child</a></td>
    <td>Unspecified</td>
    <td>Unspecified</td>
    <td> 	xiaowang</td>
    <td>2012-08-23 10:43</td>
    <td>2012-08-23 10:43</td>
    <td>xiaowang</td>
    <td>2012-08-23 13:48</a></td>
    <td> 	Approved</td>
    <td><a href="#" id="delete" ></a></td>
    </tr>
    <tr>        
    <td><a href="request_details.html">6427</a></td>
    <td><a href="request_details.html">test child</a></td>
    <td>Unspecified</td>
    <td>Unspecified</td>
    <td> 	xiaowang</td>
    <td>2012-08-23 10:43</td>
    <td>2012-08-23 10:43</td>
    <td>xiaowang</td>
    <td>2012-08-23 13:48</a></td>
    <td> 	Approved</td>
    <td><a href="#" id="delete" ></a></td>
    </tr>
    <tr>        
    <td><a href="request_details.html">6488</a></td>
    <td><a href="request_details.html">test child</a></td>
    <td>Unspecified</td>
    <td>Unspecified</td>
    <td> 	xiaowang</td>
    <td>2012-08-23 10:43</td>
    <td>2012-08-23 10:43</td>
    <td>xiaowang</td>
    <td>2012-08-23 13:48</a></td>
    <td> 	Approved</td>
    <td><a href="#" id="delete" ></a></td>
    </tr>
</tbody>
</table>
	          <div class="clear"></div>
            </div></td>
        </tr>
	    
      </tbody>
</table>
<table class="eso-table" id="table1">
  <thead>
    <tr>
      <th>My Requests<a href="Summary Report.html" class="float-right"><button class="btn">Summary Report</button></a></th>
      
      </tr>
    </thead>
  <tbody>
    <tr>
      <td class="clear-padding">
        
        
        <div class="list-margin-top">
          <table id="example2" class="eso-table table-striped eso-table-inner ">
<thead>
    <tr>
        <th><input type="checkbox" value="option1" id="inlineCheckbox1"></th>
        <th>ID</th>
        <th>Request Name</th>
         <th>Product</th>
        <th>Vertion</th>
        <th>Creator</th>
        <th>Created date</th>
        <th>Due date</th>
        <th>Last modified</th>
         <th>Modified date</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
</thead>
<tbody>
		<c:forEach var="myRequest" items="${myRequests}">
			<tr id="myrequestlist${myRequest.requestid}">
			<td><input type="checkbox" name="chkltMyRequest" id="chkltMyRequest${myRequest.requestid}" value="${myRequest.requestid}"></td>
			<td>${myRequest.requestid}</td>
			<td nowrap><a href=${pageContext.request.contextPath}/showRequest/${myRequest.requestid} title="View Request">${myRequest.requestname}</a></td>
			<td nowrap>${myRequest.productname}</td>
			<td>${myRequest.versiondesc}</td>
			<td>${myRequest.createdby}</td>
			<td nowrap><c:if test="${empty myRequest.createdtime}"><fmt:formatDate value="${myRequest.createdtime}" pattern="yyyy-MM-dd" /></c:if></td>
			<td nowrap><c:if test="${empty myRequest.createdtime}"><fmt:formatDate value="${myRequest.requesttime}" pattern="yyyy-MM-dd" /></c:if></td>
			<td nowrap>${myRequest.editedby}</td>
			<td nowrap><c:if test="${empty myRequest.createdtime}"><fmt:formatDate value="${myRequest.editedtime}" pattern="yyyy-MM-dd" /></c:if></td>
			<td nowrap>${myRequest.status}</td>
			<td nowrap>
			<c:if test="${myRequest.status ne 'withdrawn'}">
			    <c:choose>
	                <c:when test="${myRequest.from eq 0}">
						<div id="deletemyrequest${myRequest.requestid}">
							<a href="javascript:confirmDelete('index.do?operation=DeleteRequest
							&requestid=${myRequest.requestid}', '${myRequest.requestname}')">Delete</a>
						</div>
					</c:when>
					<c:otherwise>
						<div id="withdrawmyrequest${myRequest.requestid}">
						<a href="javascript:confirmWithDraw('index.do?operation=WithDrawRequest
						    &requestid=${myRequest.requestid}', '${myRequest.requestname}')">Withdraw</a>
							</div>
					</c:otherwise>
				</c:choose>
			</c:if>
			</td>
		</tr>
		</c:forEach>
</tbody>
</table>
          <div class="clear"></div>
          </div></td>
      </tr>
    
    </tbody>
</table>

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
	$(".chzn-select").chosen(); 
	$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
</script>	
    </div>
 </body>
</html>
