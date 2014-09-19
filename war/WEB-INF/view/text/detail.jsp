<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<meta charset="UTF-8">
<link href="/static/css/common.css" media="screen" rel="stylesheet" type="text/css">
<title>dolpen.net - <c:out value="${entry.title}"/></title>
<div id="all">
    <h1><a href="/">dolpen.net</a></h1>

    <h2><c:out value="${entry.title}"/></h2>

    <c:out value="${entry.compiled}" escapeXml="false"/>

    <p><a href="/text/">back to index of texts</a></p>
</div>
