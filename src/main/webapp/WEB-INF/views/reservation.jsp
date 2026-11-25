<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reservation | Movie Ticket Booking</title>
</head>
<body>
    <main>
        <h1>Reservation confirmed</h1>
        <p>Confirmation code: <strong><c:out value="${reservation.confirmationCode}" /></strong></p>
        <p>Movie: <c:out value="${reservation.movieTitle}" /></p>
        <p>Room: <c:out value="${reservation.auditoriumName}" /></p>
        <p>Starts: <c:out value="${reservation.startTime}" />; ends: <c:out value="${reservation.endTime}" /></p>
        <p>Seats:
            <c:forEach var="seatLabel" items="${reservation.seatLabels}" varStatus="loop">
                <c:if test="${not loop.first}">, </c:if><c:out value="${seatLabel}" />
            </c:forEach>
        </p>
        <p>Total: <c:out value="${reservation.totalPrice}" /></p>
        <p>Status: <c:out value="${reservation.status}" /></p>
        <p><a href="${pageContext.request.contextPath}/showtimes">Back to showtimes</a></p>
    </main>
</body>
</html>
