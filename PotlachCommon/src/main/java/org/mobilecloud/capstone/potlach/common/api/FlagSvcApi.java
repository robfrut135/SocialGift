package org.mobilecloud.capstone.potlach.common.api;

import java.util.Collection;

import org.mobilecloud.capstone.potlach.common.repository.Flag;
import org.mobilecloud.capstone.potlach.common.repository.Flag.FlagType;
import org.mobilecloud.capstone.potlach.common.repository.GiversInfo;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface FlagSvcApi {
	
	public static final String ID_PARAMETER 		= "id";
	
	public static final String USER_PARAMETER 		= "user";
	
	public static final String GIFT_PARAMETER 		= "gift";
	
	public static final String TYPE_PARAMETER 		= "type";

	public static final String FLAG_SVC_PATH 		= "/flag";
	
	public static final String FLAG_USER_GIVERS_INFO = FLAG_SVC_PATH + "/getUserGiversInfo";
	
	public static final String FLAG_GIFT_GIVERS_INFO = FLAG_SVC_PATH + "/getGiftGiversInfo";
	
	public static final String FLAG_BY_GIFT_AND_TYPE_SVC_PATH = FLAG_SVC_PATH + "/findFlagByGiftAndType";
	
	public static final String FLAG_BY_USER_AND_GIFT_AND_TYPE_SVC_PATH = FLAG_SVC_PATH + "/findFlagByUserAndGiftAndType";

	@POST(FLAG_SVC_PATH)
	public Flag addFlag(@Body Flag f);
		
	@DELETE(FlagSvcApi.FLAG_SVC_PATH + "/{"+ FlagSvcApi.ID_PARAMETER + "}")
	public Void deleteFlag(@Path(FlagSvcApi.ID_PARAMETER) String id);
	
	@GET(FLAG_BY_GIFT_AND_TYPE_SVC_PATH)
	public Collection<Flag> findFlagByGiftAndType(@Query(FlagSvcApi.GIFT_PARAMETER) String gift, @Query(FlagSvcApi.TYPE_PARAMETER) FlagType type);
	
	@GET(FLAG_BY_USER_AND_GIFT_AND_TYPE_SVC_PATH)
	public Collection<Flag> findFlagByUserAndGiftAndType(@Query(FlagSvcApi.USER_PARAMETER) String user, @Query(FlagSvcApi.GIFT_PARAMETER) String gift, @Query(FlagSvcApi.TYPE_PARAMETER) FlagType type);
	
	@GET(FLAG_USER_GIVERS_INFO)
	public Collection<GiversInfo> getUserGiversInfo();
	
	@GET(FLAG_GIFT_GIVERS_INFO)
	public Collection<GiversInfo> getGiftGiversInfo();
	
}
