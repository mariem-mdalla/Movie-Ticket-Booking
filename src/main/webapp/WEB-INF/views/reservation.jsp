<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reservation | Movie Ticket Booking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/site.css">
</head>
<body>
    <%@ include file="fragments/header.jspf" %>
    <main class="page-shell">
        <h1>Reservation confirmed</h1>
        <section class="card reservation-card">
            <p class="reservation-code">Confirmation code: <strong><c:out value="${reservation.confirmationCode}" /></strong></p>
            <h2><c:out value="${reservation.movieTitle}" /></h2>
            <p><c:out value="${reservation.displayDate}" /></p>
            <p><c:out value="${reservation.displayStartTime}" />–<c:out value="${reservation.displayEndTime}" /> · <c:out value="${reservation.auditoriumName}" /></p>
            <p>Seats:
                <c:forEach var="seatLabel" items="${reservation.seatLabels}" varStatus="loop">
                    <c:if test="${not loop.first}">, </c:if><c:out value="${seatLabel}" />
                </c:forEach>
            </p>
            <p>Total: <span class="booking-total"><c:out value="${reservation.totalPrice}" /></span></p>
            <p>Status: <c:out value="${reservation.status}" /></p>
        </section>
        <p><a class="button" href="${pageContext.request.contextPath}/showtimes">Back to showtimes</a></p>
    </main>
    <%@ include file="fragments/footer.jspf" %>
</body>
</html>
