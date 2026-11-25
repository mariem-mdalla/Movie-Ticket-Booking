<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Seat Map | Movie Ticket Booking</title>
    <style>
        .seat { display: inline-flex; align-items: center; gap: 0.2rem; margin: 0.2rem; padding: 0.45rem; border-radius: 0.25rem; }
        .free { background-color: #ffdf5d; color: #222; }
        .selected { background-color: #58bd72; color: #102b17; }
        .taken { background-color: #999; color: #fff; }
        .seat:has(input[type="checkbox"]:checked) {
            background-color: #58bd72;
            color: #102b17;
        }
        .seat input[type="checkbox"] {
            position: absolute;
            width: 1px;
            height: 1px;
            margin: -1px;
            padding: 0;
            overflow: hidden;
            clip: rect(0, 0, 0, 0);
            white-space: nowrap;
            border: 0;
        }
        .seat:focus-within {
            outline: 3px solid #1646c4;
            outline-offset: 2px;
        }
        #booking-summary { margin: 1rem 0; padding: 1rem; border: 1px solid #888; }
    </style>
</head>
<body>
    <main>
        <h1>Choose your seats</h1>
        <h2><c:out value="${seatMap.showtime.movieTitle}" /></h2>
        <p>Room: <c:out value="${seatMap.showtime.auditoriumName}" /></p>
        <p>Starts: <c:out value="${seatMap.showtime.startTime}" />; ends: <c:out value="${seatMap.showtime.endTime}" /></p>
        <p>Price per seat: <c:out value="${seatMap.showtime.price}" /></p>

        <c:if test="${not empty param.message}">
            <p id="seat-update-message" role="alert"><c:out value="${param.message}" /></p>
        </c:if>
        <c:if test="${empty param.message}">
            <p id="seat-update-message" role="alert"></p>
        </c:if>

        <form id="booking-form" method="post" action="${pageContext.request.contextPath}/book"
              data-price="${seatMap.showtime.price}"
              data-status-url="${pageContext.request.contextPath}/showtimes/seats/status?id=${seatMap.showtime.id}">
            <input type="hidden" name="showtimeId" value="${seatMap.showtime.id}">
            <p>Choose up to 6 seats. Gray seats are already taken.</p>
            <c:forEach var="row" items="${seatMap.rows}">
                <section aria-label="Row ${row.label}">
                    <strong><c:out value="${row.label}" /></strong>
                    <c:forEach var="seat" items="${row.seats}">
                        <label class="seat ${seat.taken ? 'taken' : 'free'}">
                            <input type="checkbox" name="seatIds" value="${seat.id}"
                                   data-label="${row.label}${seat.seatNumber}"
                                   <c:if test="${seat.taken}">disabled</c:if>>
                            <span><c:out value="${row.label}${seat.seatNumber}" /></span>
                        </label>
                    </c:forEach>
                </section>
            </c:forEach>

            <section id="booking-summary" aria-live="polite">
                <h3>Reservation summary</h3>
                <p>Selected seats: <span id="selected-seats">None</span></p>
                <p>Total: <span id="booking-total">0.00</span></p>
            </section>
            <button id="confirm-booking" type="submit">Confirm reservation</button>
        </form>
        <noscript><p>JavaScript is off. You can still select seats and submit; the server will check your selection.</p></noscript>
        <p><a href="${pageContext.request.contextPath}/showtimes">Back to showtimes</a></p>
    </main>
    <script src="${pageContext.request.contextPath}/seat-map.js?v=milestone6" defer></script>
</body>
</html>
