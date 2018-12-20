package uk.ac.chester;
import org.junit.*;
import uk.ac.chester.Testing.MethodTester;
import uk.ac.chester.Testing.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;


public class ProblemsTest {

    Problems tools;
    ReflectionHelper helper;


    MethodTester.MethodTestEventHandler testEventHandler;

    class TestEventHandler implements MethodTester.MethodTestEventHandler {

        @Override
        public void notFound(String methodName) {
            Assert.fail("No method with the name \""+ methodName + "\" was found");
        }

        @Override
        public void incorrectReturnType(String methodName, Class requiredReturnType) {
            Assert.fail("A method named \"" + methodName + "\" was found, but it does not return the correct type: " + requiredReturnType.getName());
        }

        @Override
        public void incorrectParameters(String methodName, Class[] requiredParamTypes) {
            Assert.fail("Method \"" + methodName + "\" found, but parameters are incorrect: " + Arrays.toString(requiredParamTypes));
        }

         @Override
        public void incorrectParamOrder(String methodName, Class[] requiredParams) {
            Assert.fail("Method \"" + methodName + "\" found, but parameters are not in the correct order");
        }
    }

    @Before
    public void setUp() throws Exception {
        tools = new Problems();
        helper = new ReflectionHelper(tools);

        testEventHandler = new TestEventHandler();
    }

    @After
    public void tearDown() throws Exception {
        tools = null;
    }


    //region too easy to google
    @Test
    public void isPalendrome() throws Exception {

        MethodTester<Boolean> tester = new MethodTester(Problems.class, boolean.class, "isPalindrome", testEventHandler);

        Assert.assertTrue("racecar is palindrome", tester.test("racecar"));

        Assert.assertTrue("racecar is palindrome",tools.isPalindrome("racecar"));
        Assert.assertTrue("Noon is palindrome",tools.isPalindrome("noon"));
        Assert.assertFalse("dead is not palindrome",tools.isPalindrome("dead"));
    }

    @Test
    public void isAnagram() throws Exception {
        //solution worth 4 marks
        Assert.assertTrue("car and arc are anagrams", tools.isAnagram("car","arc"));
        Assert.assertTrue("angered and enraged are anagrams", tools.isAnagram("angered","enraged"));
        Assert.assertFalse("carpet and repeat are NOT anagrams", tools.isAnagram("carpet","repeat"));
    }

        /* ******************** Worth 4 marks ******************** */
    //create a method called reversedSentence which takes a String as a parameter and returns a String
    //reverse the order of WORDS (but not letters in the word) in the String provided
    //e.g. "the cat sat on the mat" would become "mat the on sat cat the"
    //there will be no full stops in the sentence provided

    @org.junit.Test
    public void reversedSentence() throws Exception {



        Assert.assertEquals("the cat sat mat",tools.reversedSentence("mat sat cat the"));
        Assert.assertEquals("reversed words",tools.reversedSentence("words reversed"));
    }
    //endregion

    //region too easy to solve using built in classes

    //create a method called numberFromBinaryString which will take a String parameter and return an int
    //convert the values in the parameter (which will be a string of 1 and 0 characters) into an int
    //once you have written the method uncomment the test below to test it (the same applies to later tasks)
    @org.junit.Test
    public void numberFromBinaryString() throws Exception {
        Assert.assertEquals("test with 1",1,tools.numberFromBinaryString("1"));
        Assert.assertEquals("test with 15",15,tools.numberFromBinaryString("1111"));
        Assert.assertEquals("test with 139485",139485,tools.numberFromBinaryString("100010000011011101"));
        Assert.assertEquals("test with max int value",2147483647,tools.numberFromBinaryString("1111111111111111111111111111111"));
    }

    @Test
    public void charactersFromWordInAlphabeticalOrder() throws Exception {
        Assert.assertEquals("hello becomes ehllo","ehllo",tools.charactersFromWordInAlphabeticalOrder("hello"));
        Assert.assertEquals("abacus becomes aabcsu","aabcsu",tools.charactersFromWordInAlphabeticalOrder("abacus"));
        Assert.assertEquals("constrain becomes acinnorst","acinnorst",tools.charactersFromWordInAlphabeticalOrder("constrain"));
    }

    //endregion

    //region boolean


    @Test
    public void _1a_isTooCold() throws Exception {

        MethodTester<Boolean> tester = new MethodTester(Problems.class, Boolean.class, "isTooCold", testEventHandler);

        boolean tooCold = tester.test(25.0);

        Assert.assertFalse("25 is not too cold", tester.test(25.0));

        Assert.assertFalse("18 is not too cold", tester.test(18.0));
        Assert.assertTrue("17.5 is too cold", tester.test(17.5));
        Assert.assertTrue("-28 is too cold", tester.test(-28.0));

        //todo: figure out how to


//        Assert.assertFalse("25 is not too cold", tools.isTooCold(25.0));
//        Assert.assertFalse("18 is not too cold", tools.isTooCold(18.0));
//        Assert.assertTrue("17.5 is too cold", tools.isTooCold(17.5));
//        Assert.assertTrue("-28 is too cold", tools.isTooCold(-28));

    }
    
    
    @Test
    public void isTooLongForTweet() throws Exception {

        MethodTester<Boolean> tester = new MethodTester<>(Problems.class, boolean.class, "over140chars", testEventHandler);
        Assert.assertFalse("Short tweet", tester.test("Hello world"));
        Assert.assertFalse("Max length tweet ", tester.test("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim"));

        //Assert.assertFalse("Short tweet", tools.over140chars("Hello world"));
        //Assert.assertFalse("Max length tweet ", tools.over140chars("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim"));
        Assert.assertTrue("Slightly too long tweet", tools.over140chars("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim ad"));
        Assert.assertTrue("long tweet",tools.over140chars("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur"));
    }

    @Test
    public void isTooHot() throws Exception {
        Assert.assertFalse("25 is not too hot", tools.isTooHot(25));
        Assert.assertTrue("26 is too hot", tools.isTooHot(26));
        Assert.assertFalse("-28 is not too hot", tools.isTooHot(-28));
    }

