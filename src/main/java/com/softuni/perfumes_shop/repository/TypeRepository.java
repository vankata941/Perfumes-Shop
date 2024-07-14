package com.softuni.perfumes_shop.repository;

import com.softuni.perfumes_shop.model.entity.Type;
import com.softuni.perfumes_shop.model.enums.PerfumeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    Optional<Type> findByPerfumeType(PerfumeType perfumeType);

}
