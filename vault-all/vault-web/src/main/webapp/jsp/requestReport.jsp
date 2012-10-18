<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="tran" uri="/wezhao/tran"%>
<head>
<title>vault</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/eso-theme.css" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/vault.css" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/jquery-ui-1.8.20.custom.css" />
<link href="<%=request.getContextPath()%>/images/favicon.ico"
	rel="shortcut icon" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/dropdown.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/modal.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/datatable.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/vault.js"></script>
<script>
	var loc;
	var loc_timezoneOffset = new Date();
	var loc_ts = loc_timezoneOffset.toString();
	var loc_patern = /^(.+) (GMT.+)$/;
	var match = loc_ts.match(loc_patern);
	loc = match[2];
	function OutputLoc() {
		document.write(loc);
	}
</script>
<script type='text/javascript'
	src="<%=request.getContextPath()%>/js/vault_js.js"></script>
<script type='text/javascript'
	src="<%=request.getContextPath()%>/js/timezone.js"></script>
</head>
<body>
	<div class="content margin padding">
		<c:forEach var="request" items="${multiRequests}" varStatus="status">

			<span class="flow-left state">[ ${request.status} ]</span>
			<a
				href="${pageContext.request.contextPath}/showRequest?requestid=${request.requestid}"
				class="flow-left margin-right"><span><h4>&nbsp;&nbsp;${request.requestid}
						${request.requestname}</h4></span></a>
			<table class="eso-table blod font-normal" id="sumreport${status.index}"
				style="font-size: 13px;">
				<thead>
					<tr>
						<th width="13%">Status</th>
						<th width="13%">Sign off by</th>
						<th width="13%">Due date</th>
						<th width="61%">Last modified</th>
					</tr>
				</thead>
				<tbody>
					<c:if
						test="${request.waitingList!=null && request.waitingList!=''}">
						<tr>
							<td><span class="waiting">Waiting</span></td>
							<td>
							<c:forTokens items="${request.waitingList}" delims="," var="wait">
							<a href="#">${wait }</a>
							</c:forTokens>
							</td>
							<td>${tran:transformByFormat(request.requesttime,"yyyy-MM-dd
								HH:mm")}</td>
							<td>${tran:transformByFormat(request.editedtime,"yyyy-MM-dd
								HH:mm")}</td>
						</tr>
					</c:if>
					<c:if
						test="${request.signoffList!=null && request.signoffList!=''}">
						<tr>
							<td><span class="signed">Signed</span></td>
							<td>
							<c:forTokens items="${request.signoffList}" delims="," var="signoff">
							<a href="#">${signoff }</a>
							</c:forTokens>
							<td>${tran:transformByFormat(request.requesttime,"yyyy-MM-dd
								HH:mm")}</td>
							<td>${tran:transformByFormat(request.editedtime,"yyyy-MM-dd
								HH:mm")}</td>
						</tr>
					</c:if>
					<c:if
						test="${request.commentList!=null && request.commentList!=''}">
						<tr>
							<td><span class="Comment">Comment</span></td>
							<td>
							<c:forTokens items="${request.commentList}" delims="," var="comment">
							<a href="#">${comment }</a>
							</c:forTokens>
							</td>
							<td>${tran:transformByFormat(request.requesttime,"yyyy-MM-dd
								HH:mm")}</td>
							<td>${tran:transformByFormat(request.editedtime,"yyyy-MM-dd
								HH:mm")}</td>
						</tr>
						
					</c:if>
					<c:if
						test="${request.rejectedList!=null && request.rejectedList!=''}">
						<tr>
							<td><span class="Rejected">Reject</span></td>
							<td>
							<c:forTokens items="${request.rejectedList}" delims="," var="reject">
								<a href="#">${reject }</a>
							</c:forTokens>
							</td>
							<td>${tran:transformByFormat(request.requesttime,"yyyy-MM-dd
								HH:mm")}</td>
							<td>${tran:transformByFormat(request.editedtime,"yyyy-MM-dd
								HH:mm")}</td>
						</tr>
					</c:if>

				</tbody>
			</table>
			<script type="text/javascript">
				toreplace('sumreport${status.index}');
			</script>
		</c:forEach>
		<a href="#" class="margin">Link to quick access this report</a>
		<div class="clear"></div>
	</div>
</body>