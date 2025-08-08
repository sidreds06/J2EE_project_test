package com.expensetracker.auth.repository;

import com.expensetracker.auth.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, String> {
    AuthUser findByEmail(String email);
    
    Page<AuthUser> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);

}
