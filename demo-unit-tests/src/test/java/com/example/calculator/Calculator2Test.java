package com.example.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Calculator2Test {

    @Test
    public void testAddition() {
        Calculator calculator = new Calculator();
        double result = 0;
        for  (int i = 1; i <= 100; i++) {
            result = calculator.add(result, i);
        }
        System.out.println("Sum of numbers from 1 to 100 is: " + result);
        Assertions.assertEquals(5050, result);
    }
}
