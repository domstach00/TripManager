package com.example.tripmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(classes = TripManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TripManagerApplicationTests {

    @Test
    void contextLoads() {}
}
