package com.ysg;

public class InternalNode {
    private Long index;
    private HashValue left;
    private HashValue right;
    private boolean isFrozen;

    public InternalNode(Long index, HashValue left, HashValue right, boolean isFrozen) {
        this.index = index;
        this.left = left;
        this.right = right;
        this.isFrozen = isFrozen;
    }

    public HashValue hash() {
        byte[] input = new byte[2 * HashValue.LENGTH];
        System.arraycopy(input, 0, left.value.shadowContens(), 0, HashValue.LENGTH);
        System.arraycopy(input, HashValue.LENGTH, right.value.shadowContens(), 0, HashValue.LENGTH);
        return new HashValue(HashValue.sha3Of(input).value);
    }

    public Long getIndex() {
        return index;
    }

    public HashValue getLeft() {
        return left;
    }

    public HashValue getRight() {
        return right;
    }

    public void setFrozen() {
        isFrozen = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        InternalNode o = (InternalNode) obj;
        return index == o.index && o.left.equals(left) && o.right.equals(right) && o.isFrozen == isFrozen;
    }
}
