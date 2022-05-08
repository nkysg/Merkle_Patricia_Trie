package com.ysg;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MemoryAccumulator {
    List<HashValue> frozenSubtreeRoots;
    long numLeaves;
    HashValue rootHash;

    public MemoryAccumulator(List<HashValue> frozenSubtreeRoots, long numLeaves) {
        assert frozenSubtreeRoots.size() == Long.bitCount(numLeaves);
        this.frozenSubtreeRoots = frozenSubtreeRoots;
        this.numLeaves = numLeaves;
        this.rootHash = computeRootHash(frozenSubtreeRoots, numLeaves);
    }

    public static MemoryAccumulator fromLeaves(List<HashValue> leaves) {
        MemoryAccumulator accumulator = new MemoryAccumulator(new ArrayList<>(), 0);
        accumulator.append(leaves);
        return accumulator;
    }

    public ErrorInfo getProofFromLeaves(List<HashValue> leaves, long leafIndex, AccumulatorProof proof) {
        long leafCount = leaves.size();
        NodeIndex rootPos = NodeIndex.rootFromLeafCount(leafCount);
        List<HashValue> siblings = new ArrayList<>(rootPos.level());
        AncestorSiblingIterator iter = NodeIndex.fromLeafIndex(leafIndex).iterAncestorSibling();
        for (int i = 0; i < rootPos.level(); i++) {
            NodeIndex nodeIndex = iter.next();
            siblings.add(getHash(leaves, nodeIndex));
        }
        proof.setSiblings(siblings);
        return ErrorInfo.OK;
    }

    private HashValue hashInternalNode(HashValue left, HashValue right) {
        return new MerkleTreeInternalNode(left, right).hash();
    }

    private HashValue getHash(List<HashValue> leaves, NodeIndex nodeIndex) {
       long rightmostLeafIndex = leaves.size() - 1;
       if (nodeIndex.isPlaceholder(rightmostLeafIndex)) {
           return HashValue.placeholder();
       } else if (nodeIndex.isFreezable(rightmostLeafIndex)) {
           long leafIndex = nodeIndex.toLeafIndex();
           if (leafIndex > 0) {
               assert leafIndex < leaves.size();
               return leaves.get((int)leafIndex);
           } else {
                return  hashInternalNode(getHash(leaves, nodeIndex.leftChild()),
                       getHash(leaves, nodeIndex.rightChild()));
           }
       } else {
           return hashInternalNode(getHash(leaves, nodeIndex.leftChild()), getHash(leaves, nodeIndex.rightChild()));
       }
    }

    public void append(List<HashValue> leaves) {
        List<HashValue> frozenSubtree = new ArrayList<>(this.frozenSubtreeRoots);
        long numLeaves = this.numLeaves;
        for (HashValue leaf : leaves) {
            appendOne(frozenSubtree, numLeaves, leaf);
            numLeaves += 1;
        }
        this.frozenSubtreeRoots = frozenSubtree;
        this.numLeaves = numLeaves;
    }

    private void appendOne(List<HashValue> frozenSubtreeRoots, long numExistingLeaves, HashValue leaf) {
        frozenSubtreeRoots.add(leaf);
        int numTrailingOnes = Long.numberOfTrailingZeros(~numExistingLeaves);
        for (int i = 0; i < numTrailingOnes; i++) {
            HashValue rightHash = frozenSubtreeRoots.remove(frozenSubtreeRoots.size() - 1);
            HashValue leftHash = frozenSubtreeRoots.remove(frozenSubtreeRoots.size() - 1);
            HashValue parentHash = new MerkleTreeInternalNode(leftHash, rightHash).hash();
            frozenSubtreeRoots.add(parentHash);
        }
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

    private HashValue computeRootHash(List<HashValue> frozenSubtreeRoots, Long numLeaves) {
        if (frozenSubtreeRoots.size() == 0) {
            return HashValue.placeholder();
        } else if (frozenSubtreeRoots.size() == 1) {
            return frozenSubtreeRoots.get(0);
        }
        long bitmap = Long.numberOfTrailingZeros(numLeaves);
        HashValue currentHash = HashValue.placeholder();
        ListIterator<HashValue> frozenSubtreeIter = frozenSubtreeRoots.listIterator(frozenSubtreeRoots.size());
        while (bitmap > 0) {
            if ((bitmap & 1) != 0) {
                currentHash = new MerkleTreeInternalNode(frozenSubtreeIter.next(), currentHash).hash();
            } else {
                currentHash = new MerkleTreeInternalNode(currentHash, HashValue.placeholder()).hash();
            }
            bitmap >>= 1;
        }
        return currentHash;
    }

    public List<HashValue> getFrozenSubtreeRoots() {
        return frozenSubtreeRoots;
    }

    public long getNumLeaves() {
        return numLeaves;
    }
}