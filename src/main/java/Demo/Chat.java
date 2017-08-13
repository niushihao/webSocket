package Demo;

import util.GetHttpSessionConfigurator;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by nsh on 2017/8/9 0009.
 */
@ServerEndpoint(value = "/websocket",configurator= GetHttpSessionConfigurator.class)
//@ServerEndpoint(value = "/websocket")
public class Chat {

    //静态变量，用来记录当前在线链接数
    private static int onlineCount=0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<Chat> set = new CopyOnWriteArraySet<Chat>();

    //与某个客户端连接的会话，需要通过他给客户端发送数据
    private Session session;

    private static HttpSession httpSession;

    private static Map<String,String> userMap = new HashMap<String, String>();

    /**
     * 连接成功调用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session,EndpointConfig config){

        httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());


        this.session =session;
        userMap.put(session.getId(),httpSession.getAttribute("name").toString());
        set.add(this);              //加入set中
        addOnlineCount();           //在线数+1
        System.out.println("欢迎"+httpSession.getAttribute("name")+"加入！，当前在线人数为"+getOnlineCount());

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session){
        set.remove(this);
        userMap.remove(session.getId());
        subOnlineCount();
        System.out.println("有一个连接关闭，当前在线人数为"+getOnlineCount());
    }


    /**
     * 收到客户端消息后调用
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message,Session session){
        //群发消息
        for(Chat chat:set){
            try {
                String name =userMap.get(session.getId());
                System.out.println("当前用户："+name);
                chat.sendMessage("["+name+"] "+message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    @OnError
    public void onError(Session session,Throwable throwable){
        System.out.println("发生错误。");
        throwable.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    private static synchronized void addOnlineCount() {
        Chat.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        Chat.onlineCount--;
    }
    private static synchronized int getOnlineCount() {
       return Chat.onlineCount;
    }

}
