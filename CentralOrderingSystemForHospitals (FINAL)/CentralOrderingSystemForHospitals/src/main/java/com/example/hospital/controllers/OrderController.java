
package com.example.hospital.controllers;

import com.example.hospital.models.*;
import com.example.hospital.services.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {


    private final OrderService orderService;
    private final UserService userService;
    @PersistenceContext
    private EntityManager entityManager;




    @Autowired
    public OrderController(OrderService orderService,UserService userService){
        this.orderService = orderService;
        this.userService=userService;


    }





    @GetMapping("/admin")
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order_list";
    }




//    @GetMapping("/search")
//    public String searchOrders(
//            @RequestParam(required = false) String userId,
//            @RequestParam(required = false) String resultId,
//            Model model
//            // Add more parameters if needed
//    ) {
//        List<Order> orders = orderService.searchOrders(userId, resultId);
//        model.addAttribute("orders", orders);
//        return "order/list";  // Assuming you have a Thymeleaf template named "order/list.html"
//    }








    @GetMapping("/make_order")
    public String showOrderForm(Model model, HttpSession session) {
        model.addAttribute("order", new Order());
        User doctorGM = (User) session.getAttribute("user");   // iz sesije se dohvaca doctor of gen. med.
        List<User> patients=userService.showMyPatients(doctorGM.getId()); // dohvaca sve pacijente doktora
        List<User> specialist=userService.getAllSpecialists();

        model.addAttribute("patients",patients);
        model.addAttribute("specialist",specialist);
        return "make_order";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("order") Order order, Model model, HttpSession session)
    {
        order.setDoctorOfGeneralMedicine((User) session.getAttribute("user")); // sprema se doktor koji je napravio order u order
        Order createdOrder = orderService.createOrder(order);

        // Assuming selectedPatient and selectedDoctorSpecialist are available in your controller
        // You need to set them in the model to be accessible in the Thymeleaf template
        model.addAttribute("selectedPatient", order.getPatient());
        model.addAttribute("selectedDoctorSpecialist", order.getDoctorSpecialist());
        model.addAttribute("result",order.getResult());

        return "redirect:/orders/" + createdOrder.getId();
    }


    @GetMapping("/myPatientsGeneralMedicine")  // pronalazi sve pacijente nekog doktora
    public String findMyPatientsByDoctorOfGeneralMedicine(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("user") != null) {
            // User is logged in, retrieve the username or other details
            User user = (User) httpSession.getAttribute("user");



            List<Order> myOrders = orderService.findPatientsByDoctorOfGeneralMedicine(user);
            model.addAttribute("allOrders", myOrders);
            return "order_list";
        } else {
            // User is not logged in, handle accordingly (redirect to login page, show an error, etc.)
            return "redirect:/login";
        }
    }

    @GetMapping("/myOrdersSpecialist")
    public String findMyOrdersByDoctorSpecialist(
            Model model,
            HttpSession httpSession) {

        // User is logged in, retrieve the username or other details
        User user = (User) httpSession.getAttribute("user");

        // Get filtered and sorted orders
        List<Order> myOrders = orderService.getOrdersBySpecialistDoctor(user);

        model.addAttribute("user", user);
        model.addAttribute("orders", myOrders);

        return "my_orders_for_specialist";
    }

    @GetMapping("/{id}")    // dohvaca detalje o izabranom orderu iz liste
    public String getOrderById(@PathVariable String id, Model model) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            model.addAttribute("reason",order.get().getReasonForExamination());
            model.addAttribute("orderDate", order.get().getDateTime());
            model.addAttribute("result", order.get().getResult());
            model.addAttribute("doctorOfGeneralMedicine", order.get().getDoctorOfGeneralMedicine());
            model.addAttribute("doctorSpecialist", order.get().getDoctorSpecialist());
            model.addAttribute("patient", order.get().getPatient());

            return "order_details";  // Assuming you have a Thymeleaf template named "order/details.html"
        } else {
            // Handle the case where the order with the given ID is not found
            return "redirect:/orders"; // You can redirect to the orders listing page or show an error page
        }
    }



    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable String id) {
        entityManager.clear();
        orderService.deleteOrder(id);
        return "redirect:/orders/admin";
    }




    @GetMapping("/patient")
    public String getOrdersForPatient(HttpSession session, Model model) {  // dohvaca ordere od pacijenta
        User user = (User) session.getAttribute("user");

        if (user == null || user.getDoctorOfGeneralMedicineID() == null) {
            return "redirect:/users/login";
        }

        Optional<User> doctor = userService.getUserById(user.getDoctorOfGeneralMedicineID());

        if (doctor.isPresent()) {
            List<Order> orders = orderService.getOrdersForPatient(user);
            model.addAttribute("orders", orders);
            model.addAttribute("patient", user);
            model.addAttribute("doctor", doctor.get());  // Since you've checked for presence, safe to get()
            return "patient_orders";
        } else {
            return "redirect:/users/login";
        }
    }




    @GetMapping("/patient/{id}") // dohvaca ordere od pacijenta sa doktorovog accounta
    public String getOrdersForPatientByDoctor( HttpSession session,@PathVariable String id, Model model) {

        Optional<User> user=userService.getUserById(id);

        if(user.isPresent()) {
            List<Order> orders = orderService.getOrdersForPatient(user.get());
            model.addAttribute("orders", orders);
            model.addAttribute("patient", user.get());
            model.addAttribute("doctor",(User)session.getAttribute("user"));
            return "patient_orders";
        }

        return "redirect: users/login";

    }




}

