package com.suplog.admin.domain;

import java.util.Optional;

public interface AdminRepository {

    Optional<Admin> findByUsername(String username);
}
