# Design Note: Movie Ticket Booking

## 1. Problem
The campus cinema sells tickets at the door and popular shows sell out fast.
The main risk is two people booking the same seat for the same screening.
The system lets people see which seats are free, reserve them online, and
guarantees a seat is never given to two people for the same screening.
Every film, room and time is recorded. This version does not detect two
showtimes overlapping in the same room; the admin is responsible for that.

## 2. Users and roles
- **CUSTOMER:** students, couples, families. Every new sign-up is a CUSTOMER.
- **ADMIN:** the cinema staff who run the system. Admin accounts come from
  seed data, never from the sign-up form. There can be several admins, and
  every ADMIN has the same permissions. There is no separate staff role.

Permissions depend on the role (role-based access control). "View a
reservation" also needs an ownership check: a customer can only open their
own reservations.

| Operation | Who |
|---|---|
| Browse movies, showtimes, and free seats | Anyone |
| Register and log in | Anyone |
| Reserve seats and get a confirmation code | CUSTOMER |
| View a reservation by code | CUSTOMER (own only), ADMIN (any) |
| Add and edit movies | ADMIN |
| Create showtimes (movie + room + start time) and change prices | ADMIN |
| Look up a reservation by code and mark it checked in | ADMIN |

## 3. How films, rooms and times connect
A showtime is one screening: one film, in one room, at one start time, with
a price. The end time is calculated (start time + movie duration). A film
can play in several rooms and a room shows several films a day. The admin
creates showtimes; the customer only picks from them. Times are stored as
the cinema's local time, with no time zone conversion.

## 4. User stories
1. As a visitor, I want to browse movies and their showtimes (start and end
   time, room), so I can choose what to watch.
2. As a visitor, I want to register, so I can book tickets.
3. As a customer, I want to see which seats are free for a showtime.
4. As a customer, I want to reserve seats and get a confirmation code.
5. As a customer, I want a clear message if someone took my seat a moment
   before me, so I can pick another.
6. As a customer, I want to view my own reservation and nobody else's.
7. As an admin, I want to add and edit movies, create showtimes and change
   prices.
8. As an admin, I want to look up a reservation by code and mark it checked in.

## 5. Main booking flow
1. The customer sees the movies and showtimes for a chosen day, with start
   and end time and the room.
2. They pick a showtime and see the seat map: taken seats gray, free seats
   yellow, their selected seats highlighted.
3. They log in or register when they decide to reserve.
4. They confirm and get a confirmation code with movie, room, time and seats.
5. If someone took a seat just before them, they see "that seat was just
   taken, please choose another" and the seat map is redrawn.

## 6. The seat rule
The same seat can be booked for different showtimes, but only once for the
same showtime. The database enforces this with a unique constraint on
(showtime_id, seat_id) in ReservationSeat, so it holds even when two people
click at the same moment.

The service layer must also keep the data consistent, because the database
constraint cannot check these: a ReservationSeat's showtime must be the same
as its reservation's showtime, and its seat must belong to the showtime's room.

## 7. Risks
1. **Two customers booking the same seat at the same moment.** A Java check
   before saving is not enough, because both requests can pass it before
   either saves. Mitigation: the unique constraint, catching the violation
   and showing a friendly message, and a test with two concurrent bookings
   expecting one success and one handled conflict.
2. **A logged-in customer reading someone else's reservation.** Being a
   CUSTOMER is not enough. Mitigation: an ownership check on both web and
   REST paths, tested with two customers and one admin.

## 8. Out of scope
Real payment, holding seats while paying, cancelling a reservation,
automatic detection of overlapping showtimes, and a separate staff role.