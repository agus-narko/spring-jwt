package com.narko.project.resources.core;

import com.narko.project.payload.request.AuditReqEntity;
import com.narko.project.payload.response.FinalResEntity;

public interface DataApproval<T> {

	public abstract FinalResEntity<?> processBo(T serviceInput,AuditReqEntity audit);

}
