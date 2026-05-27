package dev.scx.sql.extractor.map;

import dev.scx.sql.TypeSQLResolver;
import dev.scx.sql.extractor.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/// MapListExtractor
///
/// @author scx567888
/// @version 0.0.1
public record MapListExtractor(
    MapBuilder mapBuilder
) implements ResultSetExtractor<List<Map<String, Object>>, RuntimeException> {

    public static final MapListExtractor DEFAULT_MAP_LIST_EXTRACTOR = new MapListExtractor(MapBuilder.of());

    @Override
    public List<Map<String, Object>> extract(ResultSet rs, TypeSQLResolver resolver) throws SQLException {
        var mappingPlan = mapBuilder.createMappingPlan(rs.getMetaData());
        var list = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            var map = mapBuilder.build(rs, mappingPlan);
            list.add(map);
        }
        return list;
    }

}
