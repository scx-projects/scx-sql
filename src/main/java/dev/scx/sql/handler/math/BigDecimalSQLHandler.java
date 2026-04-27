package dev.scx.sql.handler.math;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dev.scx.reflect.ScxReflect.typeOf;

/// BigDecimalSQLHandler
///
/// @author scx567888
/// @version 0.0.1
public final class BigDecimalSQLHandler implements TypeSQLHandler<BigDecimal> {

    @Override
    public TypeInfo valueType() {
        return typeOf(BigDecimal.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, BigDecimal value) throws SQLException {
        ps.setBigDecimal(i, value);
    }

    @Override
    public BigDecimal read(ResultSet rs, int index) throws SQLException {
        return rs.getBigDecimal(index);
    }

}
