package com.example.hospital.models;

import lombok.*;

//import javax.persistence.*;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor

@AllArgsConstructor
@Entity
@Table(name = "Result")
public class Result {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    //@Type(type = "uuid-char")
    @Column(name = "ID")
    private String id;

    @Column(name = "Diagnosis")
    private String diagnosis;

    @Column(name = "Advice")
    private String advice;
}
