package uk.ac.chester.Testing;

public class TestClass {


    //do not change these - they are used by unit tests

    int returnsPrimitiveInt(){
        return 1;
    }

    Integer returnsInteger(){
        return 1;
    }

    void noReturn(){ }

    void oneIntParam(int p0) {}

    void twoIntParams(int p0, int p1){}

    void intParamStringParam(int p0, String p1){}


    void exceptionThrower(){
        int i = 0;
        int fail = 4/i;
    }



    /**
     * sums the values of all integers in an array
     * @param numbers
     * @return the sum of all integers in the array
     */
    int arraySum(int[] numbers){
        int total = 0;
        for (double number: numbers)
        {
            total += number;
        }
        return total;
    }

}
