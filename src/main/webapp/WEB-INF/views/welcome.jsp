<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Welcome | Movie Ticket Booking</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/site.css">
</head>
<body>
    <%@ include file="fragments/header.jspf" %>
    <main class="page-shell">
        <section class="card">
            <h1>Welcome to Movie Ticket Booking</h1>
            <p>Explore films and today's showtimes.</p>
            <a class="button" href="${pageContext.request.contextPath}/showtimes">Browse showtimes</a>
        </section>
    </main>
    <%@ include file="fragments/footer.jspf" %>
</body>
</html>
