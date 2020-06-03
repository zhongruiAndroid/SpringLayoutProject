package com.test.myscrollviewproject;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void dfa() {
        int aa=16;
        System.out.println(aa<<4);
        System.out.println(aa*16);
    }
    @Test
    public void dfad() {
        String a="abcd";
        StringBuilder stringBuilder=new StringBuilder(a);
        System.out.println(stringBuilder.reverse());
    }
}