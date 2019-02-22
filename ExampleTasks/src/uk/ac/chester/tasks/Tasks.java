package uk.ac.chester.tasks;


//file used for generating figures for written work

public class Tasks {

    /**
     * sums the values of all integers in an array
     * @param numbers
     * @return the sum of all integers in the array
     */
    public int arraySum(int[] numbers){
        int total = 0;
        for (double number: numbers)
        {
            total += number;
        }
        return total;
    }

    /**
     * Converts a temperature in Fahrenheit to Kelvin
     * @param temp temperature (F)
     * @return temperature (K)
     */
    public double fahrenheitToKelvin(double temp){
        return (temp - 32) * 5/9 + 273.15;
    }

    int addTen(int number){
        return number + 10;
    }

    String reversedSentence(String sentence){
        String[] words = sentence.split(" ");
        StringBuilder result = new StringBuilder();
        for (int i = words.length-1; i>=0; i--){
            result.append(words[i] + " ");
        }
        return result.toString().trim();
    }


}