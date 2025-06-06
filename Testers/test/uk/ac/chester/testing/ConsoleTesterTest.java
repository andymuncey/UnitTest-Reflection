package uk.ac.chester.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleTesterTest {


    private static class ConsoleApplication{

        public static void main(String[] args) {
            System.out.println(getUserInt());
        }


        private static int getUserInt(){
            Scanner scanner = new Scanner(System.in);
            return scanner.nextInt();
        }

    }


    private ConsoleTester<ConsoleApplication> tester;

    @BeforeEach
    void setUp() {
        tester = new ConsoleTester<>(ConsoleApplication.class);
    }

    @AfterEach
    void tearDown() {
        tester = null;
    }

    @Test
    void testWithTimeOut() {

    }

    @Test
    void methodCompletesWithNonNullResult() {
    }

    @Test
    void testNamedMethod() {
        String[] input = {"34"};
        Integer result = tester.test(int.class, "getUserInt",input);
        assertEquals(34, result);
    }

    @Test
    void testMain() {
        String[] input = {"34"};
        assertDoesNotThrow(() -> tester.test(input));

    }

    @Test
    void outputGeneratedBeforeInputProcessed() {
    }
}