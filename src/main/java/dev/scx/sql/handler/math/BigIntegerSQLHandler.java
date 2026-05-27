package dev.scx.sql.handler.math;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;

import static dev.scx.reflect.ScxReflect.typeOf;

/// 使用 BigDecimal 间接支持 BigInteger
///
/// @author scx567888
/// @version 0.0.1
public final class BigIntegerSQLHandler implements TypeSQLHandler<BigInteger> {

    @Override
    public TypeInfo valueType() {
        return typeOf(BigInteger.class);
    }

    @Override
    public void bind(PreparedStatement ps, int i, BigInteger value) throws SQLException {
        ps.setBigDecimal(i, new BigDecimal(value));
    }

    @Override
    public BigInteger read(ResultSet rs, int i) throws SQLException {
        var value = rs.getBigDecimal(i);
        try {
            return value == null ? null : value.toBigIntegerExact();
        } catch (ArithmeticException e) {
            throw new SQLDataException("Column " + i + " value " + value + " cannot be read as BigInteger", e);
        }
    }

}
