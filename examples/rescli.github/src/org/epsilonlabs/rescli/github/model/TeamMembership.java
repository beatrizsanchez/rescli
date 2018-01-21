package org.epsilonlabs.rescli.github.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamMembership {

	@JsonProperty("state") 
	private String state;
	
	@JsonProperty("url") 
	private String url;
	
	public String getState() {
		return this.state;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	@Override
	public String toString() {
		return "TeamMembership [ "
			+ "state = " + this.state + ", "
			+ "url = " + this.url + ", "
			+ "]"; 
	}	
}	
