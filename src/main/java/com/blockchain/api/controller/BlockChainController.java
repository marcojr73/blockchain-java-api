package com.blockchain.api.controller;

import com.blockchain.api.Entity.Block;
import com.blockchain.api.Entity.Blockchain;
import com.blockchain.api.request.BlockRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class BlockChainController {

    private final int difficulty = 5;
    Blockchain blockchain = new Blockchain(difficulty);

    @DeleteMapping("blockchain")
    public ResponseEntity<Blockchain> deleteBlockChain() {
        return ResponseEntity.ok(blockchain = new Blockchain(difficulty));
    }

    @GetMapping("blockchain")
    public ResponseEntity<List<Block>> getBlockChain() {
        return ResponseEntity.ok(blockchain.displayBlockchain());
    }

    @PostMapping("blockchain")
    public ResponseEntity<Integer> addBlockIntoChain(@RequestBody String data) {
        Block lastBlock = blockchain.getLatestBlock();
        blockchain.addBlock(
                new Block(
                        lastBlock.getId() + 1,
                        data,
                        lastBlock.getHash()
                )
        );
        return ResponseEntity.status(201).body(blockchain.getLatestBlock().getId() + 1);
    }

    @PutMapping("blockchain")
    public ResponseEntity<Block> calculateHash(@RequestBody BlockRequest blockRequest) {
        Block block = blockchain.getBlockById(blockRequest.getId());
        block.setNonce(blockRequest.getNonce());
        block.setData(blockRequest.getData());
        block.setHash();
        System.out.println("updated");
        return ResponseEntity.status(200).body(block);
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
        return ResponseEntity.status(200).body(block);
    }

    @GetMapping("blockchain/validate")
    public ResponseEntity<Integer> isChainValid() {
        return ResponseEntity.status(200).body(blockchain.invalidChain());
    }
}
