CREATE TABLE User (
                      ID VARCHAR(255) NOT NULL,
                      Name VARCHAR(255),
                      Surname VARCHAR(255),
                      Username VARCHAR(255),
                      Password VARCHAR(255),
                      Email VARCHAR(255),
                      Contact VARCHAR(255),
                      Role VARCHAR(255),
                      Address VARCHAR(255),
                      Specialisation VARCHAR(255),
                      InsuranceNumber VARCHAR(255),
                      Bill VARCHAR(255),
                      PRIMARY KEY (ID)
);

CREATE TABLE Result (
                        ID VARCHAR(255) NOT NULL,
                        Diagnosis VARCHAR(255),
                        Advice VARCHAR(255),
                        PRIMARY KEY (ID)
);

CREATE TABLE Orders (
                       ID VARCHAR(255) NOT NULL,
                       Date VARCHAR(255),
                       ResultID VARCHAR(255),
                       DoctorOfGeneralMedicineID VARCHAR(255),
                       DoctorSpecialistID VARCHAR(255),
                       PatientID VARCHAR(255),
                       PRIMARY KEY (ID),
                       FOREIGN KEY (ResultID) REFERENCES Result(ID),
                       FOREIGN KEY (DoctorOfGeneralMedicineID) REFERENCES User(ID),
                       FOREIGN KEY (DoctorSpecialistID) REFERENCES User(ID),
                       FOREIGN KEY (PatientID) REFERENCES User(ID)
);
