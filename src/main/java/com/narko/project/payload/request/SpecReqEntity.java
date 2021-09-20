package com.narko.project.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class SpecReqEntity {

	@Schema(required = false, example = "ECHANNEL", description = "key spesifikasi")
	private String configMenu;

	@Schema(required = true, example = "ATM", description = "value spesifikasi")
	private String subConfig;

	public String getConfigMenu() {
		return configMenu;
	}

	public void setConfigMenu(String configMenu) {
		this.configMenu = configMenu;
	}

	public String getSubConfig() {
		return subConfig;
	}

	public void setSubConfig(String subConfig) {
		this.subConfig = subConfig;
	}


	
	

}
