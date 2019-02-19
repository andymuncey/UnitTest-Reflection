package uk.ac.chester.testing;

public class TestClass {

    public TestClass(){ }

    public TestClass(int intParam){}

    public TestClass(int anInt, String aString){}

    private TestClass(char myChar, String myString, char myChar2){}

    public TestClass(float xyz){
        throw new RuntimeException("Failing constructor");
    }


    public static String nonFinal;

    private int regularIvar;
    public static final int REGULAR_CONSTANT = 3;


    private void BadNamedMethod(){}


    //bad param name
    public TestClass(double A){}


    //do not change these - they are used by unit tests

    int returnsPrimitiveInt(){ return 1; }

    Integer returnsInteger(){ return 1; }

    void noReturn(){ }

    void oneIntParam(int p0) {}

    void twoIntParams(int p0, int p1){}

    void intParamStringParam(int p0, String p1){}

    void paramNameNotLowerCamelCase(int Number){}

    void paramNameTooShort(int x){}

    @SuppressWarnings("divzero")
    void exceptionThrower(){
        int i = 0;
        int fail = 4/i;
    }

}
