$(document).ready(function(){
	var ws;
	var usrName,wsConnected=false;
	var uniqueId=0;
	
	var colors = [
	    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
	    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
	];
	
	$(".userNameDiv button").on('click',function(){
		addNewUser();
	});
	
	
	$(".userNameDiv input[type='text']").on('keydown',function(e){
		if(e.keyCode==13){
			e.preventDefault();
			addNewUser();
		}
	});
	
	$(".chatSendArea button").on('click',function(){
		sendChat();
	});
	
	$(".chatSendArea input[type='text']").on('keydown',function(e){
		if(e.keyCode==13){
			e.preventDefault();
			sendChat();
		}
	});
	
	window.onbeforeunload = function(){
		if(ws!==undefined && ws!=null && ws!==null && 1==ws.readyState){	
			//window.event.returnValue= "Are you sure ? This will remove you from chat.";   
			sendMsg('Left',"");
	    	ws.close();
	    	//console.log("this is called");
		}
	}
	
	function createWS(){
		 ws = new WebSocket(url("/basicChatApp"));
		    ws.onopen = function() {
		        /*console.log("ws.onopen", ws);
		        console.log(ws.readyState, "websocketstatus");*/
		        wsConnected=true;
		        sendMsg('Joined',"");
		    }
		 
		    ws.onerror = function(e) {
		        //console.log("ws.onerror", ws, e);
		    	alert("Error Occured");
		    }
		 
		    ws.onmessage = function(e) {
		        //console.log("ws.onmessage", ws, e);
		        updateContainer(e.data);
		    }
		 
		    ws.onclose=function(e){
		    	 //console.log("ws.onclose", ws, e);
		    	 sendMsg('Left',"");
		    }
		  
	}	
	
	function updateContainer(message) {
    	var msg  = JSON.parse(message);
    	var usrnme=msg.userName;
    	  if(msg.uniqueId==uniqueId){
    		  usrnme="You";
    	  }
      if(msg.msgType=='Joined'){
    	  addJoined(msg);
      }else if(msg.msgType=='Left'){
    	  addLeft(msg);
      }else{
    	  addChat(msg);
      }
    }
    
	function addChat(msg){
		var usrnme=msg.userName;
	  	  if(msg.uniqueId==uniqueId){
	  		  usrnme="You";
	  	  }
		$("<li><div class='nameTag' style='background-color:"+getAvatarColor(msg.userName)+"';>"+msg.userName[0].toUpperCase()+
				"</div><div class='chatTag'><div class='upper'>" +
				"<b><i>"+usrnme+" : </i></b></div><div class='lower'>"+msg.chat+"</div></div></li>").hide().prependTo("#chatArea").slideDown('slow');
	}
	
    function sendMsg(type,chat){
    	if(type=='Joined'){
    		var d = new Date();
    		uniqueId = d.getTime();
    		hideUserNameShowChat();
    	}
    	var chatMsg = {userName:usrName,msgType:type,chat:chat,uniqueId:uniqueId};
    	ws.send(JSON.stringify(chatMsg));
    	$("input[type='text']").val("");
    }
    
    function getAvatarColor(username) {
        var hash = 0;
        for (var i = 0; i < username.length; i++) {
            hash = 31 * hash + username.charCodeAt(i);
        }
        var index = Math.abs(hash % colors.length);
        return colors[index];
    }
    
    function hideUserNameShowChat(){
    	$(".userNameDiv").hide();
    	$("#chatBox").show();
    	$("ul li").remove();
    }
    
    function addNewUser(){
    	usrName=$.trim($(".userNameDiv input[type=text]").val());
		if(usrName!==undefined && usrName!=null && usrName!==null && usrName!=''){
			if(!wsConnected){
				createWS();
			}
			if(wsConnected){
				sendMsg('Joined',"");
		    }
		}
    }
    
    function sendChat(){
    	if(usrName!==undefined && usrName!=null && usrName!==null && usrName!=''){
			sendMsg('CHAT',$(".chatSendArea input[type='text']").val());
		}
    }
    
    function url(s) {
        var l = window.location;
        return ((l.protocol === "https:") ? "wss://" : "ws://") + l.host + s;
    }
   
    function addLeft(msg){
    	var usrnme=msg.userName;
  	  if(msg.uniqueId==uniqueId){
  		  usrnme="You";
  	  }
    	$("<ul class='leave' id='leaveid-"+msg.uniqueId+"'><li>"+usrnme+" "+msg.msgType+" ! </li></ul>").appendTo("body");
    	$("#leaveid-"+msg.uniqueId).animate({"left":"82%"},500);
    	$("#leaveid-"+msg.uniqueId).delay(1300).animate({"left":"100%"},500,function(){
    		$(this).remove();
    	});
    }
    
    function addJoined(msg){
    	var usrnme=msg.userName;
  	  if(msg.uniqueId==uniqueId){
  		  usrnme="You";
  	  }
    	$("body").append("<ul class='joined' id='joinid-"+msg.uniqueId+"'><li>"+usrnme+" "+msg.msgType+" ! </li>");
    	$("#joinid-"+msg.uniqueId).animate({"margin":"0 0 1% 0"},500);
    	$("#joinid-"+msg.uniqueId).delay(1300).animate({"margin":"0 0 1% -22%"},500,function(){
    		$(this).remove();
    	});
    }
    
}); 