package dev.scx.sql.extractor.map;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.scx.sql.extractor.map.DefaultMapBuilder.DEFAULT_MAP_BUILDER;

/// MapBuilder
///
/// @author scx567888
/// @version 0.0.1
public interface MapBuilder {

    static MapBuilder of() {
        return DEFAULT_MAP_BUILDER;
    }

    /// @param mapKeyMapping 列名映射 columnLabel -> map key.
    static MapBuilder of(Function<String, String> mapKeyMapping) {
        return new DefaultMapBuilder(LinkedHashMap::new, mapKeyMapping);
    }

    static MapBuilder of(Supplier<Map<String, Object>> mapSupplier) {
        return new DefaultMapBuilder(mapSupplier, c -> c);
    }

    /// @param mapKeyMapping 列名映射 columnLabel -> map key.
    static MapBuilder of(Supplier<Map<String, Object>> mapSupplier, Function<String, String> mapKeyMapping) {
        return new DefaultMapBuilder(mapSupplier, mapKeyMapping);
    }

    MapMappingPlan createMappingPlan(ResultSetMetaData rsm) throws SQLException;

    Map<String, Object> build(ResultSet rs, MapMappingPlan mappingPlan) throws SQLException;

}
