package com.ysg;

import java.util.Iterator;

public class FrozenSubtreeSiblingIterator implements Iterator<NodeIndex> {
    private long currentNumLeaves;
    private long remainingNewLeaves;

    public FrozenSubtreeSiblingIterator(long currentNumLeaves, long newNumLeaves) {
        this.currentNumLeaves = currentNumLeaves;
        this.remainingNewLeaves = newNumLeaves - currentNumLeaves;
    }

    private long nextNewLeafBatch() {
        int zeros = Long.numberOfLeadingZeros(remainingNewLeaves);
        return 1L << (63 - zeros);
    }

    @Override
    public boolean hasNext() {
        return remainingNewLeaves != 0;
    }

    @Override
    public NodeIndex next() {
        long nextSubtreeLeaves;
        if (currentNumLeaves > 0) {
           long rightmostFrozenSubtreeLeaves = 1L << Long.numberOfTrailingZeros(currentNumLeaves);
           if (remainingNewLeaves >= rightmostFrozenSubtreeLeaves) {
               nextSubtreeLeaves = rightmostFrozenSubtreeLeaves;
           } else {
               nextSubtreeLeaves = nextNewLeafBatch();
           }
        } else {
            nextSubtreeLeaves = nextNewLeafBatch();
        }
        long firstLeafIndex = currentNumLeaves;
        long lastLeafIndex = firstLeafIndex + nextSubtreeLeaves - 1;
        currentNumLeaves += nextSubtreeLeaves;
        remainingNewLeaves -= nextSubtreeLeaves;
        return NodeIndex.fromInorderIndex(firstLeafIndex + lastLeafIndex);
    }
}