    //endregion

    //region hard boolean

    @Test
    public void parallel() throws Exception {

        int x1 = 0, y1 = 0;
        int x2 = 5, y2 = 5;
        int x3 = 0, y3 = 0;
        int x4 = 5, y4 = 5;

        //identical lines
        Assert.assertTrue("Identical lines", tools.parallel(x1,y1,x2,y2,x3,y3,x4,y4));

        //longer line
        x4 = 10; y4 = 10;
        Assert.assertTrue("longer lines parallel", tools.parallel(x1,y1,x2,y2,x3,y3,x4,y4));

        //shift up
        y1 += 4;
        y2 += 4;
        Assert.assertTrue("different y position", tools.parallel(x1,y1,x2,y2,x3,y3,x4,y4));

        //shift left
        x3 -= 30;
        x4 -= 30;
        Assert.assertTrue("different x and y positions", tools.parallel(x1,y1,x2,y2,x3,y3,x4,y4));

        //flip start and end
        int xTemp = x1;
        x1 = x2;
        x2 = xTemp;

        int yTemp = y1;
        y1 = y2;
        y2 = yTemp;

        Assert.assertTrue("line runs different direction", tools.parallel(x1,y1,x2,y2,x3,y3,x4,y4));

        y4 += 2;
        Assert.assertFalse("Same start, different end for y", tools.parallel(x1,y1,x2,y2,x3,y3,x4,y4));

        x1 += 200;
        Assert.assertFalse("x1 now way off", tools.parallel(x1,y1,x2,y2,x3,y3,x4,y4));

        //probably needs more tests
    }

    //could add, based on coordinates (ripped off Schmalz, 1986):

    //is square

    //is rectangle
    //is parallelogram
    //is perpendicular
    //is triangle
    //is right angled triagle



    //endregion

    //region array access
    
    
    
    
    
    @Test
    public void maxValue() throws Exception {
//NEW
        int[] numbers = {1,4,23,76,98,24,9};
        Assert.assertEquals(98,tools.maxValue(numbers));

        int[] negativeNumbers = {-34, -19472, -846, -21, -923746, -294};
        Assert.assertEquals(-21,tools.maxValue(negativeNumbers));

        int[] mixedNumbers = {-34, -19472, 1,4,23,76,64854,24,9, -846, -21, -923746, -294, };
        Assert.assertEquals(64854,tools.maxValue(mixedNumbers));
    }

    @Test
    public void stringsMatch() throws Exception {
        String[] items = {"Apple", "Dog", "Ball", "Object", "Dog", "Cheese", "Printer"};
        Assert.assertTrue(tools.stringsMatch(items,1,4));
        Assert.assertFalse(tools.stringsMatch(items,2,5));
    }

    //could add ints match to test alternate form of equality check

    //endregion

    //region simple mathematics

    @Test
    public void temperatureInKelvin() throws Exception {

        //todo: renaming the testWithTolerance method to simply 'test' fails as it cannot disambiguate between methods (due to varargs), need to figure out fix for this
        MethodTester<Double> tester = new MethodTester(Problems.class, Double.class, "temperatureInKelvin", testEventHandler);
//        tester.testWithTolerance("15.0 in Fahrenheit is 263.7 in Kelvin",263.7,0.01,15.0);
//        tester.testWithTolerance("-452.2 in Fahrenheit is 4.15 in Kelvin",4.15,0.01, -452.2);

//        Assert.assertEquals("15.0 in Fahrenheit is 263.7 in Kelvin",263.7,tools.temperatureInKelvin(15.0),0.01);
//        Assert.assertEquals("-452.2 in Fahrenheit is 4.15 in Kelvin",4.15,tools.temperatureInKelvin(-452.2),0.01);
    }

    @Test
    public void sumOfThreeNumbers() throws Exception {
        Assert.assertEquals("Sum of 1,2,3 should be 6",6, tools.sumOfThreeNumbers(1, 2, 3) );
        Assert.assertEquals("Sum of -2,-3,-4 should be -9",-9, tools.sumOfThreeNumbers(-2, -3, -4));
    }

    @Test
    public void productOfThreeNumbers() throws Exception {
        Assert.assertEquals("product of 1 and 2 and 3 is 6",6,tools.productOfThreeNumbers(1,2,3));
        Assert.assertEquals("product of -4 and 7 and 2 is -56",-56,tools.productOfThreeNumbers(-4,7,2));
    }

    @Test
    public void averageOfThreeNumbers() throws Exception {
        Assert.assertEquals("average of 3,4,5 should be 4", 4, tools.averageOfThreeNumbers(3, 4, 5));
        Assert.assertEquals("average of 11,13,19 should be 14", 14, tools.averageOfThreeNumbers(11, 13, 19));
    }


    //endregion

    //region maths with loops
    @Test
    public void sumOfNumbersFromOneToN() throws Exception {
        Assert.assertEquals("numbers from 1 to 5 should be 15", 15, tools.sumOfNumbersFromOneToN(5));
        Assert.assertEquals("numbers from 1 to 100 should be 5050", 5050, tools.sumOfNumbersFromOneToN(100));
    }

    @Test
    public void highestNumberBelowNDivisibleByTwoNumbers() throws Exception {
        Assert.assertEquals("13 with 3 & 4: expected 12",12, tools.highestNumberBelowNDivisibleByTwoNumbers(3, 4, 13));
        Assert.assertEquals("11 with 3 & 4: expected 0", 0, tools.highestNumberBelowNDivisibleByTwoNumbers(3, 4, 11));
        Assert.assertEquals("239 with 6 & 5: expected 210", 210, tools.highestNumberBelowNDivisibleByTwoNumbers(6, 5, 239));
        Assert.assertEquals("21 with 2 & 5: expected 20", 20, tools.highestNumberBelowNDivisibleByTwoNumbers(2, 5, 21));
    }

    //endregion

    //region maths with arrays

