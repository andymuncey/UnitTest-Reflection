package uk.ac.chester.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TesterTest {

    static class CustomTester extends Tester { }

    static class CustomConventionTester implements ConventionChecker { }

    CustomTester customTester = new CustomTester();
    CustomConventionTester customConventionTester = new CustomConventionTester();

    @BeforeEach
    void setUp() {
        customTester = new CustomTester();
        customTester.setConventionChecker(customConventionTester);

    }

    @AfterEach
    void tearDown() {
        customTester = null;
    }

    @Test
    void getConventionChecker() {
        assertEquals(customTester.getConventionChecker(), customConventionTester);
    }

    @Test
    void setConventionChecker() {
        ConventionChecker anotherConventionTester = new ConventionChecker(){
            @Override
            public boolean validMethodName(String name) {
                return true;
            }
        };

        customTester.setConventionChecker(anotherConventionTester);

        assertEquals(customTester.getConventionChecker(), anotherConventionTester);

    }
}