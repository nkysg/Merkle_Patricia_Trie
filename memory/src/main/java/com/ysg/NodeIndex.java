package com.ysg;

/***
 * NodeIndex is in-order-traversel
 *
 * //! For example
 * //! ```text
 * //!      3
 * //!     /  \
 * //!    /    \
 * //!   1      5 <-[Node index, a.k.a, Position]
 * //!  / \    / \
 * //! 0   2  4   6
 * //!
 * //! 0   1  2   3 <[Leaf index]
 * //! ```
 * //! Note1: The in-order-traversal counts from 0
 * //! Note2: The level of tree counts from leaf level, start from 0
 * //! Note3: The leaf index starting from left-most leaf, starts from 0
 */
public final class NodeIndex {
    public final int MaxAccumulatorProofLength = 63;
    private final long inOrderIndex;

    public NodeIndex(long val) {
        this.inOrderIndex = val;
    }
    public long getInOrderIndex() {
        return inOrderIndex;
    }

    public boolean isFreezable(long leafIndex) {

        NodeIndex leaf = fromLeafIndex(leafIndex);
        NodeIndex rightMostChild = rightMostChild();
        return rightMostChild.inOrderIndex <= leaf.inOrderIndex;
    }

    public boolean isLeaf() {
        return (inOrderIndex & 1) == 0;
    }

    public long toLeafIndex() {
        int level = level();
        if (level != 0) {
            return -1;
        } else {
            return inOrderIndex >> 1;
        }
    }

    public long toInOrderIndex() {
        return inOrderIndex;
    }

    /// What level is this node in the tree, 0 if the node is a leaf,
    /// 1 if the level is one above a leaf, etc.
    public int level() {
        return Long.numberOfTrailingZeros(~inOrderIndex);
    }

    /// pos count start from 0 on each level
    public static NodeIndex fromLevelAndPos(int level, long pos) {
        assert level < 63;
        assert (1L << level) > 0;
        long levelOneBits = (1L << level) - 1;
        long shiftPos = pos << (level + 1L);
        return new NodeIndex(levelOneBits | shiftPos);
    }

    public static NodeIndex fromLeafIndex(long leafIndex) {
        return fromLevelAndPos(0, leafIndex);
    }

    public static NodeIndex fromInorderIndex(long inOrderIndex) {
        return new NodeIndex(inOrderIndex);
    }

    // Given a leaf index, calculate the position of a minimum root which contains this leaf
    /// This method calculates the index of the smallest root which contains this leaf.
    /// Observe that, the root position is composed by a "height" number of ones
    ///
    /// For example
    /// ```text
    ///     0010010(node)
    ///     0011111(smearing)
    ///     -------
    ///     0001111(root)
    /// ```
    public static NodeIndex rootFromLeafIndex(long leafIndex) {
        return new NodeIndex(smearOnes(leafIndex));
    }

    public static NodeIndex rootFromLeafCount(long leafCount) {
        assert leafCount > 0;
        return rootFromLeafIndex(leafCount - 1);
    }

    public int rootLevelFromLeafCount(long leafCount) {
        assert leafCount > 0;
        long index = leafCount - 1;
        return MaxAccumulatorProofLength + 1 - Long.numberOfLeadingZeros(index);
    }

    /// Given a node, find its left most child in its subtree
    /// Left most child is a node, could be itself, at level 0
    public NodeIndex leftMostChild() {
        int level = level();
        return new NodeIndex(turnOffRightMostNBits(inOrderIndex, level));
    }

    /// Given a node, find its right most child in its subtree.
    /// Right most child is a Position, could be itself, at level 0
    public NodeIndex rightMostChild() {
        int level = level();
        return new NodeIndex(inOrderIndex + (1L << level) - 1);
    }

    public boolean isPlaceholder(long leafIndex) {
        NodeIndex leaf = fromLeafIndex(leafIndex);
        if (getInOrderIndex() <= leaf.getInOrderIndex()) {
            return false;
        }
        if (leftMostChild().getInOrderIndex() <= leaf.getInOrderIndex()) {
            return false;
        }
        return true;
    }

    /// What is the parent of this node?
    public NodeIndex parent() {
        return new NodeIndex((inOrderIndex | isolateRightmostZeroBit(inOrderIndex)) & ~(isolateRightmostZeroBit(inOrderIndex) <<1));
    }


    /// Creates an `AncestorSiblingIterator` using this node_index.
    public AncestorSiblingIterator iterAncestorSibling() {
        return new AncestorSiblingIterator(this);
    }



    /// What is the left node of this node? Will overflow if the node is a leaf
    public NodeIndex leftChild() {
        return child(NodeDirection.Left);
    }

    /// What is the right node of this node? Will overflow if the node is a leaf
    public NodeIndex rightChild() {
        return child(NodeDirection.Right);
    }

    public NodeIndex child(NodeDirection dir) {

        long direction_bit;
        if (dir == NodeDirection.Left) {
            direction_bit = 0;
        } else {
            direction_bit = isolateRightmostZeroBit(inOrderIndex);
        }
        return new NodeIndex((inOrderIndex | direction_bit) & ~(isolateRightmostZeroBit(inOrderIndex) >> 1));
    }

    /// This method takes in a node position and return its sibling position
    ///
    /// The observation is that, after stripping out the right-most common bits,
    /// two sibling nodes flip the the next right-most bits with each other.
    /// To find out the right-most common bits, first remove all the right-most ones
    /// because they are corresponding to level's indicator. Then remove next zero right after.
    public NodeIndex sibling() {
        return new NodeIndex(inOrderIndex ^ (isolateRightmostZeroBit(inOrderIndex) << 1));
    }

    /// Whether this node_index is a left child of its parent.  The observation is that,
    /// after stripping out all right-most 1 bits, a left child will have a bit pattern
    /// of xxx00(11..), while a right child will be represented by xxx10(11..)
    public boolean isLeftChild() {
       return (inOrderIndex & (isolateRightmostZeroBit(inOrderIndex) << 1)) == 0;
    }

    public boolean isRightChild() {
      return !isLeftChild();
    }

    /// Smearing all the bits starting from MSB with ones
    public static long smearOnes(long n) {
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        n |= n >> 32;
        return n;
    }


    /// Finds the rightmost 0-bit, turns off all bits, and sets this bit to 1 in
    public static long isolateRightmostZeroBit(long v) {
       return  ~v & (v + 1);
    }

    /// Turn off n right most bits
    public static long turnOffRightMostNBits(long v, int n) {
        assert n < 64;
      return  (v >> n) << n;
    }
}
