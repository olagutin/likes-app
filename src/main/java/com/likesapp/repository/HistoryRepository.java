package com.likesapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.likesapp.entities.HistoryEntity;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {
}
