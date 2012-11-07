<table class="eso-table" id="myrequest" style="display:none">
  <thead>
    <tr>
      <th>My Requests<a href="#" class="float-right"><button class="btn" onclick="javascript:request.req_sumReport()">Summary Report</button></a></th>
      
      </tr>
    </thead>
  <tbody>
    <tr>
      <td class="clear-padding">
        
        
        <div class="list-margin-top">
          <table id="myrequest_table" class="eso-table table-striped eso-table-inner " >
<thead>
    <tr>
        <th><input type="checkbox" value="option1" id="checkAllMyRequest"></th>
        <th>ID</th>
        <th class="w400">Request Name</th>
        <th class="productwidth">Product</th>
        <th>Version</th>
        <th>Creator</th>
        <th>Created Date</th>
        <th>Due Date</th>
        <th>Last Modified</th>
        <th>Modified Date</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
</thead>
<tbody>
		<c:forEach var="myRequest" items="${myRequests}">
			<tr id="myrequestlist${myRequest.requestid}">
			<td><input type="checkbox" name="chkltMyRequest" id="chkltMyRequest${myRequest.requestid}" value="${myRequest.requestid}"></td>
			<td>${myRequest.requestid}</td>
			<td ><a href=${pageContext.request.contextPath}/showRequest?requestid=${myRequest.requestid} title="View Request">${myRequest.requestname}</a></td>
			<td>${myRequest.productname}</td>
			<td>${myRequest.versiondesc}</td>
			<td>${myRequest.createdby}</td>
			<td class="nowrep">${tran:transformByFormat(myRequest.createdtime,"yyyy-MM-dd HH:mm")}</td>
			<td class="nowrep">${tran:transformByFormat(myRequest.requesttime,"yyyy-MM-dd HH:mm")}</td>
			<td class="nowrep">${myRequest.editedby}</td>
			<td class="nowrep">${tran:transformByFormat(myRequest.editedtime,"yyyy-MM-dd HH:mm")}</td>
			<td class="nowrep">${myRequest.status}</td>
			<td class="nowrep">
			<c:if test="${myRequest.status ne 'withdrawn'}">
			    <c:choose>
	                <c:when test="${myRequest.from eq 0}">
						<div id="deletemyrequest${myRequest.requestid}">
							<a href="javascript:request.req_delRequest('${myRequest.requestid}', '${myRequest.requestname}')">Delete</a>
						</div>
					</c:when>
					<c:otherwise>
						<div id="withdrawmyrequest${myRequest.requestid}">
						<a href="javascript:confirmWithDraw('${pageContext.request.contextPath}/withDrawRequest?requestid=${myRequest.requestid}&requestversion=${myRequest.requestVersion}', '${myRequest.requestname}')">Withdraw</a>
							</div>
					</c:otherwise>
				</c:choose>
			</c:if>
			</td>
		</tr>
		</c:forEach>
</tbody>
</table>
          <div class="clear"></div>
          </div></td>
      </tr>
    
    </tbody>
</table>
