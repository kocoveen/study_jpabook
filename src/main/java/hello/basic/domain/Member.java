package hello.basic.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "MEMBER")
public class Member {

    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String username;

    // 매핑 정보 없는 필드
    private Integer age;

    // 시간 필드
    private LocalDateTime signupDate;
}
