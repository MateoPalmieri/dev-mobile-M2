package com.example.devmobile

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun subtraction_isCorrect() {
        assertEquals(0, 2 - 2)
    }

    @Test
    fun multiplication_isCorrect() {
        assertEquals(4, 2 * 2)
    }

    @Test
    fun division_isCorrect() {
        assertEquals(1, 2 / 2)
    }

    @Test
    fun stringConcatenation_isCorrect() {
        val str1 = "Hello"
        val str2 = "World"
        val expected = "HelloWorld"
        assertEquals(expected, str1 + str2)
    }

    @Test
    fun stringComparison_isCorrect() {
        val str1 = "apple"
        val str2 = "Apple"
        assertNotEquals(str1, str2)
        assertEquals(str1, "apple")
    }

    @Test
    fun listSize_isCorrect() {
        val list = listOf(1, 2, 3)
        assertEquals(3, list.size)
    }

    @Test
    fun listContains_isCorrect() {
        val list = listOf(1, 2, 3)
        assertTrue(list.contains(2))
        assertFalse(list.contains(4))
    }

    @Test
    fun booleanLogic_isCorrect() {
        assertTrue(true)
        assertFalse(false)
    }

    @Test
    fun listElements_isCorrect() {
        val list = listOf(1,2,3)
        assertEquals(1, list[0])
        assertEquals(2, list[1])
        assertEquals(3, list[2])
    }
}