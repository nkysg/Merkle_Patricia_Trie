package com.ysg;

import java.lang.management.MemoryNotificationInfo;
import java.util.List;

public class MemoryAccumulator {
    List<HashValue> frozenSubtreeRoots;
    Long numLeaves;
    HashValue root_hash;

    public MemoryAccumulator(List<HashValue> frozenSubtreeRoots, Long numLeaves) {
        this.frozenSubtreeRoots = frozenSubtreeRoots;
    }

    public static MemoryAccumulator fromLeaves(List<HashValue> leaves) {
        // XXX FIXME
    }

    public int  getProofFromLeaves(List<HashValue> leaves, Long leafIndex, List<HashValue> siblings) {
        // XXX FIXME
        return 0;
    }

    private HashValue hashInternalNode(HashValue left, HashValue right) {
        return new MerkleTreeInternalNode(left, right).hash();
    }

    private HashValue computeRootHash(List<HashValue> frozenSubtreeRoots, Long numLeaves) {
        // XXX FIXME
        return HashValue.zero();
    }

    private HashValue getHash(List<HashValue> leaves, Long nodeIndex) {
        // XXX FIXME
    }

    public void append(List<HashValue> leaves) {

    }

    private void appendOne(List<HashValue> frozenSubtreeRoots, Long numExistingLeaves, HashValue leaf) {
        // XXX FIXME
    }

    public Long version() {
        if (numLeaves == 0) {
            return 0L;
        } else {
            return numLeaves - 1;
        }
    }

    public Long getNumLeaves() {
        return numLeaves;
    }

    public List<HashValue> getFrozenSubtreeRoots() {
        return frozenSubtreeRoots;
    }
}