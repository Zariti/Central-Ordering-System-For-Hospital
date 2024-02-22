package com.example.hospital.repositories;

import com.example.hospital.models.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {

    List<Result> findAll(Specification<Result> specification);  // filtrira resultate?
}

