<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<meta charset="UTF-8">
<link href="/static/css/common.css" media="screen" rel="stylesheet" type="text/css">
<title>dolpen.net - text</title>

<div id="all">

    <p><a href="/">back to the top</a></p>


    <h1>dolpen.net - text</h1>

    <c:forEach var="e" items="${entries}" varStatus="s">
        <p>
            <a href="/text/e/${e.id}"><c:out value="${e.title}"/></a>
            <span style='color:grey'>(<c:out value="${e.updateAt}"/>)</span>
        </p>
    </c:forEach>

    <p>
        <c:if test="${page > 1}">
            <a href="/text/${page - 1}">&lt;</a>
        </c:if>
        <a href="/text/${page + 1}">&gt;</a>
    </p>
</div>