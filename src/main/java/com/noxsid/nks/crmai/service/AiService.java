package com.noxsid.nks.crmai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {

    private final OllamaChatModel chatModel;

    public ChatResponse chatResponse(String input){
        String systemPrompt = """
                You are a smart assistant that detects if the input is a task assignment.
                                
                If the user input seems to be a task (e.g., "Assign task to John to make an AI model for CRM system by 22 August"), respond strictly in the following JSON format:
                                
                {
                  "title": "Short title for the task",
                  "description": "Detailed explanation of what needs to be done.",
                  "recommendation": "Useful suggestion or first step to start completing the task.",
                  "assignee": "Person responsible for the task",
                  "due_date": "Deadline in YYYY-MM-DD format (convert relative dates like 'tomorrow' or 'next week' to exact ones)",
                  "project": "Project name if mentioned, else null",
                  "original_text": "The original input provided by the user"
                }
                                
                If the input does NOT resemble a task (e.g., a question or general comment), then respond in plain English with a helpful answer or clarification. Do not return JSON in that case.
                                
                Rules:
                - Always output **valid and minimal JSON** without comments, extra text, or markdown.
                - If something is missing (e.g., assignee or date), fill that field with `null`.
                - Make the `title` a concise version of the task.
                - If project is mentioned (like "for CRM system"), include it in the `"project"` field.
                - In `recommendation`, give one or two helpful next steps (e.g., "Start by choosing a suitable ML algorithm").
                                
                Example:
                Input: "Assign task to John to make an AI model for CRM system by 22 August."
                                
                Output:
                {
                  "title": "Build AI model for CRM",
                  "description": "John should develop an AI model that can be integrated into the CRM system to automate relevant tasks or insights.",
                  "recommendation": "Start by identifying key CRM functionalities where AI can be applied and select appropriate tools or libraries.",
                  "assignee": "John",
                  "due_date": "2024-08-22",
                  "project": "CRM system",
                  "original_text": "Assign task to John to make an AI model for CRM system by 22 August."
                }
                                
                """;
        Message systemMessage = new SystemMessage(systemPrompt);
        Message userMessage = new UserMessage(input);
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        ChatResponse chatResponse = chatModel.call(new Prompt(userMessage));
        System.out.println(chatResponse.getResult().getOutput().getText());
        return chatResponse;
    }
}
