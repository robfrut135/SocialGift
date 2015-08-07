package org.mobilecloud.capstone.potlach.common.api;

import java.util.Collection;

import org.mobilecloud.capstone.potlach.common.repository.User;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface UserSvcApi {
	
	public static final String ID_PARAMETER = "id";

	public static final String USERNAME_PARAMETER = "username";
	
	public static final String PASSWORD_PARAMETER = "password";	

	public static final String USER_SVC_PATH = "/user";	

	public static final String USER_USERNAME_SEARCH_PATH = USER_SVC_PATH + "/search/findByUsername";
	
	public static final String USER_LOGIN_PATH = USER_SVC_PATH + "/login";	

	@GET(USER_SVC_PATH)
	public Collection<String> getUserList();
	
	@POST(USER_SVC_PATH)
	public User addUser(@Body User u);
	
	@PUT(USER_SVC_PATH)
	public User saveUser(@Body User u);
	
	@DELETE(USER_SVC_PATH + "/{"+ ID_PARAMETER + "}")
	public Void deleteUser(@Path("id") String id);
	
	@GET(USER_SVC_PATH + "/{"+ ID_PARAMETER + "}")
	public User findUserById(@Path("id") String id);
	
	@GET(USER_USERNAME_SEARCH_PATH)
	public Collection<User> findUserByUsername(@Query(USERNAME_PARAMETER) String username);	
				
	@POST(USER_LOGIN_PATH)
	public User login(@Query(USERNAME_PARAMETER) String username, @Query(PASSWORD_PARAMETER) String password);
}
