package com.ngayngo9x.flux;

import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Random;

@RestController
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    private final WebClient webClient = WebClient.create("http://10.26.1.249:6789");

    @Autowired
    private IMap<String, String> hsMap;

    @GetMapping(value = "/test")
    public Mono<String> reactiveService() {
        var container = new PipelineContainer();
        return Mono.create(monoSink -> {
            var cacheKey = "http://localhost:6969/test" + (new Random()).nextInt(1000);
            hsMap.getAsync(cacheKey).andThen(new ExecutionCallback<>() {
                @Override
                public void onResponse(String s) {
                    //System.out.println("Value: " + s);
                    monoSink.success(s);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    monoSink.error(throwable);
                }
            });
        }).then(webClient.get().uri("/remote_server_test").retrieve().bodyToMono(String.class))
        .flatMap(v -> Mono.create(stringMonoSink -> {
            var cacheKey = "http://localhost:6969/test" + (new Random()).nextInt(1000);
            hsMap.putAsync(cacheKey, v).andThen(new ExecutionCallback<>() {
                @Override
                public void onResponse(String s) {
//                    System.out.println("onresponse: " + s);
//                    String valueNew = v.toUpperCase();
                    stringMonoSink.success(v);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    stringMonoSink.error(throwable);
                }
            });
        }));

    }

    static class User {
        private int userId;
        private int id;
        private String title;
        private boolean completed;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }

}
