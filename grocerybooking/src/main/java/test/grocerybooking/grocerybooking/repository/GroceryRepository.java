package test.grocerybooking.grocerybooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import test.grocerybooking.grocerybooking.entities.GroceryItem;

@Repository
public interface GroceryRepository extends JpaRepository<GroceryItem, Long>{
	
	List<GroceryItem> findAll();
	
	void deleteByName(String name);

}
