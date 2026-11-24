<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Showtimes | Movie Ticket Booking</title>
</head>
<body>
    <main>
        <h1>Showtimes</h1>
        <form method="get" action="${pageContext.request.contextPath}/showtimes">
            <label for="date">Choose a date:</label>
            <input id="date" name="date" type="date" value="${selectedDate}">
            <button type="submit">Show</button>
        </form>

        <c:choose>
            <c:when test="${empty showtimes}">
                <p>No showtimes are available for this date.</p>
            </c:when>
            <c:otherwise>
                <ul>
                    <c:forEach var="showtime" items="${showtimes}">
                        <li>
                            <h2><c:out value="${showtime.movieTitle}" /></h2>
                            <p>Room: <c:out value="${showtime.auditoriumName}" /></p>
                            <p>Starts: <c:out value="${showtime.startTime}" /></p>
                            <p>Ends: <c:out value="${showtime.endTime}" /></p>
                            <a href="${pageContext.request.contextPath}/showtimes/seats?id=${showtime.id}">View seats</a>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </main>
</body>
</html>
