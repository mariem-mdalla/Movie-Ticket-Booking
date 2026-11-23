# AI Usage Log

AI assistance was used while building this project. This log records the assistance for academic transparency; review the source and make sure you can explain every submitted line yourself.

| Date | Task and prompt summary | AI assistance provided | What I changed or verified | What I learned |
| --- | --- | --- | --- | --- |
| 2026-11-20 | Implement walking skeleton; inspect existing project, align Jakarta EE 11 and Java 21 build, add servlet/JSP welcome flow, README and learning notes. | Suggested the minimal Maven configuration, servlet-to-JSP request flow, documentation structure, and manual WildFly deployment instructions. | Inspected the existing POM, wrapper settings, project files, Java/Maven versions, Git status, and installed WildFly path. The implementation is being checked with the Maven build; server deployment is intentionally not part of this run. | The server provides Jakarta APIs at runtime; a servlet can set request data and forward to a protected JSP; EL renders request attributes. |
| 2026-11-22 | Milestone 2: JPA entity model. I wrote the design note and decided the entities, relationships, and seat rule. | AI generated the seven entity classes, two enums, and `docs/MILESTONE_2_LEARNING_LOG.md`. AI selected the initial reservation statuses. | I did not write the entity code myself. I reviewed `ReservationSeat.java` and ran the build. | See my Milestone 2 explanation below. I still need to practise explaining the other entities. |

Before submission, update this file whenever AI helps with another task. Record the date, prompt summary, what the AI supplied, what you changed or checked, and what you learned. Follow your instructor's AI-use policy and do not submit code you cannot explain.

## Milestone 2: What I changed / what I can explain

- I wrote the design note and decided the entities and relationships myself.
- I can explain that Showtime is its own table because a movie plays in several rooms and a room shows several movies. Showtime says which movie, which room, and when.
- I can explain the seat rule: the same seat can be booked for different showtimes but only once per showtime. The database enforces this with a unique constraint on `(showtime_id, seat_id)`, not just in Java.
- I can explain why `ReservationSeat` stores the showtime: a unique constraint can only compare columns in the same table, so `showtime_id` and `seat_id` both need to be in `ReservationSeat`.
- I did not write the entity code myself. I reviewed `ReservationSeat.java` and ran the build, but I still need to practise explaining the other entities.
