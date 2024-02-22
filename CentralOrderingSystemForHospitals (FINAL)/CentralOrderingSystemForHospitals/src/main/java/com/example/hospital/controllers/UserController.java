package com.example.hospital.controllers;

import com.example.hospital.models.User;
import com.example.hospital.models.UserRole;
import com.example.hospital.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;




    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }







    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        System.out.println("Number of users retrieved: " + users.size());
        model.addAttribute("users", users);
        return "user_list";
    }


    @GetMapping("/{id}")
    public String getUserById(@PathVariable String id, Model model) {
        Optional<User> user = userService.getUserById(id);
        user.ifPresent(u -> model.addAttribute("user", u));


        return "profile";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/update/{id}")  // za prikazat formu za update korisnika
    public String showUpdateForm(@PathVariable String id, Model model) {
        Optional<User> userOptional = userService.getUserById(id);
        List<User> doctorList=userService.getAllDoctorsOfGeneralMedicine();
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            model.addAttribute("role",userOptional.get().getRole());
            model.addAttribute("doctorsOfGeneralMedicine", doctorList);
            return "updateProfile";
        } else {
            return "redirect:/users/login";
        }
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable String id, @ModelAttribute("user") User updatedUser, Model model) {
        Optional<User> existingUserOptional = userService.getUserById(id);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            existingUser.setName(updatedUser.getName());
            existingUser.setSurname(updatedUser.getSurname());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setContact(updatedUser.getContact());

            // Update fields based on the role
            if (existingUser.getRole().equals( "PATIENT")) {
                existingUser.setInsuranceNumber(updatedUser.getInsuranceNumber());
                existingUser.setBill(updatedUser.getBill());
                existingUser.setDoctorOfGeneralMedicineID(updatedUser.getDoctorOfGeneralMedicineID());
            } else if ("DOCTOR_SPECIALIST".equals(existingUser.getRole()) || "DOCTOR_GENERAL_MEDICINE".equals(existingUser.getRole())) {
                existingUser.setAddress(updatedUser.getAddress());
                existingUser.setSpecialisation(updatedUser.getSpecialisation());
            }

            userService.updateUser(existingUser);
            model.addAttribute("user", existingUser);
            return "redirect:/users/";
        } else {
            return "redirect:/users/login";
        }
    }


    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        // Check if the user is logged in and set the userRole accordingly
        String userRole = (String) session.getAttribute("userRole");

        model.addAttribute("userRole", userRole);

        if (userRole == null) {
            // Korisnik nije prijavljen, možete preusmjeriti na stranicu za prijavu ili obraditi kako želite
            return "redirect:/users/login";
        } else {
            // Korisnik je prijavljen, prikaži indeksnu stranicu
            return "index";
        }

        //return "index";
    }


    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/users/admin";
    }

    @GetMapping("/register") // samo prikazuje registracijsku formu
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());

        List<User> doctorsOfGeneralMedicine = userService.getAllDoctorsOfGeneralMedicine();
        model.addAttribute("doctorsOfGeneralMedicine", doctorsOfGeneralMedicine);

        return "register2";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        try {
            // Add logging
            System.out.println("Registering user: " + user.getUsername());

            if (userService.usernameTaken(user.getUsername())) {
                model.addAttribute("error", "Username taken!");
                System.out.println("Username taken!");
                return "redirect:/users/register";
            }
            if (userService.emailTaken(user.getEmail())) {
                model.addAttribute("error", "Email taken!");
                System.out.println("Email taken!");
                return "redirect:/users/register";
            }



            // Save the user locally
            userService.registerUser(user);

            System.out.println("User registered successfully!");

            return "redirect:/users/login";

        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            model.addAttribute("error", "An error occurred during registration.");
            return "redirect:/users/register";
        }
    }



    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session) {
        if (!userService.usernameTaken(user.getUsername())) {
            return "redirect:/login?error=Wrong username or password";
        }
        if (userService.authenticate(user.getUsername(), user.getPassword())) {
            User loggedInUser = userService.getUserByUsername(user.getUsername()); // dohvaca usera na osnovu username-a
            session.setAttribute("user", loggedInUser);
            session.setAttribute("userName", loggedInUser.getUsername());


            //Optional<User> userOptional = userService.getUserById(user.getId());  // optional zato jer moze bit NULL
            //User loggedInUser = userOptional.orElse(null);  // podaci o logiranom useru
            UserRole userRoleObject = loggedInUser.getRole(); // vraca UserRole objekt ili null
            String loggedInUserRole = (userRoleObject != null) ? userRoleObject.toString() : null; // vraca rolu u obliku stringa, i provjerava je li slucajno null
            System.out.println("UserRole (from object): " + loggedInUserRole);  // provjera
            System.out.println("UserID: " + loggedInUser.getId());  // provjera
            System.out.println("UserName: " + loggedInUser.getUsername());  // provjera
            session.setAttribute("userRole", loggedInUserRole); // sprema rolu u sesiju kako bi je dohvatili u indexu kasnije
            return "redirect:/users/";
        }
        return "redirect:/users/login";
    }



    @GetMapping("/admin")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user_list";
    }








    @GetMapping("/search")
    public String searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,

            @RequestParam(required = false) UserRole userRole,
            Model model
    ) {
        List<User> users = userService.searchUsers(name,surname, userRole);
        model.addAttribute("users", users);
        return "user_list";
    }
    @GetMapping("/searchPatient")
    public String searchPatient(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            Model model
    ) {
        List<User> patients = userService.searchPatients(name, surname);
        model.addAttribute("patients", patients);
        return "user_list";
    }

    @GetMapping("/searchSpecialist")
    public String searchSpecialist(
            @RequestParam(required = false) String specialization,
            Model model
    ) {
        List<User> specialists = userService.searchSpecialist(specialization);
        model.addAttribute("specialists", specialists);
        return "user_list";
    }




    @GetMapping("/profile")
    public String getUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            Optional<User> userOptional = userService.getUserById(user.getId());
            if (userOptional.isPresent()) {
                User user1=userOptional.orElseThrow(()-> new RuntimeException("User not found"));
                model.addAttribute("user", user1);
                return "profile";
            }
        }

        return "redirect:/users/login";
    }




    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/myPatients")
    public String findMyPatients(Model model, HttpSession session){
        User doctorGM = (User) session.getAttribute("user");   // iz sesije se dohvaca doctor of gen. med.
        List<User> patients=userService.showMyPatients(doctorGM.getId());
        System.out.println("session id:" + session.getId());
        model.addAttribute("patients",patients);
        return "my_patients";
    }


}
