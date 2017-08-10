package util;


import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 * Created by nsh on 2017/8/10 0010.
 */
public class GetHttpSessionConfigurator extends Configurator {

    public void modifyHandshake(ServerEndpointConfig sec,HandshakeRequest request,HandshakeResponse response ){

        HttpSession httpSession = (HttpSession) request.getHttpSession();

        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
