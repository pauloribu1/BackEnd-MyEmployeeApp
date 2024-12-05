package radity.com.MyEmployee.domain.employee;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import radity.com.MyEmployee.domain.user.User;

import java.time.LocalDateTime;

@Table(name = "employee")
@Entity(name = "employee")
@Getter
@Setter
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String jobTitle;
    private String birthDate;
    private String startDate;
    private String photoPath;
    @OneToOne(mappedBy = "employee")
    private User user;


    public Employee(String firstName, String lastName, String jobTitle, String birthDate, String startDate,String photoPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.birthDate = birthDate;
        this.startDate = startDate;
        this.photoPath = photoPath;
    }

    public Employee(String firstName, String lastName, String jobTitle, String birthDate, String startDate,String email,String photoPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.birthDate = birthDate;
        this.startDate = startDate;
        this.photoPath = photoPath;
        this.email = email;
    }

    public Employee(){
    }

    public String getPhotoPath(){
        return photoPath;
    }

    public String getfirstName(){
        return firstName;
    }

    public String getlastName(){
        return lastName;
    }

    public String getEmail(){
        return email;
    }

    public String getJobTitle(){
        return jobTitle;
    }

    public String getBirthDate(){
        return birthDate;
    }

    public String getStartDate(){
        return startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }




    public String setEmail(String email){
        return this.email = email;
    }
}
