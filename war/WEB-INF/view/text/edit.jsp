<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<meta charset="UTF-8">
<link href="/static/css/common.css" media="screen" rel="stylesheet" type="text/css">
<title>dolpen.net - text::edit</title>

<div id="all">
    <h1><a href="/">dolpen.net</a></h1>

    <form action="/text/editPost" method="post">
        <p> タイトル : <input type="text" name="title" value="<c:out value="${entry.title}" escapeXml="true" />"/></p>

        <div> 本文 : <br/>
            <textarea name="source" rows="20" cols="80"><c:out value="${entry.source}" escapeXml="true" /></textarea>
        </div>
        <input type="hidden" name="id" value="${entry.id}" />
        <button type="submit">更新</button>
    </form>

    <p><a href="/text/">back to index of texts</a></p>
</div>

