package com.example.hospital.services.Spec;

import com.example.hospital.models.Result;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class ResultSpecification {

    public static Specification<Result> buildSpecifications(Map<String, String> params) {
        //stvara početnu specifikaciju koja je "prazna" ili ne sadrži nikakve kriterije. Ovo je početna točka za izgradnju specifikacije
        Specification<Result> spec = Specification.where(null);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "diagnosis":
                    spec = spec.and(withDiagnosis(value));
                    break;
                case "advice":
                    spec = spec.and(withAdvice(value));
                    break;

                }
            }

            return spec;
        }

        public static Specification<Result> withDiagnosis(String diagnosis) {
            return (root, query, cb) -> cb.like(cb.lower(root.get("diagnosis")), "%" + diagnosis.toLowerCase() + "%");
        }


        public static Specification<Result> withAdvice(String advice) {
            return (root, query, cb) -> cb.like(cb.lower(root.get("advice")), "%" + advice.toLowerCase() + "%");
        }
    }



