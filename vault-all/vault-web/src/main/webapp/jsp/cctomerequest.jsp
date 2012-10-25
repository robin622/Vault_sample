<table class="eso-table" id="cctomerequest">
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
        <th>ID</th>
        <th class="nowrep">Request Name</th>
         <th>Product</th>
        <th>Version</th>
        <th>Creator</th>
        <th class="nowrep">Created Date</th>
        <th class="nowrep">Due Date</th>
        <th class="nowrep">Last Modified</th>
         <th class="nowrep">Modified Date</th>
        <th>Status</th>
    </tr>
</thead>
<tbody>
	<c:forEach var="ccToMeRequest" items="${ccToMeRequests}">
    <tr>        
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${ccToMeRequest.requestid} title="View Request">${ccToMeRequest.requestid}</a></td>
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${ccToMeRequest.requestid} title="View Request">${ccToMeRequest.requestname}</a></td>
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