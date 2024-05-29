package uk.ac.chester.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MethodsTesterTest {

    MethodsTester<?> tester;

    @BeforeEach
    void setUp() {
        Object x = new Object(){
            private String myString;
            private String anotherString;
            private int myInt;
            private static double myDouble;

            private static boolean someBool;

            private static boolean aBool;

            public int getMyInt(){ //valid getter
                return myInt;
            }

            public boolean isABool(){
                return aBool;
            }

            public boolean hasSomeBool(){
                return someBool;
            }

            public String getInvalidString(){ //no matching field
                return "not a valid getter";
            }

            public float getMyDouble(){ //invalid getter: wrong type
                return 0F;
            }


        };

        tester = new MethodsTester<>(x.getClass(),null);

    }

    @AfterEach
    void tearDown() {
        tester = null;
    }

    @Test
    void getters() {
        {
            Set<Method> methods = tester.getters("is", "has");
            assertEquals(3, methods.size());
        }
        {
            Set<Method> methods = tester.getters();
            assertEquals(2, methods.size());
        }
    }
}