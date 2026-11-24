# Milestone 5 Learning Log: Showtimes and Read-Only Seat Map

This milestone adds a read-only path from a date in the browser to showtimes and then to a seat map. AI generated the new repositories, service and view classes, servlets, JSPs, startup seed extension, and this log. Review the files and practise explaining them before treating the code as your own understanding.

## What we built and why

- `src/main/java/com/movieticket/repository/AuditoriumRepository.java` is the only new class that reads or writes auditorium rows. It lists rooms and saves them for the development sample data.
- `src/main/java/com/movieticket/repository/SeatRepository.java` reads seats for one room, sorted by row and number, and saves seats for the sample rooms.
- `src/main/java/com/movieticket/repository/ShowtimeRepository.java` reads and saves showtimes. The date query uses named parameters (`:start` and `:end`) and `JOIN FETCH` to load the movie and auditorium at the same time. That makes their titles/names available without a lazy-loading error after the repository returns.
- `src/main/java/com/movieticket/repository/ReservationSeatRepository.java` gets the seat IDs that already have a `ReservationSeat` row for a showtime. This is the data the service uses to mark seats taken.
- `src/main/java/com/movieticket/service/ShowtimeService.java` is the application layer. It defaults a missing date to today, asks the repositories for data, calculates each end time by calling the existing `Showtime.calculateEndTime()`, builds a row-by-row seat map, and creates sample rooms/seats/showtimes when those tables are empty. It is the only new place that groups seat data and applies the taken/free display rule. It does not use `EntityManager`.
- `src/main/java/com/movieticket/service/view/ShowtimeView.java` contains the values needed for one showtime display: ID, movie title, room name, start, and calculated end.
- `src/main/java/com/movieticket/service/view/SeatMapView.java` combines one showtime display with the list of seat rows.
- `src/main/java/com/movieticket/service/view/SeatRowView.java` represents one labeled row and its seats.
- `src/main/java/com/movieticket/service/view/SeatCellView.java` represents one seat number and whether it is taken. These small view classes keep the JSP from needing to understand entity relationships or calculate anything.
- `src/main/java/com/movieticket/config/SampleMovieInitializer.java` still runs at application startup. It seeds movies first, then asks the showtime service to seed the room and schedule data. It does not use `EntityManager` itself.
- `src/main/java/com/movieticket/web/ShowtimeListServlet.java` handles `GET /showtimes`. It reads the optional `date` parameter, rejects invalid dates with a helpful error page, asks the service for showtimes, and forwards to the JSP.
- `src/main/java/com/movieticket/web/SeatMapServlet.java` handles `GET /showtimes/seats?id=...`. It checks that the ID is a positive number, asks the service for the map, and returns a controlled 400 or 404 page for invalid or unknown IDs.
- `src/main/webapp/WEB-INF/views/showtimes.jsp` uses JSTL and EL to show the date form and each movie, room, start time, calculated end time, and seat-map link. It has no Java code.
- `src/main/webapp/WEB-INF/views/seats.jsp` renders rows and seat labels. CSS makes free seats yellow and taken seats gray. This page is read-only: it has no booking controls.
- `src/main/webapp/WEB-INF/views/error.jsp` displays a short, escaped message for a bad date or seat-map request instead of exposing an exception or stack trace.

The startup sample data is 2 rooms, 48 seats in each room (rows A through F, seats 1 through 8), and 12 showtimes: three per room on each of today and tomorrow. The sample price is `12.50` in unspecified project currency units. The seeder runs only when both the auditorium and showtime tables are empty, to avoid adding duplicate sample rows on later starts.

## How a request flows

For the showtime list, the browser sends `/showtimes?date=YYYY-MM-DD`. The servlet parses the date and calls `ShowtimeService`. The service turns the date into a start-of-day and next-day range. `ShowtimeRepository` uses that range in JPQL and fetches the linked movie and room. The service creates simple display objects, and the JSP renders them.

For the seat map, the link sends the showtime ID to `/showtimes/seats?id=...`. The repository loads the showtime together with its movie and room. `SeatRepository` loads all seats in that room, and `ReservationSeatRepository` loads IDs already used for this showtime. The service compares those IDs and groups seats into rows. The JSP only chooses the gray or yellow class based on the `taken` value.

At this milestone, any `ReservationSeat` row counts as taken, because booking status and cancellation behavior are not implemented yet. No reservation rows are seeded, so the sample map starts with every seat free.

## Files intentionally left alone

No entity classes, persistence configuration, POM dependencies, booking logic, authentication, REST endpoints, or tests were added or changed. Existing `Showtime.calculateEndTime()` was reused; the end time remains calculated instead of stored.

## Build and runtime verification

Run this command from the project root:

```powershell
.\mvnw.cmd -DskipTests clean package
```

The command completed with `BUILD SUCCESS`. Maven compiled 25 Java source files using Java release 21 and created `target/MovieTicketBooking-1.0-SNAPSHOT.war` (35,663 bytes). `-DskipTests` skipped test execution; this milestone adds no tests. The browser pages and startup data still need to be confirmed after deployment.

