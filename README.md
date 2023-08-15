# Taboola_Assessment
1. The **TreeSerializer folder** is the code for serializing and deserializing a binary tree.
BinaryTree.java: A generic binary tree implementation.
TreeSerializer.java: The interface for the serializer-deserializer.
BinaryTreeSerializer.java: Non-cyclic tree serializer and deserializer implementation.
CyclicTreeSerializer.java: Cyclic tree serializer and deserializer implementation that throws an exception if a cyclic connection is detected.
Test.java: A test class to demonstrate the functionality of the serializers and deserializers with Integer and String data types.


2. The **JSONParser folder** is the code for parsing the JSON data and storing it in a Map.
A simple and efficient JSON parser written in Java.

Description
This JSON parser converts JSON strings into Java `Map` and `List` objects, while also supporting primitive data types like `String`, `Number`, `Boolean`, and `null`. It provides a lightweight way to parse JSON strings without any external dependencies.

Features
1. Parses JSON objects to Java `Map<String, Object>`.
2. Parses JSON arrays to Java `List<Object>`.
3. Handles escape sequences in strings.
4. Can handle numbers as `Long` or `Double` based on presence of decimal points.
5. Parses `true`, `false`, and `null` literals.
6. Validates and throws a `RuntimeException` for invalid JSON input.

How to Use
1. Initialization: Create an instance of the `JSONParser` by passing the JSON string to be parsed.

    ```java
    String jsonString = "{ \"key\": \"value\" }";
    Map<String, Object> parsedJson = JSONParser.parse(jsonString);
    ```

2. Access Parsed Data: After parsing, the JSON string will be represented as a combination of `Map`, `List`, and other Java primitives.
3. 
    ```java
    String value = (String) parsedJson.get("key");
    ```
Refer to the `main` method of the `JSONParser` class for a practical example of using the parser and validating the parsed data using assertions.



3. The **ProductDetails.sql** contains the script for maintaing the Products database.

