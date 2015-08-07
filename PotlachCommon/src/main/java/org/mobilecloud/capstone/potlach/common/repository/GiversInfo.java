package org.mobilecloud.capstone.potlach.common.repository;

public class GiversInfo {
	 
	public String name;
	public int numTouched;
	public int numInnapropiated;
	
	public GiversInfo(String u, int t, int i) {
		name = u;
		numTouched = t;
		numInnapropiated = i;
	}
}