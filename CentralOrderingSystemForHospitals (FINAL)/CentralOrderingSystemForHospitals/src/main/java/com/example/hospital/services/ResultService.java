package com.example.hospital.services;

import com.example.hospital.models.*;
import com.example.hospital.repositories.OrderRepository;
import com.example.hospital.repositories.ResultRepository;
import com.example.hospital.services.Spec.ResultSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository,OrderRepository orderRepository){
        this.resultRepository = resultRepository;
        this.orderRepository=orderRepository;
    }

    //ResultRepository resultRepository;
    public List<Result> getAllResults(){
        return resultRepository.findAll();
    }
    public Result getResultById(String id){
        return resultRepository.getReferenceById(id);
    }
    public Result saveResult(Result result){
        return resultRepository.save(result);
    }

    public List<Result> searchResults(Map<String, String> params) {
        Specification<Result> spec = ResultSpecification.buildSpecifications(params);
        return resultRepository.findAll(spec);
    }

    @Transactional
    public void createResult(Result result) {
        try {
            resultRepository.save(result);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // You might want to throw or handle the exception accordingly
        }



    }


    public void deleteResult(String id){
        resultRepository.deleteById(id);
    }
}
