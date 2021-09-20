package com.narko.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.narko.project.model.RoleTypeEntity;

/*
 * contoh repo dengan default jpa
 */


@Repository
public interface RoleTypeRepository extends JpaRepository<RoleTypeEntity, Long>, PagingAndSortingRepository<RoleTypeEntity, Long>,
JpaSpecificationExecutor<RoleTypeEntity>{
	Optional<RoleTypeEntity> findByName(String name);
	Boolean existsByIdAndStatus(Long id,String status);

}
