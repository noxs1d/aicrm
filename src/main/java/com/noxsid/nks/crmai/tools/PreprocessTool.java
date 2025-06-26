package com.noxsid.nks.crmai.tools;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PreprocessTool {
    private final Logger logger = LoggerFactory.getLogger(PreprocessTool.class);
    private final OllamaChatModel chatModel;
    @Tool(name = "Prompt Generator Tool",
    description = """
            This tool is designed to assist you in organizing and presenting information clearly and effectively.
            By generating a Title, Description, and Recommendations for any given task,
            it helps break down complex tasks into manageable components.
            """
    )
    public String textGenerator(@ToolParam(description = "Prompt which was entered by user") String input){
        String sysPrompt = """
                As your helpful assistant, please generate the following for the given task:
                                
                Title: Provide a succinct and compelling title that encapsulates the main objective of the task.
                                
                Description: Write a detailed and informative description of the task.
                This should clearly explain what needs to be done, why it’s important,
                and any background or context that would help in understanding the task more thoroughly.
                                
                Recommendation: Offer actionable, practical advice for completing the task efficiently.
                Include best practices, tips, or strategies that will enhance the quality of the work
                and ensure the task is completed successfully.
                
                And other all necessary text (like deadline give as yyyy-MM-dd, username, name and etc.).
                """;
        logger.info("Generating prompt");
        UserMessage userMessage = new UserMessage(input);
        SystemMessage systemMessage = new SystemMessage(sysPrompt);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        return ChatClient.create(chatModel)
                .prompt(prompt).call().content();
    }
}
