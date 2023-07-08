package problems;

import datastructures.LinkedIntList;
// Checkstyle will complain that this is an unused import until you use it in your code.
import datastructures.LinkedIntList.ListNode;

import java.util.Iterator;


/**
 * See the spec on the website for example behavior.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not call any methods on the `LinkedIntList` objects.
 * - do not construct new `ListNode` objects for `reverse3` or `firstToLast`
 *      (though you may have as many `ListNode` variables as you like).
 * - do not construct any external data structures such as arrays, queues, lists, etc.
 * - do not mutate the `data` field of any node; instead, change the list only by modifying
 *      links between nodes.
 */

public class LinkedIntListProblems {

    /**
     * Reverses the 3 elements in the `LinkedIntList` (assume there are exactly 3 elements).
     */
    public static void reverse3(LinkedIntList list) {
        // save the tail
        ListNode newFront = list.front.next.next;
        // move the front to the end
        list.front.next.next = list.front;
        // attach new front
        newFront.next = list.front.next;
        // terminate the new tail
        list.front.next = null;
        list.front = newFront;

    }

    /**
     * Moves the first element of the input list to the back of the list.
     */
    public static void firstToLast(LinkedIntList list) {
        if (list.front != null && list.front.next != null) {
            ListNode tail = list.front;
            while (tail.next != null) {
                tail = tail.next;
            }
            tail.next = list.front;
            list.front = list.front.next;
            tail.next.next = null;

        }
    }

    /**
     * Returns a list consisting of the integers of a followed by the integers
     * of n. Does not modify items of A or B.
     */
    public static LinkedIntList concatenate(LinkedIntList a, LinkedIntList b) {
        LinkedIntList result = new LinkedIntList();
        // a dummy node to make sure we do not check for null for the first node
        // always have a "previous" node to be added to
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;
        Iterator<Integer> it = a.iterator();
        while (it.hasNext()) {
            tail.next = new ListNode(it.next());
            tail = tail.next;
        }
        it = b.iterator();
        while (it.hasNext()) {
            tail.next = new ListNode(it.next());
            tail = tail.next;
        }

        result.front = dummy.next;
        return result;
    }
}
