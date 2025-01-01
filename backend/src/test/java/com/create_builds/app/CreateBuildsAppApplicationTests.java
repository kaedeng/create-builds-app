package com.create_builds.app;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.create_builds.app.service.NBTService;

@SpringBootTest
class CreateBuildsAppApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Autowired
	private NBTService nbtService;
	
	@Test
	void testParseAndPrintNBT() {
		try {
            URL resource = getClass().getClassLoader().getResource("test.nbt");
            if (resource == null) {
                throw new IllegalArgumentException("File not found: test.nbt");
            }

            File file = new File(resource.getFile());

            nbtService.parseAndPrintNBT(file.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error during test: " + e.getMessage());
        }
    }
	

}
