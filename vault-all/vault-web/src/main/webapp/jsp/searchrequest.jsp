<table class="eso-table" id="searchrequest">
	  <thead>
	    <tr>
      		<th>Search Request</th>
        </tr>
      </thead>
	  <tbody>
	    <tr>
	      <td class="clear-padding">
	        
	        
	        <div class="list-margin-top">
	          <table id="example6" class="eso-table table-striped eso-table-inner ">
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
	<c:forEach var="searchRequest" items="${searchRequests}">
    <tr>        
    <td><a href="request_details.html">${searchRequest.requestid}</a></td>
    <td><a href="request_details.html">${searchRequest.requestname}</a></td>
    <td>${searchRequest.productname}</td>
    <td>${searchRequest.versiondesc}</td>
    <td>${searchRequest.createdby}</td>
    <td>${tran:transformByFormat(searchRequest.createdtime,"yyyy-MM-dd HH:mm")}</td>
    <td>${tran:transformByFormat(searchRequest.requesttime,"yyyy-MM-dd HH:mm")}</td>
    <td>${searchRequest.editedby}</td>
    <td>${tran:transformByFormat(searchRequest.editedtime,"yyyy-MM-dd HH:mm")}</a></td>
    <td>${searchRequest.status}</td>
    </tr>
    </c:forEach>
</tbody>
</table>
	          <div class="clear"></div>
            </div></td>
        </tr>
	    
      </tbody>
</table>