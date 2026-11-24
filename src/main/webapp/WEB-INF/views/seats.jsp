<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Seat Map | Movie Ticket Booking</title>
    <style>
        .seat { display: inline-block; min-width: 2rem; margin: 0.2rem; padding: 0.45rem; text-align: center; }
        .taken { background-color: #999; color: #fff; }
        .free { background-color: #ffdf5d; color: #222; }
    </style>
</head>
<body>
    <main>
        <h1>Seat map</h1>
        <h2><c:out value="${seatMap.showtime.movieTitle}" /></h2>
        <p>Room: <c:out value="${seatMap.showtime.auditoriumName}" /></p>
        <p>Starts: <c:out value="${seatMap.showtime.startTime}" />; ends: <c:out value="${seatMap.showtime.endTime}" /></p>
        <p><span class="seat free">Free</span> <span class="seat taken">Taken</span></p>
        <c:forEach var="row" items="${seatMap.rows}">
            <section aria-label="Row ${row.label}">
                <strong><c:out value="${row.label}" /></strong>
                <c:forEach var="seat" items="${row.seats}">
                    <span class="seat ${seat.taken ? 'taken' : 'free'}">
                        <c:out value="${row.label}${seat.seatNumber}" />
                    </span>
                </c:forEach>
            </section>
        </c:forEach>
        <p><a href="${pageContext.request.contextPath}/showtimes">Back to showtimes</a></p>
    </main>
</body>
</html>
