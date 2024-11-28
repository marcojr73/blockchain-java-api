package com.blockchain.api.Entity;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private final List<Block> chain;
    private final int difficulty;

    public Blockchain(int difficulty) {
        this.chain = new ArrayList<>();
        this.difficulty = difficulty;
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        this.addBlock(new Block(0, "Genesis Block", "0"));
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public Block getBlockById(int id) {
        for (Block block : chain) {
            if (block.getId() == id) {
                return block;
            }
        }
        return null;
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
    }

    public int invalidChain() {
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
        return chain;
    }
}
