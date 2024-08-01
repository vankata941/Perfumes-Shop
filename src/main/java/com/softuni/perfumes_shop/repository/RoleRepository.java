package com.softuni.perfumes_shop.repository;

import com.softuni.perfumes_shop.model.entity.Role;
import com.softuni.perfumes_shop.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByUserRole(UserRole userRole);

}
