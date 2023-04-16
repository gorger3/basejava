<%@ page import="com.urise.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.getUuid()}&action=edit"><img src="img/pencil.png"></a></h2>
    <h3>Контакты</h3>
    <p>
        <c:forEach items="${resume.contacts}" var="contactEntry">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>

            ${contactEntry.key.toHtml(contactEntry.value)} <br>
        </c:forEach>
    </p>
</section>
<jsp:include page="fragments/footer.jsp"></jsp:include>
</body>
</html>
