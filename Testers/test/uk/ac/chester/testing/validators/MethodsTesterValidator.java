package uk.ac.chester.testing.validators;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.chester.testing.AccessModifier;
import uk.ac.chester.testing.MethodsTester;
import uk.ac.chester.testing.TestClass;
import uk.ac.chester.testing.handlers.MethodTestEventHandlerEN;


/**
 * The tests in the class should FAIL - it's designed to verify expected failures occur and appropriate error messages are displayed
 */
public class MethodsTesterValidator {

    @Test
    public void nonExistentMethod() {
        MethodsTester tester = new MethodsTester<>(TestClass.class,  new MethodTestEventHandlerEN());
        Assert.assertNull(tester.test(Void.class, "nonExistentMethod"));
    }

    @Test
    public void wrongCaseForMethodName() {
        MethodsTester tester = new MethodsTester<>(TestClass.class, new MethodTestEventHandlerEN());
        tester.test(void.class, "RETURNSINTEGER");
    }

    @Test
    public void wrongReturnType() {
        MethodsTester tester = new MethodsTester<>(TestClass.class, new MethodTestEventHandlerEN());
        tester.test(String.class, "returnsInteger");
    }

    @Test
    public void strictReturnType() {
        MethodsTester tester = new MethodsTester<>(TestClass.class,new MethodTestEventHandlerEN());
        tester.testForExactReturnType(int.class,"returnsInteger");
    }

    @Test
    public void wrongParams(){
        MethodsTester tester = new MethodsTester<>(TestClass.class,new MethodTestEventHandlerEN());
        tester.test(void.class,"oneIntParam","text");
    }

    @Test
    public void wrongMultiParams(){
        MethodsTester tester = new MethodsTester<>(TestClass.class,new MethodTestEventHandlerEN());
        tester.test(void.class,"intParamStringParam","text", 23.5);
    }

    @Test
    public void wrongOrderParams(){
        MethodsTester tester = new MethodsTester<>(TestClass.class,new MethodTestEventHandlerEN());
        tester.test(void.class,"intParamStringParam");
    }

    @Test
    public void exceptionThrowing(){
        MethodsTester tester = new MethodsTester<>(TestClass.class,  new MethodTestEventHandlerEN());
        tester.test(void.class,"exceptionThrower"); //should throw the exception from the method being tested
    }

    @Test
    public void paramConvention(){
        MethodsTester tester = new MethodsTester<>(TestClass.class,  new MethodTestEventHandlerEN());
        tester.test(void.class, "paramNameNotLowerCamelCase",3); //should indicate the the parameter doesn't follow naming convention
    }

    @Test
    public void accessModifier(){
        MethodsTester tester = new MethodsTester<>(TestClass.class, new MethodTestEventHandlerEN());
        tester.test(AccessModifier.PUBLIC, void.class,"privateMethod" );
    }

}
