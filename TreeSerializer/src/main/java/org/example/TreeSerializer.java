package org.example;

import java.util.*;

public interface TreeSerializer<T> {
    String serialize(BinaryTree.Node<T> root);
    BinaryTree.Node<T> deserialize(String str);
}
