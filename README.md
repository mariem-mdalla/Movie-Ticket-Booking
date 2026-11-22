# Movie Ticket Booking

This is a university project built with Jakarta EE 11. Milestone 1 is a walking skeleton: a Maven WAR containing a welcome servlet that forwards to a JSP page.

## Prerequisites

- Eclipse Temurin JDK 21
- Maven (the Maven wrapper is included)
- WildFly 41.0.1.Final for manual deployment. WildFly 41 supports the Jakarta EE 11 platform on Java 21.

## Build

From the project root, run:

```powershell
.\mvnw.cmd clean package
```

If the wrapper fails and Maven is installed on your PATH, run:

```powershell
mvn clean package
```

The WAR is written to `target/MovieTicketBooking-1.0-SNAPSHOT.war`.

## Manual deployment outside IntelliJ

The server is expected at `C:\servers\wildfly-41.0.1.Final`. Open a terminal and start it:

```powershell
Set-Location C:\servers\wildfly-41.0.1.Final\bin
.\standalone.bat
```

In a second terminal, from the project root, copy the WAR into WildFly's deployment directory:

```powershell
Copy-Item .\target\MovieTicketBooking-1.0-SNAPSHOT.war C:\servers\wildfly-41.0.1.Final\standalone\deployments\
```

After deployment completes, open:

```text
http://localhost:8080/MovieTicketBooking-1.0-SNAPSHOT/
```

Deployment and browser loading have not yet been tested as part of this milestone run.
