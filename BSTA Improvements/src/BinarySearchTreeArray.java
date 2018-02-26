
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 *
 * @authors B00308929 && B00308927
 */
public class BinarySearchTreeArray<E> extends AbstractSet<E> {

    private static final int DEFAULT_SIZE = 16;
    Entry<E>[] tree;
    int root, size;
    protected int modCount = 0;

    protected class Entry<E> {

        protected E element;
        protected int left, right, parent;

        /**
         * Initializes this Entry object.
         *
         * This default constructor is defined for the sake of subclasses of the
         * BinarySearchTreeArray class.
         */
        public Entry() {
        }

        /**
         * Initializes this Entry object from element and parent.
         */
        public Entry(E element, int parent) {
            this.element = element;
            this.parent = parent;
            this.left = -1;
            this.right = -1;
        }// constructor
    }// class Entry

    /**
     * Initializes this BinarySearchTreeArray object to be empty, to contain
     * only elements of type E, to be ordered by the Comparable interface, and
     * to contain no duplicate elements.
     */
    public BinarySearchTreeArray() {
        tree = new Entry[DEFAULT_SIZE];
        size = 0;
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
        if (capacity <= 0) {
            throw new IllegalArgumentException("Non-positive capacity: "
                    + capacity);
        }
        tree = new Entry[capacity];
        size = 0;
        //modCount = 0;
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
        if(obj == null)
        {
            throw new NullPointerException();
        }
         if(!(obj instanceof String)) // Checks to see whether the object is not a String type, if so, throws ClassCastException.
        {
            throw new ClassCastException();
        }
        if (tree[root] == null) {
            return false;
        }
        return (containsElement(tree[root], obj)); //To change body of generated methods, choose Tools | Templates.
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
        if (temp.left == -1) {
            return false;
        } else if (comp < 0) {
            return containsElement(tree[temp.left], obj);
        } else if (temp.right == -1) {
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
        int parent = 0;
        if (element == null) {
            throw new NullPointerException();
        }
        if(!(element instanceof String)) // Checks to see whether the object is not a String type, if so, throws ClassCastException.
        {
            throw new ClassCastException();
        }
        if (this.size == tree.length) {
            tree = Arrays.copyOf(tree, (tree.length * 2));
        }
        if (tree[root] == null) {
            tree[root] = new Entry(element, -1);
            size++;
            //modCount++;
            return true;
        } else {
            int i = 0, comp;
            Entry temp = tree[root];
            while (true) {
                comp = ((Comparable) element).compareTo(temp.element);
                if (comp == 0) {
                    return false;
                }
                if (comp < 0) {
                    if (temp.left != -1) {
                        parent = temp.left;
                        temp = tree[temp.left];
                    } else {
                        tree[size] = new Entry(element, parent);
                        tree[parent].left = size;
                        size++;
                        //modCount++;
                        return true;
                    }
                } else if (temp.right != -1) {
                    parent = temp.right;
                    temp = tree[temp.right];
                } else {
                    tree[size] = new Entry(element, parent);
                    tree[parent].right = size;
                    size++;
                    //modCount++;
                    return true;
                }
            }
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
        if (e == -1) {
            return false;
        }
        if(!(obj instanceof String)) // Checks to see whether the object is not a String type, if so, throws ClassCastException.
        {
            throw new ClassCastException();
        }
        deleteEntry(e);
        //modCount++;                                                             //Increments as an object has been removed from the tree
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
        int comp;

        if (obj == null) {
            throw new NullPointerException();
        }
        if(!(obj instanceof String)) // Checks to see whether the object is not a String type, if so, throws ClassCastException.
        {
            throw new ClassCastException();
        }
        int e = root;
        while (e != -1) {
            comp = ((Comparable) obj).compareTo(tree[e].element);
            if (comp == 0) {
                return e;
            } else if (comp < 0) {
                e = tree[e].left;
            } else {
                e = tree[e].right;
            }
        } // while
        return -1;
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
    protected Entry<E> deleteEntry(int p) {

        // If p has two children, replace p's element with p's successor's
        // element, then make p reference that successor.
        if (tree[p].left != -1 && tree[p].right != -1) {
            int s = successor(p);
            tree[p].element = tree[s].element;
            p = s;
        }
        // At this point, p has either no children or one child.
        int replacement = -1;
        if (tree[p].left != -1) {
            replacement = tree[p].left;
        } else if (tree[p].right != -1) {
            replacement = tree[p].right;
        }
        // If p has at least one child, link replacement to p.parent.
        if (replacement != -1) {
            tree[replacement].parent = tree[p].parent;
            if (tree[p].parent == -1) {
                root = replacement;
            } else if (p == tree[tree[p].parent].left) {
                if (tree[p].left != -1) {
                    tree[tree[p].parent].left = tree[p].left;
                } else {
                    tree[tree[p].parent].left = tree[p].right;
                }
            } else if (tree[p].left != -1) {
                tree[tree[p].parent].left = tree[p].left;
            } else {
                tree[tree[p].parent].right = tree[p].right;
            }
        } // p has a parent but no children  
        else if (tree[p].parent == -1) {
            tree[root].element = null;
        } else if (p == tree[tree[p].parent].left) {
            tree[tree[p].parent].left = -1;
        } else {
            tree[tree[p].parent].right = -1;
        }
        tree[p].element = null;
        size--;
        return tree[p];
    }

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
        if (e == -1) {
            return -1;
        } else if (tree[e].right != -1) {
            // successor is leftmost Entry in right subtree of e
            int p = tree[e].right;
            while (tree[p].left != -1) {
                p = tree[p].left;
            }
            return p;

        } // e has a right child
        else {

            // go up the tree to the left as far as possible, then go up
            // to the right.
            int p = tree[e].parent;
            int ch = e;
            while (p != -1 && ch == tree[p].right) {
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

        protected int next, lastReturned = -1;

        /**
         * Positions this ArrayIterator to the smallest element, according to
         * the Comparable interface, in the BinarySearchTreeArray object. The
         * worstTime(n) is O(n) and averageTime(n) is O(log n).
         *
         */
        protected ArrayIterator() {
            next = root;
            if (next != -1) {
                while (tree[next].left != -1) {
                    next = tree[next].left;
                }
            }
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
            return next != -1;
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
             if (next == -1) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            next = successor(next);
            return tree[lastReturned].element;
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
            if (lastReturned == -1) {
                throw new IllegalStateException();
            }
            if (tree[lastReturned].left != -1 && tree[lastReturned].right != -1) {

            }
            deleteEntry(lastReturned);
            lastReturned = -1;
            //modCount++;
            //expectedModCount++;
        }
    }
}
