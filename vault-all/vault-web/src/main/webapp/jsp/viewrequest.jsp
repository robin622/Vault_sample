<table class="eso-table" id="canviewrequest">
	  <thead>
	    <tr>
      		<th>Requests I Can View</th>
        </tr>
      </thead>
	  <tbody>
	    <tr>
	      <td class="clear-padding">
	        
	        
	        <div class="list-margin-top">
	          <table id="example3" class="eso-table table-striped eso-table-inner ">
<thead>
    <tr>        
        <th >ID</th>
        <th>Request Name</th>
         <th class="productwidth">Product</th>
        <th>Version</th>
        <th>Creator</th>
        <th >Created Date</th>
        <th>	Due Date</th>
        <th>Last Modified</th>
         <th >Modified Date</th>
        <th>Status</th>
    </tr>
</thead>
<tbody>
	<c:forEach var="canViewRequest" items="${canViewRequests}">
    <tr>        
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${canViewRequest.requestid} title="View Request">${canViewRequest.requestid}</a></td>
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${canViewRequest.requestid} title="View Request">${canViewRequest.requestname}</a></td>
    <td>${canViewRequest.productname}</td>
    <td>${canViewRequest.versiondesc}</td>
    <td class="nowrep">${canViewRequest.createdby}</td>
    <td class="nowrep">${tran:transformByFormat(canViewRequest.createdtime,"yyyy-MM-dd HH:mm")}</td>
    <td class="nowrep">${tran:transformByFormat(canViewRequest.requesttime,"yyyy-MM-dd HH:mm")}</td>
    <td class="nowrep">${canViewRequest.editedby}</td>
    <td class="nowrep">${tran:transformByFormat(canViewRequest.editedtime,"yyyy-MM-dd HH:mm")}</a></td>
    <td class="nowrep">${canViewRequest.status}</td>
    </tr>
    </c:forEach>
</tbody>
</table>
	          <div class="clear"></div>
            </div></td>
        </tr>
	    
      </tbody>
</table>