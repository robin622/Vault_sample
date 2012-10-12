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
        <th>Request Name</th>
         <th>Product</th>
        <th>Vertion</th>
        <th>Creator</th>
        <th>Created date</th>
        <th>	Due date</th>
        <th>Last modified</th>
         <th>Modified date</th>
        <th>Status</th>
    </tr>
</thead>
<tbody>
<c:forEach var="waitRequest" items="${waitRequests}">
    <tr>        
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${waitRequest.requestid} title="View Request">${waitRequest.requestid}</a></td>
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${waitRequest.requestid} title="View Request">${waitRequest.requestname}</a></td>
    <td>${waitRequest.productname}</td>
    <td>${waitRequest.versiondesc}</td>
    <td>${waitRequest.createdby}</td>
    <td>${tran:transformByFormat(waitRequest.createdtime,"yyyy-MM-dd HH:mm")}</td>
    <td>${tran:transformByFormat(waitRequest.requesttime,"yyyy-MM-dd HH:mm")}</td>
    <td>${waitRequest.editedby}</td>
    <td>${tran:transformByFormat(waitRequest.editedtime,"yyyy-MM-dd HH:mm")}</a></td>
    <td>${waitRequest.status}</td>
    </tr>
    </c:forEach>
</tbody>
</table>
	          <div class="clear"></div>
            </div></td>
        </tr>
	    
      </tbody>
</table>