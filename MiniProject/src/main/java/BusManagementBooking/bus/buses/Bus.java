package BusManagementBooking.bus.buses;

import BusManagementBooking.bus.booking.Booking;
import BusManagementBooking.bus.seats.Seat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "route", nullable = false, length = 200)
    private String route;

    @Column(name = "departure_date", nullable = false)
    private String departureDate; // Format: dd-mm-yyyy

    @Column(name = "departure_time", nullable = false, length = 50)
    private String departureTime;

    @Column(name = "arrival_time", nullable = false, length = 50)
    private String arrivalTime;

    @Column(name = "available_seats")
    private Integer availableSeats;

    @Column(name = "total_seats")
    private Integer totalSeats;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Seat> seats = new ArrayList<>();

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Booking> bookings = new ArrayList<>();

    // Constructors
    public Bus() {}

    public Bus(String name, String route, String departureDate, String departureTime, String arrivalTime, Integer availableSeats, Integer totalSeats, BigDecimal price) {
        this.name = name;
        this.route = route;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
        this.totalSeats = totalSeats;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        // Ensure the date is in dd-mm-yyyy format
        if (departureDate != null && !departureDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
            try {
                // If the date is in yyyy-mm-dd format, convert it to dd-mm-yyyy
                if (departureDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    String[] parts = departureDate.split("-");
                    this.departureDate = parts[2] + "-" + parts[1] + "-" + parts[0];
                } else {
                    // Try to parse and format the date
                    java.time.LocalDate date = java.time.LocalDate.parse(departureDate);
                    this.departureDate = String.format("%02d-%02d-%d", 
                        date.getDayOfMonth(), 
                        date.getMonthValue(), 
                        date.getYear());
                }
            } catch (Exception e) {
                // If parsing fails, keep the original value
                this.departureDate = departureDate;
            }
        } else {
            this.departureDate = departureDate;
        }
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
