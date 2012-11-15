<table class="eso-table" id="canviewrequest" style="display:none">
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
        <th class="w50">ID</th>
        <th class="w400">Request Name</th>
        <th class="productwidth">Product</th>
        <th class="w110">Version</th>
        <th class="w80">Creator</th>
        <th class="w110">Created Date</th>
        <th class="w110">Due Date</th>
        <th class="w130">Last Modified</th>
        <th class="w130">Modified Date</th>
        <th class="w110">Status</th>
    </tr>
</thead>
<tbody>
	<c:forEach var="canViewRequest" items="${canViewRequests}">
    <tr>        
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${canViewRequest.requestid} title="View Request">${canViewRequest.requestid}</a></td>
    <td class="wordwrap"><a href=${pageContext.request.contextPath}/showRequest?requestid=${canViewRequest.requestid} title="View Request">${canViewRequest.requestname}</a></td>
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