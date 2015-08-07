package org.mobilecloud.capstone.potlach.common.repository;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Chain {

	@Id
	private String id;
	
	private long dateRegister;
	
	@ElementCollection
	private Set<String> gifts = new HashSet<String>();

	public Chain(){
	}
	
	public Chain(Gift g){	
		this.dateRegister = new Date().getTime();
		this.gifts.add(g.getId());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<String> getGifts() {
		return gifts;
	}

	public void setGifts(Set<String> gifts) {
		this.gifts = gifts;
	}

	public long getDateRegister() {
		return dateRegister;
	}

	public void setDateRegister(long dateRegister) {
		this.dateRegister = dateRegister;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Chain) {
			Chain other = (Chain) obj;			
			return getGifts().equals(other.getGifts());
		} else {
			return false;
		}
	}
}
