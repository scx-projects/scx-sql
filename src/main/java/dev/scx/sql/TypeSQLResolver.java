package dev.scx.sql;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;
import dev.scx.sql.handler.TypeSQLHandlerSelector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/// TypeSQLResolver
///
/// @author scx567888
/// @version 0.0.1
public final class TypeSQLResolver {

    public static final TypeSQLResolver DEFAULT_TYPE_SQL_RESOLVER = TypeSQLResolver.builder().registerDefaultHandlers().build();

    private final TypeSQLHandlerSelector selector;

    TypeSQLResolver(TypeSQLHandlerSelector selector) {
        this.selector = selector;
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
