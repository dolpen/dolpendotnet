<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/view/common/layout.jsp">
    <c:param name="title">text:edit</c:param>
    <c:param name="header">エントリの編集</c:param>
    <c:param name="content">
        <form action="/text/edit" method="post">
            <p> タイトル : <input type="text" name="title" value="<c:out value="${entry.title}" escapeXml="true" />"/></p>

            <div> 本文 : <br/>
                <textarea name="source" rows="20" cols="80"><c:out value="${entry.source}" escapeXml="true"/></textarea>
            </div>
            <input type="hidden" name="id" value="${entry.id}"/>
            <button type="submit">更新</button>
        </form>
        <p><a href="/text/">back to index of text</a></p>
    </c:param>
</c:import>


