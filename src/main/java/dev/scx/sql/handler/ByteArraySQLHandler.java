package dev.scx.sql.handler;

import dev.scx.reflect.TypeInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dev.scx.reflect.ScxReflect.typeOf;

/// ByteArraySQLHandler
///
/// @author scx567888
/// @version 0.0.1
public final class ByteArraySQLHandler implements TypeSQLHandler<byte[]> {

    @Override
    public TypeInfo valueType() {
        return typeOf(byte[].class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, byte[] value) throws SQLException {
        ps.setBytes(i, value);
    }

    @Override
    public byte[] read(ResultSet rs, int i) throws SQLException {
        return rs.getBytes(i);
    }

}
