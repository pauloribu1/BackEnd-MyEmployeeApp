package radity.com.MyEmployee.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import radity.com.MyEmployee.domain.addresses.Address;
import radity.com.MyEmployee.domain.addresses.AddressRequestDTO;
import radity.com.MyEmployee.domain.addresses.AddressType;
import radity.com.MyEmployee.domain.employee.Employee;
import radity.com.MyEmployee.domain.user.EmployeeRequestDTO;
import radity.com.MyEmployee.domain.user.EmployeeUpdateDTO;
import radity.com.MyEmployee.repository.AddressRepository;
import radity.com.MyEmployee.repository.AddressTypeRepository;
import radity.com.MyEmployee.repository.EmployeeRepository;
import radity.com.MyEmployee.service.EmployeeService;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressTypeRepository addressTypeRepository;

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<?> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "asc") String order)
            {

        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        // Building the response
        return ResponseEntity.ok().body(Map.of(
                "content", employeePage.getContent(),
                "currentPage", employeePage.getNumber(),
                "totalPages", employeePage.getTotalPages(),
                "totalElements", employeePage.getTotalElements()
        ));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeUpdateDTO employeeRequestDTO
    ) {
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
        if (existingEmployee == null) {
            return ResponseEntity.status(404).body("Employee not found with id: " + id);
        }

        // Update the employee fields
        existingEmployee.setFirstName(employeeRequestDTO.firstName());
        existingEmployee.setLastName(employeeRequestDTO.lastName());
        existingEmployee.setEmail(employeeRequestDTO.email());
        existingEmployee.setJobTitle(employeeRequestDTO.jobTitle());
        existingEmployee.setBirthDate(employeeRequestDTO.birthDate());
        existingEmployee.setStartDate(employeeRequestDTO.startDate());

        // Save updated employee
        employeeRepository.save(existingEmployee);

        return ResponseEntity.ok("Employee updated successfully");
    }



    @PostMapping("/add")
    public ResponseEntity<?> createEmployee(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("birthDate") String birthDate,
            @RequestParam("startDate") String startDate,
            @RequestParam(required = false) MultipartFile photo // MultipartFile for the photo
    ) {

        String filePath = null;
/*
        // Handle the file upload logic
        if (photo != null && !photo.isEmpty()) {
            String appDataPath = System.getenv("APPDATA");
            String uploadDir = Paths.get(appDataPath, "MyEmployee", "uploads", "employees").toString(); // Finish this if possible
            String fileName = photo.getOriginalFilename();

            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            if (photo != null && !photo.isEmpty()) {

                filePath = uploadDir + File.separator + fileName; // Ensure the path uses the correct separator for the OS

                try {
                    photo.transferTo(new File(filePath)); // Save the photo to the file system
                } catch (IOException e) {
                    return ResponseEntity.status(500).body("Error saving the photo: " + e.getMessage());
                }
            } else {
                filePath = uploadDir + File.separator + "default.png";
                return ResponseEntity.status(500).body("Error saving the photo: Default one selected");
            }
*/
            // Create and save the new employee
            Employee newEmployee = new Employee(firstName, lastName, jobTitle, birthDate, startDate, email, filePath);
            employeeRepository.save(newEmployee);
/*
            // Create address
            AddressType addressType = addressTypeRepository.findById(addressTypeId)
                    .orElseThrow(() -> new RuntimeException("Address Type not found"));

            Address address = new Address();
            address.setEmployee(newEmployee);
            address.setAddressType(addressType);
            addressRepository.save(address);
 */

            return ResponseEntity.status(201).body("Employee created successfully. "+ newEmployee.getId());
        }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        employeeRepository.deleteById(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    @GetMapping("/employee/photo/{id}")
    public ResponseEntity<Resource> getEmployeePhoto(@PathVariable Long id) throws MalformedURLException {
        Optional<Employee> employee = employeeRepository.findById(id);

        var photoPath = employee.get().getPhotoPath();

        Path path = Paths.get(photoPath);
        Resource resource = (Resource) new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getEmployeeByEmail(@PathVariable String email) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);

        if (employee.isEmpty()) {
            return ResponseEntity.status(404).body("Employee not found with email: " + email);
        }

        return ResponseEntity.ok(employee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);

        if (employee.isEmpty()) {
            return ResponseEntity.status(404).body("Employee not found with id: " + id);
        }

        return ResponseEntity.ok(employee);
    }

    @GetMapping("/{employeeId}/addresses")
    public ResponseEntity<?> getEmployeeAddresses(@PathVariable Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isEmpty()) {
            return ResponseEntity.status(404).body("Employee not found with id: " + employeeId);
        }

        Employee employee = optionalEmployee.get();
        // Fetch all addresses for the employee
        List<Address> addresses = addressRepository.findByEmployee_Id(employee.getId());
        return ResponseEntity.ok(addresses);
    }
    @PostMapping("/{employeeId}/addresses")
    public ResponseEntity<?> addAddressToEmployee(
            @PathVariable Long employeeId,
            @RequestBody AddressRequestDTO addressRequest) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        AddressType addressType = addressTypeRepository.findById(addressRequest.addressTypeId())
                .orElseThrow(() -> new RuntimeException("Address Type not found"));

        Address address = new Address();
        address.setAddress(addressRequest.address());
        address.setAddressType(addressType);
        address.setEmployee(employee);

        addressRepository.save(address);

        return ResponseEntity.status(201).body("Address added to employee successfully");
    }
}
