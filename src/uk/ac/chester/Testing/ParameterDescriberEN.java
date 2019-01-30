package uk.ac.chester.Testing;

public interface ParameterDescriberEN {

    default String parametersDescription(Class[] params){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            sb.append(params[i].getSimpleName());
            if (params.length > 1 && i == params.length - 2){
                sb.append(" and ");
            } else if (i < params.length - 1){
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
