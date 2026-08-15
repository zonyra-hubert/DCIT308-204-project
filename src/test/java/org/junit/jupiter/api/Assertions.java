package org.junit.jupiter.api;

import java.util.Objects;

public class Assertions {

    public static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, "Assertion failed: expected [" + expected + "] but found [" + actual + "]");
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (expected instanceof Number && actual instanceof Number) {
            if (((Number) expected).doubleValue() != ((Number) actual).doubleValue()) {
                throw new AssertionError(message + " - expected: <" + expected + "> but was: <" + actual + ">");
            }
            return;
        }
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(message + " - expected: <" + expected + "> but was: <" + actual + ">");
        }
    }

    public static void assertEquals(double expected, double actual, double delta) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError("Assertion failed: expected [" + expected + "] but was [" + actual + "] with delta [" + delta + "]");
        }
    }

    public static void assertEquals(double expected, double actual, double delta, String message) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError(message + " - expected: <" + expected + "> but was: <" + actual + "> with delta <" + delta + ">");
        }
    }

    public static <T> void assertArrayEquals(T[] expected, T[] actual) {
        if (expected == null && actual == null) return;
        if (expected == null || actual == null || expected.length != actual.length) {
            throw new AssertionError("Assertion failed: array lengths do not match or one is null");
        }
        for (int i = 0; i < expected.length; i++) {
            if (!Objects.equals(expected[i], actual[i])) {
                throw new AssertionError("Assertion failed at index " + i + ": expected <" + expected[i] + "> but found <" + actual[i] + ">");
            }
        }
    }

    public static void assertTrue(boolean condition) {
        assertTrue(condition, "Assertion failed: expected condition to be true");
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertFalse(boolean condition) {
        assertFalse(condition, "Assertion failed: expected condition to be false");
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertNotNull(Object object) {
        assertNotNull(object, "Assertion failed: expected object to not be null");
    }

    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new AssertionError(message);
        }
    }

    public static void assertNull(Object object) {
        assertNull(object, "Assertion failed: expected object to be null");
    }

    public static void assertNull(Object object, String message) {
        if (object != null) {
            throw new AssertionError(message);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable) {
        try {
            executable.execute();
        } catch (Throwable actualException) {
            if (expectedType.isInstance(actualException)) {
                return (T) actualException;
            }
            throw new AssertionError("Unexpected exception type thrown: expected <"
                    + expectedType.getName() + "> but got <" + actualException.getClass().getName() + ">", actualException);
        }
        throw new AssertionError("Expected " + expectedType.getName() + " to be thrown, but nothing was thrown");
    }

    @FunctionalInterface
    public interface Executable {
        void execute() throws Throwable;
    }
}
