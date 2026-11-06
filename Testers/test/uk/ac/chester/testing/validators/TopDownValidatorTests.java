package uk.ac.chester.testing.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.ac.chester.testing.*;
import uk.ac.chester.testing.handlers.*;
import uk.ac.chester.testing.reflection.PackageHelper;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Demonstrates that a class, accessed only by reflection, can be tested at the member level
 * Some tests are expected to fail, to prove that the feedback is provided
 */
public class TopDownValidatorTests {

    private ClassTester<?> classTester;
    private Class<?> clazz;

    @BeforeEach
    public void setUp() throws Exception{

        //required to ensure TestClass is loaded
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try {
            classLoader.loadClass("uk.ac.chester.testing.testclasses.TestClass");
        } catch (ClassNotFoundException e) {
            System.err.println("Unable to find class");
        }

        //Looking for a class named TestClass, package unknown
        String className = "TestClass";

        Set<Class<?>> classes = PackageHelper.findClasses(className);

        if (classes.isEmpty()) {
            fail("No class named " + className + " found. Class names are case-sensitive");
        }

        if (classes.size() > 1) {
            fail("Multiple classes found, only one class named " + className + "should be declared");
        }

        //get only item
        clazz = classes.iterator().next();

        //generic class member tests
        classTester = new ClassTester<>(clazz,new ClassTestEventHandlerEN());
    }


    @Test
    public void testFieldConventionsFollowed() {
        //should fail
        classTester.checkFields();
    }

    @Test
    public void testMethodConventionsFollowed() {
        //should fail
        classTester.checkMethods();
    }

    @Test
    public void testConstructorParams() {
        //should fail
        classTester.checkConstructorParameterNames();
    }


    @Test
    public void testConstructors() {
        ConstructorsTester.EventHandler constructorHandler = new ConstructorsTestEventHandlerEN();
        ConstructorsTester<?> constructorsTester = new ConstructorsTester<>(clazz, constructorHandler);

        Object instance = constructorsTester.test(AccessModifier.PUBLIC);
        assertNotNull(instance);

        //construct it with arguments
        Object instanceFromParams = constructorsTester.test(AccessModifier.PRIVATE, 'a', "bc", 'd');
        assertNotNull(instanceFromParams);

    }
    @Test
    public void testFields() {
        FieldsTester.EventHandler fieldHandler = new FieldsTestEventHandlerEN();
        FieldsTester<?> fieldsTester = new FieldsTester<>(clazz, fieldHandler);

        fieldsTester.test(AccessModifier.PRIVATE, int.class, "regularIvar", false);
        fieldsTester.test(AccessModifier.PUBLIC, int.class, "REGULAR_CONSTANT", false);

    }
    @Test
    public void testMethods(){
        MethodsTester.EventHandler methodHandler = new MethodTestEventHandlerEN();
        MethodsTester<?> methodsTester = new MethodsTester<>(clazz, methodHandler);

        boolean returnsPrimitiveIntExists = methodsTester.testExistenceForValues( int.class,"returnsPrimitiveInt");
        assertTrue(returnsPrimitiveIntExists);

        MethodsTester<?> methodsTester2 = new MethodsTester<>(clazz, methodHandler);
        boolean anotherMethodExists = methodsTester2.testExistenceForValues(void.class, "intParamStringParam",1,"testExistence");
        assertTrue(anotherMethodExists);
    }

    @Test
    public void testMethodExecution(){
        InstanceTester.EventHandler handler = new InstanceTestEventHandlerEN();
        InstanceTester<?> instanceTester = new InstanceTester<>(clazz,handler);
        int result = instanceTester.executeMethod(int.class, "returnsPrimitiveInt");
        assertEquals(1,result);
    }
}
