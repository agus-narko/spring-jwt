package com.narko.project.payload.request;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

import com.narko.project.searchSpec.GenericSpesification;

public class GetReqEntity {


	private GenericSpesification search;

	private Pageable page;
	
	private List<Order> orders;



	public GetReqEntity(GenericSpesification search, Pageable page,List<Order> orders) {
		super();
		
		this.search = search;
		this.page = page;
		this.orders = orders;
	}



	public GenericSpesification getSearch() {
		return search;
	}

	public void setSearch(GenericSpesification search) {
		this.search = search;
	}

	public Pageable getPage() {
		return page;
	}

	public void setPage(Pageable page) {
		this.page = page;
	}



	public List<Order> getOrders() {
		return orders;
	}



	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

}
