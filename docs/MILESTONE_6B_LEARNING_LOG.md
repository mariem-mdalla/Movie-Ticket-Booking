# Milestone 6B: Shared layout and dark cinema theme

## What we built and why

This milestone gives the existing pages one shared visual style and navigation. The page behavior and booking flow stay the same. Login and logout are only placeholders because authentication is planned for a later milestone.

## Files changed

- `src/main/webapp/css/site.css` contains the common dark cinema theme: near-black page, light text, gold links/buttons/headings, red error styling, rounded cards, responsive spacing, and the seat-map colors and layout. The checkbox remains in the page and keyboard-accessible, but CSS clips it visually so the customer sees the colored seat tile.
- `src/main/webapp/WEB-INF/views/fragments/header.jspf` is the shared site name, Movies and Showtimes navigation, and Login / Logout placeholder.
- `src/main/webapp/WEB-INF/views/fragments/footer.jspf` is the shared footer.
- `movies.jsp`, `showtimes.jsp`, `seats.jsp`, `reservation.jsp`, `error.jsp`, and `welcome.jsp` include the shared fragments and load the stylesheet with the web application's context path. That prefix keeps the links working even when the app is deployed under `/MovieTicketBooking-1.0-SNAPSHOT` rather than at the server root.
- `seats.jsp` keeps the existing form names, element IDs, data attributes, and polling JavaScript. It adds the screen bar, legend, formatted showtime details, and summary-card styling. The JavaScript asset URL uses the context path and a new cache version; the JavaScript logic is unchanged.
- `ShowtimeView.java` and `ReservationView.java` provide display-only date and time strings. The JSPs show a readable date and `HH:mm` times such as `10:00` instead of the default `T` separator from `LocalDateTime`.
- `index.jsp` forwards the root welcome-file request to `/showtimes`, so the root uses the existing showtime servlet and defaults to today.
- `WelcomeServlet.java` redirects its old `/welcome` route to `/showtimes`.
- `WEB-INF/web.xml` declares `index.jsp` as the welcome file.
- `AI_USAGE.md` records AI assistance for this milestone.

## Why `welcome.jsp` is still present

The request said to remove the old welcome JSP only after the root page works. The Maven package build packages JSP files but does not run WildFly or request the root URL, so this milestone's build alone cannot prove that the root successfully loads today's showtimes. The old `welcome.jsp` remains in the project until that browser check succeeds; it is no longer the configured root page.

## What was not changed

No entities, repositories, business services, booking behavior, seat selection logic, or seat-refresh logic were changed. No dependencies were added. The welcome servlet was changed because this milestone explicitly changes the root-page destination.

## Build and deployment

Build from the project directory:

```powershell
Set-Location C:\Users\MSI\IdeaProjects\MovieTicketBooking
.\mvnw.cmd -DskipTests clean package
```

Start WildFly in a separate PowerShell window if it is not already running:

```powershell
Set-Location C:\servers\wildfly-41.0.1.Final
.\bin\standalone.bat
```

After the management interface is listening, deploy from the WildFly installation directory:

```powershell
Set-Location C:\servers\wildfly-41.0.1.Final
& '.\bin\jboss-cli.bat' --controller=127.0.0.1:9990 --timeout=15000 --connect --command='deploy C:\Users\MSI\IdeaProjects\MovieTicketBooking\target\MovieTicketBooking-1.0-SNAPSHOT.war --force'
```

Then open `http://localhost:8080/MovieTicketBooking-1.0-SNAPSHOT/` and verify the root shows today's showtimes. Also check `/movies`, the seat map, a reservation, and an error page. If the root route works, `welcome.jsp` can then be removed in a follow-up cleanup and the WAR rebuilt.

In these commands, `Set-Location` changes the current PowerShell folder; `mvnw.cmd` runs the project's Maven wrapper; `-DskipTests` skips tests; `clean` removes generated build output; `package` compiles and creates the WAR; `standalone.bat` starts WildFly; `jboss-cli.bat` connects to the local management port; and `deploy ... --force` installs or replaces that WAR.

## Verification status

The source changes and shared markup were inspected. `mvnw.cmd -DskipTests clean package` completed with `BUILD SUCCESS`, compiled 34 Java source files using Java release 21, and created `target/MovieTicketBooking-1.0-SNAPSHOT.war` (59,208 bytes). Tests were skipped. WildFly deployment and browser checks were not performed as part of this change, so JSP rendering and the root route still need runtime verification.

## What to practise explaining

- How a JSP include directive reuses the same header and footer on several pages.
- Why every CSS, JavaScript, and in-app link starts with `pageContext.request.contextPath`.
- How CSS variables make the gold, red, and seat colors easy to change consistently.
- How display getters keep date/time formatting out of the JSP and do not alter stored date/time values.
- How the welcome-file setting selects `index.jsp`, which forwards the request to the existing showtime flow.
- Why the old welcome JSP stays until a browser check confirms the root flow.
