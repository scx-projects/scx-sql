package dev.scx.sql;

import dev.scx.sql.handler.*;
import dev.scx.sql.handler.math.BigDecimalSQLHandler;
import dev.scx.sql.handler.math.BigIntegerSQLHandler;
import dev.scx.sql.handler.primitive.*;
import dev.scx.sql.handler.time.*;

/// TypeSQLResolverBuilder
///
/// @author scx567888
/// @version 0.0.1
public final class TypeSQLResolverBuilder {

    private final TypeSQLHandlerSelector selector;

    TypeSQLResolverBuilder() {
        this.selector = new TypeSQLHandlerSelectorImpl();
    }

    public TypeSQLResolverBuilder registerHandler(TypeSQLHandler<?> handler) {
        selector.registerHandler(handler);
        return this;
    }

    public TypeSQLResolverBuilder registerHandlerFactory(TypeSQLHandlerFactory handlerFactory) {
        selector.registerHandlerFactory(handlerFactory);
        return this;
    }

    public TypeSQLResolverBuilder registerHandlerFactory(TypeSQLHandlerFactory handlerFactory, int order) {
        selector.registerHandlerFactory(handlerFactory, order);
        return this;
    }

    public TypeSQLResolverBuilder registerDefaultHandlers() {
        // 基本类型
        this.registerHandler(new ByteSQLHandler(true));
        this.registerHandler(new ShortSQLHandler(true));
        this.registerHandler(new IntSQLHandler(true));
        this.registerHandler(new LongSQLHandler(true));
        this.registerHandler(new FloatSQLHandler(true));
        this.registerHandler(new DoubleSQLHandler(true));
        this.registerHandler(new BooleanSQLHandler(true));

        // 基本类型包装类型
        this.registerHandler(new ByteSQLHandler(false));
        this.registerHandler(new ShortSQLHandler(false));
        this.registerHandler(new IntSQLHandler(false));
        this.registerHandler(new LongSQLHandler(false));
        this.registerHandler(new FloatSQLHandler(false));
        this.registerHandler(new DoubleSQLHandler(false));
        this.registerHandler(new BooleanSQLHandler(false));

        // 字符串
        this.registerHandler(new StringSQLHandler());

        // 大数字类型
        this.registerHandler(new BigIntegerSQLHandler());
        this.registerHandler(new BigDecimalSQLHandler());

        // 时间
        this.registerHandler(new LocalDateTimeSQLHandler());
        this.registerHandler(new LocalDateSQLHandler());
        this.registerHandler(new LocalTimeSQLHandler());
        this.registerHandler(new OffsetDateTimeSQLHandler());
        this.registerHandler(new OffsetTimeSQLHandler());
        this.registerHandler(new InstantSQLHandler());

        // byte[]
        this.registerHandler(new ByteArraySQLHandler());

        // 注意顺序
        this.registerHandlerFactory(new EnumSQLHandlerFactory());

        return this;
    }

    public TypeSQLResolver build() {
        return new TypeSQLResolver(selector);
    }

}
