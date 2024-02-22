package com.example.CentralOrderingSystemForHospitals.controller;

import com.example.hospital.controllers.UserController;
import com.example.hospital.models.User;
import com.example.hospital.models.UserRole;
import com.example.hospital.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        // Postavite mock objekte
        userService = Mockito.mock(UserService.class);
        // Injektirajte mock objekte u UserController
        userController = new UserController(userService);
    }

    @Test
    public void testGetAllUsers() {
        List<User> userList = Arrays.asList(new User(), new User());
        when(userService.getAllUsers()).thenReturn(userList);

        Model model = mock(Model.class);
        String result = userController.getAllUsers(model);

        assertEquals("user_list", result);
        verify(userService, times(1)).getAllUsers();
        verify(model, times(1)).addAttribute("users", userList);
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        Optional<User> userOptional = Optional.of(user);
        when(userService.getUserById("1")).thenReturn(userOptional);

        Model model = mock(Model.class);
        String result = userController.getUserById("1", model);

        assertEquals("profile", result);
        verify(userService, times(1)).getUserById("1");
        verify(model, times(1)).addAttribute("user", user);
    }

    @Test
    public void testRegisterUser() {
        User user = new User();
        String result = userController.registerUser(user);

        assertEquals("redirect:/users", result);
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    public void testShowUpdateForm() {
        User user = new User();
        Optional<User> userOptional = Optional.of(user);
        when(userService.getUserById("1")).thenReturn(userOptional);
        List<User> doctorList = Arrays.asList(new User(), new User());
        when(userService.getAllDoctorsOfGeneralMedicine()).thenReturn(doctorList);

        Model model = mock(Model.class);
        String result = userController.showUpdateForm("1", model);

        assertEquals("updateProfile", result);
        verify(userService, times(1)).getUserById("1");
        verify(userService, times(1)).getAllDoctorsOfGeneralMedicine();
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("role", user.getRole());
        verify(model, times(1)).addAttribute("doctorsOfGeneralMedicine", doctorList);
    }



    @Test
    public void testIndex() {
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);

        when(session.getAttribute("userRole")).thenReturn("ADMIN");
        String result = userController.index(model, session);
        assertEquals("index", result);
        verify(model, times(1)).addAttribute("userRole", "ADMIN");

        when(session.getAttribute("userRole")).thenReturn(null);
        result = userController.index(model, session);
        assertEquals("redirect:/users/login", result);
    }

    @Test
    public void testDeleteUser() {
        String result = userController.deleteUser("1");
        assertEquals("redirect:/users/admin", result);
        verify(userService, times(1)).deleteUser("1");
    }

    @Test
    public void testShowRegisterForm() {
        Model model = mock(Model.class);
        List<User> doctorsOfGeneralMedicine = Arrays.asList(new User(), new User());
        when(userService.getAllDoctorsOfGeneralMedicine()).thenReturn(doctorsOfGeneralMedicine);

        String result = userController.showRegisterForm(model);
        assertEquals("register2", result);
        verify(model, times(1)).addAttribute("user", new User());
        verify(model, times(1)).addAttribute("doctorsOfGeneralMedicine", doctorsOfGeneralMedicine);
    }

    @Test
    public void testRegister() {
        Model model = mock(Model.class);
        User user = new User();
        String result = userController.register(user, model);

        assertEquals("redirect:/users/login", result);
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    public void testShowLoginForm() {
        Model model = mock(Model.class);
        String result = userController.showLoginForm(model);
        assertEquals("login", result);
        verify(model, times(1)).addAttribute("loginRequest", new User());
    }

    @Test
    public void testLogin() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        HttpSession session = mock(HttpSession.class);
        when(userService.usernameTaken("testuser")).thenReturn(true);
        when(userService.authenticate("testuser", "password")).thenReturn(true);
        when(userService.getUserByUsername("testuser")).thenReturn(user);

        String result = userController.login(user, session);
        assertEquals("redirect:/users/", result);

        when(userService.authenticate("testuser", "wrongpassword")).thenReturn(false);
        result = userController.login(user, session);
        assertEquals("redirect:/users/", result);

        when(userService.usernameTaken("nonexistentuser")).thenReturn(false);
        result = userController.login(new User(), session);
        assertEquals("redirect:/login?error=Wrong username or password", result);
    }

    @Test
    public void testListUsers() {
        List<User> userList = Arrays.asList(new User(), new User());
        when(userService.getAllUsers()).thenReturn(userList);

        Model model = mock(Model.class);
        String result = userController.listUsers(model);

        assertEquals("user_list", result);
        verify(userService, times(1)).getAllUsers();
        verify(model, times(1)).addAttribute("users", userList);
    }

    @Test
    public void testSearchUsers() {
        List<User> userList = Arrays.asList(new User(), new User());
        when(userService.searchUsers("John", "Doe", UserRole.PATIENT)).thenReturn(userList);

        Model model = mock(Model.class);
        String result = userController.searchUsers("John", "Doe", UserRole.PATIENT, model);

        assertEquals("user_list", result);
        verify(userService, times(1)).searchUsers("John", "Doe", UserRole.PATIENT);
        verify(model, times(1)).addAttribute("users", userList);
    }

    @Test
    public void testSearchPatient() {
        List<User> patientList = Arrays.asList(new User(), new User());
        when(userService.searchPatients("John", "Doe")).thenReturn(patientList);

        Model model = mock(Model.class);
        String result = userController.searchPatient("John", "Doe", model);

        assertEquals("user_list", result);
        verify(userService, times(1)).searchPatients("John", "Doe");
        verify(model, times(1)).addAttribute("patients", patientList);
    }

    @Test
    public void testSearchSpecialist() {
        List<User> specialistList = Arrays.asList(new User(), new User());
        when(userService.searchSpecialist("Cardiology")).thenReturn(specialistList);

        Model model = mock(Model.class);
        String result = userController.searchSpecialist("Cardiology", model);

        assertEquals("user_list", result);
        verify(userService, times(1)).searchSpecialist("Cardiology");
        verify(model, times(1)).addAttribute("specialists", specialistList);
    }

    @Test
    public void testGetUser() {
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);

        User user = new User();
        user.setId("1");

        when(session.getAttribute("user")).thenReturn(user);
        Optional<User> userOptional = Optional.of(user);
        when(userService.getUserById("1")).thenReturn(userOptional);

        String result = userController.getUser(model, session);
        assertEquals("profile", result);
        verify(userService, times(1)).getUserById("1");
        verify(model, times(1)).addAttribute("user", user);
    }

    @Test
    public void testLogout() {
        HttpSession session = mock(HttpSession.class);
        String result = userController.logout(session);

        assertEquals("redirect:/", result);
        verify(session, times(1)).invalidate();
    }


}

