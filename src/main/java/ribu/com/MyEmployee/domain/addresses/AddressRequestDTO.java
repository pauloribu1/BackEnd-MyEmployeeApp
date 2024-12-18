package radity.com.MyEmployee.domain.addresses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public record AddressRequestDTO(Long addressTypeId, String address ) {



}