    @Test
    public void arraySum() throws Exception {
        int[] numbers = {1, 4, 6, 2, 45, 7};
        Assert.assertEquals(65,tools.arraySum(numbers));

        int[] numbers2 = {1, 4, -6, 2, 45, 7};
        Assert.assertEquals(53,tools.arraySum(numbers2));

        int[] number = {-2};
        Assert.assertEquals(-2,tools.arraySum(number));

        int[] emptyArray = {};
        int resultWithEmpty = tools.arraySum(emptyArray);
        Assert.assertEquals("empty array",resultWithEmpty,0);

        int[] singleItemArray = {4};
        int resultSingleItem = tools.arraySum(singleItemArray);
        Assert.assertEquals("single item array", 4,resultSingleItem);

        int[]multiItemArray = {3,6,8};
        int resultMultiItem = tools.arraySum(multiItemArray);
        Assert.assertEquals("multi item array",17,resultMultiItem);

        int[]largerValuesArray = {951, 762, 60485};
        int resultLarger = tools.arraySum(largerValuesArray);
        Assert.assertEquals("larger values",62198, resultLarger);
    }


    @Test
    public void arrayAverage() throws Exception {

        MethodTester<Integer> tester = new MethodTester(Problems.class, int.class,"arrayAverage", testEventHandler);

        int[] numbers = {1, 4, 6, 2, 45, 7};
        int[] moreNumbers = {321, 523, 855, 275, 834, 276, 275, 231, 845, 264};

        Assert.assertEquals("average of 1,4,6,2,45,7 should be 10", 10, (int)tester.test((Object)(numbers)));
        Assert.assertEquals("average should be 469", 469, (int)tester.test((Object)moreNumbers));

        //Assert.assertEquals("average of 1,4,6,2,45,7 should be 10", 10, tools.arrayAverage(numbers));
        //Assert.assertEquals("average should be 469", 469, tools.arrayAverage(moreNumbers));
    }

    @Test
    public void productOfNumbersInArray() throws Exception {
        double[] array = {5.0,4.0,6.0};
        Assert.assertEquals("numbers in array multiplied",120.0,tools.productOfNumbersInArray(array),0.01);
        double[] array2 = {-3.0,4.5,2.0};
        Assert.assertEquals("numbers in array multiplied with negative",-27.0,tools.productOfNumbersInArray(array2),0.01);
        double[] smallArray = {2.3};
        Assert.assertEquals("one item array",2.3,tools.productOfNumbersInArray(smallArray),0.01);

        double[] largerArray = {1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0};
        Assert.assertEquals("larger array",40320.0,tools.productOfNumbersInArray(largerArray),0.01);
    }


    @Test
    public void lastIndexOfMultipleInArray() throws Exception {
        int[] intArray = {1,2,3,4,5,6,7,8,9};
//        MethodTester<> tester = new MethodTester(Problems.class,"lastIndexOfMultipleInArray", testEventHandler);
//        tester.test(7,4,intArray);
//        tester.test(8,3,intArray);
//        tester.test(4,5,intArray);

//        Assert.assertEquals(7,tools.lastIndexOfMultipleInArray(4,intArray));
//        Assert.assertEquals(8,tools.lastIndexOfMultipleInArray(3,intArray));
//        Assert.assertEquals(4,tools.lastIndexOfMultipleInArray(5,intArray));

        int[] intArray2 = {23, 17, 9, 225, 9767};
//        tester.test(-1,2,intArray2);
//        tester.test(3,5,intArray2);
//
//        Assert.assertEquals(-1,tools.lastIndexOfMultipleInArray(2,intArray2));
//        Assert.assertEquals(3,tools.lastIndexOfMultipleInArray(5,intArray2));

    }
    //endregion

    //region string operations
    @Test
    public void concatenatedWords() throws Exception {
        String result = tools.concatenatedWords("the", "cat", "sat", "mat");
        Assert.assertEquals("string should be \"the cat sat mat\"", "the cat sat mat", result);
        String result2 = tools.concatenatedWords("Once", "upon", "a","time");
        Assert.assertEquals("string should be \"Once upon a time\"", "Once upon a time", result2);
    }

    @Test
    public void phraseInQuotes() throws Exception {
        Assert.assertEquals("Phrase in quotes","\"Carpe Diem\"",tools.phraseInQuotes("Carpe Diem"));
        Assert.assertEquals("Phrase in quotes with space","\"Hello World\"",tools.phraseInQuotes(" Hello World "));
        Assert.assertEquals("Different phrase in quotes with space","\"The quick brown fox jumps over the lazy dog\"",tools.phraseInQuotes(" The quick brown fox jumps over the lazy dog "));
    }

    @Test
    public void middleLetterOrOneBefore() throws Exception {
        Assert.assertEquals("Busy",'u',tools.middleLetterOrOneBefore("Busy"));
        Assert.assertEquals("Music",'s',tools.middleLetterOrOneBefore("Music"));
        Assert.assertEquals("Antiquated",'q',tools.middleLetterOrOneBefore("Antiquated"));
    }

    @Test
    public void lastIndexOfItem() throws Exception {
        String[] array = {"ABC", "BCD", "DEFGH", "GHI", "HIJK"};
        Assert.assertEquals("GH last index should be 3",3,tools.lastIndexOfItem("GH",array));
        Assert.assertEquals("ab last index should be 0", 0,tools.lastIndexOfItem("ab",array));
        Assert.assertEquals("XY should not be found in array", -1,tools.lastIndexOfItem("XY",array));
    }

    @Test
    public void sentenceFromArrayOfWords() throws Exception {
        String[] sentence1 = {"The", "quick", "brown", "fox"};
        String result = tools.sentenceFromArrayOfWords(sentence1);
        String expected = "The quick brown fox.";
        String message = "output was '" + result + "' expected '" + expected + "'";
        Assert.assertEquals(message, expected, result);

        String[] sentence2 = {"ready", "steady", "go"};
        String result2 = tools.sentenceFromArrayOfWords(sentence2);
        String expected2 = "Ready steady go.";
        String message2 = "output was '" + result2 + "' expected '" + expected2 + "'";
        Assert.assertEquals(message2, expected2, result2);
    }

