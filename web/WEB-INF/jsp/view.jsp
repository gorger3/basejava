<%@ page import="com.urise.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    <p>
    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.Section>"/>
            <jsp:useBean id="HtmlHelper" class="com.urise.webapp.util.HtmlHelper"/>
            <%--заработало, когда я использовал class вместо type: and most people seem to recommend using class="..." instead of type="..."
             https://stackoverflow.com/questions/17842892/java-lang-instantiationexception-bean-name-not-found-within-scope --%>
            <dl>
                <dt>${sectionEntry.key.title}</dt>
                <dd>${HtmlHelper.sectionContentToHtml(sectionEntry)}</dd>
            </dl>
        </c:forEach>
    <p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>


<%--                <c:choose>--%>
<%--                    <c:when test="${sectionEntry.key.name() == 'ACHIEVEMENT' || sectionEntry.key.name() == 'QUALIFICATIONS'}">--%>
<%--                        <dd>${HtmlHelper.sectionContentToHtml(sectionEntry)}</dd>--%>
<%--&lt;%&ndash;                            <textarea name="${sectionEntry.key.title}" rows="5"cols="50">&ndash;%&gt;--%>
<%--&lt;%&ndash;                                    ${HtmlHelper.sectionContentToHtml(sectionEntry)}&ndash;%&gt;--%>
<%--&lt;%&ndash;                            </textarea>&ndash;%&gt;--%>
<%--&lt;%&ndash;                        </dd>&ndash;%&gt;--%>
<%--                    </c:when>--%>
<%--                    <c:otherwise>--%>
<%--                        <dd><input type="text" name="${sectionEntry.key}" size="50" value="${sectionEntry.value}"></dd>--%>
<%--                    </c:otherwise>--%>
<%--                </c:choose>--%>
