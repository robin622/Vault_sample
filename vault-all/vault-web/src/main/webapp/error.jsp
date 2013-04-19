<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Vault Form Authentication</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/datatable.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/eso-theme.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/vault.css" />
<script type='text/javascript' src="<%=request.getContextPath()%>/js/browserdetector.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dropdown.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/modal.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/datatable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/vault.js"></script>
</head>
<body id='eso-body'>
	<div class="eso-inner">
		<a href="<%=request.getContextPath()%>/jsp/warn.jsp" class="warn-index" style="display: none"
			id="warning">Your browser is not supported</a>
		<%@ include file="jsp/header.jsp"%>
		<div class="navbar">
			<ul class="nav">
				<li class="active"><a href="${pageContext.request.contextPath}/HomeServlet">Login</a></li>
			</ul>
		</div>
		<div class="content">
			<section id="tables" class="cancel-margin">
			<div class="row-fluid mgt0">
				<div id="content" class="row-fluid">
					<div class="row-fluid">
						<!-- Sign in form -->
						<div class="alert alert-block">
							<button type="button" id="btn" class="close" data-dismiss="alert">&times;</button>
							<h4>
								<span class="warn-image"></span>&nbsp;The username or password is incorrect.
							</h4>
							Verify that CAPS LOCK is not on, and then retype the current username and password.
						</div>
						<a href="${pageContext.request.contextPath}/HomeServlet" class="pull-right">Login again</a>
					</div>
				</div>
			</section>
		</div>
		<%@ include file="jsp/footer.jsp"%>
	</div>
	<script>
		$(function() {
			$("#btn").click(function() {
				$("div.alert").hide();
			})

		})
	</script>
</body>
</html>
