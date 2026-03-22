package com.fitbridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fitbridge.model.Instrutor;
import java.util.Optional;


public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {
	Optional<Instrutor> findByEmail(String email);
}