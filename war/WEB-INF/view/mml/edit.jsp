<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/view/common/layout.jsp">
    <c:param name="exheader">
        <script src="//code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="/static/js/swfobject.js"></script>
        <script src="/static/js/jquery.jsmml.js"></script>
        <script src="/static/js/mml.js"></script>
    </c:param>
    <c:param name="title">mml:edit</c:param>
    <c:param name="header">MMLの編集</c:param>
    <c:param name="content">
        <form action="/mml/edit" method="post">
            <textarea id="source" name="source" rows="20" cols="80"><c:out value="${entry.source}" escapeXml="true"/></textarea><br />
            <input type="hidden" name="id" value="${entry.id}" />
            <input type="button" id="play" value="PLAY" disabled/>
            <input type="button" id="stop" value="STOP" disabled/>
            <input type="text" id="status" value="-" disabled />
            <br />
            <button type="submit">更新</button>
        </form>
        <p><a href="/mml/">back to index of mml</a></p>
    </c:param>
</c:import>
