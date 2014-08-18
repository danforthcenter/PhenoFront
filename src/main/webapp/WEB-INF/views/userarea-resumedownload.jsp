<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<tags:userarea-template>
<jsp:attribute name="active">Resume Download</jsp:attribute>
<jsp:body>
<div class="container">
	<div class="jumbotron"
		style="padding-left: 30px; padding-right: 30px;">				
		<p style="min-height:00px">

	 
		<h2>Resume Incomplete Downloads</h2>
		<p>
			Any interrupted downloads can be resumed provided at least the CSV file was properly downloaded.
		</p>
		<span class="help-block">
			Before uploading the incomplete zip file to this applet, repair it with a Zip Utility (e.g., WinRar).
			Now upload it to this page and download the desired replacement files.
		</span>
		
		<object type="application/x-java-applet" height="350" width="600">
			<param name="code" value="src.ddpsc.resumeDownloadApplet.JResumeDownloadApplet" />
			<param name="archive" value="<c:url value="/WEB-INF/lib/JResumeDownloadApplet.jar"/>" />
			<!--<param name="archive" value="PhenoFront\WEB-INF\lib\JResumeDownloadApplet.jar" />-->
		</object>
		
		<p>
			In the event that the java applet did not load (for any of a thousand reasons),
			<a href="/phenofront/userarea/downloadresumeapplet">here</a> is a link to download an equivalent java application.
			It can be run from the command line with:
			<br/><br/>
			<code>> java -jar ResumeDownloadApplication.jar</code>
			<br/><br/>
			<i>N.B. Ensure the command line's directory is in the same directory as the jar file and that java is installed and registered in the operating system's environment variables.</i>
		</p> 
		
		
	</div>
</div>
</jsp:body>
</tags:userarea-template>
</body>
</html>
