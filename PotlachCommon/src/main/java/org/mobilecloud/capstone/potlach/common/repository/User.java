package org.mobilecloud.capstone.potlach.common.repository;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.persistence.annotations.Index;
import org.eclipse.persistence.annotations.Indexes;

import com.google.common.base.Objects;

@Entity
@Indexes({	
	@Index(unique=true, columnNames={"username"})
})
public class User {

	public enum FrecuencyUpdates{
		MINUTE, FIVE_MINUTES, HOUR
	}
	
	public enum OrderType{
		DATE, MOST_TOUCHED
	}
	
	@Id
	private String id;

	private String name;
	private String username;	
	private String password;
	
	private boolean viewInappropiatedGift;
	private FrecuencyUpdates frecuencyUpdateFlags;
	private OrderType orderByGifts;
	
	private long dateRegister;
	
	
	@ElementCollection
	private Set<String> flags = new HashSet<String>();
	
	@ElementCollection
	private Set<String> gifts = new HashSet<String>();

	public User(){		
	}
	
	public User(String name, String username, String password){
		this.name = name;
		this.username = username;
		setPassword(password);
		this.dateRegister = new Date().getTime();	
	}
	
	private String encrypt(String x) throws Exception {
	    java.security.MessageDigest d = null;
	    d = MessageDigest.getInstance("SHA-1");
	    d.reset();
	    d.update(x.getBytes());	    
	    
	    StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<d.digest().length;i++) {
    	  hexString.append(Integer.toHexString(0xFF & d.digest()[i]));
    	}
    	
    	return hexString.toString();
	  }
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		try {
			this.password = encrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Collection<String> getFlags() {
		return flags;
	}

	public void setFlags(Set<String> flags) {
		this.flags = flags;
	}

	public Set<String> getGifts() {
		return gifts;
	}
	
	public boolean getViewInappropiatedGift() {
		return viewInappropiatedGift;
	}

	public void setViewInappropiatedGift(boolean viewInappropiatedGift) {
		this.viewInappropiatedGift = viewInappropiatedGift;
	}

	public FrecuencyUpdates getFrecuencyUpdateFlags() {
		return frecuencyUpdateFlags;
	}

	public void setFrecuencyUpdateFlags(FrecuencyUpdates frecuencyUpdateFlags) {
		this.frecuencyUpdateFlags = frecuencyUpdateFlags;
	}

	public void setGifts(Set<String> gifts) {
		this.gifts = gifts;
	}	
	
	public OrderType getOrderByGifts() {
		return orderByGifts;
	}

	public void setOrderByGifts(OrderType orderByGifts) {
		this.orderByGifts = orderByGifts;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public long getDateRegister() {
		return dateRegister;
	}

	public void setDateRegister(long dateRegister) {
		this.dateRegister = dateRegister;
	}
	
	@Override
	public int hashCode() {		
		return Objects.hashCode(getId(), getUsername(), getPassword());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User other = (User) obj;			
			return Objects.equal(getUsername(), other.getUsername());
		} else {
			return false;
		}
	}
}
