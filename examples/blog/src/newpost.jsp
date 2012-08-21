<%@ page import="com.cloudera.training.hbase.examples.blog.Post"%>

<% 
	String newPostBody = request.getParameter("body");
	try {
		Post.savePost(request.getParameter("title"),newPostBody);
	} catch (Exception e) {
		out.println("<br><p>Error posting data.</p></br>");
	}
	
%>

<html>
<title>Thanks</title>
<h3>This has been published:</h3>
<p>
	<%= newPostBody %>
</p>
<a href="index.jsp">Return to list</a>
</html>
