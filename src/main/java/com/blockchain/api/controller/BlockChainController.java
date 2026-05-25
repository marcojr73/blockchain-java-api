package com.blockchain.api.controller;

import com.blockchain.api.Entity.Block;
import com.blockchain.api.Entity.Blockchain;
import com.blockchain.api.Entity.Visitor;
import com.blockchain.api.repository.BlockRepository;
import com.blockchain.api.repository.VisitorRepository;
import com.blockchain.api.request.BlockRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/api/")
public class BlockChainController {

    private final int difficulty = 4;
    private final Blockchain blockchain;
    private final VisitorRepository visitorRepository;
    private final BlockRepository blockRepository;
    private final Lock lock = new ReentrantLock();

    public BlockChainController(
            VisitorRepository visitorRepository,
            BlockRepository blockRepository
    ) {
        this.visitorRepository = visitorRepository;
        this.blockRepository = blockRepository;
        this.blockchain = new Blockchain(blockRepository, difficulty);
    }

    @DeleteMapping("blockchain")
    public ResponseEntity<String> deleteBlockChain() {
        blockRepository.deleteAll();
        blockchain.addBlock(new Block(0, "Genesis Block", "0"));
        return ResponseEntity.ok("Blockchain apagada com sucesso e bloco gênesis recriado.");
    }

    @GetMapping("blockchain")
    public ResponseEntity<List<Block>> getBlockChain() {
        return ResponseEntity.ok(blockchain.displayBlockchain());
    }

    @PostMapping("blockchain")
    public ResponseEntity<Integer> addBlockIntoChain(@RequestBody String data) {
        lock.lock();

        try {
            Block lastBlock = blockchain.getLatestBlock();

            Block newBlock = new Block(
                    lastBlock.getId() + 1,
                    data,
                    lastBlock.getHash()
            );

            blockchain.addBlock(newBlock);

            return ResponseEntity.status(201)
                    .body(blockchain.getLatestBlock().getId());

        } finally {
            lock.unlock();
        }
    }

    @PutMapping("blockchain")
    public ResponseEntity<Block> calculateHash(@RequestBody BlockRequest blockRequest) {
        Block block = blockchain.getBlockById(blockRequest.getId());

        if (block == null) {
            return ResponseEntity.notFound().build();
        }

        block.setNonce(blockRequest.getNonce());
        block.setData(blockRequest.getData());
        block.setHash();

        blockRepository.save(block.toEntity());

        return ResponseEntity.ok(block);
    }

    @PutMapping("blockchain/mine")
    public ResponseEntity<Block> mine(@RequestBody BlockRequest blockRequest) {
        Block block = blockchain.getBlockById(blockRequest.getId());
        String previousHash = "0";
        if (block.getId() > 0) {
            previousHash = blockchain.getBlockById(block.getId() - 1).getHash();
        }
        block.setData(blockRequest.getData());
        block.setPreviousHash(previousHash);
        block.setHash();
        block.mineBlock(difficulty);
        blockRepository.save(block.toEntity());
        return ResponseEntity.status(200).body(block);
    }

    @GetMapping("blockchain/validate")
    public ResponseEntity<Integer> isChainValid() {
        return ResponseEntity.status(200).body(blockchain.isValidChain());
    }

    @GetMapping("blockchain/visitors")
    public ResponseEntity<Long> countVisitors() {

        Visitor visitor;

        if (visitorRepository.count() == 0) {
            visitor = new Visitor(0L);
        } else {
            visitor = visitorRepository.findById(1L).orElse(new Visitor(0L));
        }

        visitor.setTotalVisitors(visitor.getTotalVisitors() + 1);

        visitorRepository.save(visitor);

        return ResponseEntity.ok(visitor.getTotalVisitors());
    }
}