package dev.scx.sql.handler;

import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ClassKind;
import dev.scx.reflect.TypeInfo;

/// EnumSQLHandlerFactory
///
/// 参考 `dev.scx.object.x.mapper.EnumNodeMapperFactory`
///
/// @author scx567888
/// @version 0.0.1
public final class EnumSQLHandlerFactory implements TypeSQLHandlerFactory {

    @Override
    public EnumSQLHandler<?> createHandler(TypeInfo typeInfo) {
        if (typeInfo instanceof ClassInfo classInfo) {
            if (classInfo.classKind() == ClassKind.ENUM) {
                return new EnumSQLHandler<>(classInfo);
            }
        }
        return null;
    }

}
