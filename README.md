# Java Object-Oriented Feedback Tool (JOOFT)

The Testers module within this repository contains various Tester classes, designed to provide feedback on Java Programming Exercises.

The testers facilitate, using Java's reflection capabilities, the creation of unit tests to test aspects of code which students are expected to write, 
provided the class in which the code that will be tested exists at compile time. Default feedback on code can be provided though the use of one of the 
pre-supplied interfaces, or bespoke feedback can be provided by implementing the corresponding interface(s) for the class

Testers are as follows:

##ClassTester
Verifies typical cvonventions are followed when creating classes, for example that:
- static fields are final
- method names follow the lowerCamelCase convention
- method and constructor parameter names follow use lowerCamelCase
- private fields use lowerCamelCase convention
- public static fields fields use UPPER_SNAKE_CASE



Tests which check parameter names, for example, for conformance to convention require the compiler `-parameters` flag to be set.
