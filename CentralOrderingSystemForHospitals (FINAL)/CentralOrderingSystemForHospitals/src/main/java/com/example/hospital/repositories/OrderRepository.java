package com.example.hospital.repositories;

import com.example.hospital.models.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findAll(Specification<Order> specification);

    List<Order> findByDoctorSpecialistOrderByDateTime(User doctorSpecialist);

    List<Order> findAllByDoctorOfGeneralMedicine(User doctorOfGeneralMedicine);

    List<Order> findAllByDoctorSpecialist(User doctorOfGeneralMedicine);


    List<Order> findAllByPatient(User patient);




}