    @Test
    public void countCharInString() throws Exception {
        Assert.assertEquals(2, tools.countCharInString('e',"heater"));
        Assert.assertEquals(0,tools.countCharInString('p',"kettle"));
        Assert.assertEquals(4,tools.countCharInString('i',"mississippi"));
    }






    //create a method called numberContainsNumber, it should take two parameters, both ints
    //the method should return two if the second parameter appears inside the first parameter (as consecutive digits)
    //e.g. if first parameter is 123456 and second parameter is 234 then it should return true
    //if first parameter is 613425 and second parameter is 43 then it should return false

    @org.junit.Test
    public void numberContainsNumber() throws Exception {
        Assert.assertTrue("123 contains 2",tools.numberContainsNumber(123,2));
        Assert.assertTrue("123826832 contains 268",tools.numberContainsNumber(123826832,268));
        Assert.assertTrue("-353535 contains 353",tools.numberContainsNumber(-353535,353));
        Assert.assertTrue("-123 contains -12",tools.numberContainsNumber(-123,-12));
        Assert.assertFalse("456 does not contain 7",tools.numberContainsNumber(456,7));
        Assert.assertFalse("-123 does not contain -2",tools.numberContainsNumber(-123,-2));
    }


    //to do add some methods requiring change of case
    //endregion

    //region data lookup

    @Test
    public void maxEmissionsBand() throws Exception {
        Assert.assertEquals("Max for band D is 130", 130, tools.maxEmissions('D'));
        Assert.assertEquals("Max for band J is 200", 200, tools.maxEmissions('J'));
        Assert.assertEquals("Max for band B is 110", 110, tools.maxEmissions('B'));
    }

    @Test
    public void maxEmissionsPrice() throws Exception {
        Assert.assertEquals("£20 is 110kg CO2",110,tools.maxEmissions(20));
        Assert.assertEquals("£490 is 255kg CO2",255,tools.maxEmissions(490));
        Assert.assertEquals("£145 is 150kg CO2",150,tools.maxEmissions(145));
    }

    @Test
    public void carTaxBandFromPrice() throws Exception {
        Assert.assertEquals("£20 is B",'B',tools.carTaxBandFromPrice(20));
        Assert.assertEquals("£180 is G",'G',tools.carTaxBandFromPrice(180));
        Assert.assertEquals("£290 is K",'K',tools.carTaxBandFromPrice(290));
    }

    @Test
    public void vehicleDuty() throws Exception {
        Assert.assertEquals("emissions of 154 should cost 180", 180, tools.vehicleDuty(154));
        Assert.assertEquals("emissions of 225 should cost 290", 290, tools.vehicleDuty(225));
        Assert.assertEquals("emissions of 98 should cost 0", 0, tools.vehicleDuty(98));
    }

    @Test
    public void scrabbleValue() throws Exception {
        Assert.assertEquals(1,tools.scrabbleValue('a'));
        Assert.assertEquals(2,tools.scrabbleValue('d'));
        Assert.assertEquals(3,tools.scrabbleValue('m'));
        Assert.assertEquals(4,tools.scrabbleValue('f'));
        Assert.assertEquals(5,tools.scrabbleValue('k'));
        Assert.assertEquals(8,tools.scrabbleValue('j'));
        Assert.assertEquals(10,tools.scrabbleValue('z'));
    }

    @Test
    public void baseScrabbleScore() throws Exception {
        //implement a method named baseScrabbleScore in the WordTools class
        //one parameter - a string
        //return the score for the scrabble tiles, using the values for each letter as indicated below
        //the method should ignore the case of the letters
        //it may be helpful to write an additional, helper method as part of the solution

        //1: a,e,i,l,n,o,r,s,t,u
        //2: d,g
        //3: b,c,m,p
        //4: f,h,v,w,y
        //5: k
        //8: j,x
        //10: q,z

        Assert.assertEquals("HELLO scrabble score is 8", 8, tools.baseScrabbleScore("hello"));
        Assert.assertEquals("JUKEBOX scrabble score is 27", 27, tools.baseScrabbleScore("jukebox"));
        Assert.assertEquals("BOOK scrabble score is 10", 10, tools.baseScrabbleScore("book"));
    }

    //endregion

    //region type conversions
    @Test
    public void numberInWords() throws Exception {
        Assert.assertEquals("test with 23","two three",tools.numberInWords(23));
        Assert.assertEquals("test with 8","eight",tools.numberInWords(8));
        Assert.assertEquals("test with 1244511000","one two four four five one one zero zero zero",tools.numberInWords(1244511000));
    }
    //endregion

    //region medium problems

    @Test
    public void sentenceCase() throws Exception {
        String sentences = "he said. she Said. they said";
        String result = tools.sentenceCase(sentences);
        String expected = "He said. She said. They said";
        String message = "Output: " + result + ", expected: " + expected;
        Assert.assertEquals(message, expected, result);

        String sentences2 = "dAn ran.  Mel Fell.   nick kicked.";
        String result2 = tools.sentenceCase(sentences2);
        String expected2 = "Dan ran.  Mel fell.   Nick kicked.";
        String message2 = "Output: " + result2 + ", expected: " + expected2;
        Assert.assertEquals(message2, expected2, result2);

        testEqualityMethod(tools, "sentenceCase(java.lang.String)","",expected2,sentences2);
    }




