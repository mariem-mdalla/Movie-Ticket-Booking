<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Choose Seats | Movie Ticket Booking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/site.css">
</head>
<body>
    <%@ include file="fragments/header.jspf" %>
    <main class="page-shell seat-map-page">
        <h1>Choose your seats</h1>
        <section class="card seat-map-card">
            <div>
                <h2><c:out value="${seatMap.showtime.movieTitle}" /></h2>
                <p><c:out value="${seatMap.showtime.displayDate}" /> · <c:out value="${seatMap.showtime.displayStartTime}" />–<c:out value="${seatMap.showtime.displayEndTime}" /> · <c:out value="${seatMap.showtime.auditoriumName}" /></p>
                <p>Price per seat: <c:out value="${seatMap.showtime.price}" /></p>
            </div>

            <c:if test="${not empty param.message}">
                <p id="seat-update-message" class="error-message" role="alert"><c:out value="${param.message}" /></p>
            </c:if>
            <c:if test="${empty param.message}">
                <p id="seat-update-message" class="error-message" role="alert"></p>
            </c:if>

            <div class="seat-legend" aria-label="Seat key">
                <span><i class="legend-swatch legend-free"></i>Free</span>
                <span><i class="legend-swatch legend-selected"></i>Selected</span>
                <span><i class="legend-swatch legend-taken"></i>Taken</span>
            </div>
            <div class="screen-bar">Screen</div>

            <form id="booking-form" method="post" action="${pageContext.request.contextPath}/book"
                  data-price="${seatMap.showtime.price}"
                  data-status-url="${pageContext.request.contextPath}/showtimes/seats/status?id=${seatMap.showtime.id}">
                <input type="hidden" name="showtimeId" value="${seatMap.showtime.id}">
                <p class="page-intro">Choose up to 6 seats. Gray seats are already taken.</p>
                <div class="seat-map-grid">
                    <c:forEach var="row" items="${seatMap.rows}">
                        <section class="seat-row" aria-label="Row ${row.label}">
                            <span class="row-label"><c:out value="${row.label}" /></span>
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
                </div>

                <section id="booking-summary" class="card booking-summary" aria-live="polite">
                    <h2>Reservation summary</h2>
                    <p>Selected seats: <span id="selected-seats">None</span></p>
                    <p>Total: <span id="booking-total" class="booking-total">0.00</span></p>
                    <button id="confirm-booking" type="submit">Confirm reservation</button>
                </section>
            </form>
            <noscript><p>JavaScript is off. You can still select seats and submit; the server will check your selection.</p></noscript>
            <a href="${pageContext.request.contextPath}/showtimes">Back to showtimes</a>
        </section>
    </main>
    <%@ include file="fragments/footer.jspf" %>
    <script src="${pageContext.request.contextPath}/seat-map.js?v=milestone6b" defer></script>
</body>
</html>
