package com.ghana.optimizer.ui;

import com.ghana.optimizer.ds.list.DynamicArray;
import com.ghana.optimizer.ds.tree.BTree;
import com.ghana.optimizer.ds.tree.BinarySearchTree;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Interactive console UI for Binary Search Tree and B-tree operations.
 * Supports insertion, search, traversal, display, and switching between tree types.
 */
public class TreeConsoleUI {

    private final Scanner scanner;
    private BinarySearchTree<Integer, String> binarySearchTree;
    private BTree<Integer, String> bTree;
    private boolean usingBTree;

    public TreeConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.binarySearchTree = new BinarySearchTree<>();
        this.bTree = new BTree<>(2);
        this.usingBTree = false;
    }

    public static void main(String[] args) {
        TreeConsoleUI consoleUI = new TreeConsoleUI();
        consoleUI.run();
    }

    public void run() {
        printWelcome();
        while (true) {
            printPrimaryMenu();
            int selection = readInteger("Choose an option:");
            switch (selection) {
                case 1 -> useBinarySearchTree();
                case 2 -> configureBTree();
                case 0 -> exitProgram();
                default -> println("Invalid selection. Please enter a valid option.");
            }
        }
    }

    private void printWelcome() {
        println("==========================================================================");
        println("  UG-CSOO Tree Explorer");
        println("  Interactive console UI for Binary Search Tree and B-tree operations");
        println("==========================================================================");
        println("This console supports: insert, search, traversal, display, height, size, clear, and delete (BST only).");
        println("");
    }

    private void printPrimaryMenu() {
        println("Primary Menu:");
        println("  1. Use Binary Search Tree (BST)");
        println("  2. Use B-Tree");
        println("  0. Exit");
    }

    private void useBinarySearchTree() {
        usingBTree = false;
        println("\nNow using Binary Search Tree (BST).\n");
        while (true) {
            printTreeMenu();
            int option = readInteger("Select a BST menu item:");
            if (option == 0) {
                println("Returning to primary menu.\n");
                return;
            }
            handleTreeCommand(option);
        }
    }

    private void configureBTree() {
        println("\nConfiguring B-Tree.");
        int degree = readInteger("Enter minimum degree (t) for the B-tree [default 2]:");
        if (degree < 2) {
            println("Minimum degree must be at least 2. Using default value 2.");
            degree = 2;
        }
        this.bTree = new BTree<>(degree);
        usingBTree = true;
        println("Using B-tree with minimum degree " + degree + ".\n");
        while (true) {
            printTreeMenu();
            int option = readInteger("Select a B-tree menu item:");
            if (option == 0) {
                println("Returning to primary menu.\n");
                return;
            }
            handleTreeCommand(option);
        }
    }

    private void printTreeMenu() {
        println("Tree Operations Menu:");
        println("  1. Insert key/value");
        println("  2. Search by key");
        println("  3. Contains key");
        println("  4. Remove key (BST only)");
        println("  5. Print inorder traversal");
        println("  6. Print tree structure");
        println("  7. Show height");
        println("  8. Show size");
        println("  9. Clear tree");
        println("  0. Back to primary menu");
    }

    private void handleTreeCommand(int option) {
        switch (option) {
            case 1 -> insertKeyValue();
            case 2 -> searchKey();
            case 3 -> containsKey();
            case 4 -> removeKey();
            case 5 -> printInorder();
            case 6 -> printStructure();
            case 7 -> printHeight();
            case 8 -> printSize();
            case 9 -> clearTree();
            default -> println("Invalid command. Please choose a valid tree operation.");
        }
    }

    private void insertKeyValue() {
        int key = readInteger("Enter integer key to insert:");
        String value = readString("Enter value for key " + key + ":");
        if (usingBTree) {
            bTree.insert(key, value);
            println("Inserted into B-tree: (" + key + ", '" + value + "')");
        } else {
            binarySearchTree.insert(key, value);
            println("Inserted into BST: (" + key + ", '" + value + "')");
        }
    }

    private void searchKey() {
        int key = readInteger("Enter integer key to search for:");
        String result = usingBTree ? bTree.search(key) : binarySearchTree.search(key);
        if (result == null) {
            println("Key " + key + " was not found.");
        } else {
            println("Key " + key + " found with value: '" + result + "'");
        }
    }

    private void containsKey() {
        int key = readInteger("Enter integer key to check existence:");
        boolean exists = usingBTree ? bTree.contains(key) : binarySearchTree.contains(key);
        println("Key " + key + (exists ? " exists in the tree." : " does not exist in the tree."));
    }

    private void removeKey() {
        if (usingBTree) {
            println("Delete is not supported for B-tree in this UI. Use the BST mode for removal.");
            return;
        }
        int key = readInteger("Enter integer key to remove from BST:");
        try {
            binarySearchTree.remove(key);
            println("Removed key " + key + " from BST.");
        } catch (Exception e) {
            println("Could not remove key " + key + ": " + e.getMessage());
        }
    }

    private void printInorder() {
        if (usingBTree) {
            println("B-tree inorder key sequence:");
            var keys = bTree.inorderKeys();
            printArray(keys);
        } else {
            println("BST inorder key sequence:");
            var keys = binarySearchTree.inorderKeys();
            printArray(keys);
        }
    }

    private void printStructure() {
        if (usingBTree) {
            println("B-tree structure:");
            printBTreeStructure(bTree);
        } else {
            println("BST structure:");
            printBSTStructure(binarySearchTree.getRoot(), "", true);
        }
    }

    private void printHeight() {
        int height = usingBTree ? bTree.height() : binarySearchTree.height();
        println("Current tree height: " + height);
    }

    private void printSize() {
        int size = usingBTree ? bTree.size() : binarySearchTree.size();
        println("Current tree size: " + size);
    }

    private void clearTree() {
        if (usingBTree) {
            bTree.clear();
            println("Cleared B-tree.");
        } else {
            binarySearchTree = new BinarySearchTree<>();
            println("Cleared BST.");
        }
    }

    private void printBTreeStructure(BTree<Integer, String> tree) {
        printBTreeNode(tree.getRoot(), "");
    }

    private void printBTreeNode(BTree.BTreeNode<Integer, String> node, String indent) {
        if (node == null) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(indent).append("[ ");
        Integer[] keys = node.getKeys();
        for (int keyIndex = 0; keyIndex < node.getKeyCount(); keyIndex++) {
            builder.append(keys[keyIndex]);
            if (keyIndex < node.getKeyCount() - 1) {
                builder.append(" | ");
            }
        }
        builder.append(" ]");
        println(builder.toString());

        if (!node.isLeaf()) {
            BTree.BTreeNode<Integer, String>[] children = node.getChildren();
            for (int childIndex = 0; childIndex <= node.getKeyCount(); childIndex++) {
                printBTreeNode(children[childIndex], indent + "    ");
            }
        }
    }

    private void printBSTStructure(BinarySearchTree.Node<Integer, String> node, String prefix, boolean isTail) {
        if (node == null) {
            return;
        }
        println(prefix + (isTail ? "└── " : "├── ") + node.getKey() + ": '" + node.getValue() + "'");
        String childPrefix = prefix + (isTail ? "    " : "│   ");
        if (node.getLeft() != null || node.getRight() != null) {
            if (node.getLeft() != null) {
                printBSTStructure(node.getLeft(), childPrefix, node.getRight() == null);
            } else if (node.getRight() != null) {
                println(childPrefix + "└── null");
            }
            if (node.getRight() != null) {
                printBSTStructure(node.getRight(), childPrefix, true);
            }
        }
    }

    private void printArray(DynamicArray<Integer> array) {
        if (array.isEmpty()) {
            println("(empty)");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < array.size(); i++) {
            builder.append(array.get(i));
            if (i < array.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        println(builder.toString());
    }

    private int readInteger(String prompt) {
        while (true) {
            try {
                print(prompt + " ");
                int number = Integer.parseInt(scanner.nextLine().trim());
                return number;
            } catch (NumberFormatException e) {
                println("Invalid number. Please enter an integer.");
            }
        }
    }

    private String readString(String prompt) {
        print(prompt + " ");
        return scanner.nextLine().trim();
    }

    private void print(String message) {
        System.out.print(message);
    }

    private void println(String message) {
        System.out.println(message);
    }

    private void exitProgram() {
        println("\nExiting the tree explorer. Goodbye!");
        scanner.close();
        System.exit(0);
    }
}
