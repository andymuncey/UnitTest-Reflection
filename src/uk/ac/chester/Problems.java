package uk.ac.chester;

import java.util.ArrayList;
import java.util.Arrays;


public class Problems {

    //region too easy to google
    boolean isPalindrome(String word) {

        char letters[] = word.toLowerCase().toCharArray();
        for (int i = 0; i < word.length()/2; i++) {
            if (letters[i] != letters[(word.length()-1) -i]){
                return false;
            }
        }
        return true;
    }

    boolean isAnagram(String word1, String word2) {

        char[] word1Array = word1.toLowerCase().toCharArray();
        for (char letter: word1Array) {
            if (countCharInString(letter,word1.toLowerCase()) != countCharInString(letter,word2.toLowerCase())){
                return false;
            }
        }
        return true;

    }

    String reversedSentence(String sentence){
        String[] words = sentence.split(" ");
        String result = "";
        for (int i = words.length-1; i>=0; i--){
            result += words[i] + " ";
        }
        return result.trim();
    }
    //endregion

    //region too easy to solve using built in classes
    int numberFromBinaryString(String binaryString){
        return Integer.parseInt(binaryString,2);
    }

    String charactersFromWordInAlphabeticalOrder(String word){
        //this should take all the characters in the sentence variable, place them in alphabetical order, and return them as a string
        //e.f. if word was hello, the method would return ehllo
        char[] letters = word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }
    //endregion

    //region boolean

    
    /**
     * Determines if the temperature is too cold (with 'too cold' being regarded as below 18 degrees C)
     * @param temperature a temperature value in Celsius
     * @return true if the temperature value is below 18, false otherwise
     */
    boolean isTooCold(double temperature) {
        return temperature < 18;
    }
    
    
    /**
     * Determines if a String has more than 140 characters
     * @param text the String to be evaluated
     * @return true if the provided String is over 140 characters, false otherwise
     */
    boolean over140chars(String text){
        return text.length() > 140;
    }

    /**
     * Determines if the temperature is too hot (with 'too hot' being regarded as above 25 degrees C)
     * @param temperature a temperature value in Celsius
     * @return true if the temperature value is above 25, false otherwise
     */
    boolean isTooHot(double temperature) {
        //should return true if the temperature is above 25
        //return false;
        return temperature > 25;
    }
    //endregion

    //region hard boolean

    /**
     * Given a line between coordinates (x1, y1) and  (x2, y2) and a second line between (x3,y3) and (x4,y4)
     * Determine if these two lines run parallel to each other
     * @return true if the lines are at 90 degree to each other
     */
    boolean parallel(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4){
        //make the min of x1, x2 0 and adjust the others accordingly

        //make x start at 0 for line 1
        int xAdjustmentLine1 = x1 < x2 ? x1 : x2;
        x1 -=xAdjustmentLine1;
        x2 -=xAdjustmentLine1;

        //y at 0 for line 1
        int yAdjustmentLine1 = y1 < y2 ? y1 : y2;
        y1 -= yAdjustmentLine1;
        y2 -= yAdjustmentLine1;

        //x at 0 for line 2
        int xAdjustmentLine2 = x3 < x4 ? x3 : x4;
        x3 -= xAdjustmentLine2;
        x4 -= xAdjustmentLine2;

        //y at 0 for line 2
        int yAdjustmentLine2 = y3 < y4 ? y3 : y4;
        y3 -= yAdjustmentLine2;
        y4 -= yAdjustmentLine2;

        //ratio between x2 and x4 same as y2 and y4
        return (x4-x3 * y2-y1 == y4-y3 * x2-x1);

    }


    //endregion

    //region array access
    
    
    /**
     * Returns the lowest value found in the array
     * @param numbers an array of integers
     * @return the lowest value integer
     */
    int minValue(int[] numbers){
        return 0;
    }
    
    
    
    int maxValue(int[] numbers){
        int max = Integer.MIN_VALUE;
        for (int number:numbers) {
            if (number > max){
                max = number;
            }
        }
        return max;


    }

    boolean stringsMatch(String[] array, int index1, int index2) {
        //return true if the item at both indexes are an exact match
        return array[index1].equals(array[index2]);
    }
    //endregion

    //region simple mathematics

