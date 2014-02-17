#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${rootArtifactId}.base.dao;

import java.util.Map;

public class SearchInfo {

	private String searcherId;

	private Map<String, Object> paramMap;

	public SearchInfo(String searcherId2) {
		this.searcherId = searcherId2;
	}

	public String getSearcherId() {
		return searcherId;
	}

	public void setSearcherId(String searcherId) {
		this.searcherId = searcherId;
	}

	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

}
