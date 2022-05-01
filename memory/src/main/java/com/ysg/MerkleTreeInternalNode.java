package com.ysg;

public class MerkleTreeInternalNode {
    private HashValue left;
    private HashValue right;

    public MerkleTreeInternalNode(HashValue left, HashValue right) {
        this.left = left;
        this.right = right;
    }

    public HashValue hash() {
        byte[] input = new byte[2 * HashValue.LENGTH];
        System.arraycopy(input, 0, left.value.shadowContens(), 0, HashValue.LENGTH);
        System.arraycopy(input, HashValue.LENGTH, right.value.shadowContens(), 0, HashValue.LENGTH);
        return new HashValue(HashValue.sha3Of(input).value);
    }
}
