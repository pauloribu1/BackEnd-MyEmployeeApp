package radity.com.MyEmployee.domain.user;

import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public record EmployeeRequestDTO(
        @NotBlank String firstName, @NotBlank String lastName,
        @NotBlank String jobTitle, @NotBlank String birthDate,
        @NotBlank  String startDate) {
}
