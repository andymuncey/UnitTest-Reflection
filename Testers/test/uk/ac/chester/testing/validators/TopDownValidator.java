package uk.ac.chester.testing.validators;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.chester.testing.*;
import uk.ac.chester.testing.handlers.ClassTestEventHandlerEN;
import uk.ac.chester.testing.handlers.ConstructorsTestEventHandlerEN;
import uk.ac.chester.testing.handlers.FieldsTestEventHandlerEN;
import uk.ac.chester.testing.handlers.MethodTestEventHandlerEN;

import java.util.Set;

public class TopDownValidator {

    @Test
    public void test(){

        //Looking for a class named TestClass, package unknown
        String className = "TestClass";

        Set<Class> classes = PackageHelper.findClasses(className);

        if (classes.isEmpty()) {
            Assert.fail("No class named " + className + " found. Class names are case sensitive");
        }

        if (classes.size() > 1) {
            Assert.fail("Multiple classes found, only one class named " + className + "should be declared");
        }

        //get only item
        Class aClass = (Class)classes.toArray()[0];

        //generic class member tests
        @SuppressWarnings("unchecked")
        ClassTester classTester = new ClassTester(aClass,new ClassTestEventHandlerEN());
        classTester.checkFields();
        classTester.checkMethods();
        classTester.checkConstructorParameterNames();

        //individual constructor tests
        //construct it using no arg constructor
        ConstructorsTester.EventHandler constructorHandler = new ConstructorsTestEventHandlerEN();
        @SuppressWarnings("unchecked")
        ConstructorsTester constructorsTester = new ConstructorsTester<>(aClass,constructorHandler);
        Object instance = constructorsTester.test(AccessModifier.PUBLIC);
        Assert.assertNotNull(instance);

        //construct it with arguments
        Object instanceFromParams = constructorsTester.test(AccessModifier.PRIVATE,'a',"bc",'d');
        Assert.assertNotNull(instanceFromParams);

        //test specific fields
        FieldsTester.EventHandler fieldHandler = new FieldsTestEventHandlerEN();
        @SuppressWarnings("unchecked")
        FieldsTester fieldsTester = new FieldsTester(aClass,fieldHandler);
        fieldsTester.test(AccessModifier.PRIVATE,"regularIvar",int.class,false);
        fieldsTester.test(AccessModifier.PUBLIC, "REGULAR_CONSTANT", int.class, false);

        //test specific methods
        MethodTester.EventHandler methodHandler = new MethodTestEventHandlerEN();
        @SuppressWarnings("unchecked")
        MethodTester methodTester = new MethodTester(aClass, int.class,"returnsPrimitiveInt",methodHandler);
        Object result = methodTester.test();
        Assert.assertEquals(1,result);

        @SuppressWarnings("unchecked")
        MethodTester<Void, Object> methodTester2 = new MethodTester(aClass, void.class, "intParamStringParam", methodHandler);
        Object result2 = methodTester2.test(1,"test");
        Assert.assertNull(result2);
    }
}
