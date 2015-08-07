package org.mobilecloud.capstone.potlach.common.api;

import java.util.Collection;

import org.mobilecloud.capstone.potlach.common.repository.Gift;
import org.mobilecloud.capstone.potlach.common.repository.GiftStatus;
import org.mobilecloud.capstone.potlach.common.repository.User;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;


public interface GiftSvcApi {

	public static final String TITLE_PARAMETER = "title";
	
	public static final String USER_PARAMETER = "user";

	public static final String DATA_PARAMETER = "data";

	public static final String ID_PARAMETER = "id";

	public static final String GIFT_SVC_PATH = "/gift";
	
	public static final String GIFT_DATA_PATH = GIFT_SVC_PATH + "/{"+ID_PARAMETER+"}/data";

	public static final String GIFT_TITLE_SEARCH_PATH = GIFT_SVC_PATH + "/search/findGiftByTitle";	
	
	public static final String GIFT_USER_SEARCH_PATH = GIFT_SVC_PATH + "/search/findGiftByUser";

	@GET(GIFT_SVC_PATH)
	public Collection<Gift> getGiftList();
	
	@GET(GIFT_SVC_PATH + "/{"+ID_PARAMETER+"}")
	public Gift findGiftById(@Path("id") String id);
	
	@POST(GIFT_SVC_PATH)
	public Gift addGift(@Body Gift g);
	
	@POST(GIFT_SVC_PATH + "/{"+ID_PARAMETER+"}/like")
	public Void likeGift(@Path("id") String id);
	
	@POST(GIFT_SVC_PATH + "/{"+ID_PARAMETER+"}/unlike")
	public Void unlikeGift(@Path("id") String id);
	
	@POST(GIFT_SVC_PATH + "/{"+ID_PARAMETER+"}/innapropiated")
	public Void innapropiatedGift(@Path("id") String id);
	
	@POST(GIFT_SVC_PATH + "/{"+ID_PARAMETER+"}/apropiated")
	public Void apropiatedGift(@Path("id") String id);
	
	@GET(GIFT_TITLE_SEARCH_PATH)
	public Collection<Gift> findGiftByTitle(@Query(TITLE_PARAMETER) String title);	
	
	@GET(GIFT_USER_SEARCH_PATH)
	public Collection<Gift> findGiftByUser(@Query(USER_PARAMETER) String user);	
	
	@GET(GIFT_SVC_PATH + "/{"+ID_PARAMETER+"}/likedby")
	public Collection<User> whoLikedGift(@Path("id") String id);
	
	@DELETE(GIFT_SVC_PATH + "/{"+ID_PARAMETER+"}")
	public Void deleteGift(@Path(ID_PARAMETER) String id);
	
	@Multipart
	@POST(GIFT_DATA_PATH)
	public GiftStatus setGiftData(@Path(ID_PARAMETER) String id, @Part(DATA_PARAMETER) TypedFile giftData);
	
	@Streaming
    @GET(GIFT_DATA_PATH)
    Response getData(@Path(ID_PARAMETER) String id);
}
