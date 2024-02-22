package com.example.hospital.services;

import com.example.hospital.models.*;
import com.example.hospital.repositories.OrderRepository;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.hospital.services.Spec.OrderSpecification;

//import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class   OrderService {

    private final OrderRepository orderRepository;



    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;

    }

    //OrderRepository orderRepository;
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
    public Optional<Order> getOrderById(String id){
        return orderRepository.findById(id);
    }


    @Transactional // u slucaj da dode do greske transakcija ce se ponistit i baza ce ostat nepromijenjena
    public void updateResultForOrder(String orderId, Result result) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setResult(result);
        orderRepository.save(order);
    }

    public List<Order> searchOrders(String userId, String resultId) {
        Specification<Order> spec = Specification.where(null);

        if (userId != null && !userId.isEmpty()) {
            spec = spec.and(OrderSpecification.withUserId(userId)); // OrderSpecification je u nasem Spec paketu
        }

        if (resultId != null && !resultId.isEmpty()) {
            spec = spec.and(OrderSpecification.withResultId(resultId));
        }

        // If needed we can add some more parameters;

        return orderRepository.findAll(spec);
    }
    public void deleteOrder(String orderId){
        orderRepository.deleteById(orderId);
    }

    public LocalDateTime findNextAvailableTimeForDoctorSpecialist(User doctorSpecialist) {
        // Set the start and end times of the working hours
        LocalTime startWorkingHour = LocalTime.of(9, 0);
        LocalTime endWorkingHour = LocalTime.of(15, 0);

        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Set the current time to the start of the next working day
        LocalDateTime nextWorkingTime = LocalDateTime.of(LocalDate.now().plusDays(1), startWorkingHour);
        if (now.isAfter(nextWorkingTime)) {
            nextWorkingTime = nextWorkingTime.plusDays(1);
        }


        // Get the doctor's existing orders, sorted by date
        List<Order> doctorOrders = orderRepository.findByDoctorSpecialistOrderByDateTime(doctorSpecialist);

        // Find the first available time slot within working hours, excluding weekends
        LocalDateTime nextAvailableTime = nextWorkingTime;

        for (Order order : doctorOrders) {
            LocalDateTime orderDateTime = order.getDateTime();
            if (orderDateTime != null && orderDateTime.isAfter(nextAvailableTime) && !isWeekendDay(orderDateTime)) {
                return nextAvailableTime;
            }

            // Move to the end of the current order
            nextAvailableTime = (orderDateTime != null) ? orderDateTime.plusMinutes(20) : nextAvailableTime.plusMinutes(20);
        }

        // Check if the next available time is within working hours and not on the weekend
        while (nextAvailableTime.toLocalTime().isBefore(endWorkingHour) && isWeekendDay(nextAvailableTime)) {
            // Move to the next time slot
            nextAvailableTime = nextAvailableTime.plusMinutes(20);
        }

        // If no available time is found set it to the start of the next working day
        if (isWeekendDay(nextAvailableTime)) {
            nextAvailableTime = nextAvailableTime.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).with(startWorkingHour);
        }

        return nextAvailableTime;
    }



    private boolean isWeekendDay(LocalDateTime dateTime) {
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }


    public Order createOrder(Order order) {
        LocalDateTime nextAvailableTime = findNextAvailableTimeForDoctorSpecialist(order.getDoctorSpecialist());
        if (nextAvailableTime != null) {
            order.setDateTime(nextAvailableTime);
            return orderRepository.save(order);
        } else {
            // Handle the case where no available time is found, e.g., throw an exception or return a special value.
            return null;
        }
    }




    public List<Order> findPatientsByDoctorOfGeneralMedicine(User doctorId){
        return orderRepository.findAllByDoctorOfGeneralMedicine(doctorId);
    }



    public List<Order> getOrdersForPatient(User user) {
        // Assuming you have a method in your repository to find orders by user ID
        return orderRepository.findAllByPatient(user);
    }

    public List<Order> getOrdersBySpecialistDoctor(User user){
        List<Order> orders= orderRepository.findAllByDoctorSpecialist(user);
        return orders.stream()
                .sorted(Comparator.comparing(Order::getDateTime)) // Assuming your Order class has a getDateTime method
                .sorted(Comparator.comparing(o -> {
                    LocalDate today = LocalDate.now();
                    LocalDate orderDate = o.getDateTime().toLocalDate();
                    if (orderDate.isEqual(today)) {
                        return 0; // Today's orders come first
                    } else if (orderDate.isAfter(today)) {
                        return 1; // Future orders come next
                    } else {
                        return 2; // Orders before today come last
                    }
                }))
                .collect(Collectors.toList());
    }

}