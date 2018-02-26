
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author s B00308929 && B00308927
 */
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class TreeTest {

    public static void main(String[] args) {

        BinarySearchTree<String> testTree = new BinarySearchTree<>();
        BinarySearchTreeArray<String> testTreeArray = new BinarySearchTreeArray<>(4);

        testTree.add("dog");
        testTree.add("turtle");
        testTree.add("cat");
        testTree.add("bird");
        testTree.add("ferret");
        testTree.add("deer");
        testTree.add("zebra");
        testTree.add("frog");
        System.out.println(testTree.size());
        System.out.println(testTree.contains("ferret"));

        System.out.println(testTree.remove("cat"));
        System.out.println(testTree.remove("dog"));
        System.out.println(testTree.remove("turtle"));

        
        
        testTreeArray.add("dog");
        testTreeArray.add("turtle");
        testTreeArray.add("cat");
        testTreeArray.add("bird");
        testTreeArray.add("ferret");
        testTreeArray.add("deer");
        testTreeArray.add("zebra");
        testTreeArray.add("frog");
        
        System.out.println(testTreeArray.size());
        System.out.println(testTreeArray.contains("ferret"));

        System.out.println(testTreeArray.remove("cat"));
        System.out.println(testTreeArray.remove("dog"));
        System.out.println(testTreeArray.remove("turtle"));

        int count = 0;
        int testSize = testTreeArray.size();
        Iterator itr = testTreeArray.iterator();
        while (itr.hasNext()) {
            Object current = itr.next();
            System.out.println("Animal: " + current);//Remove this before hand in
            count++;
            if (count == 4) {
                itr.remove();
            }
            if (count > testSize * 2) {
                System.out.println("Apparent infinite loop when appending"
                        + " while iterating");
                break;
            }
        }
    }
}
