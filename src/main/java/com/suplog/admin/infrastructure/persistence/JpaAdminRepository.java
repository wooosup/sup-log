package com.suplog.admin.infrastructure.persistence;

import com.suplog.admin.domain.Admin;
import com.suplog.admin.domain.AdminRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaAdminRepository extends JpaRepository<Admin, Long>, AdminRepository {

    @Override
    Optional<Admin> findByUsername(String username);
}
