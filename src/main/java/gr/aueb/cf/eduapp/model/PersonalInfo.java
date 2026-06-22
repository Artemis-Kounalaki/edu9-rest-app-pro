package gr.aueb.cf.eduapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "personal_information")
public class PersonalInfo extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
   private String amka;

    @Column(name = "identity_number",unique = true)
   private String identityNumber;

    @Column(name = "date_of_birth")
   private String placeOfBirth;

    @Column(name = "municipality_of_registration")
   private String municipalityOfRegistration;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "amka_file_id", unique = true)
    private Attachment amkafile;

    public void addAmkaFile(Attachment attachment){
        this.amkafile = attachment;
    }
    public void removeAmkaFile(){
        this.amkafile = null;
    }



}
