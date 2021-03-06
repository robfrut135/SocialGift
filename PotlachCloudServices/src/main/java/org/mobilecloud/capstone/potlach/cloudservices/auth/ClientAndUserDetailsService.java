package org.mobilecloud.capstone.potlach.cloudservices.auth;

import org.mobilecloud.capstone.potlach.cloudservices.repository.UserRepository;
import org.mobilecloud.capstone.potlach.common.repository.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class ClientAndUserDetailsService implements UserDetailsService, ClientDetailsService {

	private final ClientDetailsService clients_;
	
	@Autowired
	private UserRepository userRepository_;

	public ClientAndUserDetailsService(){
		clients_ = null;
	}
	
	public ClientAndUserDetailsService(ClientDetailsService clients) {
		clients_ = clients;
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId)
			throws ClientRegistrationException {
		return clients_.loadClientByClientId(clientId);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userDb = userRepository_.findUserByUsername(username);
			 
		if( userDb == null )
			throw new UsernameNotFoundException( "Username not found, bad user." );
	 
//	    List<SimpleGrantedAuthority> authorities = Arrays.asList( new SimpleGrantedAuthority( user.getRole() ) );
	 
	    return OauthUser.create( userDb.getUsername(), userDb.getPassword(), "USER" );
		
	}

}
