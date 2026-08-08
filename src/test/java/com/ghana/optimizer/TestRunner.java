package com.ghana.optimizer;

import com.ghana.optimizer.algorithm.GenericSearchAlgorithmTest;
import com.ghana.optimizer.algorithm.SearchingTest;
import com.ghana.optimizer.ds.BinaryHeapTest;
import com.ghana.optimizer.ds.BTreeTest;
import com.ghana.optimizer.ds.CustomHashTableTest;
import com.ghana.optimizer.ds.PriorityQueueTest;

import java.lang.reflect.Method;

/**
 * Custom Test Runner to execute data structure unit tests and report results to console.
 * All variable names written in full as required by Zonyra Hubert.
 */
public class TestRunner {

    public static void main(String[] executionArguments) {
        System.out.println("==========================================================================");
        System.out.println("  UG-CSOO Data Structures Verification & Test Suite Runner               ");
        System.out.println("  Author: Zonyra Hubert                                                   ");
        System.out.println("==========================================================================");

        int totalTestsExecuted = 0;
        int totalTestsPassed = 0;
        int totalTestsFailed = 0;

        Class<?>[] testClassesToRun = new Class<?>[]{
                BinaryHeapTest.class,
                CustomHashTableTest.class,
                PriorityQueueTest.class,
                BTreeTest.class,
                GenericSearchAlgorithmTest.class,
                SearchingTest.class
        };

        for (int classIndex = 0; classIndex < testClassesToRun.length; classIndex++) {
            Class<?> currentTestClass = testClassesToRun[classIndex];
            System.out.println("\nRunning Test Class: " + currentTestClass.getName());
            System.out.println("--------------------------------------------------------------------------");

            Method[] classMethods = currentTestClass.getDeclaredMethods();

            for (int methodIndex = 0; methodIndex < classMethods.length; methodIndex++) {
                Method currentMethod = classMethods[methodIndex];

                if (currentMethod.isAnnotationPresent(org.junit.jupiter.api.Test.class)) {
                    totalTestsExecuted++;
                    try {
                        currentMethod.setAccessible(true);
                        Object testClassInstance = currentTestClass.getDeclaredConstructor().newInstance();

                        Method[] beforeEachMethods = currentTestClass.getDeclaredMethods();
                        for (int beforeEachIndex = 0; beforeEachIndex < beforeEachMethods.length; beforeEachIndex++) {
                            Method beforeEachCandidate = beforeEachMethods[beforeEachIndex];
                            if (beforeEachCandidate.isAnnotationPresent(org.junit.jupiter.api.BeforeEach.class)) {
                                beforeEachCandidate.setAccessible(true);
                                beforeEachCandidate.invoke(testClassInstance);
                            }
                        }

                        try {
                            currentMethod.invoke(testClassInstance);
                            System.out.println(" [PASS] " + currentMethod.getName());
                            totalTestsPassed++;
                        } finally {
                            for (int afterEachIndex = 0; afterEachIndex < beforeEachMethods.length; afterEachIndex++) {
                                Method afterEachCandidate = beforeEachMethods[afterEachIndex];
                                if (afterEachCandidate.isAnnotationPresent(org.junit.jupiter.api.AfterEach.class)) {
                                    afterEachCandidate.setAccessible(true);
                                    afterEachCandidate.invoke(testClassInstance);
                                }
                            }
                        }

                    } catch (Exception testExecutionException) {
                        System.err.println(" [FAIL] " + currentMethod.getName());
                        Throwable rootCauseException = testExecutionException.getCause() != null ? testExecutionException.getCause() : testExecutionException;
                        System.err.println("        Reason: " + rootCauseException.getMessage());
                        rootCauseException.printStackTrace(System.err);
                        totalTestsFailed++;
                    }
                }
            }
        }

        System.out.println("\n==========================================================================");
        System.out.println(" Test Summary Result:");
        System.out.println("   Total Tests Executed : " + totalTestsExecuted);
        System.out.println("   Total Tests Passed   : " + totalTestsPassed);
        System.out.println("   Total Tests Failed   : " + totalTestsFailed);
        System.out.println("==========================================================================");

        if (totalTestsFailed > 0) {
            System.exit(1);
        }
    }
}
