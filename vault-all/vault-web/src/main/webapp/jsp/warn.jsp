<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Warn</title>
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
    <p> Vault supports <a href="http://www.oldapps.com/firefox.php?old_firefox=72">Firefox 3.6</a>, <a href="http://mac.oldapps.com/safari.php?old_safari=8">Safari4.0</a>, <a href="http://www.oldapps.com/google_chrome.php?old_chrome=6801">Chrome 17</a> and latter versions. </p>
    <p>Please upgrade your browser. Thanks! </p>
    </div>
  </div>
<%@ include file="footer.jsp" %>
</div>
</body>
</html>