    @Test
    public void apaFormatCitation() throws Exception {
        String oneExpected = "Palmer, 2008";
        String[] author = {"Palmer"};
        Assert.assertEquals(oneExpected,tools.apaFormatCitation(author,2008));

        String twoExpected = "Palmer & Roy, 2008";
        String[] twoAuthors = {"Palmer", "Roy"};
        Assert.assertEquals(twoExpected, tools.apaFormatCitation(twoAuthors,2008));

        String fourExpected = "Sharp, Aarons, Wittenberg, & Gittens, 2007";
        String[] fourAuthors = {"Sharp", "Aarons", "Wittenberg", "Gittens"};
        Assert.assertEquals(fourExpected, tools.apaFormatCitation(fourAuthors,2007));

        String sixExpected = "Mendelsohn et al., 2010";
        String[] sixAuthors = {"Mendelsohn", "Sharp", "Aarons", "Wittenberg", "Gittens", "Palmer"};
        Assert.assertEquals(sixExpected, tools.apaFormatCitation(sixAuthors, 2010));
    }

    @Test
    public void yearOfRegistrationFromNumberplate() throws Exception {
        Assert.assertEquals("58 plate",2008,tools.yearOfRegistrationFromNumberplate("DE58 NPT"));
        Assert.assertEquals("64 plate",2014,tools.yearOfRegistrationFromNumberplate("DE64 NPT"));
        Assert.assertEquals("05 plate",2005,tools.yearOfRegistrationFromNumberplate("NS05 TSY"));
        Assert.assertEquals("96 plate",2046,tools.yearOfRegistrationFromNumberplate("PR46 LAR"));
    }


    @Test
    public void pagedData() throws Exception {
        String[] data = {"the", "cat", "sat", "on", "the", "mat"};
        String[] result2n2 = {"sat","on"};
        Assert.assertArrayEquals(tools.pagedData(data,2,2),result2n2);
        String[] result4n4 = {"the","mat"};
        Assert.assertArrayEquals(tools.pagedData(data,4,4),result4n4);

        //needs more tests
    }
    
    
    
    @Test
    public void _2d_removeDuplicates_reflection() throws Exception {
        
        String methodName = "removeDuplicates(java.util.ArrayList)";
        Optional<Method> validRemoveDuplicatesMethod = this.method(ArrayList.class, methodName);
        Assert.assertTrue("Method to remove duplicates is missing, or incorrectly declared", validRemoveDuplicatesMethod.isPresent());
        
        if (validRemoveDuplicatesMethod.isPresent()){
            
            final Integer[] intArray = {2,4,6,1,5,7,3,1,7,2,3,7,3,3};
            final Integer[] resultIntArray = {2,4,6,1,5,7,3};
            final ArrayList<Integer> myInts = new ArrayList<Integer>(Arrays.asList(intArray));
            ArrayList<Integer> resultWith_array_ = (ArrayList<Integer>)validRemoveDuplicatesMethod.get().invoke(tools,myInts);
            Assert.assertArrayEquals(resultIntArray,resultWith_array_.toArray());
            
            final Integer[] intArray2 = {1,2,2,3,4,4,5,6,6,6,7,8,8,9};
            final Integer[] resultIntArray2 = {1,2,3,4,5,6,7,8,9};
            final ArrayList<Integer> myInts2 = new ArrayList<Integer>(Arrays.asList(intArray2));
            ArrayList<Integer> resultWith_array2_ = (ArrayList<Integer>)validRemoveDuplicatesMethod.get().invoke(tools,myInts2);
            Assert.assertArrayEquals(resultIntArray2,resultWith_array2_.toArray());
        }
    }
    
    
    @Test
    public void occurrencesOfSubstring() throws Exception {
        Assert.assertEquals(2,tools.occurrencesOfSubstring("of","his cup of coffee"));
        Assert.assertEquals(0,tools.occurrencesOfSubstring("and", "the cat sat on the mat"));
        Assert.assertEquals(2,tools.occurrencesOfSubstring("in", "Winter is coming"));
    }

    //endregion

    //region enum problems (medium)
    @Test
    public void stampDuty() throws Exception {
        Assert.assertEquals(0,tools.stampDuty(Problems.HousePurchaseType.FIRST_TIME,254000));
        Assert.assertEquals(2700,tools.stampDuty(Problems.HousePurchaseType.MOVE_HOME,254000));
        Assert.assertEquals(10320,tools.stampDuty(Problems.HousePurchaseType.ADDITIONAL_PROPERTY,254000));

        Assert.assertEquals(51000,tools.stampDuty(Problems.HousePurchaseType.ADDITIONAL_PROPERTY,762500));
        Assert.assertEquals(28125,tools.stampDuty(Problems.HousePurchaseType.MOVE_HOME,762500));

        Assert.assertEquals(67200,tools.stampDuty(Problems.HousePurchaseType.MOVE_HOME,1234500));
        Assert.assertEquals(104235,tools.stampDuty(Problems.HousePurchaseType.ADDITIONAL_PROPERTY,1234500));

        Assert.assertEquals(1713750,tools.stampDuty(Problems.HousePurchaseType.MOVE_HOME,15000000));

    }

    //endregion


    //region complex problems
    @Test
    public void best100CreditsAverage() throws Exception {
        int[][] moduleCredits = {
                {20, 32},
                {20, 45},
                {20, 76},
                {20, 23},
                {20, 60},
                {20, 54}};
        Assert.assertEquals(53,tools.best100CreditsAverage(moduleCredits));

        int[][] moduleCreditsWithTens = {
                {20, 32},
                {10, 45},
                {20, 76},
                {10, 23},
                {20, 60},
                {20, 54},
                {20, 67}};
        Assert.assertEquals(59,tools.best100CreditsAverage(moduleCreditsWithTens));

        int[][] moduleCreditsWithForty = {
                {20, 47},
                {20, 45},
                {40, 32},
                {20, 60},
                {20, 54}};
        Assert.assertEquals(47,tools.best100CreditsAverage(moduleCreditsWithForty));
    }


        /* ******************** Worth 4 marks ******************** */
    //create a method called textChunks which takes a String as a parameter and return and ArrayList of Strings
    //If the String is over 12 characters in length, split the sentence into one or more strings
    //each new string should be as close to 12 characters as possible (excluding the final string)
    //ensure that you do not split text mid word
    //ensure chunks do not start or end with a space
    //all chunks should be returned in the array
    //you can assume no words in the

