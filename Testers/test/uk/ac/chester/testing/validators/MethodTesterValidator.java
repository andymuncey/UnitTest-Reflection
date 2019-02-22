package uk.ac.chester.testing.validators;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.chester.testing.MethodTester;
import uk.ac.chester.testing.TestClass;
import uk.ac.chester.testing.handlers.MethodTestEventHandlerEN;


/**
 * The tests in the class should FAIL - it's designed to verify expected failures occur and appropriate error messages are displayed
 */
public class MethodTesterValidator {

    @Test
    public void nonExistentMethod() {
        MethodTester tester = new MethodTester<>(TestClass.class, Void.class, "nonExistentMethod", new MethodTestEventHandlerEN());
        Assert.assertNull(tester.test());
    }

    @Test
    public void wrongCaseForMethodName() {
        MethodTester tester = new MethodTester<>(TestClass.class, void.class, "RETURNSINTEGER", new MethodTestEventHandlerEN());
        tester.test();
    }

    @Test
    public void wrongReturnType() {
        MethodTester tester = new MethodTester<>(TestClass.class, String.class, "returnsInteger", new MethodTestEventHandlerEN());
        tester.test();
    }

    @Test
    public void strictReturnType() {
        MethodTester tester = new MethodTester<>(TestClass.class,int.class,"returnsInteger",new MethodTestEventHandlerEN());
        tester.testForExactReturnType();
    }

    @Test
    public void wrongParams(){
        MethodTester tester = new MethodTester<>(TestClass.class,void.class,"oneIntParam",new MethodTestEventHandlerEN());
        tester.test("text");
    }

    @Test
    public void wrongMultiParams(){
        MethodTester tester = new MethodTester<>(TestClass.class,void.class,"intParamStringParam",new MethodTestEventHandlerEN());
        tester.test("text", 23.5);
    }

    @Test
    public void wrongOrderParams(){
        MethodTester tester = new MethodTester<>(TestClass.class,void.class,"intParamStringParam",new MethodTestEventHandlerEN());
        tester.test();
    }

    @Test
    public void exceptionThrowing(){
        MethodTester tester = new MethodTester<>(TestClass.class, void.class,"exceptionThrower", new MethodTestEventHandlerEN());
        tester.test(); //should throw the exception from the method being tested
    }

    @Test
    public void paramConvention(){
        MethodTester tester = new MethodTester<>(TestClass.class, void.class, "paramNameNotLowerCamelCase", new MethodTestEventHandlerEN());
        tester.test(3); //should indicate the the parameter doesn't follow naming convention
    }

}
