# AI Usage Log

AI assistance was used while building this project. This log records the assistance for academic transparency; review the source and make sure you can explain every submitted line yourself.

| Date | Task and prompt summary | AI assistance provided | What I changed or verified | What I learned |
| --- | --- | --- | --- | --- |
| 2026-11-20 | Milestone 1: Jakarta EE walking skeleton. | AI generated `pom.xml`, `WelcomeServlet.java`, `welcome.jsp`, `README.md`, and the Milestone 1 learning log. | I ran the build, deployed the WAR to WildFly, and saw the page in my browser. | I can explain the browser-to-servlet-to-JSP request flow and why the JSP is inside `WEB-INF`. I still need to practise the `pom.xml` details. |
| 2026-11-22 | Milestone 2: JPA entity model. I wrote the design note and decided the entities, relationships, and seat rule. | AI generated the seven entity classes, two enums, and `docs/MILESTONE_2_LEARNING_LOG.md`. AI selected the initial reservation statuses. | I did not write the entity code myself. I reviewed `ReservationSeat.java` and ran the build. | See my Milestone 2 explanation below. I still need to practise explaining the other entities. |
| 2026-11-23 | Milestone 3: Development JPA persistence configuration. | AI inspected the WildFly `standalone.xml`, identified the configured H2 datasource, created `persistence.xml`, and wrote `docs/MILESTONE_3_LEARNING_LOG.md`. | I ran `mvnw.cmd clean package` and got `BUILD SUCCESS`. The supplied WildFly log shows MovieTicketBooking deployed, its persistence unit started on H2, and the seat unique constraint was created. The page response was not independently checked over HTTP. | *(User to fill in after reviewing the configuration.)* |
| 2026-11-24 | Milestone 4: List movies from the database. | AI inspected the WildFly JSTL modules and generated `MovieRepository`, `MovieService`, `MovieListServlet`, `SampleMovieInitializer`, `movies.jsp`, and `docs/MILESTONE_4_LEARNING_LOG.md`. | AI ran `mvnw.cmd -DskipTests clean package`; it completed with `BUILD SUCCESS`. Tests were skipped. The user's first CLI attempt was refused before WildFly started. The user later supplied a log showing the controller listening and MovieTicketBooking deployed; a separate WAR failed due to Java 25 bytecode. AI's CLI retry in its execution environment failed with `XNIO001001: No XNIO provider found`, so redeployment of the new WAR remains unverified. AI updated the learning log with the explanation and retry command. | *(User to fill in after reviewing and explaining the code.)* |
| 2026-11-24 | Milestone 5: List showtimes by date and display a read-only seat map. | AI inspected the existing entity mappings and generated the auditorium, seat, showtime, and reservation-seat repositories; `ShowtimeService` and display-view classes; the two servlets; the showtime, seat-map, and controlled-error JSPs; startup schedule seeding; and `docs/MILESTONE_5_LEARNING_LOG.md`. | AI ran `mvnw.cmd -DskipTests clean package`; see the result in the Milestone 5 learning log. Tests were not added or run. WildFly deployment and browser rendering remain for the user to verify. | *(User to fill in after reviewing and explaining the code.)* |

Before submission, update this file whenever AI helps with another task. Record the date, prompt summary, what the AI supplied, what you changed or checked, and what you learned. Follow your instructor's AI-use policy and do not submit code you cannot explain.

## Milestone 1: My decisions

- I chose Jakarta EE 11 with WildFly, WAR packaging, and Java 21.
- I chose a servlet that forwards to a JSP inside `WEB-INF`.
- AI generated the initial configuration, servlet, JSP, README, and learning log; I did not write those files myself.
- I ran the build, deployed the WAR to WildFly, and saw the welcome page in my browser.
- I can explain the request flow from browser to servlet to JSP, and why the JSP is inside `WEB-INF`.
- I still need to practise explaining the details in `pom.xml`.

## Milestone 2: What I changed / what I can explain

- I wrote the design note and decided the entities and relationships myself.
- I can explain that Showtime is its own table because a movie plays in several rooms and a room shows several movies. Showtime says which movie, which room, and when.
- I can explain the seat rule: the same seat can be booked for different showtimes but only once per showtime. The database enforces this with a unique constraint on `(showtime_id, seat_id)`, not just in Java.
- I can explain why `ReservationSeat` stores the showtime: a unique constraint can only compare columns in the same table, so `showtime_id` and `seat_id` both need to be in `ReservationSeat`.
- I did not write the entity code myself. I reviewed `ReservationSeat.java` and ran the build, but I still need to practise explaining the other entities.
