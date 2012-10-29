<table class="eso-table" id="signedrequest">
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
        <th>ID</th>
        <th >Request Name</th>
         <th >Product</th>
        <th>Version</th>
        <th>Creator</th>
        <th >Created Date</th>
        <th >Due Date</th>
        <th >Last Modified</th>
         <th >Modified Date</th>
        <th>Status</th>
    </tr>
</thead>
<tbody>
	<c:forEach var="signedOffRequest" items="${signedOffRequests}">
    <tr>        
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${signedOffRequest.requestid} title="View Request">${signedOffRequest.requestid}</a></td>
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${signedOffRequest.requestid} title="View Request">${signedOffRequest.requestname}</a></td>
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