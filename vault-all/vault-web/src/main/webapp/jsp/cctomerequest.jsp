<table class="eso-table" id="cctomerequest" style="display:none">
	  <thead>
	    <tr>
      		<th>CC Me Request</th>
        </tr>
      </thead>
	  <tbody>
	    <tr>
	      <td class="clear-padding">
	        
	        
	        <div class="list-margin-top">
	          <table id="example7" class="eso-table table-striped eso-table-inner ">
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
	<c:forEach var="ccToMeRequest" items="${ccToMeRequests}">
    <tr>        
    <td><a href=${pageContext.request.contextPath}/showRequest/${ccToMeRequest.requestid} title="View Request">${ccToMeRequest.requestid}</a></td>
    <td class="wordwrap"><a href=${pageContext.request.contextPath}/showRequest/${ccToMeRequest.requestid} title="View Request">${ccToMeRequest.requestname}</a></td>
    <td>${ccToMeRequest.productname}</td>
    <td>${ccToMeRequest.versiondesc}</td>
    <td class="nowrep">${ccToMeRequest.createdby}</td>
    <td class="nowrep">${tran:transformByFormat(ccToMeRequest.createdtime,"yyyy-MM-dd HH:mm")}</td>
    <td class="nowrep">${tran:transformByFormat(ccToMeRequest.requesttime,"yyyy-MM-dd HH:mm")}</td>
    <td class="nowrep">${ccToMeRequest.editedby}</td>
    <td class="nowrep">${tran:transformByFormat(ccToMeRequest.editedtime,"yyyy-MM-dd HH:mm")}</a></td>
    <td class="nowrep">${ccToMeRequest.status}</td>
    </tr>
    </c:forEach>
</tbody>
</table>
	          <div class="clear"></div>
            </div></td>
        </tr>
	    
      </tbody>
</table>