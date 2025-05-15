package enter.ernter.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String status;
    private String privateKey;
    private MessageDTO message;
    private String userId;
}