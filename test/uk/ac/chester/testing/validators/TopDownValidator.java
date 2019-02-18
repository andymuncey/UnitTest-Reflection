package uk.ac.chester.testing.validators;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.chester.testing.*;

import java.util.Set;


public class TopDownValidator {

    @Test
    public void testMyClass(){

        //assuming we're looking for TestClass, package unknown

        String className = "TestClass";

        Set<Class> classes = PackageHelper.findClasses(className);

        if (classes.isEmpty()) {
            Assert.fail("No class named " + className + " found");
        }

        if (classes.size() > 1) {
            Assert.fail("Multiple classes found, only one class named " + className + "should be declared");
        }

        for (Class aClass : classes){

            //can we construct it

//            ConstructorTester constructorTester = new ConstructorTester(aClass,new ConstructorTestEventHandlerEN());
//            constructorTester.testConstructor(AccessModifier.PUBLIC,);




        }





    }








}
