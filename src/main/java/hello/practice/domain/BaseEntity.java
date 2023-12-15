package hello.practice.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    private LocalDateTime createdDate;      //등록일
    private LocalDateTime lastModifiedDate; //수정일
}
