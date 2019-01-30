package uk.ac.chester.Testing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClassTesterValidator {

    ClassTester<TestClass> t;

    @Before
    public void setUp() throws Exception {
      t  = new ClassTester<>(TestClass.class ,new ClassTestEventHandlerEN());
    }

    @After
    public void tearDown() throws Exception {
        t = null;
    }

    //region Tests that should fail

    @Test
    public void testIncorrectParameters() {
        t.test(ExecutableTester.AccessModifier.PUBLIC,"str1", "Str2");
    }

    @Test
    public void testWrongOrderParameters() {
        t.test(ExecutableTester.AccessModifier.PUBLIC,"str1", 4);
    }

    @Test
    public void testUnconventionalParamNames(){
        t.test(ExecutableTester.AccessModifier.PUBLIC, 3.2);
    }

    @Test
    public void testWrongAccessModifierMethodIsPrivate(){
        t.test(ExecutableTester.AccessModifier.PUBLIC,'s', "Hello", 's');
    }

    @Test
    public void testWrongAccessModifierMethodIsPublic(){
        t.test(ExecutableTester.AccessModifier.PRIVATE,4, "str");
    }

    @Test
    public void testExceptionThrowingConstructor(){
        t.test(ExecutableTester.AccessModifier.PUBLIC, 3.2F);
    }


    //endregion

    //region Tests that should pass

    @Test
    public void rightOrderParameters(){
        ClassTester<TestClass> t = new ClassTester<>(TestClass.class ,new ClassTestEventHandlerEN());
        t.test(ExecutableTester.AccessModifier.PUBLIC,4, "str");
    }


    //endregion

}