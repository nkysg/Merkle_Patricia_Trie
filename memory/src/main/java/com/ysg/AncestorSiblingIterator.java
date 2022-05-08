package com.ysg;

import java.util.Iterator;

public class AncestorSiblingIterator implements Iterator<NodeIndex> {
    private NodeIndex nodeIndex;

    public AncestorSiblingIterator(NodeIndex nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public NodeIndex next() {
        NodeIndex currentSiblingIndex = nodeIndex.sibling();
        nodeIndex = nodeIndex.parent();
        return currentSiblingIndex;
    }
}
