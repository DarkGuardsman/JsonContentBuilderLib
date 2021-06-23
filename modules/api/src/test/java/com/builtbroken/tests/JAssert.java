package com.builtbroken.tests;

import org.junit.jupiter.api.Assertions;

import java.util.Arrays;

/**
 * Created by Dark(DarkGuardsman, Robert) on 2019-04-05.
 */
public class JAssert
{

    /**
     * Compares two int arrays
     * @param expected
     * @param actual
     */
    public static void assertArrayEquals(int[] expected, int[] actual)
    {
        //Good
        if (expected == null && actual == null)
        {
            return;
        }

        //Fail null
        if (expected == null && actual != null || expected != null && actual == null)
        {
            Assertions.fail("One array is null while the other isn't\n" +
                    "Expected: " + Arrays.toString(expected) +
                    "\nActual: " + Arrays.toString(actual));
        }

        //Fail length
        if (expected.length != actual.length)
        {
            Assertions.fail("Array lengths do not match\n" +
                    "Expected: " + Arrays.toString(expected) + "  " + expected.length +
                    "\nActual: " + Arrays.toString(actual) + "  " + actual.length);
        }

        //Check each
        for (int i = 0; i < expected.length; i++)
        {
            if (expected[i] != actual[i])
            {
                Assertions.fail("Entry " + i + " do not match" +
                        "Expected: " + expected[i] + " from " + Arrays.toString(expected) +
                        "\nActual: " + actual[i] + " in " + Arrays.toString(actual) + "  " + actual.length);
            }
        }
    }
}
