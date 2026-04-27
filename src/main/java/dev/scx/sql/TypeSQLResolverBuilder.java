package dev.scx.sql;

import dev.scx.sql.handler.TypeSQLHandler;
import dev.scx.sql.handler.TypeSQLHandlerFactory;
import dev.scx.sql.handler.TypeSQLHandlerSelector;
import dev.scx.sql.handler.TypeSQLHandlerSelectorImpl;

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

    public TypeSQLResolver build() {
        return new TypeSQLResolver(selector);
    }

}
