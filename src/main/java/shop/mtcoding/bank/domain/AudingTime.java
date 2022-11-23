package shop.mtcoding.bank.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass // 자식이 얘를 상속할건데, 자식이 얘를 테이블에 칼럼으로 만들라는 의미, 공통으로 사용할 때 필요
@EntityListeners(AuditingEntityListener.class)
public abstract class AudingTime {

    @LastModifiedDate // insert, update 시에 현재시간 들어감
    @Column(nullable = false)
    protected LocalDateTime updatedAt;

    @CreatedDate // insert 시에 현재시간 들어감
    @Column(nullable = false)
    protected LocalDateTime createdAt;
}
