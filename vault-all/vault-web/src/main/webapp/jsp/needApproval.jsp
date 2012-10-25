<table class="eso-table" id="waitrequest">
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
<c:forEach var="waitRequest" items="${waitRequests}">
    <tr>        
    <td ><a href=${pageContext.request.contextPath}/showRequest?requestid=${waitRequest.requestid} title="View Request">${waitRequest.requestid}</a></td>
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${waitRequest.requestid} title="View Request">${waitRequest.requestname}</a></td>
    <td>${waitRequest.productname}</td>
    <td>${waitRequest.versiondesc}</td>
    <td class="nowrep">${waitRequest.createdby}</td>
    <td class="nowrep">${tran:transformByFormat(waitRequest.createdtime,"yyyy-MM-dd HH:mm")}</td>
    <td class="nowrep">${tran:transformByFormat(waitRequest.requesttime,"yyyy-MM-dd HH:mm")}</td>
    <td class="nowrep">${waitRequest.editedby}</td>
    <td class="nowrep">${tran:transformByFormat(waitRequest.editedtime,"yyyy-MM-dd HH:mm")}</a></td>
    <td class="nowrep">${waitRequest.status}</td>
    </tr>
    </c:forEach>
</tbody>
</table>
	          <div class="clear"></div>
            </div></td>
        </tr>
	    
      </tbody>
</table>