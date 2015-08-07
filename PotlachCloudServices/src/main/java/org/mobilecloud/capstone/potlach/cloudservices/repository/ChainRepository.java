package org.mobilecloud.capstone.potlach.cloudservices.repository;

import org.mobilecloud.capstone.potlach.common.api.ChainSvcApi;
import org.mobilecloud.capstone.potlach.common.repository.Chain;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = ChainSvcApi.CHAIN_SVC_PATH)
public interface ChainRepository extends MongoRepository<Chain, String>{

}
