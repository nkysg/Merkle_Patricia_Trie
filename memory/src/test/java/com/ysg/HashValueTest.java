package com.ysg;


import org.junit.Test;

import static junit.framework.TestCase.assertEquals;


public class HashValueTest {
    @Test
   public void test_zero() {
        HashValue hashValue = HashValue.zero();
        String str = hashValue.toString();
        assertEquals(str, "00000000000000000000000000000000");
    }
}
