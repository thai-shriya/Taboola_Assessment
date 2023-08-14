package org.example;

public class BinaryTree<T extends Comparable<T>> {

    public static class Node<T> {
        T value;
        Node<T> left;
        Node<T> right;

        Node(T value) {
            this.value = value;
            left = null;
            right = null;
        }
    }

    Node<T> root;

    public BinaryTree() {
        root = null;
    }

    public void insert(T value) {
        root = insertRec(root, value);
    }

    private Node<T> insertRec(Node<T> root, T value) {
        if (root == null) {
            root = new Node<>(value);
            return root;
        }

        if (value.compareTo(root.value) < 0)
            root.left = insertRec(root.left, value);
        else if (value.compareTo(root.value) > 0)
            root.right = insertRec(root.right, value);

        return root;
    }
}

