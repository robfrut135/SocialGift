package org.mobilecloud.capstone.potlach.cloudservices;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mobilecloud.capstone.potlach.cloudservices.repository.ChainRepository;
import org.mobilecloud.capstone.potlach.cloudservices.repository.FlagRepository;
import org.mobilecloud.capstone.potlach.cloudservices.repository.GiftRepository;
import org.mobilecloud.capstone.potlach.cloudservices.repository.UserRepository;
import org.mobilecloud.capstone.potlach.common.api.GiftSvcApi;
import org.mobilecloud.capstone.potlach.common.repository.Chain;
import org.mobilecloud.capstone.potlach.common.repository.Flag;
import org.mobilecloud.capstone.potlach.common.repository.Gift;
import org.mobilecloud.capstone.potlach.common.repository.GiftStatus;
import org.mobilecloud.capstone.potlach.common.repository.User;
import org.mobilecloud.capstone.potlach.common.repository.Flag.FlagType;
import org.mobilecloud.capstone.potlach.common.repository.GiftStatus.GiftState;
import org.mobilecloud.capstone.potlach.common.repository.User.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import retrofit.http.Multipart;
import retrofit.http.Query;
import retrofit.http.Streaming;

@Controller
public class GiftController {
		
	public static final String GIFT_FORMAT_RESPONSE = "application/json";
	
	@Autowired
	private GiftRepository giftRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FlagRepository flagRepository;
	
	@Autowired
	private ChainRepository chainRepository;
	
	private Sort giftsOrderByDateRegister = new Sort(Sort.Direction.DESC, "dateRegister");	
	private Sort giftsOrderByFlags = new Sort(Sort.Direction.DESC, "flags");	
	
	private String getDataUrl(String giftId){
        String url = getUrlBaseForLocalServer() + "/gift/" + giftId + "/data";
        return url;
    }

 	private String getUrlBaseForLocalServer() {
	   HttpServletRequest request = 
	       ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	   String base = 
	      "http://"+request.getServerName() 
	      + ((request.getServerPort() != 80) ? ":"+request.getServerPort() : "");
	   return base;
	}
 	
