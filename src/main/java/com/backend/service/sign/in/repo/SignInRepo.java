package com.backend.service.sign.in.repo;

import com.backend.service.sign.in.model.SignIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository of {@link SignIn} entities
 */
public interface SignInRepo extends JpaRepository<SignIn, UUID> {

}