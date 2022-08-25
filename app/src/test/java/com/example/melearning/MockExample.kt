package com.example.melearning

import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.InOrder
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


data class Dependency1(var num: Int) {
    fun number() = num
    fun mult(input: Int) = num * input
}

class SystemUnderTest(private val dep1: Dependency1, private val dep2: Dependency1) {
    fun getSum() = calculateSum()
    fun calculateSum() = dep1.number() + dep2.number()
}

class MockExample {
    @Mock private val calculator: SystemUnderTest? = null

    @Test
    fun checkIfFunctionIsCalled() {
        val dep1 = mock(Dependency1::class.java)
        val dep2 = mock(Dependency1::class.java)

        val sum = SystemUnderTest(dep1, dep2)

        verify(dep2, never()).number()
        sum.getSum()
        verify(dep1).number()
        sum.getSum()
        verify(dep2, times(2)).number()
        verify(dep2, atLeastOnce()).number()
    }

    @Test
    fun orderTest() {
        val dep1 = mock(Dependency1::class.java)
        val dep2 = mock(Dependency1::class.java)
        val sum = SystemUnderTest(dep1, dep2)

        sum.getSum()
        val inOrder: InOrder = inOrder(dep1, dep2)
        inOrder.verify(dep1).number()
        inOrder.verify(dep2).number()
    }

    @Test
    fun wrapVariableAndFunResult() {
        val dep1 = mock(Dependency1::class.java)
        `when`(dep1.num).thenReturn(5)
        assertEquals(dep1.num, 5)

        `when`(dep1.number()).thenReturn(10)
        assertEquals(dep1.number(), 10)

        val sum = SystemUnderTest(dep1, dep1)
        assertEquals(sum.getSum(), 20)

        //check equality of parameters
        dep1.mult(4)
        verify(dep1).mult(4)

        //call real method
        `when`(dep1.number()).thenCallRealMethod()

        verify(dep1, timeout(100)).mult(4)
    }
}