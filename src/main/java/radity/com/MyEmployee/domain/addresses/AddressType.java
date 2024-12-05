package radity.com.MyEmployee.domain.addresses;

import jakarta.persistence.*;

@Table(name ="address_types")
@Entity(name ="address_types")
public class AddressType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return this.type;
    }
}
