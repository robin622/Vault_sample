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
	src="<%=request.getContextPath()%>/js/jquery.js"></script>
<!--script type="text/javascript"
	src="<%=request.getContextPath()%>/js/dropdown.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/modal.js"></script-->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.dataTables.min.js"></script>
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
				href="${pageContext.request.contextPath}/showRequest/${request.requestid}"
				class="flow-left margin-right"><span><h4>&nbsp;&nbsp;${request.requestid}
						${request.requestname}</h4></span></a>
			<table class="eso-table blod font-normal" id="sumreport${status.index}"
				style="font-size: 13px;">
				<thead>
					<tr>
						<th width="13%">Status</th>
						<th width="13%">Sign Off By</th>
						<th width="13%">Due Date</th>
						<th width="61%">Last Modified</th>
					</tr>
				</thead>
				<tbody>
					<c:if
						test="${request.waitingList!=null && request.waitingList!=''}">
						<c:forTokens items="${request.waitingList}" delims="," var="wait">
							<tr>
								<td><span class="waiting">Waiting</span></td>
								<td>
								<a href="#">${wait}</a>
								</td>
								<td>${tran:transformByFormat(request.requesttime,"yyyy-MM-dd
									HH:mm")}</td>
								<td></td>
							</tr>
						</c:forTokens>
					</c:if>
					<c:if
						test="${request.signoffList!=null && request.signoffList!=''}">
						<c:forTokens items="${request.signoffList}" delims="," var="signoff">
							<tr>
								<td><span class="signed">Signed</span></td>
								<td>
								<a href="#">${fn:substringBefore(signoff, '|')}</a>
								<td>${tran:transformByFormat(request.requesttime,"yyyy-MM-dd
									HH:mm")}</td>
								<td>${fn:substringAfter(signoff, '|')}</td>
							</tr>
						</c:forTokens>
					</c:if>
					<c:if
						test="${request.commentList!=null && request.commentList!=''}">
						<c:forTokens items="${request.commentList}" delims="," var="comment">
							<tr>
								<td><span class="Comment">Comment</span></td>
								<td>
								<a href="#">${fn:substringBefore(comment, '|')}</a>
								</td>
								<td>${tran:transformByFormat(request.requesttime,"yyyy-MM-dd
									HH:mm")}</td>
								<td>${fn:substringAfter(comment, '|')}</td>
							</tr>
						</c:forTokens>
					</c:if>
					<c:if
						test="${request.rejectedList!=null && request.rejectedList!=''}">
						<c:forTokens items="${request.rejectedList}" delims="," var="reject">
							<tr>
								<td><span class="Rejected">Reject</span></td>
								<td>
									<a href="#">${fn:substringBefore(reject, '|')}</a>
								</td>
								<td>${tran:transformByFormat(request.requesttime,"yyyy-MM-dd
									HH:mm")}</td>
								<td>${fn:substringAfter(reject, '|')}</td>
							</tr>
						</c:forTokens>
					</c:if>

				</tbody>
			</table>
			<script type="text/javascript">
				toreplace('sumreport${status.index}');
			</script>
		</c:forEach>
		
		<div class="clear"></div>
	</div>
</body>