    double temperatureInKelvin(double temperatureInFahrenheit){
        //given a temperature in degrees Fahrenheit, return the value in degrees Kelvin
        return (((temperatureInFahrenheit + 459.67) * 5) / 9);
    }

    int sumOfThreeNumbers(int n1, int n2, int n3){
        return n1 + n2 + n3;
    }

    int productOfThreeNumbers(int n1, int n2, int n3){
        //should return the product of the three numbers passed in as parameters
        return n1 * n2 * n3;
    }

    int averageOfThreeNumbers(int number1, int number2, int number3) {

        return (number1 + number2 + number3) / 3;
    }

    //endregion

    //region maths with loops
    int sumOfNumbersFromOneToN(int n) {

        //add up all the numbers from 1 to the number supplied (n)
        //n will always be greater than 1
        //e.g. if n is 4, return 10 (i.e. 1+2+3+4)

        //return 0;
        int sum = 0;
        while (n > 0) {
            sum += n;
            n-=1;
        }
        return sum;
    }

    int highestNumberBelowNDivisibleByTwoNumbers (int divisor1, int divisor2, int n){

        //determine the highest number below n which is entirely divisible by both divisor1 and divisor2
        //all parameters will be greater than 1

        //e.g. n is 13, divisor1 is 3, divisor2 is 4
        //result 12 (12 is divisible by both 3 and 4 without remainder)

        //e.g. n is 11, divisor1 is 3, divisor2 is 4
        //result 0 (no other number under 11 can be divided by 3 and 4 without a remainder)

        //e.g. n is 239, divisor1 is 6, divisor2 is 5
        //result is 210 (can be divided without remainder by both 5 and 6, and no other number between 210 and 239 can)

        for (int i = n; i > 0 ; i--) {
            if ((i % divisor1) == 0 && (i % divisor2) == 0){
                return i;
            }
        }
        return 0;


        //return 0;
    }
    //endregion

    //region maths with arrays
    int arraySum(int[] arrayOfDoubles){
        int total = 0;
        for (double number: arrayOfDoubles)
        {
            total += number;
        }
        return total;
    }

    double productOfNumbersInArray(double[] arrayOfDoubles){
        //determine and return the product of all the numbers in arrayOfDoubles
        //i.e. multiple all the numbers in the array together
        double total = 1.0;
        for (double number:arrayOfDoubles) {
            total *= number;
        }
        return total;
    }

    public int arrayAverage(int[] integers) {

        //determine and return the average of all the numbers in arrayOfIntegers
        int sum = 0;
        for (int i:integers) {
            sum += i;
        }
        return sum / integers.length;
    }

    int lastIndexOfMultipleInArray(int number, int[] arrayOfInts){
        //this method should return the last index position of the array where its value is an exact multiple of 'number'
        //it should return -1 if no exact multiple is found
        for (int i = arrayOfInts.length-1; i >= 0; i--) {
            if (arrayOfInts[i] % number == 0){
                return i;
            }
        }
        return -1;
    }

    //endregion

    //region string operations
    String concatenatedWords(String word, String word2, String word3, String word4) {

        //this should return the four words as one string, separated by spaces
        return word +" "+ word2 + " " + word3 + " " + word4;
    }

    String phraseInQuotes(String sentence){
        //should surround the sentence with double quotation marks
        //you may assume it is not already in quotes
        //Any space from the start and/or end of the string should be removed
        return "\"" + sentence.trim() + "\"";
    }

    char middleLetterOrOneBefore(String sentence){
        //this should return the middle character of a string,
        //or, if the string has an even number of characters, the character before the central position
        int position = (int)((sentence.length()-0.5)/ 2);
        return sentence.charAt(position);
    }

    int lastIndexOfItem(String textToFind, String[] arrayOfText) {

        //this method should return the last index position of the array where it contains (anywhere in the string) the textToFind
        //the case (uppercase / lowercase etc) of the search term should be ignored
        //e.g. the string "his" would be found in the string "THIS"
        //if the text is not located, return -1

        for (int i = (arrayOfText.length -1); i >= 0; i--) {
            if (arrayOfText[i].toLowerCase().contains(textToFind.toLowerCase())){
                return i;
            }
        }
        return -1;
        //return -1;
    }

