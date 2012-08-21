<%@ page import="com.cloudera.training.hbase.examples.blog.Comment"%>

<% 
	String blogid = request.getParameter("blogid");
	try {
		Comment.saveComment(blogid,request.getParameter("author"),request.getParameter("body"));
	} catch(Exception e) {
		out.println("<br><p>Error adding comment.</p></br>");
	}
%>

<html>
<title>Thanks</title>
<h3>Your comment has been submited</h3>
<a href="blog.jsp?blogid=<%=blogid%>">Return to blog</a>

</html>