	@RequestMapping(value = GiftSvcApi.GIFT_SVC_PATH + "/{"+GiftSvcApi.ID_PARAMETER+"}/like", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Void> likeGift(@PathVariable("id") String id, Principal p){
		Gift gift = giftRepository.findOne(id);		
		User user = userRepository.findUserByUsername(p.getName());
		
		if (gift == null || user == null){			
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}else{
			Collection<Flag> flags = new ArrayList<Flag>();
			flags = flagRepository.findFlagByUserAndGiftAndType(user.getId(), gift.getId(), FlagType.TOUCH);
			
			if (flags.size()>0){
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}else{		
				Flag newFlag = new Flag(user.getId(), gift.getId(), FlagType.TOUCH);
				flagRepository.save(newFlag);		
								
				user.getFlags().add(newFlag.getId());
				userRepository.save(user);
				
				gift.getFlags().add(newFlag.getId());
				giftRepository.save(gift);
				
				return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
			}
		}
	}
		
	@RequestMapping(value = GiftSvcApi.GIFT_SVC_PATH + "/{"+GiftSvcApi.ID_PARAMETER+"}/unlike", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Void> unlikeGift(@PathVariable(GiftSvcApi.ID_PARAMETER) String id, Principal p){
		Gift gift = giftRepository.findOne(id);		
		User user = userRepository.findUserByUsername(p.getName());
		
		if (gift == null || user == null){			
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}else{
			Collection<Flag> flags = new ArrayList<Flag>();
			flags = flagRepository.findFlagByUserAndGiftAndType(user.getId(), gift.getId(), FlagType.TOUCH);
						
			if (flags.size()==0){
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}else{				
				Flag flag = flags.iterator().next();
				
				user.getFlags().remove(flag.getId());
				userRepository.save(user);
				
				gift.getFlags().remove(flag.getId());
				giftRepository.save(gift);
				
				flagRepository.delete(flag);
				
				return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
			}
		}
	}
	
	@RequestMapping(value = GiftSvcApi.GIFT_SVC_PATH + "/{"+GiftSvcApi.ID_PARAMETER+"}/innapropiated", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Void> innapropiatedGift(@PathVariable("id") String id, Principal p){
		Gift gift = giftRepository.findOne(id);		
		User user = userRepository.findUserByUsername(p.getName());
		
		if (gift == null || user == null){			
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}else{
			Collection<Flag> flags = new ArrayList<Flag>();
			flags = flagRepository.findFlagByUserAndGiftAndType(user.getId(), gift.getId(), FlagType.INNAPROPIATED);
			
			if (flags.size()>0){
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}else{		
				Flag newFlag = new Flag(user.getId(), gift.getId(), FlagType.INNAPROPIATED);
				flagRepository.save(newFlag);		
								
				user.getFlags().add(newFlag.getId());
				userRepository.save(user);
				
				gift.getFlags().add(newFlag.getId());
				giftRepository.save(gift);
				
				return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
			}
		}
	}
		
	@RequestMapping(value = GiftSvcApi.GIFT_SVC_PATH + "/{"+GiftSvcApi.ID_PARAMETER+"}/apropiated", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<Void> apropiatedGift(@PathVariable(GiftSvcApi.ID_PARAMETER) String id, Principal p){
		Gift gift = giftRepository.findOne(id);		
		User user = userRepository.findUserByUsername(p.getName());
		
		if (gift == null || user == null){			
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}else{
			Collection<Flag> flags = new ArrayList<Flag>();
			flags = flagRepository.findFlagByUserAndGiftAndType(user.getId(), gift.getId(), FlagType.INNAPROPIATED);
						
			if (flags.size()==0){
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}else{				
				Flag flag = flags.iterator().next();
				
				user.getFlags().remove(flag.getId());
				userRepository.save(user);
				
				gift.getFlags().remove(flag.getId());
				giftRepository.save(gift);
				
				flagRepository.delete(flag);
				
				return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
			}
		}
	}
	
	@RequestMapping(value = GiftSvcApi.GIFT_SVC_PATH + "/{"+GiftSvcApi.ID_PARAMETER+"}/likedby", method=RequestMethod.GET, produces = GIFT_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Collection<User>> whoLikedGift(@PathVariable(GiftSvcApi.ID_PARAMETER) String id, HttpServletResponse response){
		
		Collection<User> result= new ArrayList<User>();		
		Collection<Flag> flags = flagRepository.findFlagByGiftAndType(id, FlagType.TOUCH);
		
		for(Flag f: flags){
			result.add(userRepository.findOne(f.getUser()));
		}
				
		return new ResponseEntity<Collection<User>>(result, HttpStatus.ACCEPTED);				
	}
	
	@RequestMapping(value = GiftSvcApi.GIFT_SVC_PATH, method = RequestMethod.POST, produces = GIFT_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Gift> addGift(@RequestBody Gift g, Principal p){
		User user = userRepository.findUserByUsername(p.getName());
		
		Gift gift = giftRepository.save(g);
		gift.setUser(user.getId());
		gift.setMultimediaUrl(getDataUrl(gift.getId()));		
		gift = giftRepository.save(gift);
				
		user.getGifts().add(gift.getId());
		userRepository.save(user);		
				
		return new ResponseEntity<Gift>(gift, HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value = GiftSvcApi.GIFT_SVC_PATH + "/{"+ GiftSvcApi.ID_PARAMETER +"}", method = RequestMethod.DELETE, produces = GIFT_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Void> deleteGift(@PathVariable(GiftSvcApi.ID_PARAMETER) String id, Principal p) throws IOException{
		Gift gift = giftRepository.findOne(id);
		User user = userRepository.findUserByUsername(p.getName());
		
		//gift.user == user.loggedin
		if ( gift!=null && user.getId().equals(gift.getUser()) ){			
			
			Chain c = chainRepository.findOne(gift.getChain());
			if (c.getGifts().size()==1){
				chainRepository.delete(c);
			}else{
				c.getGifts().remove(gift.getId());
				chainRepository.save(c);
			}			
			
			for(String flag_id: gift.getFlags()){
				flagRepository.delete(flag_id);
				user.getFlags().remove(flag_id);
			}							
												
			user.getGifts().remove(gift.getId());			
			userRepository.save(user);		
			
			GiftFileManager.get().deleteVideoData(gift);
			giftRepository.delete(gift);
			
			return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
		}else{			
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}	
	
	@RequestMapping(value = GiftSvcApi.GIFT_SVC_PATH + "/{"+GiftSvcApi.ID_PARAMETER+"}", method=RequestMethod.GET, produces = GIFT_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Gift> findGiftById(@PathVariable(GiftSvcApi.ID_PARAMETER) String id, Principal p, HttpServletResponse response){
		Gift gift = giftRepository.findOne(id);		
		
		if (gift == null){
			return new ResponseEntity<Gift>(HttpStatus.NOT_FOUND);
		}else{			
			return new ResponseEntity<Gift>(gift, HttpStatus.ACCEPTED);
		}		
	}
	
	@RequestMapping(value = GiftSvcApi.GIFT_SVC_PATH, method = RequestMethod.GET, produces = GIFT_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Collection<Gift>> getGiftList(Principal p){			
		User user 				= userRepository.findUserByUsername(p.getName());		
		Collection<Gift> gifts 	= giftRepository.findAll(user.getOrderByGifts().equals(OrderType.DATE)?giftsOrderByDateRegister:giftsOrderByFlags);			
		gifts 					= filterSettings(gifts, user);
		
		if (gifts == null){
			return new ResponseEntity<Collection<Gift>>(HttpStatus.NOT_FOUND);
		}else{			
			return new ResponseEntity<Collection<Gift>>(gifts, HttpStatus.ACCEPTED);
		}	
	}		
	
	@RequestMapping(value = GiftSvcApi.GIFT_TITLE_SEARCH_PATH, method=RequestMethod.GET, produces = GIFT_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Collection<Gift>> findGiftByTitle(@Query(GiftSvcApi.TITLE_PARAMETER) String title, Principal p, HttpServletResponse response){
		User user 				= userRepository.findUserByUsername(p.getName());				
		Collection<Gift> gifts 	= giftRepository.findGiftByTitleRegex(title, user.getOrderByGifts().equals(OrderType.DATE)?giftsOrderByDateRegister:giftsOrderByFlags);			
		gifts 					= filterSettings(gifts, user);
		
		if (gifts == null){
			return new ResponseEntity<Collection<Gift>>(HttpStatus.NOT_FOUND);
		}else{			
			return new ResponseEntity<Collection<Gift>>(gifts, HttpStatus.ACCEPTED);
		}		
	}
	
	@RequestMapping(value = GiftSvcApi.GIFT_USER_SEARCH_PATH, method=RequestMethod.GET, produces = GIFT_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Collection<Gift>> findGiftByUser(@Query(GiftSvcApi.USER_PARAMETER) String u, HttpServletResponse response){
		User user 				= userRepository.findOne(u);		
		Collection<Gift> gifts 	= giftRepository.findGiftByUser(u, user.getOrderByGifts().equals(OrderType.DATE)?giftsOrderByFlags:giftsOrderByDateRegister);		
		gifts 					= filterSettings(gifts, user);
		
		if (gifts == null){
			return new ResponseEntity<Collection<Gift>>(HttpStatus.NOT_FOUND);
		}else{			
			return new ResponseEntity<Collection<Gift>>(gifts, HttpStatus.ACCEPTED);
		}		
	}
	
	@Multipart
	@RequestMapping(value = GiftSvcApi.GIFT_DATA_PATH, method = RequestMethod.POST)
	public @ResponseBody GiftStatus setGiftData(@PathVariable(GiftSvcApi.ID_PARAMETER) String id, @RequestParam(GiftSvcApi.DATA_PARAMETER) MultipartFile videoData) throws FileNotFoundException, IOException{
		
		if (giftRepository.exists(id)){
			Gift gift = giftRepository.findOne(id);
			
			GiftFileManager.get().saveGiftData(gift, videoData.getInputStream());
					
			return new GiftStatus(GiftState.READY);
		}else{
			throw new ResourceNotFoundException();
		}
	}	
	
	@Streaming
	@RequestMapping(value = GiftSvcApi.GIFT_DATA_PATH, method = RequestMethod.GET)
    public void getData(@PathVariable(GiftSvcApi.ID_PARAMETER) String id, HttpServletResponse response) throws IOException{
		if (giftRepository.exists(id)){
			Gift gift = giftRepository.findOne(id);			
			
			GiftFileManager.get().copyGiftData(gift, response.getOutputStream());
		
			response.setHeader("Content-Disposition", "attachment;filename=" + gift.getTitle() );		
			response.setContentType(gift.getContentType());						
									
		}else{
			throw new ResourceNotFoundException();
		}
	}
	
	private Collection<Gift> filterSettings(Collection<Gift> input, User user){
		
		Collection<Gift> result = new ArrayList<Gift>();
		
		for (Gift g: input){
			if ((flagRepository.findFlagByUserAndGiftAndType(user.getId(), g.getId(), FlagType.INNAPROPIATED).size()==0 && !user.getViewInappropiatedGift()) ||
				user.getViewInappropiatedGift()
				)
			{
				result.add(g);
			}
		}
		
		return result;
	}
}
