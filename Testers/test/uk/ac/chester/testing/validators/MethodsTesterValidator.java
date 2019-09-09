package uk.ac.chester.testing.validators;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.chester.testing.AccessModifier;
import uk.ac.chester.testing.MethodsTester;
import uk.ac.chester.testing.testclasses.TestClass;
import uk.ac.chester.testing.handlers.MethodTestEventHandlerEN;



public class MethodsTesterValidator {

    private MethodsTester tester;

    @Before
    public void setUp() throws Exception {
        tester = new MethodsTester<>(TestClass.class,  new MethodTestEventHandlerEN());
    }

    @After
    public void tearDown() throws Exception {
        tester = null;
    }

    //region failing tests

    /**
     * The tests in the section should FAIL - it's designed to verify expected failures occur and appropriate error messages are displayed
     */


    @Test
    public void nonExistentMethod() {
        Assert.assertNull(tester.testExistence(Void.class, "nonExistentMethod"));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void wrongCaseForMethodName() {
        tester.testExistence(void.class, "RETURNSINTEGER");
    }

    @Test
    public void wrongReturnType() {
        tester.testExistence(String.class, "returnsInteger");
    }

    @Test
    public void strictReturnType() {
        tester.testForExactReturnType(int.class,"returnsInteger");
    }

    @Test
    public void wrongParamCount(){
        tester.testExistence(void.class, "intParamStringParam");
    }

    @Test
    public void wrongParams(){
        tester.testExistence(void.class,"oneIntParam","text");
    }

    @Test
    public void wrongMultiParams(){
        tester.testExistence(void.class,"intParamStringParam","text", 23.5);
    }

    @Test
    public void wrongOrderParams(){
        tester.testExistence(void.class,"intParamStringParam", "text", 5);
    }



    @Test
    public void paramConvention(){
        tester.testExistence(void.class, "paramNameNotLowerCamelCase",3); //should indicate the the parameter doesn't follow naming convention
    }

    @Test
    public void accessModifier(){
        tester.testExistence(AccessModifier.PUBLIC, void.class,"privateMethod" );
    }

    @Test
    public void returnedValue(){
        int result = (Integer)tester.executeStatic(Integer.class,"returnsPrimitiveInt"); //Either cast the result to an object, or ensure that the methods tester is typed
        Assert.assertEquals("The method (deliberately) returns the wrong value",2,result);
    }


}