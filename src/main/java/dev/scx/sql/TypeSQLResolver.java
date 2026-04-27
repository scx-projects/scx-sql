package dev.scx.sql;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.*;
import dev.scx.sql.handler.math.BigDecimalSQLHandler;
import dev.scx.sql.handler.math.BigIntegerSQLHandler;
import dev.scx.sql.handler.primitive.*;
import dev.scx.sql.handler.time.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/// TypeSQLResolver
///
/// @author scx567888
/// @version 0.0.1
public final class TypeSQLResolver {

    public static final TypeSQLResolver DEFAULT_RESOLVER = initDefaultResolver();

    private final TypeSQLHandlerSelector selector;

    TypeSQLResolver(TypeSQLHandlerSelector selector) {
        this.selector = selector;
    }

    private static TypeSQLResolver initDefaultResolver() {
        return registerDefaultHandlers(TypeSQLResolver.builder()).build();
    }

    public static TypeSQLResolverBuilder registerDefaultHandlers(TypeSQLResolverBuilder builder) {
        // 基本类型
        builder.registerHandler(new ByteSQLHandler(true));
        builder.registerHandler(new ShortSQLHandler(true));
        builder.registerHandler(new IntSQLHandler(true));
        builder.registerHandler(new LongSQLHandler(true));
        builder.registerHandler(new FloatSQLHandler(true));
        builder.registerHandler(new DoubleSQLHandler(true));
        builder.registerHandler(new BooleanSQLHandler(true));

        // 基本类型包装类型
        builder.registerHandler(new ByteSQLHandler(false));
        builder.registerHandler(new ShortSQLHandler(false));
        builder.registerHandler(new IntSQLHandler(false));
        builder.registerHandler(new LongSQLHandler(false));
        builder.registerHandler(new FloatSQLHandler(false));
        builder.registerHandler(new DoubleSQLHandler(false));
        builder.registerHandler(new BooleanSQLHandler(false));

        // 字符串
        builder.registerHandler(new StringSQLHandler());

        // 大数字类型
        builder.registerHandler(new BigIntegerSQLHandler());
        builder.registerHandler(new BigDecimalSQLHandler());

        // 时间
        builder.registerHandler(new LocalDateTimeSQLHandler());
        builder.registerHandler(new LocalDateSQLHandler());
        builder.registerHandler(new LocalTimeSQLHandler());
        builder.registerHandler(new OffsetDateTimeSQLHandler());
        builder.registerHandler(new OffsetTimeSQLHandler());
        builder.registerHandler(new InstantSQLHandler());

        // byte[]
        builder.registerHandler(new ByteArraySQLHandler());

        // 注意顺序
        builder.registerHandlerFactory(new EnumSQLHandlerFactory());
        return builder;
    }

    public static TypeSQLResolverBuilder builder() {
        return new TypeSQLResolverBuilder();
    }

    public TypeSQLHandler<?> findHandler(TypeInfo type) {
        var handler = selector.findHandler(type);
        if (handler == null) {
            throw new IllegalArgumentException("No TypeSQLHandler found for type: " + type);
        }
        return handler;
    }

    public TypeSQLHandler<?> findHandler(Class<?> type) {
        var handler = selector.findHandler(type);
        if (handler == null) {
            throw new IllegalArgumentException("No TypeSQLHandler found for type: " + type);
        }
        return handler;
    }

    @SuppressWarnings("unchecked")
    public void bind(PreparedStatement ps, int i, Object value) throws SQLException {
        if (value == null) {
            ps.setNull(i, Types.NULL);
            return;
        }
        var handler = (TypeSQLHandler<Object>) findHandler(value.getClass());
        handler.bind(ps, i, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T read(ResultSet rs, int i, Class<T> type) throws SQLException {
        var handler = (TypeSQLHandler<Object>) findHandler(type);
        return (T) handler.read(rs, i);
    }

    @SuppressWarnings("unchecked")
    public <T> T read(ResultSet rs, int i, TypeInfo type) throws SQLException {
        var handler = (TypeSQLHandler<Object>) findHandler(type);
        return (T) handler.read(rs, i);
    }

}