    @Test
    public void textChunks() throws Exception {

        String sentence = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        ArrayList<String> chunks = new ArrayList<String>();
        chunks.add("Lorem ipsum");
        chunks.add("dolor sit");
        chunks.add("amet,");
        chunks.add("consectetur");
        chunks.add("adipiscing");
        chunks.add("elit, sed do");
        chunks.add("eiusmod");
        chunks.add("tempor");
        chunks.add("incididunt");
        chunks.add("ut labore et");
        chunks.add("dolore magna");
        chunks.add("aliqua.");
        Assert.assertEquals(chunks, tools.textChunks(sentence));

        String shortText = "No split";
        ArrayList<String> notSplit = new ArrayList<String>();
        notSplit.add(shortText);
        Assert.assertEquals(notSplit, tools.textChunks(shortText));

    }

    @org.junit.Test
    public void formattedNameFromArray() throws Exception {
        String[][] namesArray = {
                {"Dr", "Gregory", "House"},
                {"Thirteen"},
                {"Ms", "Stacey", "Warner"},
                {"Alison", "Barbera", "Cameron"},
                {"Andrew", "Barry", "Chris", "David", "Edward", "Gary", "Henry", "Smythe"},
                {"Robert", "Chase"}
        };

        String[] expectedNames = {"Dr. G. House",
                "Thirteen",
                "Ms. S. Warner",
                "Alison B. Cameron",
                "Andrew B. C. D. E. G. H. Smythe",
                "Robert Chase"};

        for (int i = 0; i < 6; i++) {
            String result = tools.formattedNameFromArray(namesArray[i]);
            Assert.assertEquals(expectedNames[i], result);
        }
    }

      /* ******************** Worth 4 marks ******************** */
    //create a method called oneLetterOut which should take two strings as parameters and return a boolean
    //The method should determine if the two Strings could be an anagram if just one character was changed in one of the words
    //e.g. You could change one letter in either 'row' or 'two' to make it an anagram of the other

    @Test
    public void oneLetterOut() throws Exception {

//        MethodTester tester = new MethodTester(Problems.class, "oneLetterOut", testEventHandler);
//        tester.test("abc and abd are one character out from anagrams",true,"abc","abd");
//        tester.test("abbbbc and bbbccd are one character out from anagrams",false,"abbbbc","bbbccd");
//        tester.test("abc and bcd are one character out from anagrams",true,"abc","bcd");
//        tester.test("fast and star are one character out from anagrams",true,"fast","star");
//        tester.test("fast and staf are exact anagrams (not 1 character out)",false,"fast","staf");
//        tester.test("this and car are not one character out",false,"this","car");
//        tester.test("this and thesis are not one character out", false,"this","thesis");
//        tester.test("abcd and efgh are not one character out",false,"abcd","efgh");
//        tester.test("ijkl and look are not one character out",false,"ijkl","look");


//        Assert.assertTrue("abc and abd are one character out from anagrams",tools.oneLetterOut("abc","abd"));
//        Assert.assertFalse("abbbbc and bbbccd are one character out from anagrams",tools.oneLetterOut("abbbbc","bbbccd"));
//        Assert.assertTrue("abc and bcd are one character out from anagrams",tools.oneLetterOut("abc","bcd"));
//        Assert.assertTrue("fast and star are one character out from anagrams",tools.oneLetterOut("fast","star"));
//        Assert.assertFalse("fast and staf are exact anagrams (not 1 character out)",tools.oneLetterOut("fast","staf"));
//        Assert.assertFalse("this and car are not one character out",tools.oneLetterOut("this","car"));
//        Assert.assertFalse("this and thesis are not one character out", tools.oneLetterOut("this","thesis"));
//        Assert.assertFalse("abcd and efgh are not one character out",tools.oneLetterOut("abcd","efgh"));
//        Assert.assertFalse("ijkl and look are not one character out",tools.oneLetterOut("ijkl","look"));

    }
    //endregion

    //region chess problems
      /* ******************** Worth 4 marks ******************** */
    @Test
    public void validKingMove() throws Exception {

//        MethodTester tester = new MethodTester(Problems.class,"validKingMove", testEventHandler);
//        tester.test("b7 to a8", true,'b',7,'a',8);
//        tester.test("h8 to g7",true,'h',8,'g',7);
//        tester.test("h8 to h9",false,'h',8,'h',9);
//        tester.test("e5 to e6",true,'e',5,'e',6);
//        tester.test("e5 to e5",false,'e',5,'e',5);
//        tester.test("e5 to e7",false,'e',5,'e',7);
//
//
//        int[][] validLocationsFromD4 = {{'c',3},{'c',4},{'c',5},{'d',3},{'d',5},{'e',3},{'e',4},{'e',5}};
//        for (int[] position : validLocationsFromD4) {
//            tester.test("Can go to " + (char)position[0] + position[1] + "from d4",true,'d',4,(char)position[0],position[1]);
//        }


        //        int[][] validLocationsFromD4 = {{'c',3},{'c',4},{'c',5},{'d',3},{'d',5},{'e',3},{'e',4},{'e',5}};
//
//        for (int[] position : validLocationsFromD4) {
//            Assert.assertTrue("Can go to " + (char)position[0] + position[1] + "from d4",tools.validKingMove('d',4,(char)position[0],position[1]));
//        }
//        Assert.assertTrue("On b7 can go a8",tools.validKingMove('b',7,'a',8));
//        Assert.assertTrue("On h8 can go g7",tools.validKingMove('h',8,'g',7));
//        Assert.assertFalse("On h8 can't go h9 (doesn't exist)",tools.validKingMove('h',8,'h',9));
//        Assert.assertTrue("On e5 can go e6",tools.validKingMove('e',5,'e',6));
//        Assert.assertFalse("On e5 can't go e5 (same place)",tools.validKingMove('e',5,'e',5));
//        Assert.assertFalse("On e5 can't go e7 (too far)",tools.validKingMove('e',5,'e',7));


    }







