package uk.ac.chester.testing.validators;

import uk.ac.chester.testing.TestClass;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.chester.testing.*;
import uk.ac.chester.testing.handlers.ConstructorsTestEventHandlerEN;

public class ConstructorsTesterValidator {

    ConstructorsTester<TestClass> t;

    @Before
    public void setUp() throws Exception {
        ConstructorsTestEventHandlerEN handler = new ConstructorsTestEventHandlerEN();
        t  = new ConstructorsTester<>(TestClass.class, handler);
    }

    @After
    public void tearDown() throws Exception {
        t = null;
    }

    //region Tests that should fail

    @Test
    public void testIncorrectParameters() {
        t.test(AccessModifier.PUBLIC,"str1", "Str2");
    }

    @Test
    public void testWrongOrderParameters() {
        t.test(AccessModifier.PUBLIC,"str1", 4);
    }

    @Test
    public void testUnconventionalParamNames(){
        t.test(AccessModifier.PUBLIC, 3.2);
    }

    @Test
    public void testWrongAccessModifierMethodIsPrivate(){
        t.test(AccessModifier.PUBLIC,'s', "Hello", 's');
    }

    @Test
    public void testWrongAccessModifierMethodIsPublic(){
        t.test(AccessModifier.PRIVATE,4, "str");
    }

    @Test
    public void testExceptionThrowingConstructor(){
        t.test(AccessModifier.PUBLIC, 3.2F);
    }

    //endregion



    //region Tests that should pass

    @Test
    public void rightOrderParameters(){
        t.test(AccessModifier.PUBLIC,4, "str");
    }


    //endregion

}