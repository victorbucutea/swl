#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${basePackage}.base.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

public class UriSearchInfo extends SearchInfo {

	public UriSearchInfo(String searcherId2, UriInfo uriInfo) {
		super(searcherId2);
		initSearchParams(uriInfo);
	}

	private void initSearchParams(UriInfo uriInfo) {
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		Map<String, Object> searchParams = new HashMap<String, Object>();
		for (String key : queryParams.keySet()) {
			searchParams.put(key, queryParams.getFirst(key));
		}
		setParamMap(searchParams);
	}
}