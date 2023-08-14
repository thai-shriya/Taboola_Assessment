package org.example;

import java.util.*;

public class CyclicTreeSerializer<T extends Comparable<T>> implements TreeSerializer<T> {

    @Override
    public String serialize(BinaryTree.Node<T> root) {
        StringBuilder sb = new StringBuilder();
        Set<BinaryTree.Node<T>> visited = new HashSet<>();
        serializeHelper(root, sb, visited);
        return sb.toString();
    }

    private void serializeHelper(BinaryTree.Node<T> root, StringBuilder sb, Set<BinaryTree.Node<T>> visited) {
        if (root == null) {
            sb.append("null,");
            return;
        }

        if (visited.contains(root)) {
            throw new RuntimeException("Cyclic connection found in the tree!");
        }

        visited.add(root);
        sb.append(root.value.toString()).append(",");
        serializeHelper(root.left, sb, visited);
        serializeHelper(root.right, sb, visited);
    }

    @Override
    public BinaryTree.Node<T> deserialize(String str) {
        Queue<String> queue = new LinkedList<>(Arrays.asList(str.split(",")));
        return deserializeHelper(queue);
    }

    private BinaryTree.Node<T> deserializeHelper(Queue<String> queue) {
        String val = queue.poll();
        if (val.equals("null")) return null;
        BinaryTree.Node<T> root = new BinaryTree.Node<>( (T) val);
        root.left = deserializeHelper(queue);
        root.right = deserializeHelper(queue);
        return root;
    }
}

