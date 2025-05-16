package enter.ernter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import enter.ernter.dto.NotificationRequest;
import enter.ernter.entities.Notification;
import enter.ernter.handler.CustomWebSocketHandler;
import enter.ernter.repositories.UserRepository;
import enter.ernter.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final CustomWebSocketHandler webSocketHandler;

    public NotificationController(CustomWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Autowired
    NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody NotificationRequest request) {
        try {
            notificationService.createNotificationFromPayload(request);

            // Gửi WebSocket message đến TẤT CẢ client
            webSocketHandler.broadcast("Notification mới từ userId=" + request.getUserId());

            return ResponseEntity.ok("Notification created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/get")
public ResponseEntity<?> getNotifications(
        @RequestParam String userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "2") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String direction) {

    if (!userRepository.existsByUserId(userId)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access Denied: userId " + userId + " không tồn tại.");
    }

    Sort sort = direction.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Notification> notifications = notificationService.getAllNotifications(userId, pageable);
    return ResponseEntity.ok(notifications);
}


}