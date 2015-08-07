package org.mobilecloud.capstone.potlach.common.repository;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.persistence.annotations.Index;
import org.eclipse.persistence.annotations.Indexes;

import com.google.common.base.Objects;

@Entity
@Indexes({	
    		@Index(unique=true, columnNames={"gift","user","type"})
		})
public class Flag {

	public enum FlagType {
		TOUCH, INNAPROPIATED
	}	
	
	@Id
	private String id;	

	private String user;
	
	private String gift;
	
	private FlagType type;
	
	public Flag(){		
	}
	
	public Flag(String u, String g, FlagType ft){
		user = u;
		gift = g;
		type = ft;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift;
	}

	public FlagType getType() {
		return type;
	}

	public void setType(FlagType type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Flag) {
			Flag other = (Flag) obj;			
			return Objects.equal(user, other.user)
					&& Objects.equal(gift, other.gift)
					&& Objects.equal(type, other.type);
		} else {
			return false;
		}
	}
}
