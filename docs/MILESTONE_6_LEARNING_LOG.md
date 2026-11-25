# Milestone 6 Learning Log: Booking Seats

AI wrote the Milestone 6 code from the requested behavior. This includes the temporary customer, repositories, service, servlets, JSP, JavaScript, and this learning log. Read the code and practise explaining it before presenting it as your own work.

## What was added

- `src/main/java/com/movieticket/repository/UserRepository.java` finds a user by ID or username and saves the temporary demo user. Its JPQL uses the named parameter `:username`.
- `src/main/java/com/movieticket/repository/ReservationRepository.java` saves reservations and looks one up by confirmation code. The query uses the named parameter `:code` and a JPA `EntityGraph` to load the showtime, movie, room, reservation-seat rows, and seats. It has no fetch joins, so it does not use fetch-join aliases.
- `src/main/java/com/movieticket/repository/ReservationSeatRepository.java` now also saves reservation-seat rows and flushes their persistence context. It remains the repository that reads which seat IDs are already taken.
- `src/main/java/com/movieticket/repository/SeatRepository.java` now finds selected seat IDs only when they belong to the requested auditorium. This lets the service reject a seat ID from another room.
- `src/main/java/com/movieticket/config/DemoCustomerInitializer.java` creates a `CUSTOMER` user named `demo-customer` once, if it is missing. The password field contains a placeholder, not a usable password hash. The source comment marks this as **TEMPORARY**. Replace this demo identity with the logged-in user's ID when authentication is added.
- `src/main/java/com/movieticket/service/BookingService.java` checks the showtime, user, and selected seats; limits bookings to 1–6 different seats; checks that each seat belongs to the showtime's room; calculates the total; creates the reservation and its seat rows; and loads confirmation details by code. It assigns the demo customer for this milestone, sets status to `CONFIRMED`, records the showtime price on each reserved seat, and generates a random confirmation code. `@TransactionAttribute(REQUIRED)` makes the booking and reservation lookup run in a container-managed transaction. The lookup converts the fetched reservation into a `ReservationView` before that transaction ends.
- `src/main/java/com/movieticket/service/SeatAlreadyTakenException.java` identifies a seat conflict. `@ApplicationException(rollback = true)` tells the Jakarta EE container to roll the transaction back when this exception leaves the service.
- `src/main/java/com/movieticket/service/view/ReservationView.java` carries only the movie, room, start/end time, seat labels, total, confirmation code, and reservation status needed by the reservation page. `SeatCellView` also carries a database seat ID for the checkboxes, and `ShowtimeView` carries the per-seat price for the summary.
- `src/main/java/com/movieticket/web/BookingServlet.java` handles `POST /book`. It reads form values, calls the service, and redirects to the confirmation page. If a seat was taken or the input is invalid, it redirects back to the seat page with a message. The servlet has no persistence logic.
- `src/main/java/com/movieticket/web/ReservationServlet.java` handles `GET /reservation?code=...`, asks the service for confirmation details, and forwards to the JSP. Missing and unknown codes use the controlled error page.
- `src/main/java/com/movieticket/web/SeatStatusServlet.java` handles `GET /showtimes/seats/status?id=...`. It asks the showtime service for the taken IDs and returns a small JSON object. This is a servlet response, not a REST resource.
- `src/main/java/com/movieticket/service/ShowtimeService.java` now also provides taken seat IDs after checking that the showtime exists. The browser polling endpoint uses this same service method as the seat map.
- `src/main/webapp/WEB-INF/views/seats.jsp` now contains a POST form with one real checkbox per free seat. CSS visually clips the checkbox without removing it from the DOM or keyboard tab order, so customers see only the colored seat. A focus outline shows which seat keyboard users are on. Free seats are yellow, selected seats green (the checked input controls this directly in CSS, with JavaScript also updating its class), and taken seats gray. The script URL has a version query to avoid a stale cached JavaScript file. The form can submit without JavaScript; server-side validation remains authoritative.
- `src/main/webapp/seat-map.js` updates the selected seat labels and total, disables the confirm button when none are selected, and checks the JSON status endpoint every five seconds. If a selected seat becomes taken, it unchecks and disables it and announces which seat changed. It uses browser JavaScript only; no library was added.
- `src/main/webapp/index.jsp` forwards the app's root page to `/welcome`. `WelcomeServlet` now maps only to `/welcome`. Its previous `/` mapping caught every otherwise-unmatched URL, including `/seat-map.js`, and returned the welcome HTML with HTTP 200 instead of serving the JavaScript. That was why the selection color and summary did not respond. Narrowing the servlet mapping lets WildFly's default servlet deliver the static JavaScript file.
- `src/main/webapp/WEB-INF/views/reservation.jsp` displays the confirmation code, movie, room, start/end times, seat labels, total, and status using JSTL and EL.
- `src/main/webapp/WEB-INF/views/error.jsp` shows a supplied safe message for expected input errors, or a generic message when the container forwards an unexpected error. It never prints an exception or stack trace.
- `src/main/webapp/WEB-INF/web.xml` sends 404, 500, and uncaught `Throwable` error dispatches to the controlled error JSP.

## Why flush and rollback matter for the seat rule

The service first checks whether the requested seats already appear in `ReservationSeat`. That catches an already-taken seat before creating a reservation. This check alone cannot prevent a race: two requests can both see a seat as free before either one saves it.

The database unique constraint on `(showtime_id, seat_id)` is the final protection. After creating the reservation and its seat rows, the service calls `flush()`. A flush sends pending database changes now, so the unique constraint can reject a duplicate while the service is still handling the booking. With identity-generated IDs, a database insert can also happen during `persist()`, so the service catches a uniqueness error around both the saves and the explicit flush.

