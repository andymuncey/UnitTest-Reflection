package uk.ac.chester.testing.validators;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.chester.testing.AccessModifier;
import uk.ac.chester.testing.MethodsTester;
import uk.ac.chester.testing.testclasses.TestClass;
import uk.ac.chester.testing.handlers.MethodTestEventHandlerEN;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class MethodsTesterValidator {

    private MethodsTester<TestClass> tester;

    @BeforeEach
    public void setUp()  {
        tester = new MethodsTester<>(TestClass.class,  new MethodTestEventHandlerEN());
    }

    @AfterEach
    public void tearDown()  {
        tester = null;
    }


    //region passing tests
    @Test
    public void staticMethodPass(){
        tester.testExistenceForValues(true,AccessModifier.PACKAGE_PRIVATE,true,void.class,"staticMethod");
    }

    @Test
    public void staticTest(){
        int result = tester.executeStatic(int.class,"staticInt");
    }


    /**
     * This Method must be named the same as the method that is tested
     */
    @Test
    public void staticInt(){
        int result = tester.executeStatic(int.class,null);
        assertEquals(1,result);
    }


    @Test
    public void staticTestReturningVoid () {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("abc");
        strings.add("def");
        tester.executeStatic(Void.class,"doubleArrayListContents",strings);
        assertEquals(strings.size(), 4);
    }

    //endregion


    //region failing tests

    /**
     * The tests in the section should FAIL - it's designed to verify expected failures occur and appropriate error messages are displayed
     */


    @Test
    public void staticMethod(){
        tester.testExistenceForValues(true,AccessModifier.PACKAGE_PRIVATE,true,void.class,"nonStaticMethod");
    }

    @Test
    public void nonStaticMethod(){
        tester.testExistenceForValues(true,AccessModifier.PACKAGE_PRIVATE,false,void.class,"staticMethod");
    }

    @Test
    public void nonExistentMethod() {
        //noinspection ObviousNullCheck
        assertNull(tester.testExistenceForValues(Void.class, "nonExistentMethod"));
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void wrongCaseForMethodName() {
        tester.testExistenceForValues(void.class, "RETURNSINTEGER");
    }

    @Test
    public void wrongReturnType() {
        tester.testExistenceForValues(String.class, "returnsInteger");
    }

    @Test
    public void strictReturnType() {
        tester.testForExactReturnType(int.class,"returnsInteger");
    }

    @Test
    public void wrongParamCount(){
        tester.testExistenceForValues(void.class, "intParamStringParam");
    }

    @Test
    public void wrongParams(){
        tester.testExistenceForValues(void.class,"oneIntParam","text");
    }

    @Test
    public void wrongMultiParams(){
        tester.testExistenceForValues(void.class,"intParamStringParam","text", 23.5);
    }

    @Test
    public void wrongOrderParams(){
        tester.testExistenceForValues(void.class,"intParamStringParam", "text", 5);
    }



    @Test
    public void paramConvention(){
        tester.testExistenceForValues(void.class, "paramNameNotLowerCamelCase",3); //should indicate the parameter doesn't follow naming convention
    }

    @Test
    public void accessModifier(){
        tester.testExistenceForValues(AccessModifier.PUBLIC, void.class,"privateMethod" );
    }

    @Test
    public void returnedValue(){
        int result = tester.executeStatic(Integer.class,"returnsPrimitiveInt"); //Either cast the result to an object, or ensure that the methods tester is typed
        assertEquals(2,result,"The method (deliberately) returns the wrong value");
    }


}