package com.spring.companytaskmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CompanytaskmanagerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void checkDot() {
		String str = "absdef.p";
		System.out.println(str.substring(str.lastIndexOf('.')));
	}

}