If a conflict occurs, the service translates the H2 unique-violation SQL state (`23505`) into `SeatAlreadyTakenException`. The exception is marked `rollback = true`. The Jakarta EE transaction manager then undoes every database change in this service call: the reservation row and all of its `ReservationSeat` rows. That prevents a half-saved reservation. The servlet redirects with the requested message: “that seat was just taken, please choose another”.

The database constraint is still necessary even with polling. The browser's five-second refresh improves the display, but two users can click during the same interval; only the database can reliably decide which transaction wins.

## Strict JPQL compliance and the reservation DTO

The old reservation query put aliases after fetch joins, for example `JOIN FETCH reservation.showtime showtime` and `LEFT JOIN FETCH reservation.reservationSeats reservationSeat`. Those aliases are accepted by some providers as an extension, but strict JPQL compliance rejects aliases on fetch joins. The error happened while parsing the query, before the JSP could be rendered.

The replacement query has no fetch joins and keeps the named parameter `:code`. A JPA `EntityGraph` tells the provider to load the reservation's showtime and its movie/room, plus its reservation seats and their seats. I checked `ShowtimeRepository` too: its fetch joins have no alias on the joined association, so those queries already comply and did not need changes.

`BookingService.findByConfirmationCode()` runs in a `REQUIRED` transaction. It reads the loaded entities and makes a small `ReservationView` containing only display values, including `ReservationStatus`. The servlet passes that DTO to the JSP. The JSP does not receive entities or touch lazy relationships after the transaction has closed.

Unexpected exceptions and 404/500 responses are mapped in `web.xml` to `error.jsp`. The page displays a generic safe message when no expected-message attribute exists, and never renders `exception` or `stackTrace` values.

## Temporary demo identity

All bookings in this milestone belong to `demo-customer` because login is not implemented. The account is explicitly temporary, and its password field is only a placeholder to satisfy the required database column. It is not a credential and must be replaced by the authenticated user's ID once login and password hashing are built. The reservation page currently has no ownership check, as requested.

## Build and deployment

The build command for this milestone is:

```powershell
.\mvnw.cmd -DskipTests clean package
```

For this strict-compliance change, the first wrapper clean failed to remove a locked `target/test-classes`; the installed `mvn clean package` fallback could not open its clean-plugin lock in `.m2`. After project write permission was granted, `mvnw.cmd -DskipTests clean package` completed with `BUILD SUCCESS`, compiling 34 Java files with Java release 21 and creating the WAR. Tests were skipped. I did not deploy this strict-compliance change. The previous follow-up deployment verified that the seat page and versioned JavaScript asset returned HTTP 200 and that the app root still showed the welcome page; the updated reservation query and error mappings still need deployment verification.

To run the server, open a first PowerShell terminal:

```powershell
Set-Location C:\servers\wildfly-41.0.1.Final
.\bin\standalone.bat
```

Once WildFly reports its management interface listening on port 9990, build from the project root:

```powershell
Set-Location C:\Users\MSI\IdeaProjects\MovieTicketBooking
.\mvnw.cmd -DskipTests clean package
```

Then run the deploy command with PowerShell's current folder set to WildFly's installation root. The CLI looks for `jboss-modules.jar` relative to that root, so running it from the project folder can fail:

```powershell
Set-Location C:\servers\wildfly-41.0.1.Final
& '.\bin\jboss-cli.bat' --controller=127.0.0.1:9990 --timeout=15000 --connect --command='deploy C:\Users\MSI\IdeaProjects\MovieTicketBooking\target\MovieTicketBooking-1.0-SNAPSHOT.war --force'
```

Open `http://localhost:8080/MovieTicketBooking-1.0-SNAPSHOT/showtimes`, select a showtime, choose one to six seats, and confirm. The seat page and JavaScript asset were checked over HTTP after redeployment, but selecting seats and completing a booking still need manual browser verification.

## What each command means

- `Set-Location <folder>` changes the current PowerShell folder. `cd` is a shorter alias. The WildFly command starts from its installation root; the Maven command starts from the project root so it can find `pom.xml`.
- `.\bin\standalone.bat` runs WildFly's standalone server script from the current WildFly folder. Keep that terminal open while the server runs.
- `.mvnw.cmd` runs this project's Maven wrapper from the project folder. `-DskipTests` tells Maven to skip test execution. `clean` clears generated files under `target`; `package` compiles and creates the WAR under `target`.
- PowerShell's `& '.\bin\jboss-cli.bat'` call operator runs WildFly's administration tool. Run it with the current folder set to the WildFly installation root. `--controller=127.0.0.1:9990` selects the local management interface; `--timeout=15000` allows up to 15 seconds; `--connect` connects to the controller; `--command=...` asks it to deploy the WAR; and `--force` replaces an existing deployment with the same name.
- `http://localhost:8080/MovieTicketBooking-1.0-SNAPSHOT/showtimes` is a browser address, not a PowerShell command. `localhost` means this computer; port `8080` serves the application; `/showtimes` is its servlet route.

## What to practise explaining

- Why the service checks seat ownership before saving, but still relies on the database constraint to handle races.
- What `flush()` does and how the transaction rollback removes both the reservation and all its seat rows.
- How the browser submits checkbox values and why the server validates them again.
- How the five-second status request updates the seat colors and deselects newly taken seats.
- Why the demo customer is temporary and why its placeholder is not a real password hash.
- How the reservation view gets its data through repository, service, servlet, and JSP layers.
