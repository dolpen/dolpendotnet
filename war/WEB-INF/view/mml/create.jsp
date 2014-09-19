<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<meta charset="UTF-8">
<link href="/static/css/common.css" media="screen" rel="stylesheet" type="text/css">
<script src="//code.jquery.com/jquery-2.1.1.min.js"></script>
<script src="/static/js/swfobject.js"></script>
<script src="/static/js/jquery.jsmml.js"></script>
<script src="/static/js/mml.js"></script>

<title>dolpen.net - mml::create</title>

<div id="all">
    <h1><a href="/">dolpen.net</a></h1>

    <form action="/mml/createPost" method="post">
        <textarea id="source" name="source" rows="20" cols="80"></textarea><br />
        <input type="button" id="play" value="PLAY" disabled/>
        <input type="button" id="stop" value="STOP" disabled/>
        <input type="text" id="status" value="-" disabled />
        <br />
        <button type="submit">作成</button>
    </form>

    <p><a href="/mml/">back to index of mml</a></p>
</div>

