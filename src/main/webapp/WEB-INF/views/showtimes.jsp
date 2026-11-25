<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Today's Showtimes | Movie Ticket Booking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/site.css">
</head>
<body>
    <%@ include file="fragments/header.jspf" %>
    <main class="page-shell">
        <h1>Showtimes</h1>
        <p class="page-intro">Choose a film and time, then check which seats are available.</p>

        <form class="card filter-card" method="get" action="${pageContext.request.contextPath}/showtimes">
            <label for="date">Choose a date
                <input id="date" name="date" type="date" value="${selectedDate}">
            </label>
            <button type="submit">Show times</button>
        </form>

        <c:choose>
            <c:when test="${empty showtimes}">
                <h2 class="date-heading">${selectedDate.dayOfMonth} ${selectedDate.month} ${selectedDate.year}</h2>
                <section class="card"><p>No showtimes are available for this date.</p></section>
            </c:when>
            <c:otherwise>
                <h2 class="date-heading"><c:out value="${showtimes[0].displayDate}" /></h2>
                <ul class="card-grid showtime-list">
                    <c:forEach var="showtime" items="${showtimes}">
                        <li class="card showtime-card">
                            <p class="showtime-time"><c:out value="${showtime.displayStartTime}" />–<c:out value="${showtime.displayEndTime}" /></p>
                            <h2><c:out value="${showtime.movieTitle}" /></h2>
                            <p>Room: <c:out value="${showtime.auditoriumName}" /></p>
                            <a class="button card-link" href="${pageContext.request.contextPath}/showtimes/seats?id=${showtime.id}">Choose seats</a>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </main>
    <%@ include file="fragments/footer.jspf" %>
</body>
</html>
