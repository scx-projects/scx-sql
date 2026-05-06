package dev.scx.sql.handler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/// ParameterBinder
///
/// @author scx567888
/// @version 0.0.1
public interface ParameterBinder<V> {

    /// value 永不为 null
    void bind(PreparedStatement ps, int i, V value) throws SQLException;

}
