package problems;

import datastructures.IntTree;
// Checkstyle will complain that this is an unused import until you use it in your code.
import datastructures.IntTree.IntTreeNode;

/**
 * See the spec on the website for tips and example behavior.
 *
 * Also note: you may want to use private helper methods to help you solve these problems.
 * YOU MUST MAKE THE PRIVATE HELPER METHODS STATIC, or else your code will not compile.
 * This happens for reasons that aren't the focus of this assignment and are mostly skimmed over in
 * 142 and 143. If you want to know more, you can ask on the discussion board or at office hours.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not call any methods on the `IntTree` objects
 * - do not construct new `IntTreeNode` objects (though you may have as many `IntTreeNode` variables
 *      as you like).
 * - do not construct any external data structures such as arrays, queues, lists, etc.
 * - do not mutate the `data` field of any node; instead, change the tree only by modifying
 *      links between nodes.
 */

public class IntTreeProblems {

    /**
     * Computes and returns the sum of the values multiplied by their depths in the given tree.
     * (The root node is treated as having depth 1.)
     */
    private static int depthSumHelper(IntTreeNode subRoot, int weight) {
        if (subRoot == null) {
            return 0;
        }
        return (
            subRoot.data * weight
                + depthSumHelper(subRoot.left, weight + 1)
                + depthSumHelper(subRoot.right, weight + 1)
        );

    }
    public static int depthSum(IntTree tree) {
        return depthSumHelper(tree.overallRoot, 1);
    }

    /**
     * Removes all leaf nodes from the given tree.
     */

    private static IntTreeNode removeLeavesHelper(IntTreeNode subRoot) {
        if (subRoot == null || (subRoot.left == null && subRoot.right == null)) {
            // a null of a leaf, remove
            return null;
        }
        // proceed to the left and right
        subRoot.left = removeLeavesHelper(subRoot.left);
        subRoot.right = removeLeavesHelper(subRoot.right);
        return subRoot;
    }
    public static void removeLeaves(IntTree tree) {
        tree.overallRoot = removeLeavesHelper(tree.overallRoot);
    }

    /**
     * Removes from the given BST all values less than `min` or greater than `max`.
     * (The resulting tree is still a BST.)
     */

    private static IntTreeNode trimHelper(IntTreeNode subRoot, int min, int max) {
        if (subRoot == null) {
            return  null;
        }
        if (subRoot.data < min) {
            // nothing here or on the left, may be something still on the right
            return trimHelper(subRoot.right, min, max);
        }
        if (subRoot.data > max) {
            // nothing here or on the right
            return trimHelper(subRoot.left, min, max);
        }
        // the current node is valid, just check its left and right
        subRoot.left = trimHelper(subRoot.left, min, max);
        subRoot.right = trimHelper(subRoot.right, min, max);
        return subRoot;
    }
    public static void trim(IntTree tree, int min, int max) {
        tree.overallRoot = trimHelper(tree.overallRoot, min, max);
    }
}
