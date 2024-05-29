# Java Object-Oriented Feedback Tool (JOOFT)

The Testers module within this repository contains various Tester classes, designed to provide feedback on Java Programming Exercises, typically in conjunction with a unit testing framwork, such as JUnit.

It is suggested that the Testers module is compiled to a jar file to be included in any project that requires the automation of feedback on Java programming exercises.

An example [tutorial on the designing of a LibraryBook class](https://tutorials.tinyappco.com/java/classtaskautofeedback), indicates how it may be used and downloading the associate project and reviewing the supplied unit tests will allow the reader to explore how the tool is used in practice.

The testers facilitate, using Java's reflection capabilities, the creation of unit tests, or other code, to test aspects of code which students are expected to write, provided the class in which the code that will be tested exists at compile time.

No other aspect of the class needs to exist or be correct, simply the declaration (e.g., `class Foo {}`), as long as the class would compile.

Default feedback on code can be provided though the use of one of the pre-supplied interfaces, or bespoke feedback can be provided by implementing the corresponding interface(s) for the Tester class.

The **InstanceTester** class is probably the most powerful and useful class in the repository, as it allows the writing of code to invoke methods prior to the methods themselves having been written, with minimal overhead. Prior evaluation of the class with the **MethodsTester** is suggested to provide feedback on any errors in declaring methods.

The below example shows how the InstanceTester could be used to call a method on a LibraryBook class which returns a boolean

```
InstanceTester<LibraryBook> instanceTester = new InstanceTester<>(LibraryBook.class, new InstanceTestEventHandlerEN(), "War and Peace",7,3);
instanceTester.executeMethod(boolean.class,"checkOut");
```

If the method called required parameters, these would be passed in to the executeMethod method as subsequent parameters (varargs). `InstanceTestEventHandlerEN` provides default feedback via unit test assertions if execution fails, but users can provide their own customised implementations of the `InstanceTester.EventHandler` interface to suit their needs


The methods tester can be used to execute static methods, example shown here:
```
MethodsTester<Main> methodsTester = new MethodsTester<>(Main.class, new MethodTestEventHandlerEN());
int result = methodsTester.executeStatic(int.class,"getCurrentYear");
assertEquals(result,new GregorianCalendar().get(Calendar.YEAR));
```

Key Testers are as follows:

## ClassTester
Verifies typical conventions are followed when creating classes, for example that:
- static fields are final
- method names follow the lowerCamelCase convention
- method and constructor parameter names follow use lowerCamelCase
- private fields use lowerCamelCase convention
- public static fields fields use UPPER_SNAKE_CASE


## ConstructorsTester
Tests that constructors for a class are as expected, this includes checking
- whether the expected types for a constructor are in place
- that the constructor parameters are in the correct order
- that the names of the parameters follow convention
- that the constructor has the desired access modifier
- that, given a set of arguments, the object can be constructed


## FieldsTester
Tests that fields within a class are as expected, including
- that a field with a given name exists
- that the type of a given field is as expected
- that the access modifier for a field is as expected
- that the non access modifier(s) for a field are correct

## InstanceTester
Allows the invocation of methods of a class (returning the result of invoking the method on a given object), and inspection of the values of fields in a class. It can also directluy set the value of a field (regardless of access modifiers). It can identify the following errors
- Where an instance cannot be constructed
- Where a method cannot be invoked with a given set of arguments
- Where a field does not exist 

## MethodsTester
Allows the existance of instance methods to be checked, and various aspects of these to be verified, including:
- whether any method with a given name exists
- whether the correct naming convention for a method has been used
- whether a method with a specific name exists with a specified return type
- whether a method exists with a specified number of parameters
- whether a method exists with the parameter of specified types
- whether parameter types for a method are in a specified order
- whether the naming convention for a parameter is correct
- whether the access modifier for a method is correct
- whether a method is or isnt correctly declared as static
It also allows static methods to be invoked (in much the same way as an instance tester, without the requirement for an instance of the class to be created)


---
## Note on usage
Tests which check parameter names, for example, for conformance to convention require the compiler `-parameters` flag to be set to function.

---
## Research
This tool forms part of the research presented as a poster. You can view the poster abstract here: [Towards automated feedback of object-oriented programming tasks in Java](https://dl.acm.org/doi/10.1145/3610969.3611129)
