package radity.com.MyEmployee.domain.user;


import radity.com.MyEmployee.domain.user.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