    @Test
    public void validQueenMove() throws Exception {
//        MethodTester tester = new MethodTester(Problems.class, "validQueenMove", testEventHandler);
//
//        tester.test("c3 to e4",false,'c',3,'e',4);
//        tester.test(true,'h',8,'g',7);
//        tester.test(true,'d',4,'e',3);
//        tester.test(true,'b',7,'a',8);
//        tester.test(true,'h',8,'a',1);
//        tester.test(true,'c',7,'c',8);
//        tester.test(true,'c',7,'c',5);
//        tester.test(true,'c',7,'a',7);
//        tester.test(true,'c',7,'f',7);


//        Assert.assertFalse("On c3 can't go e4 (col and row move not same)",tools.validQueenMove('c',3,'e',4));
//        Assert.assertTrue("On h8 can go g7",tools.validQueenMove('h',8,'g',7));
//        Assert.assertTrue("On d4 can go e3",tools.validQueenMove('d',4,'e',3));
//        Assert.assertTrue("On b7 can go a8",tools.validQueenMove('b',7,'a',8));
//        Assert.assertTrue("On h8 can go a1",tools.validQueenMove('h',8,'a',1));
//        Assert.assertTrue("On c7 can go c8",tools.validQueenMove('c',7,'c',8));
//        Assert.assertTrue("On c7 can go c5",tools.validQueenMove('c',7,'c',5));
//        Assert.assertTrue("On c7 can go a7",tools.validQueenMove('c',7,'a',7));
//        Assert.assertTrue("On c7 can go f7",tools.validQueenMove('c',7,'f',7));
    }


    //Create a method called isValidBishopMove which will return a boolean and take 4 parameters as follows:
    //char (denoting the current column of the piece on the board), int (denoting the current row of the piece on the board)
    //char (denoting the destination column of the piece on the board), int (denoting the destination row of the piece on the board)
    //the first char and int will indicate a starting position on the board (based on an 8 x 8 chess board)
    //where rows are labelled 1-8 and columns a-h (lowercase letters used)
    //calculate if a given move would be legal for a Bishop piece
    //Bishops can only move diagonally, but by any number of squares
    @Test
    public void validMoveBishopMove() throws Exception {

//        MethodTester tester = new MethodTester(Problems.class, "validBishopMove", testEventHandler);
//        tester.test("Can't stay still (not a move)", false,'c',3,'c',3);
//        tester.test("On h8 can go g7",true,'h',8,'g',7);
//        tester.test("On d4 can go e3",true,'d',4,'e',3);
//        tester.test("On b7 can go a8",true,'b',7,'a',8);
//        tester.test("On h8 can't go h9 (doesn't exist)",false,'h',8,'h',9);
//        tester.test("On h8 can go a1",true,'h',8,'a',1);
//        tester.test("On c3 can't go e4 (not diagonal)",false,'c',3,'e',4);
//        tester.test("On e3 can't go b0 (doesn't exist)",false,'e',3,'b',0);
//        tester.test("On h3 can't go k6 (doesn't exist)",false,'h',3,'k',0);


//        Assert.assertFalse("Can't stay still (not a move)", tools.validBishopMove('c',3,'c',3));
//        Assert.assertTrue("On h8 can go g7",tools.validBishopMove('h',8,'g',7));
//        Assert.assertTrue("On d4 can go e3",tools.validBishopMove('d',4,'e',3));
//        Assert.assertTrue("On b7 can go a8",tools.validBishopMove('b',7,'a',8));
//        Assert.assertFalse("On h8 can't go h9 (doesn't exist)",tools.validBishopMove('h',8,'h',9));
//        Assert.assertTrue("On h8 can go a1",tools.validBishopMove('h',8,'a',1));
//        Assert.assertFalse("On c3 can't go e4 (not diagonal)",tools.validBishopMove('c',3,'e',4));
//        Assert.assertFalse("On e3 can't go b0 (doesn't exist)",tools.validBishopMove('e',3,'b',0));
//        Assert.assertFalse("On h3 can't go k6 (doesn't exist)",tools.validBishopMove('h',3,'k',0));
    }


    @Test
    public void validRookMove() throws Exception {
        Assert.assertFalse("Can't stay still (not a move)", tools.validRookMove('c',3,'c',3));
        Assert.assertFalse("Can't go off the board", tools.validRookMove('d',4,'i',4));
        Assert.assertFalse("Can't go diagonal",tools.validRookMove('b',7,'a',8));
        Assert.assertTrue("On c7 can go c8",tools.validRookMove('c',7,'c',8));
        Assert.assertTrue("On c7 can go c5",tools.validRookMove('c',7,'c',5));
        Assert.assertTrue("On c7 can go a7",tools.validRookMove('c',7,'a',7));
        Assert.assertTrue("On c7 can go f7",tools.validRookMove('c',7,'f',7));
    }

    @Test
    public void validKnightMove() throws Exception {
        Assert.assertFalse("Can't stay still (not a move)", tools.validKnightMove('c',3,'c',3));
        Assert.assertFalse("Can't go off the board", tools.validKnightMove('h',4,'i',6));
        Assert.assertFalse("Can't go diagonal",tools.validKnightMove('b',7,'a',8));
        Assert.assertTrue("On d3 can go e1",tools.validKnightMove('d',3,'e',1));
        Assert.assertTrue("On d3 can go e5",tools.validKnightMove('d',3,'e',5));
        Assert.assertTrue("On d3 can go b2",tools.validKnightMove('d',3,'b',2));
        Assert.assertTrue("On d3 can go c5",tools.validKnightMove('d',3,'c',5));
        Assert.assertTrue("On d3 can go e1",tools.validKnightMove('d',3,'e',1));
        Assert.assertTrue("On d3 can go f4",tools.validKnightMove('d',3,'f',4));
        Assert.assertFalse("Can't go double distance",tools.validKnightMove('b',2,'d',6));
    }

