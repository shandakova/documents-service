package com.shandakova.documents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.shandakova.documents")
public class DocumentServiceApp {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(DocumentServiceApp.class, args);
    }
}
