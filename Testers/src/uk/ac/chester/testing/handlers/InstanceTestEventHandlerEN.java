package uk.ac.chester.testing.handlers;

import static org.junit.jupiter.api.Assertions.fail;
import uk.ac.chester.testing.ClassDescriber;
import uk.ac.chester.testing.InstanceTester;
import uk.ac.chester.testing.reflection.Utilities;

public class InstanceTestEventHandlerEN implements InstanceTester.EventHandler, ClassDescriber {
    @Override
    public void cannotConstructWithArgs(String className, Object[] args) {

        fail("Couldn't construct an instance of " + className + " with the supplied arguments: " + Utilities.commaSeparatedArgList(args));
    }

    @Override
    public void notConstructed() {
        fail("No constructed instance of the class is available to evaluate");
    }

    @Override
    public void cannotInvokeMethod(Class returnType, String name, Object[] args) {

        fail("Unable to invoke method " + name);
    }

    @Override
    public void cannotRetrieveFieldValue(Class type, String name) {
        fail("Unable to retrieve value from field named '" + name + "' of type: " + type.getSimpleName());
    }

    @Override
    public void fieldNotInitialized(Class<?> type, String name) {
        fail("After construction, the field named '"+ name + "' of type: " + type.getSimpleName()+ " has not been initialised" +
                " (i.e. it is null). " + System.lineSeparator() +
                "It's generally best to avoid fields having a null value, instead the value should be set by the constructor");
    }

//    @Override
//    public void throwsException(Class returnType, String name, Object[] args, Exception e) {
//
//        final String[] ignoredPackages = {
//                "com.intellij",
//                "java",
//                "jdk",
//                "junit",
//                "org.hamcrest",
//                "org.jetbrains",
//                "org.junit",
//                "sun",
//                "uk.ac.chester.testing"
//        };
//
//        //process stack trace
//        StringBuilder stackTrace = new StringBuilder(e.getCause().toString() + System.lineSeparator() + System.lineSeparator());
//
//        outerloop:
//        for (StackTraceElement item : e.getStackTrace()) {
//
//            for (String ignoredPackage : ignoredPackages){
//                if (item.toString().startsWith(ignoredPackage)){
//                    continue outerloop;
//                }
//            }
//
//            stackTrace.append(item.toString());
//            stackTrace.append(System.lineSeparator());
//
//        }
//
//        StringBuilder arguments = new StringBuilder();
//        boolean first = true;
//        for (Object object: args){
//            if (first){
//                first = false;
//            } else {
//             arguments.append(", ");
//            }
//            arguments.append(object.toString());
//        }
//
//        String argumentsStatements = args.length > 0 ? " when passed the arguments "+arguments.toString() : "";
//        Assert.fail("The " + name + " method"+argumentsStatements+" threw an exception as follows: " + System.lineSeparator() + stackTrace.toString());
//
//    }
}
