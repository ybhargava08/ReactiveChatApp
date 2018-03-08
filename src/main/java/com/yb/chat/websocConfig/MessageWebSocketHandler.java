package com.yb.chat.websocConfig;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.google.gson.Gson;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

public class MessageWebSocketHandler implements WebSocketHandler{

    Gson gson = new Gson();	
        
    private Flux<String> outputMessages;
    private UnicastProcessor<MessageBean> messagePub;
    
    public MessageWebSocketHandler(UnicastProcessor<MessageBean> _messagePub,Flux<MessageBean> _outputMessages) {
    	this.outputMessages=Flux.from(_outputMessages).map(this::convertBeanToJSON);
    	this.messagePub=_messagePub;
    }
    
    
	@Override
	public Mono<Void> handle(WebSocketSession session) {
	    WebSocketMessageSubscriber subscriber = new WebSocketMessageSubscriber(messagePub);
		session.receive().map(WebSocketMessage::getPayloadAsText)
				.map(this::convertJSONToBean)
				.subscribe(subscriber::onNext,subscriber::onError,subscriber::onComplete);
		
		return session.send(outputMessages.map(session::textMessage));
	}
	
	
private MessageBean convertJSONToBean(String json) {
	//System.out.println("converting json to bean");
	return gson.fromJson(json, MessageBean.class);
}

private String convertBeanToJSON(MessageBean bean) {
	//System.out.println("converting bean to json");
	return gson.toJson(bean);
}

private static class WebSocketMessageSubscriber {
	 private UnicastProcessor<MessageBean> messagePub;
	 
	 public WebSocketMessageSubscriber(UnicastProcessor<MessageBean> _messagePub) {
		 this.messagePub=_messagePub; 
     }
	
   public void onNext(MessageBean message) {
      
   //	System.out.println("In on next: "+message.toString()+" "+message.msgType);
   	messagePub.onNext(message);
   	
   
   }

   public void onError(Throwable error) {
       error.printStackTrace();
   }

   public void onComplete() {
   }
}
}

