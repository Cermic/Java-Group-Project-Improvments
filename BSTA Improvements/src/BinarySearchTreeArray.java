
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 *
 * @authors B00308929 && B00308927
 */
public class BinarySearchTreeArray<E> extends AbstractSet<E> {

    protected Entry<E>[] tree;
    int root, size;
    protected int modCount = 0;
    protected static final int NIL = -1;
    protected static final int DEFAULT_SIZE = 16;

    // The freeList is a queue of array positions that have been
    // emptied through the removal of an element and are now available
    // to store a new element as suggested in Richard Beeby's code.
    protected Queue<Integer> freeList = new LinkedList<>();

    protected static class Entry<E> {

        E element;
        protected int left = NIL, right = NIL, parent;

        /**
         * Initializes this Entry object.
         *
         * This default constructor is defined for the sake of subclasses of the
         * BinarySearchTreeArray class.
         */
        public Entry() {
            this(null, NIL);
        }

        /**
         * Initializes this Entry object from element and parent.
         */
        public Entry(E element, int parent) {
            this.element = element;
            this.parent = parent;
        }// constructor

        /**
         * For testing and debugging purposes.
         *
         * @return a String representation of the state of this Entry.
         */
        @Override
        public String toString() {
            return "Element=" + element + " parent=" + parent
                    + " left=" + left + " right=" + right;
        }
    }// class Entry

    /**
     * Initializes this BinarySearchTreeArray object to be empty, to contain
     * only elements of type E, to be ordered by the Comparable interface, and
     * to contain no duplicate elements.
     */
    public BinarySearchTreeArray() {
        this(DEFAULT_SIZE);
    }//default constructor

