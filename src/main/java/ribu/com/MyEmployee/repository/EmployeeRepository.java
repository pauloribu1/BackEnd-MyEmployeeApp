package radity.com.MyEmployee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import radity.com.MyEmployee.domain.employee.Employee;
import radity.com.MyEmployee.domain.user.User;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findById(Long id);

    Optional<Employee> findByEmail(String email);
}
