package org.mobilecloud.capstone.potlach.cloudservices;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.mobilecloud.capstone.potlach.cloudservices.repository.UserRepository;
import org.mobilecloud.capstone.potlach.common.api.UserSvcApi;
import org.mobilecloud.capstone.potlach.common.repository.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
	
	public static final String USER_FORMAT_RESPONSE = "application/json";
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value = UserSvcApi.USER_SVC_PATH, method = RequestMethod.POST, produces = USER_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<User> addUser(@RequestBody User u){
		User existUser = userRepository.findUserByUsername(u.getUsername());
		if (existUser==null) {		
			User user = userRepository.save(u);		
			return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
		}else{			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}	
	
	@RequestMapping(value = UserSvcApi.USER_SVC_PATH, method = RequestMethod.PUT, produces = USER_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<User> saveUser(@RequestBody User u){
		User existUser = userRepository.findUserByUsername(u.getUsername());
		if (existUser!=null) {		
			User user = userRepository.save(u);		
			return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
		}else{			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = UserSvcApi.USER_SVC_PATH + "/{"+ UserSvcApi.ID_PARAMETER + "}", method = RequestMethod.DELETE, produces = USER_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Void> deleteUser(@PathVariable(UserSvcApi.ID_PARAMETER) String id) throws IOException{		
		User existUser = userRepository.findOne(id);
		if (existUser!=null) {			
			userRepository.delete(existUser);		
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}else{			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}	
	
	@RequestMapping(value = UserSvcApi.USER_SVC_PATH, method = RequestMethod.GET, produces = USER_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<Collection<User>> getUserList(){				
		return new ResponseEntity<Collection<User>>((Collection<User>) userRepository.findAll(), HttpStatus.ACCEPTED);
	}	
	
	@RequestMapping(value = UserSvcApi.USER_SVC_PATH + "/{"+ UserSvcApi.ID_PARAMETER + "}", method=RequestMethod.GET, produces = USER_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<User> findUserById(@PathVariable(UserSvcApi.ID_PARAMETER) String id, HttpServletResponse response){
		User user = userRepository.findOne(id);		
		
		if (user == null){
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}else{			
			return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
		}	
	
	}
	
	@RequestMapping(value = UserSvcApi.USER_LOGIN_PATH, method = RequestMethod.POST, produces = USER_FORMAT_RESPONSE)
	public @ResponseBody ResponseEntity<User> login(@RequestParam(UserSvcApi.USERNAME_PARAMETER) String username, 
													@RequestParam(UserSvcApi.PASSWORD_PARAMETER) String password){		
		User user = userRepository.findUserByUsernameAndPassword(username, password);				
		if (user == null){
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}else{			
			return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
		}	
	}		
}
