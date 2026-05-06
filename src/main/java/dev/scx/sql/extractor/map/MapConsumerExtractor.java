package dev.scx.sql.extractor.map;

import dev.scx.function.Function1Void;
import dev.scx.sql.TypeSQLResolver;
import dev.scx.sql.extractor.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/// MapConsumerExtractor
///
/// @author scx567888
/// @version 0.0.1
public record MapConsumerExtractor<X extends Throwable>(
    MapBuilder mapBuilder,
    Function1Void<Map<String, Object>, X> consumer
) implements ResultSetExtractor<Void, X> {

    @Override
    public Void extract(ResultSet rs, TypeSQLResolver resolver) throws SQLException, X {
        var mappingPlan = mapBuilder.createMappingPlan(rs.getMetaData());
        while (rs.next()) {
            var map = mapBuilder.build(rs, mappingPlan);
            consumer.apply(map);
        }
        return null;
    }

}
