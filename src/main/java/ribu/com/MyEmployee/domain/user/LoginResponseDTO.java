package radity.com.MyEmployee.domain.user;

public record LoginResponseDTO(String token, UserRole role, Long employeeId) {
}