    String sentenceFromArrayOfWords(String[] words) {

        //If necessary, capitalise the first letter of the first word
        //The sentence should be terminated with a full stop

        //return "";

        String sentence = words[0];
        char firstLetter = sentence.charAt(0);
        String letter = ""+firstLetter;
        letter = letter.toUpperCase();
        sentence = letter + sentence.substring(1) + " ";

        for (int i = 1; i < words.length; i++) {
            sentence += words[i]+" ";
        }

        sentence = sentence.trim() +".";
        return sentence;

    }

    int countCharInString(char letter, String word) {
        //count the number of times the specified character appears in the string (case sensitive)
        int count = 0;
        for (char character : word.toCharArray()) {
            count += character == letter ? 1 : 0;
        }
        return count;
    }

    boolean numberContainsNumber(int container, int containee){
        return ("" + container).contains(""+containee);
    }

    //endregion

    //region data lookup

    int maxEmissions (char band){

        //based on the following table, return the maximum CO2 emission value based on the band
        //for band G which has no maximum, return 1024
        //e.g. if band was H, 175 would be returned

        //Emissions  Band  Price
        //Up to 100  (A)    £0
        //101-110    (B) 	£20
        //111-120    (C) 	£30
        //121-130    (D) 	£110
        //131-140    (E) 	£130
        //141-150    (F) 	£145
        //151-165    (G) 	£180
        //166-175    (H) 	£205
        //176-185    (I) 	£225
        //186-200    (J) 	£265
        //201-225    (K) 	£290
        //226-255    (L) 	£490
        //Over 255   (M) 	£505

        switch (band) {
            case 'A':return 100;
            case 'B':return 110;
            case 'C':return 120;
            case 'D':return 130;
            case 'E':return 140;
            case 'F':return 150;
            case 'G':return 165;
            case 'H':return 175;
            case 'I':return 185;
            case 'J':return 200;
            case 'K':return 225;
            case 'L':return 255;
            case 'M':return 1024;
        }

        return 0;
    }

    int maxEmissions(int price){
        //based on the following table, return the maximum CO2 emission value based on the price
        //for the £505 price which has no maximum, return the maximum value for an int
        //e.g. if price was 205, 175 would be returned

        //Emissions  Band  Price
        //Up to 100  (A)    £0
        //101-110    (B) 	£20
        //111-120    (C) 	£30
        //121-130    (D) 	£110
        //131-140    (E) 	£130
        //141-150    (F) 	£145
        //151-165    (G) 	£180
        //166-175    (H) 	£205
        //176-185    (I) 	£225
        //186-200    (J) 	£265
        //201-225    (K) 	£290
        //226-255    (L) 	£490
        //Over 255   (M) 	£505

        int[] prices = {0,20,30,110,130,145,180,205,225,265,290,490,505};
        int[] emissions = {100,110,120,130,140,150,165,175,185,200,225,255,1024};
        for (int i = 0; i < prices.length; i++) {
            if (price == prices[i]){
                return emissions[i];
            }
        }

        return 0;
    }

    char carTaxBandFromPrice(int price){
        //based on the following table, return the tax band for the vehicle based on the price
        //e.g. if price was 205, H would be returned

        //Emissions  Band  Price
        //Up to 100  (A)    £0
        //101-110    (B) 	£20
        //111-120    (C) 	£30
        //121-130    (D) 	£110
        //131-140    (E) 	£130
        //141-150    (F) 	£145
        //151-165    (G) 	£180
        //166-175    (H) 	£205
        //176-185    (I) 	£225
        //186-200    (J) 	£265
        //201-225    (K) 	£290
        //226-255    (L) 	£490
        //Over 255   (M) 	£505

        int[] prices = {0,20,30,110,130,145,180,205,225,265,290,490,505};

        for (int i = 0; i < prices.length; i++) {
            if (price == prices[i]){
                char band = 'A';
                return (char)(band + i);
            }
        }

        return ' ';
    }

    char vehicleExciseBand (int co2Emissions){

        //based on the table in the method above, return the appropriate uppercase character denoting the vehicle excise duty band
        //e.g. if co2Emissions were 154, G would be returned
        char band = 'M';

        while (band > 'A'){
            if (maxEmissions((char)(band - 1)) < co2Emissions) {
                break;
            }
            else {
                band = (char)(band - 1);
            }
        }

        return band;

    }

