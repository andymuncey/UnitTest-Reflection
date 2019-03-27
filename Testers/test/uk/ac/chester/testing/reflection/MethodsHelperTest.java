package uk.ac.chester.testing.reflection;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.chester.testing.reflection.MethodsHelper;
import uk.ac.chester.testing.reflection.Utilities;
import uk.ac.chester.testing.testclasses.TestClass;

public class MethodsHelperTest {

    private MethodsHelper<TestClass> h;

    @Before
    public void setUp()  {
        h = new MethodsHelper<>(TestClass.class);
    }

    @After
    public void tearDown()  {
        h = null;
    }


    @Test
    public void typesForArgs() {

        Assert.assertNotEquals(Integer.class, int.class); //just as a reminder
        Assert.assertNotEquals(Void.class, void.class); //also a reminder

        Object[] args = {1, 5000000000L, 1.5F, 2.345, 'a', "text"};
        Class[] actualTypes = Utilities.classesForArgs(args);
        Class[] expectedTypes = {Integer.class, Long.class, Float.class, Double.class, Character.class, String.class};
        Assert.assertArrayEquals(expectedTypes, actualTypes);

        Class[] primitiveTypes = {int.class, long.class, float.class, double.class, char.class, String.class};
        Assert.assertThat(primitiveTypes, IsNot.not(IsEqual.equalTo(actualTypes)));
    }


    @Test
    public void findMethods() {
        //name and return type
        Assert.assertFalse("Primitive int type", h.findMethods(int.class, "returnsPrimitiveInt").isEmpty());
        Assert.assertTrue("Primitive int type", h.findMethods(Integer.class, "returnsPrimitiveInt").isEmpty());

        Assert.assertFalse("Class Integer type", h.findMethods(Integer.class, "returnsInteger").isEmpty());
        Assert.assertTrue("Class Integer type", h.findMethods(int.class, "returnsInteger").isEmpty());
    }

    @Test
    public void findMethodsNotStrict() {
        Assert.assertFalse("Primitive int type", h.findMethods(true, Integer.class, "returnsPrimitiveInt").isEmpty());
        Assert.assertFalse("Class Integer type", h.findMethods(true, int.class, "returnsInteger").isEmpty());
    }

    @Test
    public void findMethod() {
        Assert.assertTrue(h.findMethod(false, int.class, "returnsPrimitiveInt").isPresent());
        Assert.assertFalse(h.findMethod(false, Integer.class, "returnsPrimitiveInt").isPresent());
        Assert.assertTrue(h.findMethod(true, Integer.class, "returnsPrimitiveInt").isPresent());

        Assert.assertTrue(h.findMethod(false, Integer.class, "returnsInteger").isPresent());
        Assert.assertFalse(h.findMethod(false, int.class, "returnsInteger").isPresent());
        Assert.assertTrue(h.findMethod(true, int.class, "returnsInteger").isPresent());

        Assert.assertTrue(h.findMethod(false, void.class, "noReturn").isPresent());
        Assert.assertFalse(h.findMethod(false, Void.class, "noReturn").isPresent());
        Assert.assertTrue(h.findMethod(true, Void.class, "noReturn").isPresent());

    }

    @Test
    public void methodParamNames() {
    String[] paramNames = {"p0", "p1"};
    String message = "Ensure that the -parameters argument is passed to the compiler and check you;ve recompiled the project";
    Assert.assertArrayEquals(message, paramNames,h.methodParamNames(void.class,"twoIntParams",int.class, int.class));
    }

    @Test
    public void methodsWithSignature() {
    }

    //todo: move to test for ConstructorsHelper (in reflection package)

//    @Test
//    public void constructorAutoBoxing(){
//
//        Optional<Constructor<TestClass>> c = h.constructorForParamTypes(true,false,true,int.class);
//        Assert.assertTrue(c.isPresent());
//
//        Optional<Constructor<TestClass>> nonExistentC = h.constructorForArgTypes(true,false,true, Integer.class);
//        Assert.assertFalse(nonExistentC.isPresent());
//    }


}