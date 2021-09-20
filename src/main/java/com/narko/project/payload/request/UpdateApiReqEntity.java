package com.narko.project.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.narko.project.model.ApiEntity;

@JsonIgnoreProperties(value = { "createdBy", "createdAt", "updatedBy", "updatedAt", "status" }, allowGetters = true)
public class UpdateApiReqEntity extends ApiEntity {

}
