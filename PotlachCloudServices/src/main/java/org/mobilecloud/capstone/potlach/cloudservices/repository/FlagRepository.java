package org.mobilecloud.capstone.potlach.cloudservices.repository;

import java.util.Collection;

import org.mobilecloud.capstone.potlach.common.api.FlagSvcApi;
import org.mobilecloud.capstone.potlach.common.repository.Flag;
import org.mobilecloud.capstone.potlach.common.repository.Flag.FlagType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import retrofit.http.Query;

@RepositoryRestResource(path = FlagSvcApi.FLAG_SVC_PATH)
public interface FlagRepository extends MongoRepository<Flag, String>{
	
	public Collection<Flag> findFlagByGiftAndType(@Query(FlagSvcApi.GIFT_PARAMETER) String gift, 
												  @Query(FlagSvcApi.TYPE_PARAMETER) FlagType type);
	
	public Collection<Flag> findFlagByUserAndGiftAndType(@Query(FlagSvcApi.USER_PARAMETER) String user, 
														 @Query(FlagSvcApi.GIFT_PARAMETER) String gift, 
														 @Query(FlagSvcApi.TYPE_PARAMETER) FlagType type);
}
