package enter.ernter.entities;


import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String domain;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime updatedAt;
}
