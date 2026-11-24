<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Request error | Movie Ticket Booking</title>
</head>
<body>
    <main>
        <h1>We could not show that page</h1>
        <p><c:out value="${errorMessage}" /></p>
        <p><a href="${pageContext.request.contextPath}/showtimes">Return to showtimes</a></p>
    </main>
</body>
</html>
