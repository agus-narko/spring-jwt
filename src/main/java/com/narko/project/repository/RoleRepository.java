package com.narko.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.narko.project.model.RoleEntity;

/*
 * contoh repo dengan default jpa
 */

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>, PagingAndSortingRepository<RoleEntity, Long>,
		JpaSpecificationExecutor<RoleEntity> {
	Optional<RoleEntity> findByName(String name);

	Boolean existsByNameAndStatus(String name, String status);

	Boolean existsByIdAndStatus(Long id, String status);

	Boolean existsByApprovalAndStatus(RoleEntity role, String status);

	Boolean existsByName(String name);

	@Query(value = "SELECT count(1) FROM m_role a join user_role b on a.id = b.m_role_id where a.id = :roleId", nativeQuery = true)
	Long cekUserRole(@Param("roleId") Long roleId);


}
