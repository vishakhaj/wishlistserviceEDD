package com.axon.common;

public class Content {

	private String source;

	private String privacy;

	private String type;

	public Content(String source, String privacy, String type) {
		this.source = source;
		this.privacy = privacy;
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public String getPrivacy() {
		return privacy;
	}

	public String getType() {
		return type;
	}

}
