package com.softuni.perfumes_shop.repository;

import com.softuni.perfumes_shop.model.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    Optional<UserDetail> findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndPhoneNumberAndUserId(String firstName, String lastName, String phoneNumber, Long id);

}
