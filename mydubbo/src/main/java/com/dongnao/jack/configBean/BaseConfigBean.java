package com.dongnao.jack.configBean;

import java.io.Serializable;

public abstract class BaseConfigBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
