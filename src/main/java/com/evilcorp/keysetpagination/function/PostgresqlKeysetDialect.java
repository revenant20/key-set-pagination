package com.evilcorp.keysetpagination.function;

import org.hibernate.dialect.PostgreSQL10Dialect;

public class PostgresqlKeysetDialect extends PostgreSQL10Dialect {
    public PostgresqlKeysetDialect() {
        super();
        registerFunction("row_values", new RowValuesFunction());
    }
}
