# Milestone 1 Learning Log: Walking Skeleton

This note explains the steps completed for the first Movie Ticket Booking milestone. Read it alongside the source files and try explaining the request path in your own words.

## Step 1: Inspect the starting project

The project already had a Maven `pom.xml`, Maven wrapper scripts, `beans.xml`, and IntelliJ settings. There were no Java classes or JSP pages. The project directory was a Git repository with no commits; all existing files were already staged as additions. Those staged files were left alone, and no commit or Git history change was made.

The active environment reported Eclipse Temurin Java 21 and Maven 3.10.1. The wrapper is configured to download/use Maven 3.9.6. An existing WildFly 41.0.1.Final directory was found at `C:\servers\wildfly-41.0.1.Final`.

## Step 2: Configure the Jakarta EE build

In `pom.xml`, the platform dependency is now `jakarta.jakartaee-api` version 11 with `provided` scope. The server supplies Jakarta APIs when the application runs, so `provided` keeps those platform classes out of the WAR. The compiler uses `maven.compiler.release` set to 21, which tells Java to compile for Java 21 language and API compatibility. Maven Compiler Plugin compiles Java; Maven WAR Plugin packages the web application.

## Step 3: Handle a browser request

`src/main/java/com/example/movieticket/WelcomeServlet.java` is mapped to `/` by `@WebServlet("/")`. Its `doGet` method puts the text `Movie Ticket Booking` in a request attribute named `projectName`, then forwards the same request to `/WEB-INF/views/welcome.jsp`.

Forwarding keeps the request inside the server. Placing the JSP under `WEB-INF` means a browser cannot request that file directly; it must go through the servlet.

## Step 4: Render the page

`src/main/webapp/WEB-INF/views/welcome.jsp` contains the page markup. `${projectName}` is JSP Expression Language (EL): it reads the request attribute set by the servlet and inserts its value into the title and heading. The JSP contains no Java scriptlet or business logic.

## Step 5: Build and package

The intended command is `mvn clean package`, with `mvnw.cmd clean package` used first if the wrapper works. `clean` removes the previous Maven build output, and `package` compiles the source and creates a WAR. The resulting archive should be `target/MovieTicketBooking-1.0-SNAPSHOT.war`.

## Step 6: Deployment is documented, not run

WildFly 41.0.1.Final is already present. Its official release information states that standard WildFly 41 supports Jakarta EE 11 on Java 21. The manual start and WAR-copy commands are in the README. The server was not started and no deployment or browser check was performed in this milestone run.

## What to learn and explain

- A WAR is a Java web application archive that an application server deploys.
- `provided` means Maven uses the API to compile, while the server supplies it at runtime.
- A servlet receives the HTTP request and can prepare data for a view.
- A request attribute carries data from the servlet to the JSP during the same request.
- A forward dispatches the current request internally; a redirect asks the browser to make another request.
- JSP EL displays values without embedding Java code in the page.
- `WEB-INF` protects internal web files from direct browser access.
- The servlet mapping `/` makes this servlet handle the application's root URL.
