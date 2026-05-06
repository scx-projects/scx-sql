package dev.scx.sql.extractor.map;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.scx.sql.extractor.map.MapBuilderHelper.createColumnLabels;
import static dev.scx.sql.extractor.map.MapBuilderHelper.createMapKeys;

/// DefaultMapBuilder
///
/// @author scx567888
/// @version 0.0.1
record DefaultMapBuilder(
    Supplier<Map<String, Object>> mapSupplier,
    Function<String, String> mapKeyMapping
) implements MapBuilder {

    public static final MapBuilder DEFAULT_MAP_BUILDER = new DefaultMapBuilder(LinkedHashMap::new, c -> c);

    @Override
    public MapMappingPlan createMappingPlan(ResultSetMetaData rsm) throws SQLException {
        var columnLabels = createColumnLabels(rsm);
        var mapKeys = createMapKeys(columnLabels, mapKeyMapping);
        return new MapMappingPlan(mapKeys);
    }

    @Override
    public Map<String, Object> build(ResultSet rs, MapMappingPlan mappingPlan) throws SQLException {

        var map = mapSupplier.get();
        var mapKeys = mappingPlan.mapKeys();

        for (int i = 1; i < mapKeys.length; i = i + 1) {
            map.put(mapKeys[i], rs.getObject(i));
        }

        return map;
    }

}
