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
				<li class="active"><a href="${pageContext.request.contextPath}/HomeServlet">Home</a></li>
			</ul>
		</div>
		<div class="content" style="border-radius: 0px 0px 6px 6px;">
			<section id="tables" style="margin: 0px 15px 50px;">
			<div class="row-fluid">
				<div id="content">
					<div style="display: table;">
						<div style="width: 320px; display: table-cell; padding: 0 10px;">
							<form action="j_security_check" method="POST" class="form-horizontal">
								<div class="control-group">
									<label class="control-label" for="username">Username</label>
									<div class="controls">
										<input type="text" class="input-medium" name="j_username" placeholder="Kerberos username"
											required="required" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="password">Password</label>
									<div class="controls">
										<input type="password" class="input-medium" name="j_password"
											placeholder="Kerberos Password" required="required" />
									</div>
								</div>
								<div class="form-actions">
									<button type="submit" class="btn btn-primary pull-right">Login</button>
								</div>
							</form>
						</div>
						<div style="display: table-cell; padding: 0 10px;">
							<div class="content error-content">
								<h2>401 — Kerberos Authentication Failed</h2>
								<p>
									All Engineering Services secure sites require kerberos forwarding in your browser. Please
									make sure that you have <a href="https://docspace.corp.redhat.com/docs/DOC-55498">valid
										Kerberos tickets</a> (obtainable via kinit), and that you have <a
										href="https://docspace.corp.redhat.com/docs/DOC-43518">configured Firefox correctly</a>.
									If you are still unable to access this, please contact Engineering Services in IRC on
									#eng-ops for assistance.
								</p>
								<h4>Users of Google Chrome can use the following procedure to enable kerberos
									forwarding:</h4>
								<ol>
									<li>edit /etc/opt/chrome/policies/managed/redhat-corp.json</li>
									<li>add the following content
										<p>{ "AuthServerWhitelist": "*.redhat.com", "AuthNegotiateDelegateWhitelist":
											"*.redhat.com" }</p>
									</li>
									<li>save changes</li>
									<li>restart chrome</li>
								</ol>
							</div>
						</div>
					</div>
				</div>
			</div>
			</section>
		</div>
		<%@ include file="jsp/footer.jsp"%>
	</div>
</body>
</html>
