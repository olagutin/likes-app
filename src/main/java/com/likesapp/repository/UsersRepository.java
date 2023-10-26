package com.likesapp.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import com.likesapp.entities.UserEntity;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserEntity> findByNickName(String nickName);

}
