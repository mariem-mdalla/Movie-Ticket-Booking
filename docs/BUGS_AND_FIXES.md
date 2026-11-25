# Errors and Bugs Found During Development

This file records errors encountered while building and running MovieTicketBooking, what caused them, and how they were handled. It separates application defects from local build/server issues so an unrelated environment problem is not mistaken for a project bug.

## 1. Milestone 2: Maven could not clean generated files

**Observed:** `mvnw clean package` could not delete `target/test-classes`; a later package attempt could not write compiler status files under `target`. At that point, a successful WAR build had not been confirmed.

**Cause:** Windows or the execution environment held or denied access to generated build files. This was not a Java compilation error.

**Resolution:** Later, after project write access was granted to Maven, `mvnw.cmd -DskipTests clean package` completed successfully. Similar locks recurred during later work; see the Milestone 6 entries below.

## 2. Milestone 3: Another WAR failed under Java 21

**Observed:** WildFly started with an error mentioning `jakartaee-course-1.0-SNAPSHOT.war` and “Java Runtime only recognizes class file versions up to 65.0”.

**Cause:** That separate WAR contained Java 25 bytecode (class version 69), while the running WildFly process used Java 21 (class version 65).

**Resolution/status:** This was not a MovieTicketBooking defect. The supplied server log also showed `MovieTicketBooking-1.0-SNAPSHOT.war` deployed. The unrelated `jakartaee-course` project needs to be rebuilt for the Java version used by its server if it must run there.

## 3. Milestone 4: WildFly CLI connection refused

**Observed:** The deploy command failed to connect to `localhost:9990` with `Connection refused`.

**Cause:** Port 9990 is WildFly's management interface. WildFly was stopped or had not finished starting when the command ran; Maven had already built the WAR.

**Resolution:** Start WildFly and wait for its management-interface startup message before running the CLI deploy command. A later server log showed the controller listening and MovieTicketBooking deployed.

## 4. Milestone 4: CLI retry reported a missing XNIO provider

**Observed:** An assistant-side CLI retry returned `XNIO001001: No XNIO provider found`.

**Cause/status:** This came from the assistant's execution environment while connecting to WildFly, not from project source code. The user's own server log later showed the MovieTicketBooking deployment. A later deployment from the WildFly installation folder worked as confirmed by HTTP checks.

## 5. Milestone 6: Seat selection appeared not to work

**Observed:** Clicking a seat did not turn it green or update the summary. The browser could still return HTTP 200 for the JavaScript URL, which hid the underlying problem.

**Cause:** `WelcomeServlet` was mapped to `/`, the default servlet mapping. It also received the otherwise-unmatched `/seat-map.js` request and returned the welcome page's HTML instead of JavaScript. The browser therefore had no working seat-change handler.

**Fix:** The welcome servlet now maps only to `/welcome`. A root `index.jsp` forwards `/` to that servlet, preserving the welcome page while allowing WildFly's default servlet to serve static files. The seat page uses a versioned script URL to avoid a stale cached asset. CSS also colors a checked seat green directly, while JavaScript updates the summary and button.

**Verification:** After rebuilding and redeploying, HTTP checks returned the seat page with status 200, `/seat-map.js` with status 200 and content type `application/javascript`, and the root welcome page with status 200. The code has not been manually clicked through in a browser as part of that HTTP check.

## 6. Milestone 6: CLI could not locate `jboss-modules.jar`

**Observed:** Running WildFly's `jboss-cli.bat` while the current folder was the project root reported that it could not find `jboss-modules.jar` in the project.

**Cause:** The CLI expects to run with WildFly's installation folder as its working directory so it can locate its modules.

**Fix:** Change PowerShell's current folder to `C:\servers\wildfly-41.0.1.Final` before running `bin\jboss-cli.bat`. The deployment command uses the WAR's full path under the project `target` folder.

## 7. Milestone 6: Maven build files were locked

**Observed:** The wrapper's `clean` goal sometimes could not delete `target/test-classes`. An installed `mvn clean package` attempt could not open a Maven clean-plugin lock under `.m2`. A wrapper build without `clean` compiled 34 Java files but failed while writing Maven compiler status files.

**Cause:** Build-output or local Maven-cache files were locked or not writable in the execution environment. These were file-access issues, not Java compiler errors.

**Resolution:** After project write access was granted, `mvnw.cmd -DskipTests clean package` completed with `BUILD SUCCESS` and created the WAR. Tests were skipped; no tests were added or run in these milestones.

## 8. Milestone 6: Strict JPQL rejected aliased fetch joins

**Observed:** Opening `/reservation?code=...` raised `StrictJpaComplianceViolation: Encountered aliased fetch join, but strict JPQL compliance was requested` in `ReservationRepository.findByConfirmationCode`.

**Cause:** The query assigned aliases to fetch joins, such as `JOIN FETCH reservation.showtime showtime` and `LEFT JOIN FETCH reservation.reservationSeats reservationSeat`. Strict JPQL compliance rejects those fetch-join aliases.

**Fix:** The repository now uses a plain JPQL query with the named parameter `:code` and a JPA `EntityGraph` to request the related showtime, movie, room, reservation seats, and seats. I also checked `ShowtimeRepository`; its fetch joins do not alias the fetched association, so they did not need changes.

`BookingService.findByConfirmationCode()` maps the fetched entities to `ReservationView` inside a transaction. The DTO includes movie title, room name, start/end time, seat labels, total, confirmation code, and status. The JSP receives that DTO and does not read lazy entity relationships.

**Verification/status:** The updated Java code built successfully with `mvnw.cmd -DskipTests clean package`. The strict-compliance fix has not yet been redeployed, so its runtime behavior still needs verification.

## 9. Milestone 6: Unexpected errors needed safe pages

**Concern:** An unhandled exception or missing URL should not expose a stack trace in the browser.

**Fix:** `WEB-INF/web.xml` maps 404, 500, and uncaught `Throwable` errors to `error.jsp`. The JSP shows an expected safe message when one is provided, otherwise it displays a generic message. It never renders exception details or a stack trace.

**Verification/status:** The error mappings were included in the successful WAR build, but their behavior has not yet been checked after deployment.

## Current verification limits

- The latest strict-JPQL and error-page changes compile and package successfully, but have not been redeployed.
- The earlier seat-map asset fix was redeployed and checked over HTTP. Manual seat selection and booking behavior still need a browser check.
- No Git commit was made. Git status could not be checked because the project path was reported as not being inside a Git work tree.
