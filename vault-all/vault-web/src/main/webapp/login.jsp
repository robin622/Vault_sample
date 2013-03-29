<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
      <title>Vault Form Authentication</title>
      <script type='text/javascript' src="<%=request.getContextPath()%>/js/browserdetector.js"></script>
    </head>
    <body>
      <a href="<%=request.getContextPath()%>/jsp/warn.jsp" class="warn-index" style="display: none" id="warning">Your browser is not supported</a>
      <h1>Vault Login Page</h1>
     <p>   
      <form method=post action="j_security_check" > 
<!--<form method=post action=<%= response.encodeURL("j_security_check") %> > -->
        <table>
          <tr>
            <td>Username</td><td>-</td>
            <td><input type="text"  name= "j_username" ></td>
          </tr>
          <tr>
            <td>Password</td><td>-</td>
            <td><input type="password"  name= "j_password" ></td>
          </tr>
          <tr>
            <td colspan="2"><input type="submit"></td>
          </tr>              
        </table>
      </form>
      </p> 
      <hr>
    </body>
  </html>
