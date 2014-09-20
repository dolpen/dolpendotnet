<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<meta charset="UTF-8">
<link href="/static/css/common.css" media="screen" rel="stylesheet" type="text/css">
<title>${param.title} - dolpen.net</title>
<c:out value="${param.exheader}" escapeXml="false" />
<div id="all">
    <h1><c:out value="${param.header}" escapeXml="false" /></h1>
    <c:out value="${param.content}" escapeXml="false" />
    <p><a href="/">back to the top</a></p>
</div>
