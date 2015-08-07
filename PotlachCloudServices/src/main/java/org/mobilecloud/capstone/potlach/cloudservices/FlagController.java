package org.mobilecloud.capstone.potlach.cloudservices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.mobilecloud.capstone.potlach.cloudservices.repository.FlagRepository;
import org.mobilecloud.capstone.potlach.cloudservices.repository.GiftRepository;
import org.mobilecloud.capstone.potlach.cloudservices.repository.UserRepository;
import org.mobilecloud.capstone.potlach.common.api.FlagSvcApi;
import org.mobilecloud.capstone.potlach.common.repository.Flag;
import org.mobilecloud.capstone.potlach.common.repository.Flag.FlagType;
import org.mobilecloud.capstone.potlach.common.repository.Gift;
import org.mobilecloud.capstone.potlach.common.repository.GiversInfo;
import org.mobilecloud.capstone.potlach.common.repository.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import retrofit.http.Query;

@Controller
public class FlagController {
	
	public static final String FLAG_FORMAT_RESPONSE = "application/json";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GiftRepository giftRepository;
	
	@Autowired
	private FlagRepository flagRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
 
	@RequestMapping(value = FlagSvcApi.FLAG_SVC_PATH, method = RequestMethod.POST, produces = FLAG_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Flag> addFlag(@RequestBody Flag f){
				
		Flag flag = flagRepository.save(f);
		return new ResponseEntity<Flag>(flag, HttpStatus.ACCEPTED);
	}	
	
	@RequestMapping(value = FlagSvcApi.FLAG_USER_GIVERS_INFO, method = RequestMethod.GET, produces = FLAG_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Collection<GiversInfo>> userGiversInfo(){
		
		HashMap<String, GiversInfo> partResult = new HashMap<String, GiversInfo>();				
		
		TypedAggregation<Flag> aggregationTouch = newAggregation(Flag.class,
				match(Criteria.where("type").is(FlagType.TOUCH.toString())),		
				group("user")
					.count().as("total")							        
		);
		AggregationResults<GiversInfoType> resultTouch = mongoTemplate.aggregate(aggregationTouch, GiversInfoType.class);
		List<GiversInfoType> resultListTouch = resultTouch.getMappedResults();
		
		TypedAggregation<Flag> aggregationInnapropiated = newAggregation(Flag.class,
				match(Criteria.where("type").is(FlagType.INNAPROPIATED.toString())),		
				group("user")
					.count().as("total")							        
		);
		AggregationResults<GiversInfoType> resultInnapropiated = mongoTemplate.aggregate(aggregationInnapropiated, GiversInfoType.class);
		List<GiversInfoType> resultListInnapropiated = resultInnapropiated.getMappedResults();
					
		for (GiversInfoType infoT: resultListTouch){				
			User u = userRepository.findOne(infoT.id);
			partResult.put(u.getId(), new GiversInfo(u.getName(), infoT.total, 0));																	
		}	
		
		for (GiversInfoType infoI: resultListInnapropiated){
			User u = userRepository.findOne(infoI.id);			
			if (partResult.get(u.getId())!=null){				
				GiversInfo infoSaved = partResult.get(u.getId());
				infoSaved.numInnapropiated = infoI.total;
				partResult.put(infoI.id, infoSaved);					
			}else{
				partResult.put(infoI.id, new GiversInfo(u.getName(), 0, infoI.total));
			}
		}
		
		List<GiversInfo> result = new ArrayList<GiversInfo>(partResult.values());
		Collections.sort(result, new GiversInfoComparator());
		
		return new ResponseEntity<Collection<GiversInfo>>(result, HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value = FlagSvcApi.FLAG_GIFT_GIVERS_INFO, method = RequestMethod.GET, produces = FLAG_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Collection<GiversInfo>> giftGiversInfo(){
		
		HashMap<String, GiversInfo> partResult = new HashMap<String, GiversInfo>();
		
		TypedAggregation<Flag> aggregationTouch = newAggregation(Flag.class,
				match(Criteria.where("type").is(FlagType.TOUCH.toString())),		
				group("gift")
					.count().as("total")							        
		);
		AggregationResults<GiversInfoType> resultTouch = mongoTemplate.aggregate(aggregationTouch, GiversInfoType.class);
		List<GiversInfoType> resultListTouch = resultTouch.getMappedResults();
		
		TypedAggregation<Flag> aggregationInnapropiated = newAggregation(Flag.class,
				match(Criteria.where("type").is(FlagType.INNAPROPIATED.toString())),		
				group("gift")
					.count().as("total")							        
		);
		AggregationResults<GiversInfoType> resultInnapropiated = mongoTemplate.aggregate(aggregationInnapropiated, GiversInfoType.class);
		List<GiversInfoType> resultListInnapropiated = resultInnapropiated.getMappedResults();
					
		for (GiversInfoType infoT: resultListTouch){				
			Gift g = giftRepository.findOne(infoT.id);
			partResult.put(g.getId(), new GiversInfo(g.getTitle(), infoT.total, 0));																	
		}	
		
		for (GiversInfoType infoI: resultListInnapropiated){
			Gift g = giftRepository.findOne(infoI.id);
			if (partResult.get(g.getId())!=null){				
				GiversInfo infoSaved = partResult.get(g.getId());
				infoSaved.numInnapropiated = infoI.total;
				partResult.put(infoI.id, infoSaved);					
			}else{
				partResult.put(infoI.id, new GiversInfo(g.getTitle(), 0, infoI.total));	
			}
		}
		
		List<GiversInfo> result = new ArrayList<GiversInfo>(partResult.values());
		Collections.sort(result, new GiversInfoComparator());
		
		return new ResponseEntity<Collection<GiversInfo>>(result, HttpStatus.ACCEPTED);
	}	
	
	@RequestMapping(value = FlagSvcApi.FLAG_SVC_PATH + "/{"+ FlagSvcApi.ID_PARAMETER + "}", method = RequestMethod.DELETE, produces = FLAG_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Void> deleteFlag(@PathVariable(FlagSvcApi.ID_PARAMETER) String id) throws IOException{
		
		Flag existFlag = flagRepository.findOne(id);
		if (existFlag!=null) {			
			flagRepository.delete(existFlag);	
			return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
		}else{			
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}				
	}
	
	@RequestMapping(value = FlagSvcApi.FLAG_BY_GIFT_AND_TYPE_SVC_PATH , method = RequestMethod.GET, produces = FLAG_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Collection<Flag>> findFlagByGiftAndType(@Query(FlagSvcApi.GIFT_PARAMETER) String gift, @Query(FlagSvcApi.TYPE_PARAMETER) FlagType type){
		
		Collection<Flag> flags = flagRepository.findFlagByGiftAndType(gift, type);
		return new ResponseEntity<Collection<Flag>>(flags, HttpStatus.ACCEPTED);
		
	}
	
	@RequestMapping(value = FlagSvcApi.FLAG_BY_USER_AND_GIFT_AND_TYPE_SVC_PATH , method = RequestMethod.GET, produces = FLAG_FORMAT_RESPONSE)
	public  @ResponseBody ResponseEntity<Collection<Flag>> findFlagByUserAndGiftAndType(@Query(FlagSvcApi.USER_PARAMETER) String user, @Query(FlagSvcApi.GIFT_PARAMETER) String gift, @Query(FlagSvcApi.TYPE_PARAMETER) FlagType type){
		
		Collection<Flag> flags = flagRepository.findFlagByUserAndGiftAndType(user, gift, type);
		return new ResponseEntity<Collection<Flag>>(flags, HttpStatus.ACCEPTED);
		
	}
	
	class GiversInfoComparator implements Comparator<GiversInfo> {
	    public int compare(GiversInfo infoA, GiversInfo infoB) {
	        return (infoB.numInnapropiated+infoB.numTouched) - (infoA.numInnapropiated+infoA.numTouched);
	    }
	}
}
