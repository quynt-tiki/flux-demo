package com.ngayngo9x.flux;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.IMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class Application {

	@Bean
	public IMap<String, String> createHsMap() {
		var clientConfig = new ClientConfig();
		var networkConfig = new ClientNetworkConfig();
		networkConfig.addAddress("127.0.0.1:5701");
		clientConfig.setNetworkConfig(networkConfig);
		return HazelcastClient.newHazelcastClient().getMap("customer");
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		/*CompletableFuture<String> f = new CompletableFuture<>();
		f.handleAsync((s, throwable) -> s.toUpperCase());
		String join = f.join();
		System.out.println(join);*/
	}

}
