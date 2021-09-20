package com.narko.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.narko.project.model.LogAccessEntity;

@Repository
public interface LogAccesRepository extends JpaRepository<LogAccessEntity, Long> {

}
