package dev.scx.sql.extractor.bean;

import dev.scx.function.Function1Void;
import dev.scx.sql.TypeSQLResolver;
import dev.scx.sql.extractor.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/// BeanConsumerExtractor
///
/// @author scx567888
/// @version 0.0.1
public record BeanConsumerExtractor<T, X extends Throwable>(
    BeanBuilder<T> beanBuilder,
    Function1Void<T, X> consumer
) implements ResultSetExtractor<Void, X> {

    @Override
    public Void extract(ResultSet rs, TypeSQLResolver resolver) throws SQLException, X {
        var mappingPlan = beanBuilder.createMappingPlan(rs.getMetaData(), resolver);
        while (rs.next()) {
            T t = beanBuilder.build(rs, mappingPlan);
            consumer.apply(t);
        }
        return null;
    }

}
