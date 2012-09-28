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
	<c:forEach var="ccToMeRequest" items="${ccToMeRequests}">
    <tr>        
    <td><a href="request_details.html">${ccToMeRequest.requestid}</a></td>
    <td><a href="request_details.html">${ccToMeRequest.requestname}</a></td>
    <td>${ccToMeRequest.productname}</td>
    <td>${ccToMeRequest.versiondesc}</td>
    <td>${ccToMeRequest.createdby}</td>
    <td>${tran:transformByFormat(ccToMeRequest.createdtime,"yyyy-MM-dd HH:mm")}</td>
    <td>${tran:transformByFormat(ccToMeRequest.requesttime,"yyyy-MM-dd HH:mm")}</td>
    <td>${ccToMeRequest.editedby}</td>
    <td>${tran:transformByFormat(ccToMeRequest.editedtime,"yyyy-MM-dd HH:mm")}</a></td>
    <td>${ccToMeRequest.status}</td>
    </tr>
    </c:forEach>
</tbody>
</table>
	          <div class="clear"></div>
            </div></td>
        </tr>
	    
      </tbody>
</table>