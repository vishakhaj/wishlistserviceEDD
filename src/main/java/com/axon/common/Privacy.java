package com.axon.common;

public enum Privacy {

	PUBLIC("public"), PRIVATE("private");

	private String privacy;

	private Privacy(String privacy) {
		this.privacy = privacy;
	}

	public String getPrivacy() {
		return privacy;
	}

}
