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
	<c:forEach var="canViewRequest" items="${canViewRequests}">
    <tr>        
    <td><a href="request_details.html">${canViewRequest.requestid}</a></td>
    <td><a href="request_details.html">${canViewRequest.requestname}</a></td>
    <td>${canViewRequest.productname}</td>
    <td>${canViewRequest.versiondesc}</td>
    <td>${canViewRequest.createdby}</td>
    <td>${tran:transformByFormat(canViewRequest.createdtime,"yyyy-MM-dd HH:mm")}</td>
    <td>${tran:transformByFormat(canViewRequest.requesttime,"yyyy-MM-dd HH:mm")}</td>
    <td>${canViewRequest.editedby}</td>
    <td>${tran:transformByFormat(canViewRequest.editedtime,"yyyy-MM-dd HH:mm")}</a></td>
    <td>${canViewRequest.status}</td>
    </tr>
    </c:forEach>
</tbody>
</table>
	          <div class="clear"></div>
            </div></td>
        </tr>
	    
      </tbody>
</table>