    /**
     * Initialises this BinarySearchTreeArray object to be empty, with a
     * specified initial capacity.
     *
     * @param capacity - the initial capacity of this BinarySearchTreeArray
     * object.
     *
     * @throws IllegalArgumentException - if capacity is non-positive
     */
    public BinarySearchTreeArray(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Non-positive capacity: "
                    + capacity);
        }
        root = NIL;
        size = 0;
        modCount = 0;
        tree = new Entry[capacity];
    }

    /**
     * Returns the size of this BinarySearchTreeArray object. worstTime(n) is
     * constant O(1).
     *
     * @return the size of this BinarySearchTreeArray object.
     *
     */
    @Override
    public int size() {
        return size;
    }

    // For testing purposes, displays the whole tree array contents and
    // the values of other fields of the class (it would be better
    // to have this return a String for the application to display, but
    // the project specification suggested a display method).
    protected void displayTreeArray() {
        System.out.println("\nLength = " + tree.length + ", size = " + size
                + ", listSize = " + freeList.size());
        System.out.println("List = " + freeList);
        System.out.println(Arrays.toString(tree));
        System.out.println("\n");
    }

    /**
     * Determines if there is an element in this BinarySearchTreeArray object
     * that equals a specified element. The worstTime(n) is O(n) and
     * averageTime(n) is O(log n).
     *
     * @param obj – the element sought in this BinarySearchTreeArray object.
     *
     * @return true – if there is an element in this BinarySearchTreeArray
     * object that equals obj; otherwise, return false.
     *
     * @throws ClassCastException – if obj is not null but cannot be compared to
     * the elements already in this BinarySearchTreeArray object.
     * @throws NullPointerException – if obj is null.
     */
    @Override
    public boolean contains(Object obj) {
        return getEntry(obj) != NIL; //To change body of generated methods, choose Tools | Templates.
    }

    protected boolean containsElement(Entry<E> e, Object obj) {
        int comp;

        if (e == null) {
            return false;
        }
        Entry temp = e;
        comp = ((Comparable) obj).compareTo(e.element);
        if (comp == 0) {
            return true;
        }
        if (temp.left == NIL) {
            return false;
        } else if (comp < 0) {
            return containsElement(tree[temp.left], obj);
        } else if (temp.right == NIL) {
            return false;
        } else {
            return containsElement(tree[temp.right], obj);
        }
        // method requires a return method
    }

    /**
     * Ensures that this BinarySearchTreeArray object contains a specified
     * element. The worstTime(n) is O(n) and averageTime(n) is O(log n).
     *
     * @param element – the element whose presence is ensured in this
     * BinarySearchTreeArray object.
     *
     * @return true – if this BinarySearchTreeArray object changed as a result
     * of this method call (that is, if element was actually inserted);
     * otherwise, return false.
     *
     * @throws ClassCastException – if element is not null but cannot be
     * compared to the elements of this BinarySearchTreeArray object.
     * @throws NullPointerException – if element is null.
     *
     */
    @Override
    public boolean add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (this.size == tree.length) {
            tree = Arrays.copyOf(tree, (tree.length * 2));
        }
        if (root == NIL) {
            root = 0;
            tree[root] = new Entry(element, NIL);
            size++;
            modCount++;
            return true;
        } else {
            int parent = root;
            int comp, nextSlot;
            Entry<E> temp = tree[root];
            if (freeList.size() > 0) {
                nextSlot = freeList.peek();
            } else {
                nextSlot = size;
            }
            boolean found = false;
            while (!found) {
                comp = ((Comparable) element).compareTo(temp.element);
                if (comp == 0) {
                    return false;
                }
                if (comp < 0) {
                    if (temp.left != NIL) {
                        parent = temp.left;
                        temp = tree[parent];
                    } else {
                        temp.left = nextSlot;
                        found = true;
                    }// temp.left ==NIL
                } else if (temp.right != NIL) {
                    parent = temp.right;
                    temp = tree[parent];
                } else {
                    temp.right = nextSlot;
                    found = true;
                }// temp.right == NIL
            }//while
            tree[nextSlot] = new Entry<>(element, parent);
            size++;
            freeList.poll();
            modCount++;
            return true;
        }
    }

    /**
     * Ensures that this BinarySearchTreeArray object does not contain a
     * specified element. The worstTime(n) is O(n) and averageTime(n) is O(log
     * n).
     *
     * @param obj – the object whose absence is ensured in this
     * BinarySearchTreeArray object.
     *
     * @return true – if this BinarySearchTreeArray object changed as a result
     * of this method call (that is, if obj was actually removed); otherwise,
     * return false.
     *
     * @throws ClassCastException – if obj is not null but cannot be compared to
     * the elements of this BinarySearchTreeArray object.
     * @throws NullPointerException – if obj is null.
     */
    public boolean remove(Object obj) {
        int e = getEntry(obj);
        if (e == NIL) {
            return false;
        }
        deleteEntry(e);
        modCount++;                                                             //Increments as an object has been removed from the tree
        return true;
    } // method remove

    /**
     * Finds the Entry object that houses a specified element, if there is such
     * an Entry. The worstTime(n) is O(n), and averageTime(n) is O(log n).
     *
     * @param obj - the element whose Entry is sought.
     *
     * @return the Entry object that houses obj - if there is such an Entry;
     * otherwise, return null.
     *
     * @throws ClassCastException - if obj is not comparable to the elements
     * already in this BinarySearchTreeArray object.
     * @throws NullPointerException - if obj is null.
     *
     */
    protected int getEntry(Object obj) {
        int comp, e = root;

        while (e != NIL) {
            comp = ((Comparable) obj).compareTo(tree[e].element);
            if (comp == 0) {
                return e;
            } else if (comp < 0) {
                e = tree[e].left;
            } else {
                e = tree[e].right;
            }
        } // while

        return NIL;
    } // method getEntry

    /**
     * Deletes the element in a specified Entry object from this
     * BinarySearchTreeArray.
     *
     * @param p – the Entry object whose element is to be deleted from this
     * BinarySearchTreeArray object.
     *
     * @return the Entry object that was actually deleted from this
     * BinarySearchTreeArray object.
     *
     */
    //this whole section needs to be revised for readability and to accomodate reordering the tree when an entry is deleted from the middle of the array
    protected Entry<E> deleteEntry(int pInput) {
        size--;
        Entry<E> p = tree[pInput];
        // If p has two children, replace p's element with p's successor's
        // element, then make p reference that successor.
        if (p.left != NIL && p.right != NIL) {
            pInput = successor(pInput);
            Entry<E> s = tree[pInput];
            p.element = s.element;
            p = s;
        } //p has two children

        // At this point, p has either no children or one child.
        int replacement;
        if (p.left != NIL) {
            replacement = p.left;
        } else {
            replacement = p.right;
        }

        // If p has at least one child, link replacement to p.parent.
        if (replacement != NIL) {
            tree[replacement].parent = p.parent;
            tree[pInput] = tree[replacement];
            if (tree[replacement].left != NIL) {
                tree[tree[replacement].left].parent = pInput;
            }
            if (tree[replacement].right != NIL) {
                tree[tree[replacement].right].parent = pInput;
            }
            tree[replacement] = null;
            freeList.add(replacement);
            return tree[replacement];
        }//p has at least one child
        else if (p.parent == NIL) {                                             //if tree[pInput] has no parent = tree[pInput] must be the root
            tree[root] = null;
            freeList.add(root);
            root = NIL;
        } else {
            Entry<E> parentP = tree[p.parent];
            freeList.add(pInput);
            tree[pInput] = null;
            if (parentP.left == pInput) {                                       //if tree[pInput] is the left child of its parent
                parentP.left = NIL;
            } else {
                parentP.right = NIL;
            }
        }//p has a parent but no children
        return tree[pInput];
    }//method deleteEntry

    /**
     * Finds the successor of a specified Entry object in this
     * BinarySearchTreeArray. The worstTime(n) is O(n) and averageTime(n) is
     * constant.
     *
     * @param e – the Entry object whose successor is to be found.
     *
     * @return the successor of e, if e has a successor; otherwise, return null.
     *
     */
    protected int successor(int e) {
        if (e == NIL) {
            return NIL;
        } else if (tree[e].right != NIL) {
            // successor is leftmost Entry in right subtree of e
            int p = tree[e].right;
            while (tree[p].left != NIL) {
                p = tree[p].left;
            }
            return p;
        } // e has a right child
        else {
            // go up the tree to the left as far as possible, then go up
            // to the right.
            int p = tree[e].parent;
            int ch = e;
            while (p != NIL && ch == tree[p].right) {
                ch = p;
                p = tree[p].parent;
            } // while
            return p;

        } // e has no right child
    } // method successor    }

    /**
     * Returns an iterator positioned at the smallest element in this
     * BinarySearchTreeArray object.
     *
     * @return an iterator positioned at the smallest element (according to the
     * element class’s implementation of the Comparable interface) in this
     * BinarySearchTreeArray object.
     */
    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator();
    }

    protected class ArrayIterator implements Iterator<E> {

        protected int next = NIL, lastReturned = NIL;
        protected final int modCountOnEntry;

        /**
         * Positions this ArrayIterator to the smallest element, according to
         * the Comparable interface, in the BinarySearchTreeArray object. The
         * worstTime(n) is O(n) and averageTime(n) is O(log n).
         *
         */
        protected ArrayIterator() {
            if (root != NIL) {
                next = root;
                while (tree[next].left != NIL) {
                    next = tree[next].left;
                }
            }
            modCountOnEntry = modCount;
        }

        /**
         * Determines if there are still some elements, in the
         * BinarySearchTreeArray object this ArrayIterator object is iterating
         * over, that have not been accessed by this ArrayIterator object.
         *
         * @return true – if there are still some elements that have not been
         * accessed by this ArrayIterator object; otherwise, return false.
         *
         */
        public boolean hasNext() {
            return next != NIL;
        }

        /**
         * Returns the element in the Entry this ArrayIterator object was
         * positioned at before this call, and advances this ArrayIterator
         * object. The worstTime(n) is O(n) and averageTime(n) is constant.
         *
         * @return the element this ArrayIterator object was positioned at
         * before this call.
         *
         * @throws NoSuchElementException – if this ArrayIterator object was not
         * positioned at an Entry before this call.
         *
         */
        public E next() {
            if (modCountOnEntry != modCount) {
                throw new ConcurrentModificationException();
            }
            if (next == NIL) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            next = successor(next);
            return (E) tree[lastReturned].element;
        }

        /**
         * Removes the element returned by the most recent call to this
         * ArrayIterator object’s next() method. The worstTime(n) is O(n) and
         * averageTime(n) is constant.
         *
         * @throws IllegalStateException – if this ArrayIterator’s next() method
         * was not called before this call, or if this ArrayIterator’s remove()
         * method was called between the call to the next() method and this
         * call.
         *
         */
        public void remove() {
            if (lastReturned == NIL) {
                throw new IllegalStateException();
            }
            if (modCountOnEntry != modCount) {
                throw new ConcurrentModificationException();
            }
            Entry deleted = deleteEntry(lastReturned);
            if (tree[next] == deleted) {
                next = lastReturned;
            }
            lastReturned = NIL;
        }
    }
}
