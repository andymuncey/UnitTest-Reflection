package uk.ac.chester.testing.validators;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import uk.ac.chester.testing.InstanceTester;
import uk.ac.chester.testing.handlers.InstanceTestEventHandlerEN;
import uk.ac.chester.testing.testclasses.PointTestClass;
import uk.ac.chester.testing.testclasses.TestClass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InstanceValidator {


    //region failing tests
    @Test
    public void exceptionThrowing(){
        InstanceTester<TestClass> tester = new InstanceTester<>(TestClass.class,  new InstanceTestEventHandlerEN());
        tester.executeMethod(void.class,"exceptionThrower"); //should throw the exception from the method being tested
    }

    @Test
    public void invalidConstruction(){
        InstanceTester<PointTestClass> tester = new InstanceTester<>(PointTestClass.class,  new InstanceTestEventHandlerEN(), "not","valid","params");
        tester.getFieldValue(int.class, "x"); //field is valid but above construction is not
    }

    @Test
    public void nonExistentMethod(){
        InstanceTester<PointTestClass> tester = new InstanceTester<>(PointTestClass.class,  new InstanceTestEventHandlerEN(), 1,2);
        tester.executeNonReturningMethod("nonExistentMethod", 1,2,3);
    }


    //region passing tests
    @Test
    public void nonExistentField(){
        InstanceTester<PointTestClass> tester = new InstanceTester<>(PointTestClass.class,  new InstanceTestEventHandlerEN(), 1,2);
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
        Integer gotValue = tester.executeMethod(Integer.class, "getX");
        assertEquals(Integer.valueOf(5),gotValue);
    }


    @Test
    public void getX(){
        InstanceTester<PointTestClass> tester = new InstanceTester<>(PointTestClass.class,  new InstanceTestEventHandlerEN(), 0,0);
        tester.executeNonReturningMethod("setX",5);
        Integer gotValue = tester.executeMethod(Integer.class, null);
        assertEquals(Integer.valueOf(5),gotValue);
    }

    @Test
    public void field(){
        InstanceTester<PointTestClass> tester = new InstanceTester<>(PointTestClass.class,  new InstanceTestEventHandlerEN(), 5,6);
        int xValue = tester.getFieldValue(int.class, "x");
        assertEquals(5,xValue);
    }
    //endregion

}
