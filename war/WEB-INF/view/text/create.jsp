<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/view/common/layout.jsp">
    <c:param name="title">text:create</c:param>
    <c:param name="header">エントリの新規作成</c:param>
    <c:param name="content">
        <form action="/text/create" method="post">
            <p> タイトル : <input type="text" name="title"/></p>

            <div> 本文 : <br/>
                <textarea name="source" rows="20" cols="80"></textarea>
            </div>
            <button type="submit">作成</button>
        </form>
        <p><a href="/text/">back to index of text</a></p>
    </c:param>
</c:import>