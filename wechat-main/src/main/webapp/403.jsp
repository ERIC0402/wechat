<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="US-ASCII"%>
<%@page import="org.beangle.security.web.access.DefaultAccessDeniedHandler"%>
<%@page import="org.beangle.security.core.context.SecurityContextHolder"%>
<%@ page import="org.beangle.security.core.Authentication" %>

<h1>Sorry, access is denied</h1>
<p>
<%= request.getAttribute(DefaultAccessDeniedHandler.ACCESS_DENIED_EXCEPTION_KEY)%>
</p>
<p>

<%		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) { %>
			Authentication object as a String: <%= auth.toString() %>
<%	  } %>
</p>
