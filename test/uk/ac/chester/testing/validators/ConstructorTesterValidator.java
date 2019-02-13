package uk.ac.chester.testing.validators;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.chester.testing.*;

public class ConstructorTesterValidator {

    ConstructorTester<TestClass> t;

    @Before
    public void setUp() throws Exception {
        ConstructorTestEventHandlerEN handler = new ConstructorTestEventHandlerEN();
        t  = new ConstructorTester<>(TestClass.class, handler);
    }

    @After
    public void tearDown() throws Exception {
        t = null;
    }

    //region Tests that should fail


    @Test
    public void testIncorrectParameters() {
        t.testConstructor(ExecutableTester.AccessModifier.PUBLIC,false,"str1", "Str2");
    }

    @Test
    public void testWrongOrderParameters() {
        t.testConstructor(ExecutableTester.AccessModifier.PUBLIC,false,"str1", 4);
    }

    @Test
    public void testUnconventionalParamNames(){
        t.testConstructor(ExecutableTester.AccessModifier.PUBLIC,false, 3.2);
    }

    @Test
    public void testWrongAccessModifierMethodIsPrivate(){
        t.testConstructor(ExecutableTester.AccessModifier.PUBLIC,false,'s', "Hello", 's');
    }

    @Test
    public void testWrongAccessModifierMethodIsPublic(){
        t.testConstructor(ExecutableTester.AccessModifier.PRIVATE,false,4, "str");
    }

    @Test
    public void testExceptionThrowingConstructor(){
        t.testConstructor(ExecutableTester.AccessModifier.PUBLIC,false, 3.2F);
    }

    //endregion





    //region Tests that should pass

    @Test
    public void rightOrderParameters(){
        t.testConstructor(ExecutableTester.AccessModifier.PUBLIC,false,4, "str");
    }


    //endregion

}