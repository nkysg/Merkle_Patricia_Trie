package com.ysg;

import java.util.Iterator;

public class FrozenSubtreeSiblingIterator implements Iterator<FrozenSubtreeSiblingIterator> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public FrozenSubtreeSiblingIterator next() {
        return null;
    }
}
