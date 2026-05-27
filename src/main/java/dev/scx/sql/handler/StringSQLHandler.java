package dev.scx.sql.handler;

import dev.scx.reflect.TypeInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dev.scx.reflect.ScxReflect.typeOf;

/// StringSQLHandler
///
/// @author scx567888
/// @version 0.0.1
public final class StringSQLHandler implements TypeSQLHandler<String> {

    @Override
    public TypeInfo valueType() {
        return typeOf(String.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, String value) throws SQLException {
        ps.setString(i, value);
    }

    @Override
    public String read(ResultSet rs, int i) throws SQLException {
        return rs.getString(i);
    }

}