    int vehicleDuty (int co2Emissions){

        //based on the table in the above method, return the price based on the CO2 emissions
        //e.g. if co2Emissions were 154, 180 would be returned
        char band = vehicleExciseBand(co2Emissions);

        switch (band) {
            case 'A':return 0;
            case 'B':return 20;
            case 'C':return 30;
            case 'D':return 110;
            case 'E':return 130;
            case 'F':return 145;
            case 'G':return 180;
            case 'H':return 205;
            case 'I':return 225;
            case 'J':return 265;
            case 'K':return 290;
            case 'L':return 490;
            case 'M':return 505;
        }

        return 0;
    }

    int baseScrabbleScore(String word){

        int score = 0;
        for (char letter: word.toLowerCase().toCharArray()) {
            score += scrabbleValue(letter);
        }
        return score;
    }

    int scrabbleValue(char letter){
        //scrabble letters have the values as follows:
        //1: a,e,i,l,n,o,r,s,t,u
        //2: d,g
        //3: b,c,m,p
        //4: f,h,v,w,y
        //5: k
        //8: j,x
        //10: q,z

        //return the value that corresponds to the supplied letter

        switch (letter) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
            case 'l':
            case 'n':
            case 'r':
            case 's':
            case 't':
                return 1;

            case 'd':
            case 'g':
                return 2;

            case 'b':
            case 'c':
            case 'm':
            case 'p':
                return 3;

            case 'f':
            case 'h':
            case 'v':
            case 'w':
            case 'y':
                return 4;

            case 'k':
                return 5;

            case 'j':
            case 'x':
                return 8;

            case 'q':
            case 'z':
                return 10;

        }
        return 0;
    }
    //endregion

    //region type conversions
    String numberInWords(int number){
        //should return the number as series of words (all lowercase)
        //e.g. given 4 it should return "four"
        //given 186 it should return "one eight six"
        char[] numberCharacters = String.valueOf(number).toCharArray();
        String wordString = "";
        for (char letter: numberCharacters) {
            switch (letter){
                case '0':
                    wordString += "zero ";
                    break;
                case '1':
                    wordString += "one ";
                    break;
                case '2':
                    wordString += "two ";
                    break;
                case '3':
                    wordString += "three ";
                    break;
                case '4':
                    wordString += "four ";
                    break;
                case '5':
                    wordString += "five ";
                    break;
                case '6':
                    wordString += "six ";
                    break;
                case '7':
                    wordString += "seven ";
                    break;
                case '8':
                    wordString += "eight ";
                    break;
                case '9':
                    wordString += "nine ";
                    break;
            }
        }
        return wordString.trim();
    }
    //endregion

    //region medium problems
    String sentenceCase(String sentences) {

        //Capitalise the first letter of the first word of every sentence in the string
        //there should be no capitals in subsequent words in each sentence
        //e.g. he said. she Said. they said should return He said, She said. They said

        //return "";
        sentences = sentences.toLowerCase();

        String result = "";
        boolean startOfSentence = true;
        for (int i = 0; i < sentences.length(); i++) {
            char letter = sentences.charAt(i);
            if (letter == '.') {
                startOfSentence = true;
            } else {
                if (startOfSentence && letter != ' ') {
                    letter = (""+letter).toUpperCase().charAt(0);
                    startOfSentence = false;
                }

            }
            result += letter;
        }

        return result;


    }

    int yearOfRegistrationFromNumberplate(String registration) {
        //UK cars registered from late 2001 onwards typically indicate their date of registration
        //in their numberplate, this is indicated by the third and fourth characters which are an integer
        //the format is as follows: cars registered in 2002 will have either 02 or 52 as their second two digits (e.g. LX02 ABC or DE52 XNT)
        //The pattern repeats for similar years, e.g. 2014 cars could be AB14 XYZ or BC64 GHJ

        //Assuming this naming pattern continues until 2049, return the year of registration of a car given the plate supplied
        //for the purpose of this method you can assume no registrations take place between January and March (where the above does not work)

        return Integer.parseInt(registration.substring(2,4)) > 50 ? Integer.parseInt(registration.substring(2,4))+1950 : Integer.parseInt(registration.substring(2,4)) + 2000;


//        String plateNo = registration.substring(2,4);
//        int yearNo = Integer.parseInt(plateNo);
//        if (yearNo > 50){
//            yearNo -=50;
//        }
//        return yearNo + 2000;
    }

    String[] pagedData(String[] array, int startIndex, int maxSize){
        //given an array, return an array consisting of the contents of the array starting at the index given
        //up to and including as many elements in maxSize, but without exceeding the bounds of the array
        int dataSize = Math.min(maxSize,array.length - startIndex);
        String[] newData = new String[dataSize];
        for (int i = 0; i< dataSize; i++){
            newData[i] = array[i + startIndex];
        }
        return newData;
    }
    
    //create a method called remove duplicates which will take an ArrayList as a parameter and also return an ArrayList
    //The first (or only) occurrence of any item in the list should be retained, but subsequent items should be removed
    ArrayList removeDuplicates(ArrayList list){
        ArrayList<Object> uniqueObjects = new ArrayList<Object>();
        for (Object item:list)
        {
            if (!uniqueObjects.contains(item)){
                uniqueObjects.add(item);
            }
        }
        return uniqueObjects;
    }

    String apaFormatCitation(String[] authors, int year){
        //using the first text citation format, return a string for the citation
        //guide: http://blog.apastyle.org/apastyle/2011/11/the-proper-use-of-et-al-in-apa-style.html

        if (authors.length == 1){
            return authors[0]+", "+ year;}

        if (authors.length == 2){
            return authors[0] + " & " + authors[1]+", "+ year;
        }

        if (authors.length >=6){
            return authors[0] + " et al., " + year;
        }

        String citation = authors[0];

        for (int i = 1; i < authors.length - 1; i++) {
            citation += ", "+authors[i];
        }
        citation += ", & "+authors[authors.length-1] + ", " + year;
        return citation;

    }

    int occurrencesOfSubstring(String substring, String source){
        int occurrences = 0;

        for (int i = 0; i < source.length() - substring.length(); i++) {
            if (source.substring(i).startsWith(substring)){
                occurrences++;
            }
        }
        return occurrences;
    }

    //endregion

    //region enum use

    enum HousePurchaseType {
        FIRST_TIME, MOVE_HOME, ADDITIONAL_PROPERTY;
    }

    /**
     * Calculates the stamp duty payable on a residential property purchase in England
     *
     * normal rates are as follows, based on purchase price:
     * up to £125,000 0% of purchase price
     * portion between £125.001 and £250k 2%
     * portion between £250,001 and £925k 5%
     * portion between 925,001 and £1.5m 10%
     * portion over £1.5m 12%

     * for first time buyers, if house price is £500k or less, the following rates apply
     * up to £300k 0%
     * portion between £300,001 and £500k 5%

     * anyone buying an additional property pays 3% on top of the normal rates
     *
     * @param type the type of purchase
     * @param price the selling price of the house
     * @return the amount of stamp duty payable
     */
    int stampDuty(HousePurchaseType type, int price){

        int tax = 0;

        //first time buyers
        if (type == HousePurchaseType.FIRST_TIME && price <= 500000) {
            int taxable = Math.max(0,price - 300000);
            tax += taxable * 0.05;
        }

        //apply standard rates
        if (type == HousePurchaseType.MOVE_HOME || type == HousePurchaseType.ADDITIONAL_PROPERTY){
            //deduct free portion
            int taxable = Math.max(0,price - 125000);
            int firstTierTaxable = Math.min(taxable,125000);
            tax += firstTierTaxable * 0.02;
            taxable -= firstTierTaxable;

            int secondTierTaxable = Math.min(taxable,675000);
            tax += secondTierTaxable * 0.05;
            taxable -= secondTierTaxable;

            int thirdTierTaxable = Math.min(taxable,575000);
            tax += thirdTierTaxable * 0.1;
            taxable -= thirdTierTaxable;

            //final tier
            tax += taxable * 0.12;
        }

        //levy if required
        if (type == HousePurchaseType.ADDITIONAL_PROPERTY) {
            tax += price * 0.03;
        }

        return tax;
    }


    //endregion

    //region complex problems

    int best100CreditsAverage(int[][] results){
        //each nested array contains the credit value for a module followed by its mark
        //max mark is 100, min mark is 0
        //total credits will be 120. Modules will only every have credit values of 10, 20 or 40
        //if weakest mark is worth more than 20 credits, adjust its weighting as necessary

        int markPerCredit = 0;

        int lowestMark = 100;
        int lowestCreditValue = 0;
        int lowestMarkIndex = -1;
        for (int i = 0; i < results.length; i++) {
            int credit = results[i][0];
            int result = results[i][1];

            markPerCredit += (credit * result);
            if (result < lowestMark){
                lowestMark = result;
                lowestCreditValue = credit;
                lowestMarkIndex = i;
            }
        }

        if (lowestCreditValue >= 20) {
            markPerCredit -= (lowestMark * 20);
        } else {
            markPerCredit -= (lowestMark * 10);

            //need second lowest
            int secondLowestMark = 100;
            for (int i = 0; i < results.length; i++) {
                if (i != lowestMarkIndex) {
                    int result = results[i][1];
                    if (result < secondLowestMark) {
                        secondLowestMark = result;
                    }
                }
            }
            markPerCredit -= (secondLowestMark * 10);
        }

        return markPerCredit / 100;

    }


    /**
     * splits sentence into strings 12 chars or less, without breaking words, or making chunks unnecessarily short
     * @param sentence the sentence to be split
     * @return an ArrayList of Strings, each String containing one or more words, but no longer than 12 chars
     */
    ArrayList<String> textChunks(String sentence){
        ArrayList<String> chunks = new ArrayList<String>();
        int splitLength = 12;
        while (sentence.length() > splitLength) {
            for (int i = splitLength; i > 0 ; i--) {
                if (sentence.charAt(i) == ' '){
                    chunks.add(sentence.substring(0,i).trim());
                    sentence = sentence.substring(i).trim();
                    break;
                }
            }
        }
        chunks.add(sentence.trim());
        return chunks;
    }


    /**
     * Generates a Formatted a name, given an array of names as follows:
     *
     * if they have a title, provide all forenames (if any) in initialised format then the surname as follows
     *  with title: Miss. A. B. Stark
     *  with title: Mrs. Lannister
     *  with title: Ms. D. Targaryen
     * if they do not have a title, print the entire first name, subsequent initials  and surname as follows:
     *  without title: Margaery L. Tyrell
     *  without title: Jon Snow
     *  without title: Shae
     *
     * @param nameComponents an array which may or may not start with a title (either Dr, Miss, Ms, Mr, Mrs, or Rev)
     * there may be 0 or more subsequent names
     * the final string in the array will be the surname (or only name - e.g. 'Bono' or 'Cheryl')
     *
     * @return the name as a string
     */
    String formattedNameFromArray (String[] nameComponents) {


        //return
        String formattedName = "";

        boolean hasTitle = false;

        if (nameComponents[0] == "Dr" || nameComponents[0] == "Miss" || nameComponents[0] == "Ms" || nameComponents[0] == "Mr" || nameComponents[0] == "Mrs" || nameComponents[0] == "Rev") {
            hasTitle = true;
            formattedName += nameComponents[0]+". ";
        }

        if (nameComponents.length > 1 && !hasTitle){
            formattedName += nameComponents[0] + " ";
        }

        for (int i = 1; i < (nameComponents.length - 1); i++) {
            formattedName += nameComponents[i].substring(0,1) + ". ";
        }
        formattedName += nameComponents[nameComponents.length -1];
        return formattedName;

        //return "";

    }

    boolean oneLetterOut(String word1, String word2){
        //should determine if the two words could be an anagram if one character was changed in one of the words
        //eg you could change one letter in either 'row' or 'two' to make it an anagram of the other

        if (word1.length() != word2.length()){
            return false;
        }

        int totalDifference = 0;
        //for each letter count characters in both words, increment count with difference

        for (char letter : word1.toLowerCase().toCharArray()){
            int difference = countCharInString(letter, word1) - countCharInString(letter, word2);
            totalDifference += Math.abs(difference);
        }

        return totalDifference == 1;
    }



    //endregion

    //region chess problems
    boolean validKingMove(char currentCol, int currentRow, char destinationCol, int destinationRow){
        //given a starting position (based on an 8 x 8 chess board)
        // where rows are labelled 1-8 and columns a-h (lowercase letters used)
        //calculate if a given move would be legal for a King piece
        //kings can move one position in any direction (e.g. straight or diagonal)
        //e.g. a King at d2 could move to any of the following: d1, d3, c1, c2, c3, e1, e2, e3
        //note that a king at h8 could only move to h7, g8 and g7 (to avoid going off the board)
        //Be aware destination positions passed to this method may not be valid
        //out of bounds
        if (destinationCol > 'h' || destinationCol < 'a' || destinationRow < 1 || destinationRow > 8){
            return false;
        }

        //same square
        if (destinationCol == currentCol && destinationRow == currentRow){
            return false;
        }

        //x and y 1 or 0
        int colMove = Math.abs(destinationCol - currentCol);
        int rowMove = Math.abs(destinationRow - currentRow);

        return (colMove <= 1 && rowMove <= 1);

    }

    boolean validQueenMove(char startCol, int startRow, char destinationCol, int destinationRow){
        return validBishopMove(startCol, startRow, destinationCol, destinationRow) ||
               validRookMove(startCol, startRow, destinationCol, destinationRow);
    }

    boolean validBishopMove(char currentCol, int currentRow, char destinationCol, int destinationRow){
        //given a starting position (based on an 8 x 8 chess board)
        //where rows are labelled 1-8 and columns a-h (lowercase letters used)
        //calculate if a given move would be legal for a Queen piece
        //queens can only move diagonally, but by any number of squares

        //out of bounds
        if (destinationCol > 'h' || destinationCol < 'a' || destinationRow < 1 || destinationRow > 8){
            return false;
        }

        //same square
        if (destinationCol == currentCol && destinationRow == currentRow){
            return false;
        }

        //x and y are same
        int colMove = destinationCol - currentCol;
        int rowMove = destinationRow - currentRow;

        if (Math.abs(colMove) != Math.abs(rowMove)){
            return false;
        }
        return true;
    }

    boolean validRookMove(char currentCol, int currentRow, char destinationCol, int destinationRow){
        //also known as castle - only in straight lines

        //out of bounds
        if (destinationCol > 'h' || destinationCol < 'a' || destinationRow < 1 || destinationRow > 8){
            return false;
        }

        //same square
        if (destinationCol == currentCol && destinationRow == currentRow){
            return false;
        }

        int colMove = Math.abs(destinationCol - currentCol);
        int rowMove = Math.abs(destinationRow - currentRow);

        return colMove == 0 || rowMove == 0;
    }

    boolean validKnightMove(char currentCol, int currentRow, char destinationCol, int destinationRow){

        //out of bounds
        if (destinationCol > 'h' || destinationCol < 'a' || destinationRow < 1 || destinationRow > 8){
            return false;
        }

        //same square
        if (destinationCol == currentCol && destinationRow == currentRow){
            return false;
        }

        int colMove = Math.abs(currentCol - destinationCol);
        int rowMove = Math.abs(currentRow - destinationRow);

        //two in one direction, one in the other
        return (colMove == 1 && rowMove == 2) || (colMove == 2 && rowMove == 1);
    }

    ///hard
    boolean validPawnMove(char currentCol, int currentRow, char destinationCol, int destinationRow, boolean isWhite, boolean willTake){
        //white goes + the rows
        //black goes - the rows
        //initial move (from start position) may be two forward
        //only move 'forward'
        //can go one diagonally if taking

        //flip row position for black then treat as white
        if (!isWhite){
            currentRow = 9 - currentRow;
            destinationRow = 9 - destinationRow;
        }

        //invalid start position
        if (currentRow < 2){
            return false;
        }

        //backwards or same square row (covers sideways and non-moves)
        if (currentRow >= destinationRow) {
            return false;
        }

        //out of bounds
        if (destinationCol > 'h' || destinationCol < 'a' || destinationRow < 1 || destinationRow > 8){
            return false;
        }


        //forward two (must stay on same column)
        if (destinationRow - currentRow == 2){
            return currentRow == 2 && currentCol == destinationCol && !willTake;
        }

        //forward one, same column
        if (destinationRow - currentRow == 1 && currentCol == destinationCol && !willTake) {
            return true;
        }

        //forward one, next row, will take
        return (destinationRow - currentRow == 1 && Math.abs(currentCol-destinationCol) == 1) && willTake;

    }
    //endregion

}
