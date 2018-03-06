package com.yb.chat.ChatApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.yb.chat.websocConfig.MessageBean;

import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@SpringBootApplication
@ComponentScan(basePackages={"com.yb.chat"})
public class ChatAppApplication {

	
	
	public static void main(String[] args) {
		SpringApplication.run(ChatAppApplication.class, args);
		
	}
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ChatAppApplication.class);
	}
	   @Bean
	    public RouterFunction<ServerResponse> routes(){
	        return RouterFunctions.route(
	                RequestPredicates.GET("/ChatApp"),
	                request -> ServerResponse.ok().body(BodyInserters.fromResource(new ClassPathResource("static/websoc.html")))
	        );
	    }
	
	@Bean
	public UnicastProcessor<MessageBean> messagePublisher() {
		return UnicastProcessor.create();
	}
	
	@Bean
	public Flux<MessageBean> messages(UnicastProcessor<MessageBean> messagePublisher){
		return messagePublisher.replay(30).autoConnect();
	}
}
