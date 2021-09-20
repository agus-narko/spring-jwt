package com.narko.project.payload.response;

import java.util.ArrayList;
import java.util.List;

public class PageableRespEntity {

	private List<?> content = new ArrayList<>();
	private Long totalElements = 0L;
	private int totalPages = 0;
	private int size = 0;
	private int number = 0;
	
	
	public Boolean cekPage() {
		if(this.totalElements > 0 || this.totalPages > 0 || this.size > 0 || this.number > 0 ) {
			return true;
		}
		return false;
			
	}
	

	public List<?> getContent() {
		return content;
	}


	public void setContent(List<?> content) {
		this.content = content;
	}


	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	
	

}
