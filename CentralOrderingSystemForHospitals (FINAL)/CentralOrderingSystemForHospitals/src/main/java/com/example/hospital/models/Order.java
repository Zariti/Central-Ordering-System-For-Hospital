package com.example.hospital.models;

import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
   //@Type(type = "uuid-char")
    @Column(name = "ID")
    private String id;



    @Column(name = "Date")
    private LocalDateTime dateTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resultId")
    private Result result;




    @ManyToOne
    @JoinColumn(name = "doctor_of_general_medicineid")
    private User doctorOfGeneralMedicine;

    @ManyToOne
    @JoinColumn(name = "doctor_specialistid")
    private User doctorSpecialist;

    @ManyToOne
    @JoinColumn(name = "PatientID")
    private User patient;


    @Column(name="reason_for_examination")
    private String reasonForExamination;

}
