package com.blockchain.api.Entity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Block {
    private final int id;
    private int nonce;
    private String data;
    private String hash;
    private String previousHash;

    public Block(int id, String data, String previousHash) {
        this.id = id;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String input = id + previousHash + Integer.toString(nonce) + data;
        return applySHA256(input);
    }

    private String applySHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block generated! " + hash);
    }

    public boolean isValidHash(int difficulty) {
        String calculatedHash = calculateHash();
        String target = new String(new char[difficulty]).replace('\0', '0');
        System.out.println(difficulty);
        return hash.equals(calculatedHash) && hash.substring(0, difficulty).equals(target);
    }

    public int getId() {
        return id;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHash() {
        this.hash = this.calculateHash();
    }

    public void setPreviousHash(String hash) {
        this.previousHash = hash;
    }

    public int getNonce() {
        return nonce;
    }

    public String getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    @Override
    public String toString() {
        return "Block{" + "id=" + id + ", nonce=" + nonce + ", data='" + data + '\'' + ", hash='" + hash + '\'' + ", previousHash='" + previousHash + '\'' + '}';
    }
}
