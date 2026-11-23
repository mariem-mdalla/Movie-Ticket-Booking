```mermaid
erDiagram
    USERS ||--o{ RESERVATION : makes
    MOVIE ||--o{ SHOWTIME : has
    AUDITORIUM ||--o{ SEAT : contains
    AUDITORIUM ||--o{ SHOWTIME : hosts
    SHOWTIME ||--o{ RESERVATION : "booked through"
    RESERVATION ||--o{ RESERVATION_SEAT : includes
    SEAT ||--o{ RESERVATION_SEAT : "reserved as"
    SHOWTIME ||--o{ RESERVATION_SEAT : "seat taken for"

    USERS {
        long id PK
        string username UK
        string password_hash
        string role
    }
    MOVIE {
        long id PK
        string title
        string description
        int duration_minutes
        string rating
        string poster_url
    }
    AUDITORIUM {
        long id PK
        string name
    }
    SEAT {
        long id PK
        long auditorium_id FK
        string row_label
        int seat_number
    }
    SHOWTIME {
        long id PK
        long movie_id FK
        long auditorium_id FK
        datetime start_time
        decimal price
    }
    RESERVATION {
        long id PK
        long user_id FK
        long showtime_id FK
        string confirmation_code UK
        decimal total_price
        string status
        datetime created_at
    }
    RESERVATION_SEAT {
        long id PK
        long reservation_id FK
        long showtime_id FK
        long seat_id FK
        decimal price_at_booking
    }
```

**Constraints**
- `USERS.username` is unique.
- `SEAT` is unique on (auditorium_id, row_label, seat_number).
- `RESERVATION.confirmation_code` is unique.
- `RESERVATION_SEAT` is unique on (showtime_id, seat_id). This is the seat rule.