package com.narko.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.narko.project.model.RoleEntity;
import com.narko.project.model.UserEntity;

/*
 * contoh repo tanpa pagination
 * fungsi nya bisa get update create delete
 */

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long>,
		JpaSpecificationExecutor<UserEntity> {
	Optional<UserEntity> findByUsernameAndStatus(String username, String status);

	Optional<UserEntity> findByIdAndLogin(Long id, String login);

	Optional<UserEntity> findByLogin(String login);

	Boolean existsByIdAndLogin(Long id, String login);

	Boolean existsByUsernameAndStatus(String username, String status);
	Boolean existsByUsernameAndStatusAndLogin(String username, String status, String login);

	Boolean existsByRoles(RoleEntity roles);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE UserEntity c SET c.login = :login")
	void autoLogout(@Param("login") String login);

}