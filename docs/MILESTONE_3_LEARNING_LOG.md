# Milestone 3 Learning Log: Development Persistence Configuration

This milestone connects the JPA entity model to the H2 datasource already configured in WildFly. The persistence configuration is for local development only. AI created the persistence descriptor and this learning log; the user has not yet claimed to have written or independently explained that configuration.

## Step 1: Verify WildFly's datasource

The WildFly installation is `C:\servers\wildfly-41.0.1.Final`. I inspected `standalone\configuration\standalone.xml` and found the datasource JNDI name `java:jboss/datasources/ExampleDS`. It uses the H2 driver and the in-memory URL `jdbc:h2:mem:test;...`. The persistence unit uses that exact JNDI name rather than guessing a datasource.

## Step 2: Define the persistence unit

`src/main/resources/META-INF/persistence.xml` defines a persistence unit named `MovieTicketBookingDevPU` with JTA transactions. The `<jta-data-source>` entry tells WildFly which managed database connection pool to use.

The file explicitly lists the seven classes annotated with `@Entity`: `User`, `Movie`, `Auditorium`, `Seat`, `Showtime`, `Reservation`, and `ReservationSeat`. `Role` and `ReservationStatus` are enums, not entities, so they are not listed as entity classes. `exclude-unlisted-classes` is true to make the entity list explicit.

## Step 3: Create and show the development schema

The standard JPA property `jakarta.persistence.schema-generation.database.action=create` asks the persistence provider to create the schema when the unit starts. This is intended for the in-memory development database, not a production database. The `hibernate.show_sql` and `hibernate.format_sql` properties turn on formatted SQL output.

When deployed, the schema creation SQL should include the tables for the listed entities and their constraints. The seat uniqueness constraint is named `uk_reservation_seat_showtime_seat`; it protects the `(showtime_id, seat_id)` pair in `reservation_seats`.

## Step 4: Build and deploy manually

The Maven wrapper command `.\mvnw.cmd clean package` completed with `BUILD SUCCESS` and generated `target/MovieTicketBooking-1.0-SNAPSHOT.war`. The later WildFly startup log supplied for review shows that this WAR deployed, `MovieTicketBookingDevPU` started against `jdbc:h2:mem:test`, and Hibernate created the tables and the `uk_reservation_seat_showtime_seat` constraint. The page response was not independently checked over HTTP.

The same server startup log also shows a separate deployment, jakartaee-course-1.0-SNAPSHOT.war, failed because it contains Java 25 bytecode while this WildFly process runs on Java 21. That failure is not from MovieTicketBooking.

To deploy from PowerShell outside IntelliJ, start WildFly in one terminal:

```powershell
Set-Location C:\servers\wildfly-41.0.1.Final\bin
.\standalone.bat
```

In a second terminal, copy the WAR to WildFly's deployment scanner folder:

```powershell
Copy-Item C:\Users\MSI\IdeaProjects\MovieTicketBooking\target\MovieTicketBooking-1.0-SNAPSHOT.war C:\servers\wildfly-41.0.1.Final\standalone\deployments\
```

Wait for the deployment success message in the server terminal. WildFly's configured file handler writes to `C:\servers\wildfly-41.0.1.Final\standalone\log\server.log`. Search that file for `create table`, `alter table`, `reservation_seats`, or `uk_reservation_seat_showtime_seat`. The `hibernate.show_sql` output is also visible in the server console. The constraint may appear inline in a `create table` statement or in a later `alter table ... add constraint` statement.

After successful deployment, the welcome page URL is:

```text
http://localhost:8080/MovieTicketBooking-1.0-SNAPSHOT/
```

## What to learn and explain

- A persistence unit groups the JPA entities and the settings used to connect them to a database.
- A JNDI datasource is a named connection pool managed by the application server.
- The schema-generation setting creates development tables when the persistence unit starts.
- SQL logging helps inspect the generated DDL, including table and constraint creation.
- The H2 datasource in this WildFly configuration is in memory and is for development only.
