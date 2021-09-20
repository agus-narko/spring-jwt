package com.narko.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.narko.project.model.ApprovalEntity;

@Repository
public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Long>,
		PagingAndSortingRepository<ApprovalEntity, Long>, JpaSpecificationExecutor<ApprovalEntity> {

	Boolean existsByDataIdAndStatus(Long dataId, String status);

}
