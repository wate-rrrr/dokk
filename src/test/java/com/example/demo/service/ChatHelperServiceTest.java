package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatHelperServiceTest {

    @Autowired
    private  ChatHelperService chatHelperService;

    @Test
    void test_question(){
        System.out.println(chatHelperService.question("who is the red queen", null));
        
    }
    
}
