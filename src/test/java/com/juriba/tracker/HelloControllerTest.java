//package com.juriba.tracker;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.security.test.context.support.WithAnonymousUser;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(HelloController.class)
//@DisplayName("Hello Controller")
//class HelloControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Nested
//    @DisplayName("GET /")
//    class GetHelloEndpoint {
//
//        @Test
//        @DisplayName("should return 401 when unauthenticated")
//        @WithAnonymousUser
//        void testHelloEndpointUnauthenticated() throws Exception {
//            mockMvc.perform(get("/"))
//                    .andExpect(status().isUnauthorized());
//        }
//
//        @Test
//        @DisplayName("should return 'hello let's have fun' when authenticated")
//        @WithMockUser
//        void testHelloEndpoint() throws Exception {
//            mockMvc.perform(get("/"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().string("hello let's have fun"));
//        }
//    }
//}