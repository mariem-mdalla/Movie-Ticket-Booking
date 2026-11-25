<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Movies | Movie Ticket Booking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/site.css">
</head>
<body>
    <%@ include file="fragments/header.jspf" %>
    <main class="page-shell">
        <h1>Movie catalog</h1>
        <p class="page-intro">Browse the films currently in our cinema catalog.</p>

        <c:choose>
            <c:when test="${empty movies}">
                <section class="card"><p>No movies are available yet.</p></section>
            </c:when>
            <c:otherwise>
                <ul class="card-grid">
                    <c:forEach var="movie" items="${movies}">
                        <li class="card movie-card">
                            <h2><c:out value="${movie.title}" /></h2>
                            <p>Duration: <c:out value="${movie.durationMinutes}" /> minutes</p>
                            <p>Rating: <c:out value="${movie.rating}" /></p>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </main>
    <%@ include file="fragments/footer.jspf" %>
</body>
</html>
