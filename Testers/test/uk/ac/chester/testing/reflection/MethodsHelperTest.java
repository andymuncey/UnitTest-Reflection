package uk.ac.chester.testing.reflection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.chester.testing.reflection.MethodsHelper;
import uk.ac.chester.testing.reflection.Utilities;
import uk.ac.chester.testing.testclasses.TestClass;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MethodsHelperTest {

    private MethodsHelper<TestClass> h;

    @BeforeEach
    public void setUp()  {
        h = new MethodsHelper<>(TestClass.class);
    }

    @AfterEach
    public void tearDown()  {
        h = null;
    }


    @Test
    public void typesForArgs() {

        Object[] args = {1, 5000000000L, 1.5F, 2.345, 'a', "text"};
        Class<?>[] actualTypes = Utilities.classesForArgs(args);
        Class<?>[] expectedTypes = {Integer.class, Long.class, Float.class, Double.class, Character.class, String.class};
        assertArrayEquals(expectedTypes, actualTypes);

        Class<?>[] primitiveTypes = {int.class, long.class, float.class, double.class, char.class, String.class};
        assertNotEquals(primitiveTypes, actualTypes);
        //assertThat(primitiveTypes, IsNot.not(IsEqual.equalTo(actualTypes)));
    }


    @Test
    public void findMethods() {
        //name and return type
        assertFalse(h.findMethods(int.class, "returnsPrimitiveInt").isEmpty(), "Primitive int type");
        assertTrue(h.findMethods(Integer.class, "returnsPrimitiveInt").isEmpty(), "Primitive int type");

        assertFalse(h.findMethods(Integer.class, "returnsInteger").isEmpty(), "Class Integer type");
        assertTrue(h.findMethods(int.class, "returnsInteger").isEmpty(), "Class Integer type");
    }

    @Test
    public void findMethodsNotStrict() {
        assertFalse(h.findMethods(true, Integer.class, "returnsPrimitiveInt").isEmpty(), "Primitive int type");
        assertFalse(h.findMethods(true, int.class, "returnsInteger").isEmpty(), "Class Integer type");
    }

    @Test
    public void findMethod() {
        assertTrue(h.findMethod(false, int.class, "returnsPrimitiveInt").isPresent());
        assertFalse(h.findMethod(false, Integer.class, "returnsPrimitiveInt").isPresent());
        assertTrue(h.findMethod(true, Integer.class, "returnsPrimitiveInt").isPresent());

        assertTrue(h.findMethod(false, Integer.class, "returnsInteger").isPresent());
        assertFalse(h.findMethod(false, int.class, "returnsInteger").isPresent());
        assertTrue(h.findMethod(true, int.class, "returnsInteger").isPresent());

        assertTrue(h.findMethod(false, void.class, "noReturn").isPresent());
        assertFalse(h.findMethod(false, Void.class, "noReturn").isPresent());
        assertTrue(h.findMethod(true, Void.class, "noReturn").isPresent());

    }

    @Test
    public void methodParamNames() {
    String[] paramNames = {"p0", "p1"};
    String message = "Ensure that the -parameters argument is passed to the compiler and check you;ve recompiled the project";
    assertArrayEquals(paramNames,h.methodParamNames(void.class,"twoIntParams",int.class, int.class), message);
    }

    @Test
    public  void methodForParams(){
        {
            Optional<Method> optionalMethod = h.methodForParams(true, void.class, "twoIntParams", 2, 3);
            assert (optionalMethod.isPresent());
            assertEquals("twoIntParams", optionalMethod.get().getName());
        }
        {
            Optional<Method> optionalMethod = h.methodForParams(false, Integer.class, "returnsInteger");
            assert (optionalMethod.isPresent());
            assertEquals("returnsInteger", optionalMethod.get().getName());
        }
        {
            Optional<Method> optionalMethod = h.methodForParams(true, int.class, "returnsInteger");
            assert (optionalMethod.isPresent());
            assertEquals("returnsInteger", optionalMethod.get().getName());
        }
        {
            Optional<Method> optionalMethod = h.methodForParams(false, int.class, "returnsInteger");
            assert (optionalMethod.isEmpty());
        }

    }


    @Test
    void methodsWithSignature(){
        Set<Method> methods = h.methodsWithSignature(void.class, int.class, String.class);
        assertTrue(methods.stream().findFirst().isPresent());
        assertEquals("intParamStringParam",methods.stream().findFirst().get().getName());

    }


}