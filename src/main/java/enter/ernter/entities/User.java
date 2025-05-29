package enter.ernter.entities;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "userId", unique = true)
    private String userId;
    private String info;
    @ManyToOne
    @JoinColumn(name = "siteId", insertable = true, updatable = true)
    private Site site;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime updatedAt;
    private String status;
}
