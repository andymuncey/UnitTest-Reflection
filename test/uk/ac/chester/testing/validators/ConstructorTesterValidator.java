package uk.ac.chester.testing.validators;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.chester.testing.*;
import uk.ac.chester.testing.handlers.ConstructorTestEventHandlerEN;

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
        t.testConstructor(AccessModifier.PUBLIC,"str1", "Str2");
    }

    @Test
    public void testWrongOrderParameters() {
        t.testConstructor(AccessModifier.PUBLIC,"str1", 4);
    }

    @Test
    public void testUnconventionalParamNames(){
        t.testConstructor(AccessModifier.PUBLIC, 3.2);
    }

    @Test
    public void testWrongAccessModifierMethodIsPrivate(){
        t.testConstructor(AccessModifier.PUBLIC,'s', "Hello", 's');
    }

    @Test
    public void testWrongAccessModifierMethodIsPublic(){
        t.testConstructor(AccessModifier.PRIVATE,4, "str");
    }

    @Test
    public void testExceptionThrowingConstructor(){
        t.testConstructor(AccessModifier.PUBLIC, 3.2F);
    }

    //endregion



    //region Tests that should pass

    @Test
    public void rightOrderParameters(){
        t.testConstructor(AccessModifier.PUBLIC,4, "str");
    }


    //endregion

}