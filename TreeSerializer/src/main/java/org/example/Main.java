package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        testIntegerTree();
        testStringTree();
    }
    private static void testIntegerTree() {
        System.out.println("Testing BinaryTree with Integers...");

        BinaryTree<Integer> tree = new BinaryTree<>();
        tree.insert(50);
        tree.insert(30);
        tree.insert(20);
        tree.insert(40);
        tree.insert(70);
        tree.insert(60);
        tree.insert(80);

        // Test normal serialization/deserialization
        BinaryTreeSerializer<Integer> serializer = new BinaryTreeSerializer<>();
        String serialized = serializer.serialize(tree.root);
        System.out.println("Normal Serialized tree: " + serialized);
        BinaryTree.Node<Integer> deserializedRoot = serializer.deserialize(serialized);
        String serializedAgain = serializer.serialize(deserializedRoot);
        System.out.println("Normal Serialized again after deserialization: " + serializedAgain);

        // Introduce cyclic connection
        tree.root.left.right.right = tree.root.left;

        // Test cyclic serialization
        CyclicTreeSerializer<Integer> cyclicSerializer = new CyclicTreeSerializer<>();
        try {
            String cyclicSerialized = cyclicSerializer.serialize(tree.root);
            System.out.println("Cyclic Serialized tree: " + cyclicSerialized);
        } catch (RuntimeException e) {
            System.out.println("Exception caught during cyclic serialization: " + e.getMessage());
        }
    }

    private static void testStringTree() {
        System.out.println("\nTesting BinaryTree with Strings...");

        BinaryTree<String> tree = new BinaryTree<>();
        tree.insert("dog");
        tree.insert("cat");
        tree.insert("apple");
        tree.insert("elephant");
        tree.insert("zebra");
        tree.insert("mongoose");

        // Test normal serialization/deserialization
        BinaryTreeSerializer<String> serializer = new BinaryTreeSerializer<>();
        String serialized = serializer.serialize(tree.root);
        System.out.println("Normal Serialized tree: " + serialized);
        BinaryTree.Node<String> deserializedRoot = serializer.deserialize(serialized);
        String serializedAgain = serializer.serialize(deserializedRoot);
        System.out.println("Normal Serialized again after deserialization: " + serializedAgain);

        // Introduce cyclic connection
        tree.root.left.right = tree.root.left;

        // Test cyclic serialization
        CyclicTreeSerializer<String> cyclicSerializer = new CyclicTreeSerializer<>();
        try {
            String cyclicSerialized = cyclicSerializer.serialize(tree.root);
            System.out.println("Cyclic Serialized tree: " + cyclicSerialized);
        } catch (RuntimeException e) {
            System.out.println("Exception caught during cyclic serialization: " + e.getMessage());
        }
    }
}
