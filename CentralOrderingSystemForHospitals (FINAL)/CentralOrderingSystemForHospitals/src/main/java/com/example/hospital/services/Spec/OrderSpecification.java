package com.example.hospital.services.Spec;

import com.example.hospital.models.Order;
import org.springframework.data.jpa.domain.Specification;


public class OrderSpecification {

    //withUserId vraća Specification<Order> koji filtrira narudžbe prema određenom korisničkom ID-u
    public static Specification<Order> withUserId(String userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }
    //withResultId vraća Specification<Order> koji filtrira narudžbe prema određenom result ID-u
    public static Specification<Order> withResultId(String resultId) {
        return (root, query, cb) -> cb.equal(root.get("result").get("id"), resultId);
    }

    // Add more methods for additional parameters
}
