<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/view/common/layout.jsp">
    <c:param name="title"><c:out value="${entry.title}"/></c:param>
    <c:param name="header"><a href="/">dolpen.net</a></c:param>
    <c:param name="content">
        <h2><c:out value="${entry.title}"/></h2>
        <c:out value="${entry.compiled}" escapeXml="false"/>
        <p><a href="/text/">back to index of text</a></p>
    </c:param>
</c:import>

