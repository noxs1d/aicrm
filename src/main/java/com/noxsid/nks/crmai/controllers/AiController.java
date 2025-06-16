package com.noxsid.nks.crmai.controllers;

import com.noxsid.nks.crmai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;

    @GetMapping
    public String getAi(){
        return "ai";
    }

    @PostMapping
    public void getResponse(@RequestParam("text") String text){
        Logger logger = LoggerFactory.getLogger(AiService.class);
        logger.info(text); //aiService.chatResponse(text).toString() );
        logger.info(aiService.chatResponse(text).toString() );

    }

}
