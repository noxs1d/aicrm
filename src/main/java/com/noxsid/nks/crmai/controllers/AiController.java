package com.noxsid.nks.crmai.controllers;

import com.noxsid.nks.crmai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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
        Logger logger = LoggerFactory.getLogger(AiController.class);
        logger.info(text); //aiService.chatResponse(text).toString() );
        logger.info(aiService.chatResponse(text));

    }

}
