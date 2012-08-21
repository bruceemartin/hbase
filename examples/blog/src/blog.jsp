<%@ page import="com.cloudera.training.hbase.examples.blog.Blog"%>

<% 	
	Blog blog=null;
	try {
		blog = new Blog(request.getParameter("blogid"));
	} catch (Exception e) {
		out.println("<br><p>Error obtaining blog.</p></br>");
	}
%>

<html>
<title>Welcome to my blog!</title>


<h1><%= blog.blogTitle %></h1>
<%= blog.dateOfPost %>
<br>
<br>
<%= blog.blogText %>
<hr>
<h3>Comments:</h3>
<ul>
	<%
	if (blog!=null) {
		while (blog.hasNextComment()) {
			out.println("<li>");			
			out.println(blog.nextCommentAuthor() + " said...<br>");
			out.println(blog.nextCommentBody() + "<br>");
			out.println(blog.nextDateOfComment() + "<br><br>");
			out.println("</li>");
		}
	}
%>
</ul>

<!-- print new comment form -->
<h3>Add comment:</h3>
<FORM METHOD="POST" ACTION="newcomment.jsp">
	<table>
		<tr>
			<td>Your name:</td>
			<td><INPUT TYPE="TEXT" NAME="author" SIZE="50"></td>
		</tr>
		<tr>
			<td>Comment:</td>
			<td><INPUT TYPE="TEXT" NAME="body" SIZE="50"></td>
		</tr>
	</table>
	<INPUT TYPE="SUBMIT" VALUE="Submit"> <INPUT TYPE="hidden" NAME="blogid"
		VALUE="<%=request.getParameter("blogid")%>">
</FORM>
<p>
	<a href="index.jsp">Home</a>
</p>
</html>
