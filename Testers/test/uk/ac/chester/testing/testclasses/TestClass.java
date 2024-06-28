package uk.ac.chester.testing.testclasses;

import java.util.ArrayList;

@SuppressWarnings("EmptyMethod")
public class TestClass {



    public TestClass(){ }

    public TestClass(int intParam){}

    public TestClass(int anInt, String aString){}

    private TestClass(char myChar, String myString, char myChar2){}

    public TestClass(float xyz){
        throw new RuntimeException("Failing constructor");
    }


    //Property for testing
    private int intProperty;

    public int getIntProperty() {
        return intProperty;
    }

    public void setIntProperty(int intProperty) {
        this.intProperty = intProperty;
    }



    public static String nonFinal;

    private int regularIvar;
    public static final int REGULAR_CONSTANT = 3;


    private void BadNamedMethod(){}


    private void privateMethod(){}

    void nonStaticMethod(){}
    static void staticMethod(){}

    static int staticInt(){
        return 1;
    }

    private String uninitialisedField;

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

    static void doubleArrayListContents(ArrayList<String> list)   {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            list.add(list.get(i));
        }
    }

    @SuppressWarnings("divzero")
    void exceptionThrower(){
        int i = 0;
        int fail = 4/i;
    }


}
