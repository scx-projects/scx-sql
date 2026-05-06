package dev.scx.sql.handler;

import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.TypeInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;

/// EnumSQLHandler
///
/// 参考 `dev.scx.object.x.mapper.EnumNodeMapper`
///
/// @author scx567888
/// @version 0.0.1
public final class EnumSQLHandler<E extends Enum<E>> implements TypeSQLHandler<E> {

    private final ClassInfo enumType;
    private final Class<E> enumClass;

    @SuppressWarnings("unchecked")
    EnumSQLHandler(ClassInfo enumType) {
        this.enumType = enumType;
        this.enumClass = (Class<E>) this.enumType.enumClass().rawClass();
    }

    @Override
    public TypeInfo valueType() {
        return enumType;
    }

    @Override
    public void bind(PreparedStatement ps, int i, E value) throws SQLException {
        ps.setString(i, value.name());
    }

    @Override
    public E read(ResultSet rs, int i) throws SQLException {
        var value = rs.getString(i);
        if (value == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            throw new SQLDataException("Column " + i + " value '" + value + "' cannot be read as enum " + enumClass.getName(), e);
        }
    }

}
