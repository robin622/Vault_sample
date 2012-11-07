<table class="eso-table" id="searchrequest" style="display:none">
	  <thead>
	    <tr>
      		<th>Search Request<a href="#" class="float-right"><button class="btn" onclick="javascript:request.req_sumReport('search')">Summary Report</button></a></th>
        </tr>
      </thead>
	  <tbody>
	    <tr>
	      <td class="clear-padding">
	        
	        
	        <div class="list-margin-top">
	          <table id="searchreq_table" class="eso-table table-striped eso-table-inner ">
<thead>
    <tr>
    	<th><input type="checkbox" value="option1" id="checkAllSearchRequest"></th>        
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
    <td><input type="checkbox" name="chkltSearchRequest" id="chkltSearchRequest${searchRequest.requestid}" value="${searchRequest.requestid}"></td>
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${searchRequest.requestid} title="View Request">${searchRequest.requestid}</a></td>
    <td><a href=${pageContext.request.contextPath}/showRequest?requestid=${searchRequest.requestid} title="View Request">${searchRequest.requestname}</a></td>
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