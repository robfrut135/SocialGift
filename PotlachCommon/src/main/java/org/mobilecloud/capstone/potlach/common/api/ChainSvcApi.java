package org.mobilecloud.capstone.potlach.common.api;

import org.mobilecloud.capstone.potlach.common.repository.Chain;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface ChainSvcApi {
	
	public static final String ID_PARAMETER = "id";

	public static final String CHAIN_SVC_PATH = "/chain";	

	@POST(CHAIN_SVC_PATH)
	public Chain addChain(@Body Chain c);
	
	@GET(CHAIN_SVC_PATH + "/{"+ID_PARAMETER+"}")
	public Chain getChainById(@Path("id") String id);
}
