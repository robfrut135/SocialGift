/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.mobilecloud.capstone.potlach.cloudservices;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.mobilecloud.capstone.potlach.common.repository.Gift;

public class GiftFileManager {

	public static GiftFileManager get() throws IOException {
		return new GiftFileManager();
	}
	
	private Path targetDir_ = Paths.get("gifts");
	
	private GiftFileManager() throws IOException{
		if(!Files.exists(targetDir_)){
			Files.createDirectories(targetDir_);
		}
	}
	
	private Path getGiftPath(Gift v){
		assert(v != null);
		
		return targetDir_.resolve("gift_"+v.getId());
	}
	
	public boolean hasGiftData(Gift v){
		Path source = getGiftPath(v);
		return Files.exists(source);
	}
	
	public void deleteVideoData(Gift v){
		Path source = getGiftPath(v);
		try {
			Files.deleteIfExists(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void copyGiftData(Gift gift, OutputStream out) throws IOException {
		Path source = getGiftPath(gift);
		if(!Files.exists(source)){
			throw new FileNotFoundException("Unable to find the referenced video file for videoId:"+gift.getId());
		}
		Files.copy(source, out);
	}
	
	public void saveGiftData(Gift v, InputStream giftData) throws IOException{
		assert(giftData != null);
		
		Path target = getGiftPath(v);
		Files.copy(giftData, target, StandardCopyOption.REPLACE_EXISTING);
	}	
}
