package com.create_builds.app.service;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.api.Tag;
import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.collection.ListTag;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class NBTService {
    public Map<String, Integer> parseAndPrintNBT(String filePath) {
        try {
            Nbt NBT = new Nbt();
            File file = new File(filePath);
            
            if(!file.exists()) {
            	System.err.println("File not found: " + filePath);
                return new HashMap<>();
            }
            
            CompoundTag root = NBT.fromFile(file);
            ListTag<?> palette = root.get("palette");
            ListTag<?> blocks = root.get("blocks");

            Map<Integer, String> indexStringMap = new HashMap<>();
            Map<String, Integer> blockCountMap = new HashMap<>();

            int index = 0;
            for (Tag item : palette) {
                if (item instanceof CompoundTag compoundItem) {
                    for (Map.Entry<String, Tag> entry : compoundItem.getValue().entrySet()) {
                    	
                    	String key = entry.getKey();
                    	
                    	if(!key.equals("Name")) continue;
                    		                    		
                    	String stringValue = (String) entry.getValue().getValue();
                    	
                        indexStringMap.put(index++, stringValue);
                    }
                }
            }
            
            for (Tag item : blocks) {
            	if (item instanceof CompoundTag compoundItem) {
                    for (Map.Entry<String, Tag> entry : compoundItem.getValue().entrySet()) {
                    	
                    	String key = entry.getKey();
                    	
                    	if(!key.equals("state")) continue;
                    		                    		
                    	Integer stateValue = (Integer) entry.getValue().getValue();
                    	
                    	String blockName = indexStringMap.getOrDefault(stateValue, "Unknown");
                        blockCountMap.put(blockName, blockCountMap.getOrDefault(blockName, 0) + 1);

                    }
                }
            }

//            blockCountMap.forEach((blockName, count) -> System.out.println(blockName + ", Count: " + count));
            return blockCountMap;
        }catch(Exception e){
        	System.err.println("Error parsing NBT:" + e.getMessage());
        	return new HashMap<>();
        }
    }
  
}
