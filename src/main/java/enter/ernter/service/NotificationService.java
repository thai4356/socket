package enter.ernter.service;

import enter.ernter.dto.NotificationRequest;
import enter.ernter.entities.Notification;
import enter.ernter.entities.NotificationMessage;
import enter.ernter.entities.User;
import enter.ernter.repositories.NotificationRepository;
import enter.ernter.repositories.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public NotificationService(UserRepository userRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public void handleUserConnection(String userId, int siteId, WebSocketSession session) throws IOException {
        Optional<User> userOpt = userRepository.findByUserIdAndSiteId(userId, siteId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            NotificationMessage msg = new NotificationMessage(
                    "Thông báo mới",
                    "người dùng đang online");
            Notification notification = new Notification();
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(msg);

            notification.setUser(user);
            notification.setMessage(jsonString);
            notification.setStatus("UNREAD");
            notification.setCreatedAt(LocalDateTime.now());
            notification.setUpdatedAt(LocalDateTime.now());

            notificationRepository.save(notification);
   
        } else {
   
            session.close(); //  Ngắt kết nối socket
        }
    }

    public void createNotificationFromPayload(NotificationRequest request) throws Exception {
        Optional<User> userOpt = userRepository.findByUserId(request.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID " + request.getUserId());
        }

        User user = userOpt.get();

        // Chuyển message object thành JSON string
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(request.getMessage());

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(jsonString);
        notification.setStatus(request.getStatus());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());

        // Tạo private key RSA
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            notification.setPrivateKey(privateKeyBase64);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating private key");
        }

        notificationRepository.save(notification);
    }

    public Page<Notification> getAllNotifications(int userId,Pageable pageable) {
         return notificationRepository.findAllByUserId(userId, pageable);
    }

}