    @Test
    public void validPawnMove() throws Exception {
        //white goes + the rows
        //black goes - the rows
        //initial move (from start position) may be two forward
        //only move 'forward'
        //can go one diagonally if taking


        boolean initialMove2 = tools.validPawnMove('a',2,'a',4,true,false);
        Assert.assertTrue("White pawn initial move 2 squares",initialMove2);

        boolean initialMove1 = tools.validPawnMove('a',2,'a',3,true,false);
        Assert.assertTrue("White pawn initial move 1 square",initialMove1);

        boolean initialMove2InvalidTake = tools.validPawnMove('a',2,'a',4,true,true);
        Assert.assertFalse("White pawn cannot take if not diagonal", initialMove2InvalidTake);

        boolean initialMove1InvalidTake = tools.validPawnMove('a',2,'a',3,true,true);
        Assert.assertFalse("White pawn cannot take if not diagonal", initialMove1InvalidTake);

        boolean initialBlackMove2 = tools.validPawnMove('a',7,'a',5,false,false);
        Assert.assertTrue("Black pawn initial move 2 squares",initialBlackMove2);

        boolean initialBlackMove1 = tools.validPawnMove('a',7,'a',6,false,false);
        Assert.assertTrue("Black pawn initial move 1 square",initialBlackMove1);

        boolean initialBlackMove2InvalidTake = tools.validPawnMove('a',7,'a',5,false,true);
        Assert.assertFalse("Black pawn cannot take if not diagonal", initialBlackMove2InvalidTake);

        boolean initialBlackMove1InvalidTake = tools.validPawnMove('a',7,'a',6,false,true);
        Assert.assertFalse("Black pawn cannot take if not diagonal", initialBlackMove1InvalidTake);

        boolean overRowValueWhite = tools.validPawnMove('e',8,'e',9,true,false);
        Assert.assertFalse("Pawn cannot move off board (white)",overRowValueWhite);

        boolean overRowValueBlack = tools.validPawnMove('c',1,'c',0,false,false);
        Assert.assertFalse("Pawn cannot move off board (white)",overRowValueBlack);

        boolean overColumn = tools.validPawnMove('h',5,'i',6,true,true);
        Assert.assertFalse("Cannot go off the board (when taking, white)",overColumn);

        boolean overColumnBlack = tools.validPawnMove('h',3,'i',2,false,true);
        Assert.assertFalse("Cannot go off the board (when taking, white)",overColumn);

        boolean diagonalNonTakingMove = tools.validPawnMove('d',6,'e',7,true,false);
        Assert.assertFalse("Can't move diagonal if not taking (White)",diagonalNonTakingMove);

        boolean diagonalNonTakingMoveBlack = tools.validPawnMove('h',5,'g',4,false,false);
        Assert.assertFalse("Can't move diagonal if not taking (Black)",diagonalNonTakingMoveBlack);

        boolean diagonalTakingMove = tools.validPawnMove('d',6,'e',7,true,true);
        Assert.assertTrue("Can move diagonal if taking (White)",diagonalTakingMove);

        boolean diagonalTakingMoveBlack = tools.validPawnMove('h',5,'g',4,false,true);
        Assert.assertTrue("Can move diagonal if taking (Black)",diagonalTakingMoveBlack);

        boolean nonMove = tools.validPawnMove('e',7,'e',7,true,false);
        Assert.assertFalse("No move is not a valid move (White)",nonMove);

        boolean nonMoveBlack = tools.validPawnMove('e',7,'e',7,false,false);
        Assert.assertFalse("No move is not a valid move (Black)",nonMoveBlack);

        boolean wrongDirection = tools.validPawnMove('a',4,'a',3,true,false);
        Assert.assertFalse("Cannot go backwards (White)", wrongDirection);

        boolean wrongDirectionBlack = tools.validPawnMove('g',6,'g',7,false,false);
        Assert.assertFalse("Cannot go backwards (Black)",wrongDirectionBlack);

        boolean doubleDiagonal = tools.validPawnMove('f',2,'e',4,true,true);
        Assert.assertFalse("cannot move two and take",doubleDiagonal);
    }

    //endregion

    //region reflection helpers

    void testBooleanMethod(boolean expectedResult, String testMessage, String testMethodName, Object... args){



        Class testedClass = tools.getClass();
        Method[] methods = testedClass.getDeclaredMethods();
        for (Method method: methods){
            String className = testedClass.getCanonicalName();
            String methodName = method.toGenericString();

            String targetMethodName = "boolean"+ " " + className+"."+testMethodName;
            if (methodName.equals(targetMethodName)) {
                try{
                    if (expectedResult) {
                        Assert.assertTrue( testMessage, (boolean) method.invoke(tools, args));
                    }
                    else {
                        Assert.assertFalse(testMessage, (boolean) method.invoke(tools, args));
                    }
                }
                catch (Exception ex){
                    System.out.println("error "+ex.getMessage());
                }
            }
        }
    }


    private void testEqualityMethod(Object theClass, String testMethodName, String testMessage, Object expectedResult,  Object... args){
        Optional<Method> method = method(expectedResult,testMethodName,args);
        if (method.isPresent()){
            try{
                Assert.assertEquals(testMessage, expectedResult, method.get().invoke(theClass, args));
            }
            catch (Exception ex){
                System.out.println("error "+ex.getMessage());
            }
        }
        else {
            Assert.fail("No method matching " + testMethodName + " found");
        }
    }


    private Optional<Method> method(Object expectedResult, String name, Object... args){
        return method(expectedResult.getClass(), name, args);
    }

    ///Method string should be provided in the format methodName(java.lang.String)
    private Optional<Method> method(Class<?> returnType, String name, Object... args){
        Class testedClass = tools.getClass();
        String className = testedClass.getCanonicalName();
        String returnTypeString = returnType.getName();
        String targetMethodName = returnTypeString+ " " + className+"."+name;

        for (Method method: testedClass.getDeclaredMethods()){
            if (method.toGenericString().equals(targetMethodName)){
                return Optional.of(method);
            }
        }
        return Optional.empty();
    }

    //endregion





























}
