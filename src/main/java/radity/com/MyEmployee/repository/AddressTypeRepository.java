package radity.com.MyEmployee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import radity.com.MyEmployee.domain.addresses.AddressType;

@Repository
public interface AddressTypeRepository extends JpaRepository<AddressType, Long> {
}

