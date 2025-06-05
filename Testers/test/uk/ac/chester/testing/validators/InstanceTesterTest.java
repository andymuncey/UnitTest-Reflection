package uk.ac.chester.testing.validators;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import uk.ac.chester.testing.InstanceTester;
import uk.ac.chester.testing.handlers.InstanceTestEventHandlerEN;
import uk.ac.chester.testing.testclasses.PointTestClass;
import uk.ac.chester.testing.testclasses.TestClass;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InstanceTesterTest {

    private InstanceTester<PointTestClass> tester;

    @BeforeEach
    public void setUp() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        tester = new InstanceTester<>(PointTestClass.class,  new InstanceTestEventHandlerEN(), 5,0);

    }

    @AfterEach
    public void tearDown()  {
        tester = null;
    }



    @Test
    public void exceptionThrowing(){
        InstanceTester<TestClass> tester = new InstanceTester<>(TestClass.class,  new InstanceTestEventHandlerEN());
        assertThrows(RuntimeException.class, () -> {
            tester.executeMethod(void.class,"exceptionThrower"); //should throw the div/0 exception from the method being tested
        });
    }

    @Test
    public void invalidConstruction(){
        InstanceTester<PointTestClass> tester = new InstanceTester<>(PointTestClass.class,  new InstanceTestEventHandlerEN(), "not","valid","params");

        Throwable error = assertThrows(AssertionFailedError.class, () -> {
            tester.getFieldValue(int.class, "x"); //field is valid but above construction is not
        });
        assertEquals("No constructed instance of the class is available to evaluate", error.getMessage());
    }

    @Test
    public void nonExistentMethod(){
        InstanceTester<PointTestClass> tester = new InstanceTester<>(PointTestClass.class,  new InstanceTestEventHandlerEN(), 1,2);
        Throwable error = assertThrows(AssertionFailedError.class, () -> {
            tester.executeNonReturningMethod("nonExistentMethod", 1, 2, 3);
        });
        assertEquals("Unable to invoke method nonExistentMethod", error.getMessage());
    }



    @Test
    public void nonExistentField(){
       // InstanceTester<PointTestClass> tester = new InstanceTester<>(PointTestClass.class,  new InstanceTestEventHandlerEN(), 1,2);
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class,
                () -> tester.getFieldValue(int.class, "nonExistentField"),
                "Expected failure");
        assert(thrown.getMessage().contains("Unable to retrieve value from field"));
    }


    @Test
    public void uninitialisedFields(){

        InstanceTester<TestClass> tester = new InstanceTester<>(TestClass.class, new InstanceTestEventHandlerEN());

        AssertionFailedError thrown = assertThrows(AssertionFailedError.class,
                tester::verifyAllFieldsInitialised,
                "Expected failure");
        assert(thrown.getMessage().contains("avoid fields having a null value"));
    }

    @Test
    public void property(){
        InstanceTester<PointTestClass> tester = new InstanceTester<>(PointTestClass.class,  new InstanceTestEventHandlerEN(), 0,0);
        tester.executeNonReturningMethod("setX",5);
        Integer gotValue = tester.executeMethod(int.class, "getX");
        assertEquals(5,gotValue);
    }


    @Test
    public void getXByName(){
        int gotValue = tester.executeMethod(int.class, "getX");
        assertEquals(5,gotValue);
    }

    @Test
    public void getX(){
        Integer gotValue = tester.executeMethodMatchingCaller(int.class);
        assertEquals(5,gotValue);
    }

    @Test
    public void field(){
        InstanceTester<PointTestClass> tester = new InstanceTester<>(PointTestClass.class,  new InstanceTestEventHandlerEN(), 5,6);
        int xValue = tester.getFieldValue(int.class, "x");
        assertEquals(5,xValue);
    }
    //endregion

}
