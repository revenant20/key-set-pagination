package com.evilcorp.keysetpagination.function;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;

import java.util.ArrayList;
import java.util.List;

public class RowValuesFunction implements SQLFunction {
    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public boolean hasParenthesesIfNoArguments() {
        return false;
    }

    @Override
    public Type getReturnType(Type type, Mapping mapping)
            throws QueryException {
        return BooleanType.INSTANCE;
    }

    @Override
    public String render(Type type, List list
            , SessionFactoryImplementor sessionFactoryImplementor)
            throws QueryException {
        List<String> names = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 2) {
            String fieldName = list.get(i).toString();
            String fieldValue = list.get(i + 1).toString();
            names.add(fieldName);
            values.add(fieldValue);
        }
        String left = "(" + String.join(",", names) + ")";
        String right = "(" + String.join(",", values) + ")";
        return "(" + left + " >= " + right + ")";
    }

    private static String removeSurroundingQuotes(String operator) {
        return operator.substring(1, operator.length() - 1);
    }
}
