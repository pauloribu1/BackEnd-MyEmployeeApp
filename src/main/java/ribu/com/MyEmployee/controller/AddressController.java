package radity.com.MyEmployee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import radity.com.MyEmployee.domain.addresses.Address;
import radity.com.MyEmployee.domain.addresses.AddressRequestDTO;
import radity.com.MyEmployee.domain.addresses.AddressType;
import radity.com.MyEmployee.domain.employee.Employee;
import radity.com.MyEmployee.repository.AddressRepository;
import radity.com.MyEmployee.repository.AddressTypeRepository;
import radity.com.MyEmployee.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("address-types")
public class AddressController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressTypeRepository addressTypeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @PostMapping("/{employeeId}/addresses")
    public ResponseEntity<?> addAddressToEmployee(
            @PathVariable String employeeId,
            @RequestBody AddressRequestDTO addressRequest) {

        Optional<Employee> optionalEmployee = employeeRepository.findById(Long.valueOf(employeeId));

        if (optionalEmployee.isEmpty()) {
            return ResponseEntity.status(404).body("Funcionário não encontrado com o email: " + employeeId);
        }

        Employee employee = optionalEmployee.get(); // Agora pode obter o Employee
        AddressType addressType = addressTypeRepository.findById(addressRequest.addressTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de endereço inválido"));

        Address address = new Address();
        address.setAddress(addressRequest.address());
        address.setAddressType(addressType);
        address.setEmployee(employee);

        addressRepository.save(address);

        return ResponseEntity.status(201).body("Endereço vinculado com sucesso!");
    }

    @GetMapping("/{employeeId}/addresses")
    public ResponseEntity<?> getEmployeeAddresses(@PathVariable Long employeeId) {
        List<Address> addresses = addressRepository.findByEmployee_Id(employeeId);

        if (addresses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(addresses);
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping
    public ResponseEntity<List<AddressType>> getAddressTypes() {
        List<AddressType> addressTypes = addressTypeRepository.findAll();
        if (addressTypes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(addressTypes);
    }

    @PostMapping
    public ResponseEntity<?> createAddressType(@RequestBody AddressType addressType) {
        if (addressType.getType() == null || addressType.getType().isBlank()) {
            return ResponseEntity.badRequest().body("Address Type name is required.");
        }

        AddressType savedAddressType = addressTypeRepository.save(addressType);
        return ResponseEntity.status(201).body(savedAddressType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddressType(@PathVariable Long id) {
        if (!addressTypeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        addressTypeRepository.deleteById(id);
        return ResponseEntity.ok().body("Address Type deleted successfully.");
    }


}


