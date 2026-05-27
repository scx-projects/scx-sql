package dev.scx.sql.extractor.map;

import dev.scx.sql.TypeSQLResolver;
import dev.scx.sql.extractor.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/// MapExtractor
///
/// @author scx567888
/// @version 0.0.1
public record MapExtractor(MapBuilder mapBuilder) implements ResultSetExtractor<Map<String, Object>, RuntimeException> {

    public static final MapExtractor DEFAULT_MAP_EXTRACTOR = new MapExtractor(MapBuilder.of());

    @Override
    public Map<String, Object> extract(ResultSet rs, TypeSQLResolver resolver) throws SQLException {
        if (rs.next()) {
            var mappingPlan = mapBuilder.createMappingPlan(rs.getMetaData());
            return mapBuilder.build(rs, mappingPlan);
        } else {
            return null;
        }
    }

}
