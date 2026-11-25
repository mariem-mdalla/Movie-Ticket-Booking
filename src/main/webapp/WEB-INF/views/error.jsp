<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Request error | Movie Ticket Booking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/site.css">
</head>
<body>
    <%@ include file="fragments/header.jspf" %>
    <main class="page-shell">
        <section class="card error-card">
            <h1>We could not show that page</h1>
            <c:choose>
                <c:when test="${not empty errorMessage}">
                    <p class="error-message"><c:out value="${errorMessage}" /></p>
                </c:when>
                <c:otherwise>
                    <p class="error-message">The request could not be completed. Please check the address or return to showtimes.</p>
                </c:otherwise>
            </c:choose>
            <a class="button" href="${pageContext.request.contextPath}/showtimes">Return to showtimes</a>
        </section>
    </main>
    <%@ include file="fragments/footer.jspf" %>
</body>
</html>
