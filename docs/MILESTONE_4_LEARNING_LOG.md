# Milestone 4 Learning Log: List Movies from the Database

This milestone adds one end-to-end read flow: a browser requests the movie list, the servlet calls a service, the service reads through a repository, and the JSP renders the results. AI generated the new Java classes and JSP from the user's milestone requirements. Read and practise explaining each file before treating the code as your own understanding.

## What we built and why

- `src/main/java/com/movieticket/repository/MovieRepository.java` is the only new class that uses `EntityManager`. `@PersistenceContext` injects the persistence context named `MovieTicketBookingDevPU`. `findAll()` runs JPQL that returns movies sorted by title. The query has no parameters, so it does not need a named parameter. `save()` persists sample movies for the startup seeder.
- `src/main/java/com/movieticket/service/MovieService.java` is an `@Stateless` EJB for application operations. `@Inject` supplies `MovieRepository`. `listMovies()` delegates the read to the repository. `seedSampleMoviesIfEmpty()` checks whether there are any movies, and if not, creates and saves three sample movies. The seeding decision belongs in the service, not the servlet or JSP.
- `src/main/java/com/movieticket/web/MovieListServlet.java` handles `GET /movies`. It calls `listMovies()`, puts the returned list in the request under `movies`, and forwards the same request to the JSP. It does not contain query or movie-selection logic.
- `src/main/java/com/movieticket/config/SampleMovieInitializer.java` is an EJB marked `@Singleton` and `@Startup`, so the container creates one instance and initializes it at application startup. Its `@PostConstruct` method calls the service to seed the sample rows. The startup bean itself does not use `EntityManager`.
- `src/main/webapp/WEB-INF/views/movies.jsp` uses JSTL and EL to display a message when the list is empty or loop over the movies and show title, duration, and rating. `<c:out>` escapes displayed values. The page contains no Java scriptlets.

## JSTL on this WildFly installation

I inspected WildFly 41.0.1.Final rather than adding a JSTL dependency by guess. Its `io.undertow.jsp` module depends on `jakarta.servlet.jstl.api`; that module contains the JSTL API 3.0.2 and implementation 3.0.1. The core tag library declares the URI `jakarta.tags.core`. Therefore, this WildFly deployment does not need an extra JSTL Maven dependency, and the JSP uses:

```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
```

## Build and remaining runtime check

The command `mvnw.cmd -DskipTests clean package` completed with `BUILD SUCCESS` and generated `target/MovieTicketBooking-1.0-SNAPSHOT.war`. Maven compiled 14 Java source files with Java release 21 and produced the WAR. Tests were skipped and none were added. The new WAR has not yet been deployed, so database injection, startup seeding, and JSTL rendering still need to be confirmed at runtime.

## Rebuild and redeploy

The user's deploy command returned `Connection refused` for `localhost:9990`. Port 9990 is WildFly's management controller; the error means WildFly was not running (or had not finished starting), not that the WAR build failed. I checked and found no process listening on that port. The user later supplied a startup log showing WildFly listening on 127.0.0.1:9990 and reporting MovieTicketBooking deployed; a separate jakartaee-course WAR failed because its classes target Java 25. Since the original deploy command was run before the controller was available, the new Milestone 4 WAR still needs a redeploy confirmation. A CLI retry from the assistant execution environment failed with `XNIO001001: No XNIO provider found`; use the user's normal PowerShell terminal for the retry.

From the project root in PowerShell, rebuild the WAR:

```powershell
.\mvnw.cmd -DskipTests clean package
```

Open a first PowerShell terminal, change to the WildFly installation root (not its `bin` subfolder), and start WildFly. Keep this terminal open; it is the server process. Starting from the root ensures `JBOSS_HOME` points to the folder containing `jboss-modules.jar`:

```powershell
Set-Location C:\servers\wildfly-41.0.1.Final
.\bin\standalone.bat
```

Wait until the log says the management interface is listening on port 9990. A separate unrelated WAR may still report an error; the important check for this app is whether `MovieTicketBooking-1.0-SNAPSHOT.war` deploys.

Then, in a second PowerShell terminal, redeploy using WildFly's command-line interface:

```powershell
& 'C:\servers\wildfly-41.0.1.Final\bin\jboss-cli.bat' --controller=127.0.0.1:9990 --connect --command='deploy C:\Users\MSI\IdeaProjects\MovieTicketBooking\target\MovieTicketBooking-1.0-SNAPSHOT.war --force'
```

Then open `http://localhost:8080/MovieTicketBooking-1.0-SNAPSHOT/movies`. The expected page lists three sample titles in alphabetical order, with each movie's duration and rating. If WildFly is stopped, start it first from `C:\servers\wildfly-41.0.1.Final\bin` with `standalone.bat`.

## What to learn and explain

- Why the repository is the only class that uses `EntityManager`.
- How JPQL maps `Movie` objects and sorts their titles.
- How the service and servlet pass the list to the JSP without putting data access in the web layer.
- What `@Stateless`, `@Singleton`, `@Startup`, `@Inject`, `@PersistenceContext`, and `@PostConstruct` do in this flow.
- How JSTL `<c:forEach>`, `<c:choose>`, and `<c:out>` work with EL values.
- Why the startup initializer only creates sample rows when the table is empty.
