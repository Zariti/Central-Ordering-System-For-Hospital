package com.example.hospital.services;

import com.example.hospital.models.*;
import com.example.hospital.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id){
        return userRepository.findById(id);
    }

    @Transactional
    public void saveUser(User user){
        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    public List<User> getAllDoctorsOfGeneralMedicine() {
        return userRepository.findAllByRole(UserRole.DOCTOR_GENERAL_MEDICINE);
    }


     //this was in Username Service
    @Transactional
     public void registerUser(User user) {
         try {
             PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
             String hashedPassword = passwordEncoder.encode(user.getPassword());
             user.setPassword(hashedPassword);

             userRepository.save(user);
         } catch (Exception e) {
             // Log the exception or handle it accordingly
             e.printStackTrace();
             throw new RuntimeException("Error registering user", e);
         }
     }


    public boolean usernameTaken(String username){
        Optional<User> userOptional=userRepository.findUserByUsername(username);
        return userOptional.isPresent();
    }


    public boolean emailTaken(String email){
        Optional<User> userOptional=userRepository.findUsernameByEmail(email);
        return userOptional.isPresent();
    }

    public boolean authenticate(String username,String password){
        BCryptPasswordEncoder passwordEncoder =new BCryptPasswordEncoder();
        Optional<User> optionalUsername =userRepository.findUserByUsername(username);

        if(optionalUsername.isPresent()){
            User user=optionalUsername.get();
            return passwordEncoder.matches(password, user.getPassword());

        }return false;
    }

    public List<User> searchUsers(String name,String surname, UserRole userRole) {
        if (name != null && surname!= null && userRole != null) {
            // Search by both username and user role
            return userRepository.findByNameAndSurnameAndRole(name,surname, userRole);
        } else if (name != null) {
            // Search by username only
            return userRepository.findByName(name);
        } else if (userRole != null) {
            // Search by user role only
            return userRepository.findByRole(userRole);
        } else {
            // Return all users if no search criteria provided
            return getAllUsers();
        }
    }


    public List<User> searchPatients(String name,String surname) {
        if (name != null && surname!= null ) {
            // Search by both username and user role
            return userRepository.findByNameAndSurnameAndRole(name,surname, UserRole.valueOf("PATIENT"));
        } else if (name != null) {
            // Search by username only
            return userRepository.findByNameAndRole(name,UserRole.valueOf("PATIENT"));

        } else {
            // Return all users if no search criteria provided
            return userRepository.findBySurnameAndRole(surname,UserRole.valueOf("PATIENT"));
        }
    }

    public List<User> searchSpecialist(String specialist) {

            return userRepository.findBySpecialisation(specialist);

    }


    public List<User> getAllPatients() {
        return userRepository.findAllByRole(UserRole.PATIENT);
    }

    public User getUserByUsername(String username) {
        // Koristite UserRepository ili odgovarajući mehanizam za dohvat korisnika iz baze prema korisničkom imenu
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        // Ako korisnik postoji, vraća se User objekt, inače null
        return userOptional.orElse(null);
    }

     public List<User> getAllSpecialists(){
        return userRepository.findAllByRole(UserRole.valueOf("DOCTOR_SPECIALIST"));
    }
    @Transactional
    public void updateUser(User user){
        userRepository.updateUserByAddressAndDoctorOfGeneralMedicineIDAndSpecialisationAndContactAndBillAndInsuranceNumberAndNameAndSurname
                (user.getAddress(),user.getDoctorOfGeneralMedicineID(),user.getSpecialisation(),user.getContact(),user.getBill(),user.getInsuranceNumber(),user.getName(),user.getSurname(), user.getId());
    }


    public List<User> showMyPatients(String doctor){
        return userRepository.findAllByDoctorOfGeneralMedicineID(doctor);
    }

    public User getMyDoctorOfGeneralMedicine(String doctorId){
        return userRepository.findByDoctorOfGeneralMedicineID(doctorId);
    }
}
