<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/view/common/layout.jsp">
    <c:param name="title">mml</c:param>
    <c:param name="header">dolpen.net - mml</c:param>
    <c:param name="content">
        <c:forEach var="e" items="${entries}" varStatus="s">
            <p>
                <a href="/mml/${e.id}"><c:out value="${e.updateAt}"/></a>
            </p>
        </c:forEach>
    </c:param>
</c:import>