package enter.ernter.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import enter.ernter.service.NotificationService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private final NotificationService notificationService;
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    public CustomWebSocketHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Chờ user gửi userId và siteId
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(message.getPayload());

        String userId = node.get("userId").asText();
        int siteId = node.get("siteId").asInt();

        // Gọi service để xử lý logic validate + lưu DB
        notificationService.handleUserConnection(userId, siteId, session);

        // Nếu đến đây thì user hợp lệ, lưu session để gửi tin nhắn sau này
        userSessions.put(userId, session);
    }

    public void sendMessageToUser(String userId, String message) throws IOException {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    public void broadcast(String message) {
        for (WebSocketSession session : userSessions.values()) {
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
}
