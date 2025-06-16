package com.noxsid.nks.crmai;

import com.noxsid.nks.crmai.service.AiService;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class CrmaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmaiApplication.class, args);
	}

}
