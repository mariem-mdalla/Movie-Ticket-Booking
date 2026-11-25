# Movie Ticket Booking: Remaining Pages and Milestones

This roadmap records the next page work discussed on 2026-11-25. It follows `docs/design-note.md` and keeps the user's proposed split into Milestones 7 and 8. The pages listed here are planned; they are not implemented yet.

## Current state

Milestones 1–6 have been implemented. The user reports that the latest WAR has been deployed and the reservation confirmation page works.

The current rendered pages are:

1. Welcome page
2. Public movie list
3. Showtime list
4. Seat map and booking form
5. Reservation confirmation
6. Shared error page

The seat-status URL returns JSON for the seat map's refresh and is not an HTML page. `index.jsp` forwards to the welcome page; it is not a separate screen.

## Planned pages

| Page | Milestone | Purpose |
| --- | ---: | --- |
| Login | 7 | Authenticate an existing customer or admin. |
| Register | 7 | Create a customer account; public registration must not allow the ADMIN role. |
| My Reservations | 7 | Show the signed-in customer's reservations in one list, newest first, and let them reopen details. |
| 403 Not Allowed | 7 | Explain that the user is signed in but does not have permission for a page or action. |
| Admin movie list | 8 | Let an admin review the movie catalog and choose a movie to edit. |
| Add/edit movie form | 8 | Let an admin create a movie or change an existing movie. |
| Create showtime | 8 | Let an admin choose a movie and room, enter a start time, and set a price. |
| Check-in by code | 8 | Let an admin look up a reservation code and mark that reservation checked in. |

The approximate total is **13 rendered pages** if the Welcome page is retired, the other five current pages are retained (including the public `/movies` catalog), and the eight planned pages above are added. The root page will then show today's showtimes instead of a separate welcome screen. The JSON seat-status endpoint is not included in the page count.

## Milestone 7: Customer accounts and reservation history

Build Login, Register, My Reservations, and the 403 page. The design note says visitors can browse movies, showtimes, and seats; login or registration is required when they proceed to reserve. If the visitor is sent to login from a seat map, return them to that same seat-map path after login, but do not restore their previous checkbox selections. Validate the return path so it can only point inside this application; do not accept a full external URL. A signed-in CUSTOMER must be the owner when opening a reservation. ADMIN accounts come from seed data, not the registration form. My Reservations is one list ordered newest first.

This milestone also replaces the temporary `demo-customer` booking identity with the authenticated user's ID. Login, password hashing, session handling, role checks, reservation ownership checks, and a query for the current user's reservations still need implementation. The current booking and lookup services provide some starting operations, but they do not supply this security or customer-specific history yet.

## Milestone 8: Admin operations

Build the movie list and add/edit form, the create-showtime form, and the check-in-by-code page. Existing movie and showtime list operations can be reused, but the admin write/update operations and check-in status change still need service and repository work. Every admin action must check the ADMIN role and return the 403 page when access is denied.

## Later layout pass

In a layout/design milestone, make the root page show today's showtimes using the existing showtime flow and retire the separate Welcome screen. Keep `/movies` as a separate, plain movie catalog. Apply the selected dark cinema theme and shared navigation after the required flows are settled. The current pages are intentionally simple and do not yet represent the final design.

## Decisions already made

- Keep `/movies` as the plain catalog; the root home page shows today's showtimes.
- After login, return visitors to the same show's seat map. Do not save or restore the checked seat selection.
- Accept only an in-application return path, never a full URL, to prevent an external redirect.
- My Reservations is one list, newest first.
- ADMIN can open any reservation by code; CUSTOMER can open only their own reservation.
- Use the previously selected dark cinema theme for the layout/design pass.

Keep the learning log and `AI_USAGE.md` updated after each milestone.
