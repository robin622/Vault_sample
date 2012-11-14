<table class="eso-table" id="waitrequest" style="display:none">
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
        <th class="w400">Request Name</th>
        <th class="productwidth">Product</th>
        <th>Version</th>
        <th class="w80">Creator</th>
        <th>Created Date</th>
        <th>Due Date</th>
        <th class="w130">Last Modified</th>
        <th class="w130">Modified Date</th>
        <th>Status</th>
    </tr>
</thead>
<tbody>
<c:forEach var="waitRequest" items="${waitRequests}">
    <tr>        
    <td ><a href=${pageContext.request.contextPath}/showRequest?requestid=${waitRequest.requestid} title="View Request">${waitRequest.requestid}</a></td>
    <td class="wordwrap"><a href=${pageContext.request.contextPath}/showRequest?requestid=${waitRequest.requestid} title="View Request">${waitRequest.requestname}</a></td>
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