package BusManagementBooking.bus.buses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;

    @Override
    public Bus addBus(BusAddRequestDTO busAddRequestDTO) {
        // Ensure the departure date is in dd-mm-yyyy format
        String departureDate = busAddRequestDTO.getDepartureDate();
        if (departureDate != null && !departureDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
            try {
                // If the date is in yyyy-mm-dd format, convert it to dd-mm-yyyy
                if (departureDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    String[] parts = departureDate.split("-");
                    departureDate = parts[2] + "-" + parts[1] + "-" + parts[0];
                } else {
                    // Try to parse and format the date
                    java.time.LocalDate date = java.time.LocalDate.parse(departureDate);
                    departureDate = String.format("%02d-%02d-%d", 
                        date.getDayOfMonth(), 
                        date.getMonthValue(), 
                        date.getYear());
                }
            } catch (Exception e) {
                // If parsing fails, keep the original value
            }
        }

        Bus bus = new Bus(
                busAddRequestDTO.getName(),
                busAddRequestDTO.getRoute(),
                departureDate,
                busAddRequestDTO.getDepartureTime(),
                busAddRequestDTO.getArrivalTime(),
                busAddRequestDTO.getAvailableSeats(),
                busAddRequestDTO.getTotalSeats(),
                new BigDecimal(busAddRequestDTO.getPrice())
        );
        return busRepository.save(bus);
    }

    @Override
    public List<Bus> getBuses() {
        return busRepository.findAll();
    }
    
    @Override
    public Optional<Bus> getBusById(Long busId) {
        return busRepository.findById(busId);
    }
    
    @Override
    public List<Bus> searchBuses(String name, String route, String departure, String arrival) {
        boolean hasName = name != null && !name.isEmpty();
        boolean hasRoute = route != null && !route.isEmpty();
        
        // Both name and route provided
        if (hasName && hasRoute) {
            List<Bus> routeResults = busRepository.findByRouteContainingIgnoreCase(route);
            return routeResults.stream()
                .filter(bus -> bus.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        }
        
        // Only name provided
        if (hasName) {
            return busRepository.findByNameContainingIgnoreCase(name);
        }
        
        // Only route provided
        if (hasRoute) {
            return busRepository.findByRouteContainingIgnoreCase(route);
        }
        
        // No parameters - return all buses
        return busRepository.findAll();
    }

    @Override
    public void deleteBus(Long busId) {
        busRepository.deleteById(busId);
    }

    @Override
    public Bus updateBus(Long busId, BusAddRequestDTO busUpdateRequestDTO) {
        Optional<Bus> existingBus = busRepository.findById(busId);
        if (existingBus.isPresent()) {
            Bus bus = existingBus.get();
            bus.setName(busUpdateRequestDTO.getName());
            bus.setRoute(busUpdateRequestDTO.getRoute());
            bus.setDepartureDate(busUpdateRequestDTO.getDepartureDate());
            bus.setDepartureTime(busUpdateRequestDTO.getDepartureTime());
            bus.setArrivalTime(busUpdateRequestDTO.getArrivalTime());
            bus.setAvailableSeats(busUpdateRequestDTO.getAvailableSeats());
            bus.setTotalSeats(busUpdateRequestDTO.getTotalSeats());
            bus.setPrice(new BigDecimal(busUpdateRequestDTO.getPrice()));
            return busRepository.save(bus);
        }
        return null;
    }
}
