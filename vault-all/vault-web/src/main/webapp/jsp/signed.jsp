<table class="eso-table" id="signedrequest" style="display:none">
	  <thead>
	    <tr>
      		<th>Requests I Have Signed</th>
        </tr>
      </thead>
	  <tbody>
	    <tr>
	      <td class="clear-padding">
	        
	        
	        <div class="list-margin-top">
	          <table id="example4" class="eso-table table-striped eso-table-inner ">
<thead>
    <tr>        
        <th class="w50">ID</th>
        <th class="w400">Request Name</th>
        <th class="productwidth">Product</th>
        <th>Version</th>
        <th class="w80">Creator</th>
        <th class="w110">Created Date</th>
        <th class="w110">Due Date</th>
        <th class="w130">Last Modified</th>
        <th class="w130">Modified Date</th>
        <th class="w110">Status</th>
    </tr>
</thead>
<tbody>
	<c:forEach var="signedOffRequest" items="${signedOffRequests}">
    <tr>        
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${signedOffRequest.requestid} title="View Request">${signedOffRequest.requestid}</a></td>
    <td class="wordwrap"><a href=${pageContext.request.contextPath}/showRequest?requestid=${signedOffRequest.requestid} title="View Request">${signedOffRequest.requestname}</a></td>
    <td>${signedOffRequest.productname}</td>
    <td>${signedOffRequest.versiondesc}</td>
    <td class="nowrep">${signedOffRequest.createdby}</td>
    <td class="nowrep">${tran:transformByFormat(signedOffRequest.createdtime,"yyyy-MM-dd HH:mm")}</td>
    <td class="nowrep">${tran:transformByFormat(signedOffRequest.requesttime,"yyyy-MM-dd HH:mm")}</td>
    <td class="nowrep">${signedOffRequest.editedby}</td>
    <td class="nowrep">${tran:transformByFormat(signedOffRequest.editedtime,"yyyy-MM-dd HH:mm")}</a></td>
    <td class="nowrep">${signedOffRequest.status}</td>
    </tr>
    </c:forEach>
</tbody>
</table>
	          <div class="clear"></div>
            </div></td>
        </tr>
	    
      </tbody>
</table>