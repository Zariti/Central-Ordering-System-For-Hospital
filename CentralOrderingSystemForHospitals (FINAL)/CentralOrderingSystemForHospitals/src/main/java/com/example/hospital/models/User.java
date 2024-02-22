package com.example.hospital.models;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

//import javax.persistence.*;
import jakarta.persistence.*;

@AllArgsConstructor
@NoArgsConstructor


@Data
@Entity
@Table(name = "user")
public class User {


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    //@Type(type = "uuid-char")
    @Column(name = "ID")
    private String id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Surname")
    private String surname;

    @Column(name = "Username")
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "Email")
    private String email;

    @Column(name = "Contact")
    private String contact;

    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "Address")
    private String address;    // mozda bude null ovisno o roli

    @Column(name = "Specialisation")
    private String specialisation;  // mozda bude null ovisno o roli

    @Column(name = "insurance_number")
    private String insuranceNumber;  // mozda bude null ovisno o roli

    @Column(name = "Bill")
    private String bill;   // mozda bude null ovisno o roli

    @Column(name = "DoctorOfGeneralMedicineID")  // Stupac za strani ključ
    private String doctorOfGeneralMedicineID;

}
