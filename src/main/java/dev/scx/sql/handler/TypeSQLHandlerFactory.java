package dev.scx.sql.handler;

import dev.scx.reflect.TypeInfo;

/// TypeSQLHandlerFactory
///
/// 参考 `TypeNodeMapperFactory`
///
/// @author scx567888
public interface TypeSQLHandlerFactory {

    TypeSQLHandler<?> createHandler(TypeInfo typeInfo);

}
