<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Movies | Movie Ticket Booking</title>
</head>
<body>
    <main>
        <h1>Movies</h1>

        <c:choose>
            <c:when test="${empty movies}">
                <p>No movies are available yet.</p>
            </c:when>
            <c:otherwise>
                <ul>
                    <c:forEach var="movie" items="${movies}">
                        <li>
                            <h2><c:out value="${movie.title}" /></h2>
                            <p>Duration: <c:out value="${movie.durationMinutes}" /> minutes</p>
                            <p>Rating: <c:out value="${movie.rating}" /></p>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </main>
</body>
</html>
