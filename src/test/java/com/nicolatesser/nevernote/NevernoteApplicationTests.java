package com.nicolatesser.nevernote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NevernoteApplicationTests {

    //this unusual test (shipped with SpringBoot) is simply loading the sprint context, and failing is the context
	// does not start.
    @Test
    void contextLoads() {
    }

}
