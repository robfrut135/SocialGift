/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.mobilecloud.capstone.potlach.client;

import org.mobilecloud.capstone.potlach.client.LoginScreenActivity;
import org.mobilecloud.capstone.potlach.common.api.ChainSvcApi;
import org.mobilecloud.capstone.potlach.common.api.FlagSvcApi;
import org.mobilecloud.capstone.potlach.common.api.GiftSvcApi;
import org.mobilecloud.capstone.potlach.common.api.OauthSvcApi;
import org.mobilecloud.capstone.potlach.common.api.UserSvcApi;
import org.mobilecloud.capstone.potlach.common.client.SecuredRestBuilder;
import org.mobilecloud.capstone.potlach.common.client.UnsafeHttpsClient;
import org.mobilecloud.capstone.potlach.common.repository.User;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;
import android.content.Intent;

public class PotlachSvc {

	private static final String SERVER 			= "https://10.0.2.2:8443/";	
	private static final String CLIENT_ID 		= "mobile";
	
	/*
	 * OAUTH CLIENT - User
	 */
	public static synchronized UserSvcApi getUserSvcApi(User user){
		UserSvcApi readWriteUserSvcApi = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(SERVER)
			.setLoginEndpoint(SERVER + OauthSvcApi.TOKEN_PATH)
			.setLogLevel(LogLevel.FULL)
			.setUsername(user.getUsername()).setPassword(user.getPassword()).setClientId(CLIENT_ID)
			.build().create(UserSvcApi.class);
		return readWriteUserSvcApi;
	}
	
	/*
	 * OAUTH CLIENT - Gift
	 */
	public static synchronized GiftSvcApi getGiftSvcApi(User user){
		GiftSvcApi readWriteGiftSvcApi = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(SERVER)
			.setLoginEndpoint(SERVER + OauthSvcApi.TOKEN_PATH)
			.setLogLevel(LogLevel.FULL)
			.setUsername(user.getUsername()).setPassword(user.getPassword()).setClientId(CLIENT_ID)
			.build().create(GiftSvcApi.class);
		return readWriteGiftSvcApi;
	}
	
	/*
	 * OAUTH CLIENT - Flag
	 */
	public static synchronized FlagSvcApi getFlagSvcApi(User user){
		FlagSvcApi readWriteFlagSvcApi = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(SERVER)
			.setLoginEndpoint(SERVER + OauthSvcApi.TOKEN_PATH)
			.setLogLevel(LogLevel.FULL)
			.setUsername(user.getUsername()).setPassword(user.getPassword()).setClientId(CLIENT_ID)
			.build().create(FlagSvcApi.class);
		return readWriteFlagSvcApi;
	}
	
	/*
	 * OAUTH CLIENT - Chain
	 */
	public static synchronized ChainSvcApi getChainSvcApi(User user){
		ChainSvcApi readWriteChainSvcApi = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(SERVER)
			.setLoginEndpoint(SERVER + OauthSvcApi.TOKEN_PATH)
			.setLogLevel(LogLevel.FULL)
			.setUsername(user.getUsername()).setPassword(user.getPassword()).setClientId(CLIENT_ID)
			.build().create(ChainSvcApi.class);
		return readWriteChainSvcApi;
	}
	
	/* 
	 * HTTPS CLIENT - User public
	 */	
	public static synchronized UserSvcApi getUserSvcApi(){
		UserSvcApi userSvcPublic = new RestAdapter.Builder()
				.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
				.setEndpoint(SERVER)
				.setLogLevel(LogLevel.FULL)	
				.build().create(UserSvcApi.class);
		return userSvcPublic;
	}
	
	public static synchronized User getOrShowLogin(Context ctx, String u, String p) {
		try{
			User user = getUserSvcApi().login(u, p);
			
			return user;
		} catch (RetrofitError e) {			
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}			
	}	
}
