package test.grocerybooking.grocerybooking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import test.grocerybooking.grocerybooking.entities.CheckoutItem;
import test.grocerybooking.grocerybooking.entities.Users;

@Repository
public interface CheckoutItemRepository extends JpaRepository<CheckoutItem, Long>{
	
	List<CheckoutItem> findAll();
	
	Optional<CheckoutItem> getByName(String name);
	
}
