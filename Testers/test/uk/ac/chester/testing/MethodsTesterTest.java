package uk.ac.chester.testing;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import uk.ac.chester.testing.testclasses.TestClass;
import uk.ac.chester.testing.handlers.MethodTestEventHandlerEN;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class MethodsTesterTest {

    private MethodsTester<TestClass> tester;

    @BeforeEach
    public void setUp()  {
        tester = new MethodsTester<>(TestClass.class,  new MethodTestEventHandlerEN());
    }

    @AfterEach
    public void tearDown()  {
        tester = null;
    }


    @Test
    public void staticMethodPass(){
        tester.testExistenceForValues(true,AccessModifier.PACKAGE_PRIVATE,true,void.class,"staticMethod");
    }

    @Test
    public void staticTest(){
        int result = tester.executeStatic(int.class, "staticInt");
        assertEquals(1,result);
    }

    /**
     * This Method must be named the same as the method that is tested
     */
    @Test
    public void staticInt(){
        int result = tester.executeStatic(int.class,null);
        assertEquals(1,result);

    }

    /**
     * This Method must be named the same as the method that is tested
     */
    @Test
    public void staticString(){
        String result = tester.executeStaticMethodMatchingCaller(String.class,2);
        assertEquals("texttext",result);
    }


    @Test
    public void staticTestReturningVoid () {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("abc");
        strings.add("def");
        tester.executeStatic(Void.class,"doubleArrayListContents",strings);
        assertEquals(4, strings.size());
    }

    //endregion


    //region failing tests

    /**
     * The tests in the section should FAIL - it's designed to verify expected failures occur and appropriate error messages are displayed
     */


    @Test
    public void staticMethod(){
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> tester.testExistenceForValues(true,AccessModifier.PACKAGE_PRIVATE,true,void.class,"nonStaticMethod"));
        Assertions.assertEquals("staticDeclarationIncorrect", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));
    }

    @Test
    public void nonStaticMethod(){
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("staticDeclarationIncorrect", () ->
                tester.testExistenceForValues(true,AccessModifier.PACKAGE_PRIVATE,false,void.class,"staticMethod")
        );
    }

    @Test
    public void nonExistentMethod() {
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("notFound", () ->
               tester.testExistenceForValues(Void.class, "nonExistentMethod")
        );
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void wrongCaseForMethodName() {
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("wrongCaseName", () ->
        tester.testExistenceForValues(void.class, "RETURNSINTEGER")
                );
    }

    @Test
    public void wrongReturnType() {
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("incorrectReturnType", () ->
                tester.testExistenceForValues(String.class, "returnsInteger")
        );
    }

    @Test
    public void strictReturnType() {
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("incorrectReturnType", () ->
                        tester.testExistenceForValues(String.class, "returnsInteger")

        );
    }

    @Test
    public void wrongParamCount(){
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("incorrectNumberOfParameters", () ->
                tester.testExistenceForValues(void.class, "intParamStringParam")
        );
    }

    @Test
    public void wrongParams(){
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("incorrectParameters", () ->
                tester.testExistenceForValues(void.class,"oneIntParam","text")
        );
    }

    @Test
    public void wrongMultiParams(){
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("incorrectParameters", () ->
                tester.testExistenceForValues(void.class,"intParamStringParam","text", 23.5)
        );
    }

    @Test
    public void wrongOrderParams(){
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("incorrectParamOrder", () ->
                tester.testExistenceForValues(void.class,"intParamStringParam", "text", 5));
    }

    @Test
    public void paramConvention(){
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("paramNameUnconventional", () ->
                tester.testExistenceForValues(void.class, "paramNameNotLowerCamelCase",3)
        ); //should indicate the parameter doesn't follow naming convention
    }

    @Test
    public void accessModifier(){
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("accessModifierIncorrect", () ->
        tester.testExistenceForValues(AccessModifier.PUBLIC, void.class,"privateMethod")
        );
    }

    @Test
    public void returnedValue(){
        TestUtilities.assertMethodCallThrowsAssertionErrorInMethod("staticDeclarationIncorrect", () -> {
            int result = tester.executeStatic(Integer.class, "returnsPrimitiveInt"); //Either cast the result to an object, or ensure that the methods tester is typed
            assertEquals(2, result, "The method (deliberately) returns the wrong value");
        });
    }


    @SuppressWarnings("unused")
    private static class LocalTestClass {

            @SuppressWarnings("FieldCanBeLocal")

            //good fields
            private String myString;
            private String anotherString;
            private int myInt;

            //bad fields
            private static double myDouble;
            private static boolean someBool;
            private static boolean aBool;


            // proper getters
            public int getMyInt(){ //valid getter
            return myInt;
        }
            public boolean hasSomeBool(){
            return someBool;
        }

            //invalid getter (field is static)
            public boolean isABool(){
                return aBool;
            }

            @SuppressWarnings("SameReturnValue")
            public String getInvalidString(){ //no matching field
            return "not a valid getter";
        }

            @SuppressWarnings("SameReturnValue")
            public float getMyDouble(){ //invalid getter: wrong type
            return 0F;
        }

            public void setMyDouble(double value){ //valid setter
            myDouble = value;
        }

            public void setSomeBool(double value){ //invalid setter - wrong parameter type
            myDouble = value;
        }

            public String setMyString(String value){ //invalid setter - shouldn't return a value
            myString = value;
            return myString;
        }


    }



    @Test
    public void getters(){
        MethodsTester<LocalTestClass> localTester = new MethodsTester<>(LocalTestClass.class, null);
        Set<Method> isHasMethods = localTester.getters("is","has");
        assertEquals(2, isHasMethods.size());

        Set<Method> methods = localTester.getters(); //uses is and get as prefixes - only one match
        assertEquals(1, methods.size());
    }


    @Test
    void setters() {
        MethodsTester<LocalTestClass> localTester = new MethodsTester<>(LocalTestClass.class, null);

        assertEquals(1, localTester.setters().size());
    }

}