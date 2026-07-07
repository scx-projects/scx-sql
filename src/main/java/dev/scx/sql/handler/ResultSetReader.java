package dev.scx.sql.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/// ResultSetReader
///
/// @author scx567888
public interface ResultSetReader<V> {

    V read(ResultSet rs, int i) throws SQLException;

}
