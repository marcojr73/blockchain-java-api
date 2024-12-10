package com.blockchain.api.repository;

import com.blockchain.api.Entity.Block;
import com.blockchain.api.Entity.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<BlockEntity, Integer> {
    Optional<BlockEntity> findTopByOrderByIdDesc();
}