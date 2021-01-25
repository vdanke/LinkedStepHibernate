package org.step.linked.step.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.step.linked.step.service.EventService;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final RestTemplate restTemplate;

    @Autowired
    public EventServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendEventToEventLogger(String event) {
        CompletableFuture.runAsync(() -> {
            HttpEntity<String> httpEntity = new HttpEntity<>(event);
            ResponseEntity<String> response = restTemplate
                    .exchange("http://localhost:5555/event", HttpMethod.POST, httpEntity, String.class);
            System.out.printf("Response: %s%n", response.getBody());
        });
    }

    @Override
    public void sendFileEventToLoggerService() {
        CompletableFuture<Void> pipeline = CompletableFuture.supplyAsync(() -> {
            String pathToLogFile = "C:/Users/viele/IdeaProjects/LinkedStep/src/main/resources/logs/logs.txt";
            return new FileSystemResource(
                    pathToLogFile
            );
        }, executorService).thenApply(res -> {
            MultiValueMap<String, Object> fileMap = new LinkedMultiValueMap<>();
            fileMap.add("file", res);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(fileMap, httpHeaders);
            return restTemplate.postForEntity("http://localhost:5555/event/file", httpEntity, String.class);
        }).thenAccept(resp -> {
            System.out.println(resp.getStatusCode());
            System.out.println(resp.getHeaders());
            System.out.println(resp.getBody());
        }).exceptionally(ex -> {
            System.out.println(ex.getLocalizedMessage());
            return null;
        });
    }

    private void completableFutureExample(String filename) {
        CompletableFuture.supplyAsync(() -> new File(filename))
                .thenAccept(file -> {
                    FileReader fileReader = null;
                    try {
                        fileReader = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String collect = bufferedReader.lines().collect(Collectors.joining());
                        System.out.println(collect);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }).exceptionally(ex -> {
            System.out.println(ex.getLocalizedMessage());
            return null;
        });
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> "some");
        CompletableFuture<Void> other = CompletableFuture.supplyAsync(() -> "filename")
                .thenApply(s -> s.concat("other"))
                .thenCombine(stringCompletableFuture, String::concat)
                .thenApply(String::length)
                .thenAccept(System.out::println)
                .exceptionally(ex -> {
                    String localizedMessage = ex.getLocalizedMessage();
                    System.out.println(localizedMessage);
                    return null;
                });
    }
}
