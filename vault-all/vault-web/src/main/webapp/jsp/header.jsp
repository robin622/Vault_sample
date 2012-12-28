<header id='eso-topbar'>
  <a href="${pageContext.request.contextPath}/HomeServlet" title="go back home" class="logo">Vault</a>
  <a href="https://engineering.redhat.com/hss-portal" class="eso-logo"><img src="<%=request.getContextPath()%>/images/header-logo-eso-developed.png" alt="Developed by HSS"></a>
  <ul class="quick-menu unstyled">
  <li class="dropdown">
    <a href="https://engineering.redhat.com/hss-portal/products/" title="Engineering Services">Engineering Services</a>
  </li>
  <li class="dropdown header-help">
    <a class="dropdown-toggle" data-toggle="dropdown" href="#" title="User Guide">Help<b class="caret"></b></a>
      <ul class="dropdown-menu">
        <li><a href="https://dart.qe.lab.eng.bne.redhat.com/User_Guides/Vault/tmp/en-US/html-single/index.html" target="_blank">User guide</a></li>
        <li class="divider"></li>
        <li><a href="mailto:hss-eip@redhat.com">EIP Request</a></li>
        <li><a href="mailto:eng-ops@redhat.com">Eng-ops Request</a></li>
        <li><a href="mailto:hss-ied-list@redhat.com">Application Request</a></li>
        <li class="divider"></li>
        <li><a href="mailto:vault-dev-list@redhat.com">Contact developers</a></li>
      </ul>
    </li>
 <li class="dropdown header-user">
   <a class="dropdown-toggle" href="#" title="User Info">Hello, ${userName}</a>
 </li>
 </ul>
</header>
