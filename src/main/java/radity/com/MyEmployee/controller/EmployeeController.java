package radity.com.MyEmployee.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import radity.com.MyEmployee.domain.addresses.Address;
import radity.com.MyEmployee.domain.employee.Employee;
import radity.com.MyEmployee.domain.user.EmployeeRequestDTO;
import radity.com.MyEmployee.repository.AddressRepository;
import radity.com.MyEmployee.repository.EmployeeRepository;
import radity.com.MyEmployee.service.EmployeeService;
import org.springframework.data.domain.Pageable;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressRepository addressRepository;

    private EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<?> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sortBy) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        // Building the response
        return ResponseEntity.ok().body(Map.of(
                "content", employeePage.getContent(),
                "currentPage", employeePage.getNumber(),
                "totalPages", employeePage.getTotalPages(),
                "totalElements", employeePage.getTotalElements()
        ));
    }


    @PostMapping("/add")
    public ResponseEntity<?> createEmployee(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("jobTitle") String jobTitle,
            @RequestParam("birthDate") String birthDate,
            @RequestParam("startDate") String startDate,
            @RequestParam(required = false) MultipartFile photo, // MultipartFile for the photo
            @RequestParam("addressTypeId") String addressTypeId) {

        String filePath = null;

        // Handle the file upload logic
        if (photo != null && !photo.isEmpty()) {
            String appDataPath = System.getenv("APPDATA");
            String uploadDir = Paths.get(appDataPath, "MyEmployee", "uploads", "employees").toString(); // MyEmployee > uploads > employees
            String fileName = photo.getOriginalFilename();
            filePath = uploadDir + fileName;

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

            // Create and save the new employee
            Employee newEmployee = new Employee(firstName, lastName, email, jobTitle, birthDate, startDate, filePath);
            employeeRepository.save(newEmployee);

        }
        return ResponseEntity.status(201).body("Employee created successfully");
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
    public ResponseEntity<Resource> getEmployeePhoto(@PathVariable String photoPath) throws MalformedURLException {
        Path path = Paths.get(photoPath);
        Resource resource = (Resource) new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Adjust MIME type as needed
                .body(resource);
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getEmployeeByEmail(@PathVariable String email) {
        Employee employee = employeeRepository.findByEmail(email);

        if (employee == null) {
            return ResponseEntity.status(404).body("Employee not found with email: " + email);
        }

        return ResponseEntity.ok(employee);
    }

    @GetMapping("/{email}/addresses")
    public ResponseEntity<?> getEmployeeAddresses(@PathVariable String email) {
        Employee employee = employeeRepository.findByEmail(email);
        if (employee == null) {
            return ResponseEntity.status(404).body("Employee not found with email: " + email);
        }

        // Fetch all addresses for the employee
        List<Address> addresses = addressRepository.findByEmployee(employee);
        return ResponseEntity.ok(addresses);
    }


}
