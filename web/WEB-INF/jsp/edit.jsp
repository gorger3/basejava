<%@ page import="com.urise.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.TextSection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>

    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"></jsp:include>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты</h3>
        <p>
            <c:forEach var="contactType" items="${ContactType.values()}">
                <jsp:useBean id="contactType" type="com.urise.webapp.model.ContactType"/>
        <dl>
            <dt>${contactType.title}</dt>
            <dd><input type="text" name="${contactType.name()}" size="30" value="${resume.getContact(contactType)}">
            </dd>
        </dl>
        </c:forEach>
        </p>
        <h3>Секции</h3>
        <jsp:useBean id="HtmlHelper" class="com.urise.webapp.util.HtmlHelper"/>
        <c:forEach var="sectionType" items="${SectionType.values()}">
            <jsp:useBean id="sectionType" type="com.urise.webapp.model.SectionType"/>
<%--            <c:set var="section" value="${resume.getSection(sectionType)}" />--%>
            <dl>
                <dt>${sectionType.title}</dt>
                <dd>${HtmlHelper.htmlToSectionContent(sectionType, resume)}</dd>
            </dl>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
    </form>
    <button onclick="window.history.back()">Отменить</button>
</section>
<jsp:include page="fragments/footer.jsp"></jsp:include>
</body>
</html>
