package com.example.movieticket;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
// servlet hia java class testa9bal w tet3emel ma3 web requests
@WebServlet("/welcome") // Keep this servlet on one URL so static files can use the default servlet.
public class WelcomeServlet extends HttpServlet {
// doGet() hia method tetnafadh ki el browser ya3mal HTTP GET Request
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // hatina value esmha Movie Ticket Booking fi request w samineha projectName
        request.setAttribute("projectName", "Movie Ticket Booking");
        // hni 9olna lel server 5oudh nafs el request haka w ab3thou lel JSP bch el page twarih
        request.getRequestDispatcher("/WEB-INF/views/welcome.jsp").forward(request, response);
        //hatina forward mch redirect khater el forward ysir dekhel el server ama redirect yotlob mel browser yamal request jdid
    }
}
