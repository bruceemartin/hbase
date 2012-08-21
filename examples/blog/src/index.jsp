<%@ page import="com.cloudera.training.hbase.examples.blog.UserBlogs"%>

<%
	UserBlogs ubs=null;
	try {
		ubs = new UserBlogs();
	} catch(Exception e) {
		out.println("<br><p>Error scanning blogs.</p></br>");
	}
%>

<html>
<title>Welcome to my blogs!</title>

<h1>My blogs</h1>
<ul>
	<%
	if (ubs!=null) {
		while (ubs.hasNext()) {
			out.println("<li><a href=\"blog.jsp?blogid=" + 
							ubs.nextKey() + "\">" + 
							ubs.nextTitle() + " (" + 
							ubs.nextPostDate() +  ")</a>");
		}
	}
%>
</ul>

<h3>Add new blog post:</h3>
<FORM METHOD="POST" ACTION="newpost.jsp">
	Title:<br> <INPUT TYPE="text" NAME="title" SIZE=50><br> Post:<br>
	<TEXTAREA NAME="body" ROWS=8 COLS=80></TEXTAREA>
	<BR> <INPUT TYPE="SUBMIT" VALUE="Submit"> <INPUT TYPE="hidden"
		NAME="blogid" VALUE="<%=request.getParameter("blogid")%>">
</FORM>

</html>
