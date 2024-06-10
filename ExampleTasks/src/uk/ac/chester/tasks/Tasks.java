package uk.ac.chester.tasks;


class Tasks {

    /**
     * sums the values of all integers in an array
     * @param numbers an array of numbers
     * @return the sum of all integers in the array
     */
     static int arraySum(int[] numbers){
        int total = 0;
        for (int number: numbers)
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
    static double fahrenheitToKelvin(double temp){
        return (temp - 32) * 5/9 + 273.15;
    }

    static int addTen(int number){
        return number + 10;
    }

    static String reversedSentence(String sentence){
        String[] words = sentence.split(" ");
        StringBuilder result = new StringBuilder();
        for (int i = words.length-1; i>=0; i--){
            result.append(words[i]);
            if (i != 0){
                result.append(" ");
            }
        }
        return result.toString().trim();
    }

}
