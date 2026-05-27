package dev.scx.sql.handler;

import dev.scx.reflect.TypeInfo;

/// TypeSQLHandlerSelector
///
/// 参考 `TypeNodeMapperSelector`
///
/// @author scx567888
/// @version 0.0.1
public interface TypeSQLHandlerSelector {

    void registerHandler(TypeSQLHandler<?> handler);

    void registerHandlerFactory(TypeSQLHandlerFactory handlerFactory);

    void registerHandlerFactory(TypeSQLHandlerFactory handlerFactory, int order);

    /// 没找到会返回 null
    TypeSQLHandler<?> findHandler(TypeInfo type);

    /// 没找到会返回 null
    TypeSQLHandler<?> findHandler(Class<?> type);

}
