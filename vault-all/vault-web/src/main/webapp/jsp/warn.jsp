<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Warning</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="HSS UX">
<link href="<%=request.getContextPath()%>/css/eso-theme.css" rel="stylesheet">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.2.min.js" ></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dropdown.js"></script>
</head>
<body id='eso-body' class="darktheme">
<div class=" w780 eso-inner">
<%@ include file="header.jsp" %>
  <div class="content error-content">
	<div class="warn h250">
    <h3>Your browser is not supported.</h3>
    <p> Vault supports <a href="http://www.mozilla.org/en-US/firefox/fx/#desktop">Firefox</a> 3.6, <a href="http://support.apple.com/downloads/#safari">Safari</a> 4.0, <a href="https://www.google.com/intl/en/chrome/browser/?hl=en&brand=CHMI">Chrome</a> 17 and later versions. </p>
    <p>Please upgrade your browser. Thanks! </p>
    </div>
  </div>
<%@ include file="footer.jsp" %>
</div>
</body>
</html>
