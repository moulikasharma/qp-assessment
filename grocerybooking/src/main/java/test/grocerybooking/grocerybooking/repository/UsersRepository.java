package test.grocerybooking.grocerybooking.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import test.grocerybooking.grocerybooking.entities.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
	
	Optional<Users> getByUsername(String username);

}
