package com.blog.publish.publisher.utils.data;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GistCodeData {
	
	private String description;
	private boolean isPublic;
	private Map<String, Map<String, String>> files;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonProperty("public")
	public boolean isPublic() {
		return isPublic;
	}
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public Map<String, Map<String, String>> getFiles() {
		return files;
	}
	public void setFiles(Map<String, Map<String, String>> files) {
		this.files = files;
	}
}
