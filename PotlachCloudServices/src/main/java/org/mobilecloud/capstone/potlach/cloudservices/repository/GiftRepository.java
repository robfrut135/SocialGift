package org.mobilecloud.capstone.potlach.cloudservices.repository;

import java.util.Collection;

import org.mobilecloud.capstone.potlach.common.api.GiftSvcApi;
import org.mobilecloud.capstone.potlach.common.repository.Gift;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = GiftSvcApi.GIFT_SVC_PATH)
public interface GiftRepository extends MongoRepository<Gift, String>{
			
	public Collection<Gift> findGiftByTitleRegex(@Param(GiftSvcApi.TITLE_PARAMETER) String title, Sort s);
	
	public Collection<Gift> findGiftByUser(@Param(GiftSvcApi.USER_PARAMETER) String user, Sort s);
}
