package radity.com.MyEmployee.service;

import org.springframework.stereotype.Service;
import radity.com.MyEmployee.domain.employee.Employee;
import radity.com.MyEmployee.domain.user.User;
import radity.com.MyEmployee.repository.EmployeeRepository;

import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Optional<Employee> findById(Long id){
        return employeeRepository.findById(id);
    }
}
