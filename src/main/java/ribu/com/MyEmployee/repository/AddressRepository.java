package radity.com.MyEmployee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import radity.com.MyEmployee.domain.addresses.Address;
import radity.com.MyEmployee.domain.employee.Employee;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByEmployee_Id( Long employeeId);

    List<Address> findByEmployee( Employee employee);
}
