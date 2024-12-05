package radity.com.MyEmployee.domain.addresses;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import radity.com.MyEmployee.domain.employee.Employee;

@Entity(name = "addresses")
@Table(name = "addresses")
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "address_type_id",nullable = false)
    private AddressType addressType;

    private String address;


    public String setAddress(String address){
        return this.address = address;
    }

    public AddressType setAddressType(AddressType addressType){
        return this.addressType = addressType;
    }

    public Employee setEmployee(Employee employee){
        return this.employee = employee;
    }
}
