package com.narko.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.narko.project.model.ApiEntity;

@Repository
public interface ApiRepository extends JpaRepository<ApiEntity, Long>, PagingAndSortingRepository<ApiEntity, Long>,
		JpaSpecificationExecutor<ApiEntity> {
	Optional<ApiEntity> findByApiName(String ApiName);

	Boolean existsByApiName(String ApiName);

}