## Rebuild and deploy manually

1. In a PowerShell terminal, change to the WildFly installation root and start it. Keep the terminal open and wait for the management interface message on port 9990.

   ```powershell
   Set-Location C:\servers\wildfly-41.0.1.Final
   .\bin\standalone.bat
   ```

2. In another PowerShell terminal, change to the project root, build the WAR, and deploy it:

   ```powershell
   Set-Location C:\Users\MSI\IdeaProjects\MovieTicketBooking
   .\mvnw.cmd -DskipTests clean package
   & 'C:\servers\wildfly-41.0.1.Final\bin\jboss-cli.bat' --controller=127.0.0.1:9990 --connect --command='deploy C:\Users\MSI\IdeaProjects\MovieTicketBooking\target\MovieTicketBooking-1.0-SNAPSHOT.war --force'
   ```

3. Open `http://localhost:8080/MovieTicketBooking-1.0-SNAPSHOT/showtimes`. Use the date box to choose today or tomorrow. Choose **View seats** to open the read-only map. A direct URL can use `?date=YYYY-MM-DD`; the seat page uses `/showtimes/seats?id=1` with a real showtime ID from the list.

The WAR build was checked, but WildFly deployment and browser rendering have not been verified as part of this milestone. If the server was stopped, start it in the first terminal before running the CLI command; a refused connection on port 9990 means the management controller is not listening yet.

## What each command means

These commands are typed in PowerShell. The first terminal runs the server; the second one builds and deploys the application.

### Start WildFly

```powershell
Set-Location C:\servers\wildfly-41.0.1.Final
.\bin\standalone.bat
```

- `Set-Location` changes the current folder. Here it changes to the WildFly installation root, where WildFly can find its modules and configuration. It is similar to `cd`.
- `.` at the beginning of `.\bin\standalone.bat` means “run a program from this folder.” `bin` is WildFly's tools folder, and `standalone.bat` starts a standalone WildFly server using its default standalone configuration.
- Leave this first terminal open. The server keeps running there. Its management controller listens on port `9990`; the web application will normally be served on port `8080`.

### Build the WAR

```powershell
Set-Location C:\Users\MSI\IdeaProjects\MovieTicketBooking
.\mvnw.cmd -DskipTests clean package
```

- The first line changes PowerShell's current folder to the existing project root. Maven then finds `pom.xml` and the wrapper configuration there.
- `.\mvnw.cmd` runs this project's Maven wrapper from the current project folder. The wrapper uses the Maven version configured for the project, rather than relying on a separately installed Maven command.
- `-DskipTests` tells Maven not to run tests. It does not add tests or prove runtime behavior.
- `clean` removes the previous generated `target` build folder so the next build starts fresh. It does not remove source files.
- `package` compiles the application and packages it as a WAR under `target`.

### Deploy the WAR

```powershell
& 'C:\servers\wildfly-41.0.1.Final\bin\jboss-cli.bat' --controller=127.0.0.1:9990 --connect --command='deploy C:\Users\MSI\IdeaProjects\MovieTicketBooking\target\MovieTicketBooking-1.0-SNAPSHOT.war --force'
```

- PowerShell's `&` is the call operator: it runs the program at the quoted path. Here that program is WildFly's command-line administration tool, `jboss-cli.bat`.
- `--controller=127.0.0.1:9990` tells the CLI where WildFly's management interface is. `127.0.0.1` means this same computer; `9990` is the management port.
- `--connect` asks the CLI to connect to that running server.
- `--command='...'` tells the CLI which management command to run after connecting. Inside it, `deploy` installs the WAR at the given path.
- `--force` replaces the deployment with the same name if it is already deployed. It is useful when deploying a newly built version over the previous one.
- The CLI command can connect only after WildFly has finished starting. If it says the controller is unavailable, check that the first terminal is still running and has reported the management interface as listening.

### Open the page

`http://localhost:8080/MovieTicketBooking-1.0-SNAPSHOT/showtimes` is a browser address, not a PowerShell command. `localhost` means this computer, `8080` is the application's web port, the middle part is the WAR's context path, and `/showtimes` is the servlet route. Add `?date=YYYY-MM-DD` to request a chosen date, for example `?date=2026-11-25`. The seat-map link uses the showtime ID, such as `/showtimes/seats?id=1`.

## What to practise explaining

- Why only repository classes use `EntityManager`, and why the service coordinates repository calls.
- What the named JPQL parameters represent, and why the date query ends at (but does not include) the next midnight.
- How `JOIN FETCH` loads the movie and room needed for a showtime page.
- How the application decides that a seat is taken from `ReservationSeat` rows.
- Why the servlet validates input and delegates data work to the service.
- How JSTL loops and EL display service-provided values without Java code in the JSP.
- Why these sample rows are only development data and why no booking action exists yet.
