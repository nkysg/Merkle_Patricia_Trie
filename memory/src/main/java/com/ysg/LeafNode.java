package com.ysg;

public class LeafNode {
    private HashValue hash;
    private Long index;

    public LeafNode(HashValue hash, Long index) {
        this.hash = hash;
        this.index = index;
    }

    public HashValue getHash() {
        return hash;
    }

    public Long getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        LeafNode o = (LeafNode) obj;
        return hash.equals(o.hash) && index == o.index;
    }
}
