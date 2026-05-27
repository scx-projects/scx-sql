package dev.scx.sql.extractor.bean;

import dev.scx.sql.TypeSQLResolver;
import dev.scx.sql.extractor.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/// BeanExtractor
///
/// @author scx567888
/// @version 0.0.1
public record BeanExtractor<T>(BeanBuilder<T> beanBuilder) implements ResultSetExtractor<T, RuntimeException> {

    @Override
    public T extract(ResultSet rs, TypeSQLResolver resolver) throws SQLException {
        if (rs.next()) {
            var mappingPlan = beanBuilder.createMappingPlan(rs.getMetaData(), resolver);
            return beanBuilder.build(rs, mappingPlan);
        } else {
            return null;
        }
    }

}
