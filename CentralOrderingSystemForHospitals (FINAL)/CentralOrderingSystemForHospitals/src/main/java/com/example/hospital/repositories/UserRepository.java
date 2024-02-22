package com.example.hospital.repositories;

import com.example.hospital.models.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Modifying
    @Query("UPDATE User u SET u.address = :address, u.doctorOfGeneralMedicineID = :doctorId, u.specialisation = :specialisation, u.contact = :contact, u.bill = :bill, u.insuranceNumber = :insuranceNumber, u.name = :name, u.surname = :surname WHERE u.id = :userId")
    void updateUserByAddressAndDoctorOfGeneralMedicineIDAndSpecialisationAndContactAndBillAndInsuranceNumberAndNameAndSurname(
            @Param("address") String address,
            @Param("doctorId") String doctorOfGeneralMedicineID,
            @Param("specialisation") String specialisation,
            @Param("contact") String contact,
            @Param("bill") String bill,
            @Param("insuranceNumber") String insuranceNumber,
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("userId") String userId);
    public Optional<User> findUserByUsername(String username);
    public Optional<User> findUsernameByEmail(String email);

    public List<User> findAll(Specification<User> specification);

    public List<User> findByNameAndSurnameAndRole(String name,String surname,UserRole role);

    public List<User> findByName(String name);

    public List<User> findBySurname(String name);

    public List<User> findByRole(UserRole role);


    public List<User> findByNameAndRole(String name,UserRole role);

    public List<User> findBySurnameAndRole(String name,UserRole role);

    public List<User> findBySpecialisation(String specialisation);

    public User findByDoctorOfGeneralMedicineID(String doctorId);
    public List<User> findAllByDoctorOfGeneralMedicineID(String doctor);
    public  List<User> findAllByRole(UserRole role);
}

