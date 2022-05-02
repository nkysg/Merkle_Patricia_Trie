package com.ysg;

import java.util.Iterator;

public class AncestorSiblingIterator implements Iterator<AncestorSiblingIterator> {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public AncestorSiblingIterator next() {
        return null;
    }
}
