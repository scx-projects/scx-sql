package dev.scx.sql.extractor.map;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.function.Function;

/// MapBuilderHelper
///
/// @author scx567888
/// @version 0.0.1
final class MapBuilderHelper {

    public static String[] createColumnLabels(ResultSetMetaData rsm) throws SQLException {
        var count = rsm.getColumnCount();
        var columnLabels = new String[count + 1]; // +1 是为了匹配 JDBC 索引从 1 开始, 下标 0 永远不用
        for (int i = 1; i <= count; i = i + 1) {
            columnLabels[i] = rsm.getColumnLabel(i);
        }
        return columnLabels;
    }

    public static String[] createMapKeys(String[] columnLabels, Function<String, String> mapKeyMapping) {
        var mapKeys = new String[columnLabels.length];

        for (int i = 1; i < columnLabels.length; i = i + 1) {
            var columnLabel = columnLabels[i];
            var mapKey = mapKeyMapping.apply(columnLabel);
            if (mapKey == null) {
                mapKey = columnLabel;
            }
            mapKeys[i] = mapKey;
        }

        return mapKeys;
    }

}
