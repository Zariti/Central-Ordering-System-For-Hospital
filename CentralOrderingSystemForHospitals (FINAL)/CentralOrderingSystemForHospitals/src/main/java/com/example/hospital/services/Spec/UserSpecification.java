package com.example.hospital.services.Spec;


import com.example.hospital.models.User;
import com.example.hospital.models.UserRole;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class UserSpecification {

    public static Specification<User> buildSpecifications(Map<String, String> params) {
        Specification<User> spec = Specification.where(null);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            switch (key) {
                case "name":
                    spec = spec.and(withName(value));
                    break;
                case "surname":
                    spec = spec.and(withSurname(value));
                    break;
                case "username":
                    spec = spec.and(withUsername(value));
                    break;
                case "email":
                    spec = spec.and(withEmail(value));
                    break;
                case "contact":
                    spec = spec.and(withContact(value));
                    break;
                case "role":
                    spec = spec.and(withRole(UserRole.valueOf(value)));
                    break;
                case "address":
                    spec = spec.and(withAddress(value));
                    break;
                case "specialisation":
                    spec = spec.and(withSpecialisation(value));
                    break;
                case "insuranceNumber":
                    spec = spec.and(withInsuranceNumber(value));
                    break;
                case "bill":
                    spec = spec.and(withBill(value));
                    break;
                // If we need more params we only add them here
            }
        }

        return spec;
    }

    public static Specification<User> withName(String name) {  // pronalazi usera sa odredenim imenom
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<User> withSurname(String surname) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("surname")), "%" + surname.toLowerCase() + "%");
    }

    public static Specification<User> withUsername(String username) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<User> withEmail(String email) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> withContact(String contact) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("contact")), "%" + contact.toLowerCase() + "%");
    }

    public static Specification<User> withRole(UserRole role) {
        return (root, query, cb) -> cb.equal(root.get("role"), role);
    }

    public static Specification<User> withAddress(String address) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
    }

    public static Specification<User> withSpecialisation(String specialisation) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("specialisation")), "%" + specialisation.toLowerCase() + "%");
    }

    public static Specification<User> withInsuranceNumber(String insuranceNumber) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("insuranceNumber")), "%" + insuranceNumber.toLowerCase() + "%");
    }

    public static Specification<User> withBill(String bill) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("bill")), "%" + bill.toLowerCase() + "%");
    }

    // If we need more params we only add them here
}
