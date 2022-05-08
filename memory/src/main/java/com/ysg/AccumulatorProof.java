package com.ysg;
import java.util.List;

public class AccumulatorProof {
    private List<HashValue> siblings;

    public AccumulatorProof() {

    }
    public AccumulatorProof(List<HashValue> values) {
        siblings = values;
    }

    public List<HashValue> getSiblings() {
        return siblings;
    }

    public ErrorInfo verify(HashValue expectedRootHash, HashValue elementHash, long elementIndex) {
        // XXX FIXME
        return ErrorInfo.OK;
    }
    public void setSiblings(List<HashValue> values) {
        assert siblings == null;
        siblings = values;
    }
}
