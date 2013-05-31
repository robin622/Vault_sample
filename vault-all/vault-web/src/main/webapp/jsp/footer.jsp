<footer class="footer">
	<div class="hss-logo"></div>
	<div class='copyright'>
		<p>
			Vault 3.1.1 <a href="https://bugzilla.redhat.com/enter_bug.cgi?product=Vault&version=3.1.1"
				target="_blank"> Report an Issue</a>
		</p>
		<p>Copyright © 2012 Red Hat, Inc. All rights reserved.</p>
		<p>INTERNAL USE ONLY</p>
		<!--when the application use some tech supported by opensource, then please mark it here><p>Powered by <a href='http://www.redhat.com/products/jbossenterprisemiddleware/portal/'>@JBoss EAP</a></p><-->
	</div>
	<!-- Piwik -->
	<script type="text/javascript">
		 var pkBaseURL = (("https:" == document.location.protocol) ? "https://${piwikServer}" : "http://${piwikServer}");
		 document.write(unescape("%3Cscript src='" + pkBaseURL + "piwik.js' type='text/javascript'%3E%3C/script%3E"));
		 </script>
			<script type="text/javascript">
		 try {
		 var piwikTracker = Piwik.getTracker(pkBaseURL + "piwik.php",${piwikIdSite});
		 <%String username = request.getRemoteUser();
		 if(username!=null){
		 out.println("piwikTracker.setCustomVariable(1,\"userName\",\""+username+"\",\"visit\");");
		 }%>
		 piwikTracker.trackPageView();
		 piwikTracker.enableLinkTracking();
		 } catch( err ) {}
 	</script>
	<noscript>
		<p>
			<img src="http://${piwikServer}/piwik.php?idsite=${piwikIdSite}" style="border: 0" alt="" />
		</p>
	</noscript>
	<!-- End Piwik Code -->
</footer>
