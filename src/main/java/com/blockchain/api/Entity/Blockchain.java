package com.blockchain.api.Entity;

import com.blockchain.api.repository.BlockRepository;

import java.util.List;
import java.util.stream.Collectors;

public class Blockchain {
    private final BlockRepository blockRepository;
    private final int difficulty;

    public Blockchain(BlockRepository blockRepository, int difficulty) {
        this.blockRepository = blockRepository;
        this.difficulty = difficulty;

        if (blockRepository.count() == 0) {
            createGenesisBlock();
        }
    }

    private void createGenesisBlock() {
        addBlock(new Block(0, "Genesis Block", "0"));
    }

    public Block getLatestBlock() {
        return blockRepository.findTopByOrderByIdDesc()
                .map(Block::fromEntity)
                .orElseThrow(() -> new IllegalStateException("Nenhum bloco encontrado"));
    }

    public Block getBlockById(int id) {
        return blockRepository.findById(id)
                .map(Block::fromEntity)
                .orElse(null);
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockRepository.save(newBlock.toEntity());
    }

    public int invalidChain() {
        List<Block> chain = blockRepository.findAll()
                .stream()
                .map(Block::fromEntity)
                .collect(Collectors.toList());

        for (int i = 0; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);

            if (i > 0) {
                Block previousBlock = chain.get(i - 1);

                if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                    System.out.println("Previous block hash is invalid at block " + currentBlock.getId());
                    return i;
                }
            }

            if (!currentBlock.isValidHash(difficulty)) {
                System.out.println("Block hash is invalid at block " + currentBlock.getId());
                return i;
            }
        }

        return -1;
    }

    public List<Block> displayBlockchain() {
        return blockRepository.findAll()
                .stream()
                .map(Block::fromEntity)
                .collect(Collectors.toList());
    }
}