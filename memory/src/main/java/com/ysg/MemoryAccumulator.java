package com.ysg;

import java.util.List;

public class MemoryAccumulator {
    List<HashValue> frozenSubtreeRoots;
    long numLeaves;
    HashValue rootHash;

    public MemoryAccumulator(List<HashValue> frozenSubtreeRoots, long numLeaves) {
        this.frozenSubtreeRoots = frozenSubtreeRoots;
    }

    public static MemoryAccumulator fromLeaves(List<HashValue> leaves) {
        // XXX FIXME
        return new MemoryAccumulator(null, 0);
    }

    public ErrorInfo getProofFromLeaves(List<HashValue> leaves, long leafIndex, List<HashValue> siblings) {
        // XXX FIXME
        return ErrorInfo.OK;
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
        return HashValue.zero();
    }

    public ErrorInfo append(List<HashValue> leaves) {
        // XXX FIXME
        return ErrorInfo.OK;
    }

    private ErrorInfo appendOne(List<HashValue> frozenSubtreeRoots, Long numExistingLeaves, HashValue leaf) {
        // XXX FIXME
        return ErrorInfo.OK;
    }



    public ErrorInfo appendSubtrees(List<HashValue> subtrees, Long numNewLeaves) {
        // XXX FIXME
        return ErrorInfo.OK;
    }

    public HashValue getRootHash() {
        return rootHash;
    }

    public Long version() {
        if (numLeaves == 0) {
            return 0L;
        } else {
            return numLeaves - 1;
        }
    }



    public List<HashValue> getFrozenSubtreeRoots() {
        return frozenSubtreeRoots;
    }

    public long getNumLeaves() {
        return numLeaves;
    }
}