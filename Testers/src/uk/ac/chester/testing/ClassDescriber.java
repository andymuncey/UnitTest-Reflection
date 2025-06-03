package uk.ac.chester.testing;

public interface ClassDescriber {

    /**
     * Returns class names in the form "String, Double, Object & Integer"
     * @param classes array of class objects
     * @return the names, separated by commas, with the final two separated by ampersand  */
    default String describe(Class<?>[] classes){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < classes.length; i++) {
            sb.append(classes[i].getSimpleName());
            if (classes.length > 1 && i == classes.length - 2){
                sb.append(" & ");
            } else if (i < classes.length - 1){
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
