package com.CeyBazaar.backend.dto;

public class PaymentDTO {
    private String oid;
    private String hash;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
