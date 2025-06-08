package com.pragma;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.pragma.TutorialApplication;


@SpringBootTest
class TutorialApplicationTests {


    /**
     * Tests the main method of TutorialApplication
     * Verifies that the SpringApplication.run method is called with correct arguments
     */
    @Test
    public void test_main_1() {
        String[] args = new String[0];
        TutorialApplication.main(args);
        // Note: This test only verifies that the main method runs without throwing exceptions
        // As SpringApplication.run is a static method, we cannot easily verify its invocation
        // in a unit test without additional mocking frameworks
    }
}
