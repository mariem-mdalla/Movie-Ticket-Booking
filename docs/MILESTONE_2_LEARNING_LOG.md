# Milestone 2 Learning Log: JPA Entities

This note explains the entity model added for the Movie Ticket Booking project. The user wrote the design note and chose the entities, relationships, and seat uniqueness rule. AI generated the seven entity classes, two enums, and this learning log. Read the Java files and use this note to help explain the model; do not claim to understand or have independently verified code before you have done so.

## Step 1: Model the cinema data

The seven entity classes represent the main things the application stores: users, movies, auditoriums, seats, showtimes, reservations, and the individual seats included in each reservation. The two enums provide fixed lists of roles and reservation states. Each entity has an `id` marked with `@Id`; `@GeneratedValue` asks the database to create that identifier.

`@Entity` tells Jakarta Persistence that a Java class is stored as rows in a database table. `@Column` describes a field's column, such as whether it can be null or must be unique. `@Table` lets an entity specify a table name and table-level constraints. Each entity has a public no-argument constructor so a JPA provider can create it when loading a row.

## Step 2: Understand each entity

- **User** represents an account. It stores a username, password hash, and `Role`, and has a collection of reservations. The username column is unique so two accounts cannot use the same username. `Role` is stored as text with `@Enumerated(EnumType.STRING)`, making its database value readable and avoiding ordinal numbers that could change meaning if enum order changes.
- **Movie** stores the title, description, duration in minutes, rating, and poster URL. Its showtimes collection represents screenings of that movie.
- **Auditorium** represents a cinema room by name. Its collections point to the seats physically in the room and the showtimes scheduled there.
- **Seat** represents a physical seat in an auditorium, identified by its row label and seat number. The auditorium, row label, and seat number together must be unique.
- **Showtime** represents one movie screening in one auditorium at one start time and price. It does not store an end time. `calculateEndTime()` adds the movie's `durationMinutes` to `startTime`, so the end is derived from the source values instead of stored twice. If either value needed for that calculation is missing, the method throws an `IllegalStateException`.
- **Reservation** represents a customer's booking for a showtime. It stores the user, a unique confirmation code, the total price, a `ReservationStatus`, and its creation time. Its collection contains the individual reserved seats.
- **ReservationSeat** represents one seat included in one reservation. It links the reservation, showtime, and physical seat, and stores `priceAtBooking` so the amount for that seat is recorded even if the showtime's current price later changes.

## Step 3: Read the relationships

`@ManyToOne(fetch = FetchType.LAZY)` means many rows of one entity can point to one related row. This side owns the foreign key. `@JoinColumn` names that foreign-key column. `LAZY` means JPA can wait to load the related object until code asks for it.

`@OneToMany(mappedBy = "...")` describes the inverse collection. `mappedBy` names the field on the other entity that owns the relationship; the collection does not create a second foreign key.

- One **Movie** has many **Showtimes**; each Showtime points to one Movie.
- One **Auditorium** has many **Seats** and many **Showtimes**; each Seat and each Showtime points to one Auditorium.
- One **User** has many **Reservations**; each Reservation points to one User.
- One **Showtime** has many **Reservations**; each Reservation points to one Showtime.
- One **Reservation** has many **ReservationSeats**; each ReservationSeat points to one Reservation.
- Each **ReservationSeat** points to one **Seat** and one **Showtime**. A Seat and a Showtime can each appear in many ReservationSeat rows across different bookings or screenings, subject to the seat uniqueness rule below.

## Step 4: Enforce the seat rule in the database

`ReservationSeat` has a table-level unique constraint on `(showtime_id, seat_id)`. The database rejects a second row with the same showtime and seat, even if two booking requests check availability at nearly the same time. A Java-only availability check is not enough: two requests could both see the seat as free before either saves its reservation. The database constraint is the final guard against that race. A later service layer must catch the constraint violation and report a friendly conflict to the customer.

The `Seat` table also has a unique constraint on `(auditorium_id, row_label, seat_number)`, so one auditorium cannot have two physical seats with the same label. The `users` table has a unique username column. The reservation confirmation code is unique as well.

## Step 5: Table and time choices

The `User` entity maps to a table named `users` with `@Table(name = "users")`. `USER` is a reserved or special word in some SQL databases and tools. Naming the table `users` avoids relying on quoting rules that vary by database.

`Showtime` has no persisted end-time field because the end is determined by its start and the movie's duration. Storing both the duration and an independently editable end time could leave inconsistent values. Calculating the end keeps one source of truth.

## Step 6: Reservation statuses

The design calls for successful reservations and staff check-in, but it did not specify the enum members. AI selected `PENDING`, `CONFIRMED`, `CANCELLED`, and `CHECKED_IN` as an initial lifecycle: a reservation may be awaiting completion, confirmed, cancelled, or checked in. These choices still need the user's review before later code relies on them. `@Enumerated(EnumType.STRING)` stores names instead of numeric positions.

## Build verification status

The Milestone 2 build was not verified successfully. A clean build could not delete `target/test-classes`; a package attempt then failed while Maven wrote compiler status files under `target`. No successful WAR build was confirmed, and no tests were run. Close any program that may hold files under `target`, then retry the Maven build before relying on these entities in the next milestone.

## What to learn and explain

- Why each class is an entity and what each field represents.
- Which side owns each foreign key and what `mappedBy` refers to.
- What lazy loading changes when code follows an entity relationship.
- How the database unique constraint protects a seat booking during concurrent requests.
- Why the table name is `users`, why the showtime end time is calculated, and what each reservation status means.
