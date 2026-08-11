package com.ghana.optimizer.ui.views;

/**
 * Utility to format console tables cleanly with borders and column alignment.
 */
public class TableFormatter {

    public static void printHeader(String title) {
        int width = 74;
        String line = "=".repeat(width);
        System.out.println("\n" + line);
        int pad = (width - title.length() - 2) / 2;
        System.out.println(" " + " ".repeat(Math.max(0, pad)) + title);
        System.out.println(line);
    }

    public static void printSubHeader(String subtitle) {
        System.out.println("\n--- " + subtitle + " " + "-".repeat(Math.max(0, 68 - subtitle.length())));
    }

    public static void printDivider() {
        System.out.println("-".repeat(74));
    }
}
