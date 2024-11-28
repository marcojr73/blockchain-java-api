package com.blockchain.api.Entity;

public class Main {
    public static void main(String[] args) {
        int difficulty = 5;

        Blockchain blockchain = new Blockchain(difficulty);

        System.out.println("Start mining...");
        blockchain.addBlock(new Block(
                blockchain.getLatestBlock().getId() + 1,
                "Gatos são legais",
                blockchain.getLatestBlock().getHash()
        ));

        blockchain.addBlock(new Block(
                blockchain.getLatestBlock().getId() + 1,
                "Café é uma bebida saborosa",
                blockchain.getLatestBlock().getHash()
        ));

        blockchain.addBlock(new Block(
                blockchain.getLatestBlock().getId() + 1,
                "O bitcoin é incrível",
                blockchain.getLatestBlock().getHash()
        ));

        System.out.println("Blockchain:");
        blockchain.displayBlockchain();
    }
}
