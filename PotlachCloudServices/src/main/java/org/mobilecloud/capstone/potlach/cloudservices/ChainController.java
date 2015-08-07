package org.mobilecloud.capstone.potlach.cloudservices;

import javax.servlet.http.HttpServletResponse;

import org.mobilecloud.capstone.potlach.cloudservices.repository.ChainRepository;
import org.mobilecloud.capstone.potlach.common.api.ChainSvcApi;
import org.mobilecloud.capstone.potlach.common.repository.Chain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChainController {
	
	public static final String CHAIN_FORMAT_RESPONSE = "application/json";
		
	@Autowired
	private ChainRepository chainRepository;
	
	@RequestMapping(value = ChainSvcApi.CHAIN_SVC_PATH, method = RequestMethod.POST, produces = CHAIN_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Chain> addChain(@RequestBody Chain c){
				
		Chain chain = chainRepository.save(c);
		return new ResponseEntity<Chain>(chain, HttpStatus.ACCEPTED);
	}	
	
	@RequestMapping(value = ChainSvcApi.CHAIN_SVC_PATH + "/{"+ChainSvcApi.ID_PARAMETER+"}", method=RequestMethod.GET, produces = CHAIN_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Chain> findById(@PathVariable(ChainSvcApi.ID_PARAMETER) String id, HttpServletResponse response){
		Chain chain = chainRepository.findOne(id);		
		
		if (chain == null){
			return new ResponseEntity<Chain>(HttpStatus.NOT_FOUND);
		}else{			
			return new ResponseEntity<Chain>(chain, HttpStatus.ACCEPTED);
		}	
	}
}
