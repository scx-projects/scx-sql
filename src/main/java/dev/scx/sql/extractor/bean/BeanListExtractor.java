package dev.scx.sql.extractor.bean;

import dev.scx.sql.TypeSQLResolver;
import dev.scx.sql.extractor.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/// BeanListExtractor
///
/// @author scx567888
/// @version 0.0.1
public record BeanListExtractor<T>(
    BeanBuilder<T> beanBuilder
) implements ResultSetExtractor<List<T>, RuntimeException> {

    @Override
    public List<T> extract(ResultSet rs, TypeSQLResolver resolver) throws SQLException {
        var mappingPlan = beanBuilder.createMappingPlan(rs.getMetaData(), resolver);
        var list = new ArrayList<T>();
        while (rs.next()) {
            T t = beanBuilder.build(rs, mappingPlan);
            list.add(t);
        }
        return list;
    }

}
