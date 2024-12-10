package com.blockchain.api.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "blocks")
public class BlockEntity {

    @Id
    private Integer id;

    private int nonce;
    private String data;
    private String hash;
    private String previousHash;

    public BlockEntity() {
    }

    public BlockEntity(Integer id, int nonce, String data, String hash, String previousHash) {
        this.id = id;
        this.nonce = nonce;
        this.data = data;
        this.hash = hash;
        this.previousHash = previousHash;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
}