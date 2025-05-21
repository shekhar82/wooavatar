package com.example.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilsTest {

    @Test
    void testAllLowercase() {
        assertEquals("ABCDEF", StringUtils.lowerToUpperCase("abcdef"));
    }

    @Test
    void testMixedCase() {
        assertEquals("ABCDEF", StringUtils.lowerToUpperCase("aBcDeF"));
    }

    @Test
    void testAllUppercase() {
        assertEquals("ABCDEF", StringUtils.lowerToUpperCase("ABCDEF"));
    }

    @Test
    void testEmptyString() {
        assertEquals("", StringUtils.lowerToUpperCase(""));
    }

    @Test
    void testNullString() {
        assertEquals(null, StringUtils.lowerToUpperCase(null));
    }

    @Test
    void testMixedWithNumbersAndSpecialChars() {
        assertEquals("ABC123XYZ!@#", StringUtils.lowerToUpperCase("abc123XYZ!@#"));
    }
}
