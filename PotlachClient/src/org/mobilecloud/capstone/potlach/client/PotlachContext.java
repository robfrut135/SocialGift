package org.mobilecloud.capstone.potlach.client;

import java.io.Serializable;

import org.mobilecloud.capstone.potlach.common.api.ChainSvcApi;
import org.mobilecloud.capstone.potlach.common.api.FlagSvcApi;
import org.mobilecloud.capstone.potlach.common.api.GiftSvcApi;
import org.mobilecloud.capstone.potlach.common.api.UserSvcApi;
import org.mobilecloud.capstone.potlach.common.repository.User;

public class PotlachContext implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static PotlachContext context 	= null;	
	private UserSvcApi userApi 				= null;
	private GiftSvcApi giftApi 				= null;
	private ChainSvcApi chainApi 			= null;
	private FlagSvcApi flagApi 				= null;
	private User user 						= null;
	
	public static PotlachContext getInstance(){
		if (context!=null){
			return context;
		}else{
			context = new PotlachContext();
			return context;
		}
	}		
	
	private void setUserSvcApi(UserSvcApi uApi){
		userApi = uApi;				
	}
	
	private void setGiftSvcApi(GiftSvcApi gApi){
		giftApi = gApi;
	}
	
	private void setFlagSvcApi(FlagSvcApi fApi){
		flagApi = fApi;
	}
	
	private void setChainSvcApi(ChainSvcApi cApi){
		chainApi = cApi;
	}
	
	public UserSvcApi getUserSvcApi(){
		return userApi;
	}
	
	public void setUser(User u){
		user = u;
		
		setUserSvcApi( PotlachSvc.getUserSvcApi(user) );
		setGiftSvcApi( PotlachSvc.getGiftSvcApi(user) );
		setChainSvcApi( PotlachSvc.getChainSvcApi(user) );
		setFlagSvcApi( PotlachSvc.getFlagSvcApi(user) );
	}
	
	public User getUser(){
		return user;
	}
			
	public GiftSvcApi getGiftSvcApi(){
		return giftApi;
	}		
	
	public ChainSvcApi getChainSvcApi(){
		return chainApi;
	}
			
	public FlagSvcApi getFlagSvcApi(){
		return flagApi;
	}
}
