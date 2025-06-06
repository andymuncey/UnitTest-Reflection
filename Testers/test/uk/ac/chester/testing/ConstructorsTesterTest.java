package uk.ac.chester.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import uk.ac.chester.testing.testclasses.TestClass;
import uk.ac.chester.testing.handlers.ConstructorsTestEventHandlerEN;

import static org.junit.jupiter.api.Assertions.*;

public class ConstructorsTesterTest {

    private ConstructorsTester<TestClass> t;

    @BeforeEach
    public void setUp() {
        ConstructorsTestEventHandlerEN handler = new ConstructorsTestEventHandlerEN();
        t  = new ConstructorsTester<>(TestClass.class, handler);
    }

    @AfterEach
    public void tearDown() {
        t = null;
    }



    @Test
    public void testIncorrectParameters() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () ->
                t.test(AccessModifier.PUBLIC,"str1", "Str2"));
        Assertions.assertEquals("incorrectParameters", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));

    }

    @Test
    public void testWrongOrderParameters() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> t.test(AccessModifier.PUBLIC,"str1", 4));
        assertEquals("incorrectParamOrder", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));

    }

    @Test
    public void testUnconventionalParamNames(){
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> t.test(AccessModifier.PUBLIC, 3.2));
        assertEquals("paramNameUnconventional", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));

    }

    @Test
    public void testWrongAccessModifierMethodIsPrivate() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> t.test(AccessModifier.PUBLIC, 's', "Hello", 's'));
        assertEquals("wrongAccessModifier", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));

    }

    @Test
    public void testWrongAccessModifierMethodIsPublic(){
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> t.test(AccessModifier.PRIVATE, 4, "str"));
        assertEquals("wrongAccessModifier", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));
    }

    @Test
    public void testExceptionThrowingConstructor(){
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class, () -> t.test(AccessModifier.PUBLIC, 3.2F));
        assertEquals("constructionFails", TestUtilities.firstNonTestingMethodName(thrown.getStackTrace()));
    }

    @Test
    public void rightOrderParameters(){
       TestClass instance = t.test(AccessModifier.PUBLIC,4, "str");
       assertNotNull(instance);
    }

    @Test
    public void constructorWithNoArgsExistsTest(){

       assertTrue(t.constructorWithNoArgsExists(true));
       assertTrue(t.constructorWithNoArgsExists(false));

        ConstructorsTestEventHandlerEN handler = new ConstructorsTestEventHandlerEN();

        Object x = new Object(){
            @SuppressWarnings("unused")
            private String MY_STRING;
        };

        ConstructorsTester<?> tester = new ConstructorsTester<>(x.getClass(), handler);
        assertFalse(tester.constructorWithNoArgsExists(true));
        assertFalse(tester.constructorWithNoArgsExists(false));

    }


}