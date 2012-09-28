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
	<c:forEach var="signedOffRequest" items="${signedOffRequests}">
    <tr>        
    <td><a href="request_details.html">${signedOffRequest.requestid}</a></td>
    <td><a href="request_details.html">${signedOffRequest.requestname}</a></td>
    <td>${signedOffRequest.productname}</td>
    <td>${signedOffRequest.versiondesc}</td>
    <td>${signedOffRequest.createdby}</td>
    <td>${tran:transformByFormat(signedOffRequest.createdtime,"yyyy-MM-dd HH:mm")}</td>
    <td>${tran:transformByFormat(signedOffRequest.requesttime,"yyyy-MM-dd HH:mm")}</td>
    <td>${signedOffRequest.editedby}</td>
    <td>${tran:transformByFormat(signedOffRequest.editedtime,"yyyy-MM-dd HH:mm")}</a></td>
    <td>${signedOffRequest.status}</td>
    </tr>
    </c:forEach>
</tbody>
</table>
	          <div class="clear"></div>
            </div></td>
        </tr>
	    
      </tbody>
</table>