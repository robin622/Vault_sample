<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="tran" uri="/wezhao/tran"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>vault</title>
<link href="<%=request.getContextPath()%>/css/eso-theme.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/vault.css"
	rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/css/jquery-ui-1.8.20.custom.css"
	rel="stylesheet" />
<link href="<%=request.getContextPath()%>/images/favicon.ico"
	rel="shortcut icon">
<script src="<%=request.getContextPath()%>/js/jquery-1.7.2.min.js"></script>
<script src="<%=request.getContextPath()%>/js/dropdown.js"></script>
<script src="<%=request.getContextPath()%>/js/modal.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.dataTables.min.js"></script>
<script type='text/javascript' src="<%=request.getContextPath()%>/js/timezone.js"></script> 
<script type='text/javascript' src="<%=request.getContextPath()%>/js/vault_js.js"></script> 
</head>
<body>
	<div class="content margin padding">
		<table class="eso-table table-width blod bgnone detail-table fix-table" id="home_table">
			<thead>
				<tr>
					<th colspan="3"><h3 class="w800 wordwrap">${reportRequest.requestid}&nbsp;&nbsp;${reportRequest.requestname}</h3>
					</th>
					<th width="52%"><a
						href="<%=request.getContextPath()%>/ReportServlet?doExport=export&id=${reportRequest.requestid}"
						class="float-right report">Export The Report As XML</a></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><span>Product:</span></td>
					<td>${reportRequest.productname}</td>
					<td width="8%" class="bg-gray">Version:</td>
					<td>${reportRequest.versiondesc}</td>
				</tr>
				<tr>
					<td>Due Date:</td>
					<td>${tran:transformByFormat(reportRequest.requesttime,"yyyy-MM-dd
						HH:mm")}</td>
					<td class="bg-gray">Create Date:</td>
					<td>${tran:transformByFormat(reportRequest.createdtime,"yyyy-MM-dd
						HH:mm")}</td>
				</tr>
				<tr>
					<td><span>CC:</span></td>
					<td colspan="3">${reportRequest.forward}</td>
				</tr>
				<tr>
					<td><span>Detailed description:</span></td>
					<td colspan="3">
						<div id="detail_value" class="description wordwrap"></div>
				</tr>
				<tr>
					<td><span>Attachment:</span></td>
					<td colspan="3">${tran:createFileInfo(reportRequest,pageContext.request.contextPath)}</td>
				</tr>
			</tbody>
		</table>

		<table class="eso-table blod font-normal fix-table" id="report_tbl">
			<thead>
				<tr>
					<th width="13%">Status</th>
					<th width="16%">Sign Off By</th>
					<th width="14%">Due Date</th>
					<th width="14%">Last Modified</th>
					<th width="43%">Comment</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty reports}">
					<c:forEach items="${reports}" var="report" varStatus="reportstatus">
						<c:set value="${report}" var="temps"></c:set>
						<c:set value="${tran:getReportStatus(report)}" var="elementofreport"></c:set>
						<tr>
							<td><span class="${elementofreport.reportStatus}m">${elementofreport.reportStatus}</span>
							</td>
							<td><a href="#">${elementofreport.useremail}</a></td>
							<td>${tran:transformByFormat(reportRequest.requesttime,"yyyy-MM-dd
								HH:mm")}</td>
							<td>${tran:transformByFormat(elementofreport.editedtime,"yyyy-MM-dd
								HH:mm")}</td>
							<td class="wordwrap"><c:if test="${not empty temps}">
									<c:forEach items="${temps}" var="temp" varStatus="temptatus">
										<c:if
											test="${(not empty temp.editedtime) and (temp.status ne Request.SIGNED_BY)}">
											<div class="comment_list">
												${tran:replaceCharacter(temp.comment)}
												<div class='comment_pt'>
													${tran:transformByFormat(temp.editedtime,"yyyy-MM-dd
													HH:mm")} ${temp.editedby} ${temp.status}</div>
												<c:if test="${not empty replys.get(temp.historyid)}">
													<c:set value="${replys.get(temp.historyid)}"
														var="replyList"></c:set>
												</c:if>
												<c:if test="${not empty replyList}">
													<ul id="reply${reportstatus.index}_${temptatus.index}"></ul>
													<c:forEach items="${replyList}" var="replycomment">
														<script type="text/javascript">
															$(document)
																	.ready(
																			function() {
																				jQuery(function(
																						$) {
																					<c:if test="${replycomment.baseid eq '-1'}">
																					$(
																							"#reply${reportstatus.index}_${temptatus.index}")
																							.append(
																									"<li id='lireply${replycomment.replyid}'>${replycomment.replycomment}<div class='comment_pt'>${replycomment.editedtime} ${replycomment.editedby} Reply</div></li>");
																					$(
																							"#lireply${replycomment.replyid}")
																							.append(
																									"<ul id='ulreply${replycomment.replyid}' style='display:none'></ul>");
																					</c:if>
																					<c:if test="${replycomment.baseid ne '-1'}">
																					document
																							.getElementById("ulreply${replycomment.baseid}").style.display = "block";
																					$(
																							"#ulreply${replycomment.baseid}")
																							.append(
																									"<li id='lireply${replycomment.replyid}'>${replycomment.replycomment}<div class='comment_pt'>${replycomment.editedtime} ${replycomment.editedby} Reply</div></li>");
																					$(
																							"#lireply${replycomment.replyid}")
																							.append(
																									"<ul id='ulreply${replycomment.replyid}' style='display:none'></ul>");
																					</c:if>
																					toreplace3("lireply${replycomment.replyid}");
																				});
																			});
														</script>
													</c:forEach>
												</c:if>
											</div>
										</c:if>
									</c:forEach>
								</c:if></td>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>
	<c:if test="${not empty pRequest}">
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="detail_title"><h3 class="margin">Parent
			Request</h3></span>
		<table cellpadding="0" cellspacing="0" class="eso-table table-width blod">
			<tr>
				<td class="new_bg" width="35%"><span class="detail_title">${pRequest.requestid}&nbsp;&nbsp;${pRequest.requestname}</span></td>
				<td class="new_bg_white" width="65%"><span
						class='<c:if test="${pRequest.status == \"Approved\"}">signed</c:if><c:if test="${pRequest.status != \"Approved\"}">waiting</c:if>'>
							<c:if test="${pRequest.status == \"Approved\"}">"Signed"</c:if>
							<c:if test="${pRequest.status != \"Approved\"}">${pRequest.status}</c:if>
				</span></td>
			</tr>
			<c:if test="${not empty pRequest.waitingList}">
				<tr>
					<td class="new_bg" width="35%"><span class="detail_title">Waiting:</span></td>
					<td class="new_bg_white" width="65%">${pRequest.waitingList}</td>
				</tr>
			</c:if>

			<c:if test="${not empty pRequest.commentList}">
				<tr>
					<td class="new_bg" width="35%"><span class="detail_title">Comment:</span></td>
					<td class="new_bg_white" width="65%">${pRequest.commentList}</td>
				</tr>
			</c:if>

			<c:if test="${not empty pRequest.rejectedList}">
				<tr>
					<td class="new_bg" width="35%"><span class="detail_title">Reject:</span></td>
					<td class="new_bg_white" width="65%">${pRequest.rejectedList}
					</td>
				</tr>
			</c:if>

			<c:if test="${not empty pRequest.signoffList}">
				<tr>
					<td class="new_bg" width="35%"><span class="detail_title">Sign-off:</span></td>
					<td class="new_bg_white" width="65%">${pRequest.signoffList}</td>
				</tr>
			</c:if>
		</table>
	</c:if>
	<c:if test="${not empty cRequests}">
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="detail_title"><h3 class="margin">Child
			Request</h3></span>
		<c:forEach items="${cRequests}" var="rq">
			<table cellpadding="0" cellspacing="0" class="eso-table table-width blod">
				<tr>
					<td class="new_bg" width="35%"><span class="detail_title">${rq.requestid}&nbsp;&nbsp;${rq.requestname}</span></td>
					<td class="new_bg_white" width="65%"><span
						class='<c:if test="${rq.status == \"Approved\"}">signed</c:if><c:if test="${rq.status != \"Approved\"}">waiting</c:if>'>
							<c:if test="${rq.status == \"Approved\"}">"Signed"</c:if>
							<c:if test="${rq.status != \"Approved\"}">${rq.status}</c:if>
					</span></td>
				</tr>
				<c:if test="${not empty rq.waitingList}">
					<tr>
						<td class="new_bg" width="35%"><span class="detail_title">Waiting:</span></td>
						<td class="new_bg_white" width="65%">${rq.waitingList}</td>
					</tr>
				</c:if>
				<c:if test="${not empty rq.commentList}">
					<tr>
						<td class="new_bg" width="35%"><span class="detail_title">Comment:</span></td>
						<td class="new_bg_white" width="65%">${rq.commentList}</td>
					</tr>
				</c:if>

				<c:if test="${not empty rq.rejectedList}">
					<tr>
						<td class="new_bg" width="35%"><span class="detail_title">Reject:</span></td>
						<td class="new_bg_white" width="65%">${rq.rejectedList}</td>
					</tr>
				</c:if>

				<c:if test="${not empty rq.signoffList}">
					<tr>
						<td class="new_bg" width="35%"><span class="detail_title">Sign-off:</span></td>
						<td class="new_bg_white" width="65%">${rq.signoffList}</td>
					</tr>
				</c:if>

			</table>
		</c:forEach>
	</c:if>
</div>
	<script type="text/javascript" charset="utf-8">
		jQuery(function($) {
			$(document)
					.ready(
							function() {
								var detail = "${reportRequest.detail}";
								detail = detail.replaceAll("&amp;amp;", '&');
								detail = detail.replaceAll("&amp;", '&');
								detail = detail.replaceAll("&nbsp;", " ");
								detail = detail.replaceAll("\n", '<br>');
								detail = detail.replaceAll("&quot;", '"');
								detail = detail.replaceAll("&copy;", 'ï¿½');
								//var newdetail = getDetailStr(detail);
								var reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|&|-)+)/g;
								detail = detail
										.replace(reg,
												'<a href="$1$2" target="_blank">$1$2</a>');
								detail = detail.replaceAll(' ', '&nbsp;');
								detail = detail.replaceAll("&nbsp</a>;",
										"</a>&nbsp;");
								//$("#detail_value").html(newdetail);
								$("#detail_value").html(detail);
							});
		});
		toreplace("home_table");
		toreplace("report_tbl");
	</script>
</body>
</html>
