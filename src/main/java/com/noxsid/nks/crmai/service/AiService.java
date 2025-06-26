package com.noxsid.nks.crmai.service;

import com.noxsid.nks.crmai.tools.PreprocessTool;
import com.noxsid.nks.crmai.tools.ProjectTool;
import com.noxsid.nks.crmai.tools.TaskTool;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;

@Service
@RequiredArgsConstructor
public class AiService {

    private final OllamaChatModel chatModel;
    private final TaskTool taskTool;
    private final ProjectTool projectTool;
    private final PreprocessTool preprocessTool;
    public String chatResponse(String input){
        Logger logger = LoggerFactory.getLogger(AiService.class);
        final var systemMessage = getMessage();
        Message userMessage = new UserMessage(input);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        logger.info(ChatClient.create(chatModel).prompt(prompt).tools(projectTool, taskTool, preprocessTool).toString());
        String response = ChatClient.create(chatModel).prompt(prompt).tools(preprocessTool, projectTool, taskTool).call().content();
        System.out.println(response);
        logger.info(response);
        return response;
    }

    private static Message getMessage() {
        String systemPrompt = """
                You are a smart assistant that detects if the input is a task assignment, or Project assignment.
                Depending on given prompt call necessary tool, but before calling any tool use 'Prompt Generator Tool' to give better results.
                Create Task Tool: To create task depending on the prompt.
                Project Creation Toll: To create project depending on the prompt.
                """;
        Message systemMessage = new SystemMessage(systemPrompt);
        return systemMessage;
    }
}
