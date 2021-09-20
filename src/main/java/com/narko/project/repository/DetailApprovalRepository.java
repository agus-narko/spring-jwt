package com.narko.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.narko.project.model.DetailApprovalEntity;

@Repository
public interface DetailApprovalRepository extends JpaRepository<DetailApprovalEntity, Long>, PagingAndSortingRepository<DetailApprovalEntity, Long>,
		JpaSpecificationExecutor<DetailApprovalEntity> {
	
}
