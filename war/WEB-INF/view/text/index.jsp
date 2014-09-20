<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/view/common/layout.jsp">
    <c:param name="title">text</c:param>
    <c:param name="header">dolpen.net - text</c:param>
    <c:param name="content">
        <c:forEach var="e" items="${entries}" varStatus="s">
            <p>
                <a href="/text/${e.id}"><c:out value="${e.title}"/></a>
                <span style='color:grey'>(<c:out value="${e.updateAt}"/>)</span>
            </p>
        </c:forEach>
    </c:param>
</c:import>