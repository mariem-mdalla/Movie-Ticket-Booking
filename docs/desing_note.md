# Design Note: Movie Ticket Booking

## 1. What problem the app solves
The campus cinema sells tickets at the door and popular shows sell out fast.
The main risk is two people booking the same seat for the same screening.
The system lets people see which seats are free, reserve them online, and
guarantees a seat can never be given to two people for the same screening.
It also keeps the schedule organized: every film, room and time is recorded,
so nothing conflicts.

## 2. Who uses it
- **Customers:** students, couples, families. They browse films, pick a
  showtime, see the free seats, and reserve.
- **Staff / admin:** they manage the cinema side: films, rooms, showtimes,
  prices, and checking tickets at the door.

## 3. What each role can do
**Customer**
- Browse the list of movies
- See a movie's details and its showtimes (start and end time)
- See which seats are free for a showtime
- Reserve one or more seats and get a confirmation code
- View a reservation by its code

**Admin**
- Add and edit movies
- Create showtimes (which movie, which room, what time, what price)
- Change prices
- Look up a reservation by code and mark it as checked in

## Key idea: how films, rooms and times connect
A showtime is one screening: one film, in one room, at one start time.
A room shows many films over a day, and a film can play in several rooms.
Seats are reserved per showtime, so the same seat is free again at the next screening.

## Operations and access
| Operation | Who |
|---|---|
| Browse movies, movie details, showtimes | Anyone |
| Register and log in | Anyone |
| See free seats for a showtime | Anyone |
| Reserve seats and get a confirmation code | CUSTOMER |
| View my own reservation by code | CUSTOMER (own only) |
| Add and edit movies | ADMIN |
| Create showtimes (movie + room + start time) and change prices | ADMIN |
| Look up any reservation by code and mark it checked in | ADMIN |

## Showtimes
The admin creates a showtime by choosing a movie, a room and a start time.
The end time is calculated (start time + movie duration). The customer only
picks from existing showtimes and never chooses a room.

## The seat rule
The same seat can be booked for different showtimes, but only once for the
same showtime. The database enforces this with a unique constraint on
(showtime_id, seat_id) in ReservationSeat, so it holds even if two people
click at the same moment. If a customer loses the race, they see "that seat
was just taken, please choose another."

## Out of scope for now
Real payment, holding seats while paying, automatic check for overlapping
showtimes in the same room.