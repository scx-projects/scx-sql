package dev.scx.sql.handler;

import dev.scx.reflect.TypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static dev.scx.reflect.ScxReflect.typeOf;

/// TypeSQLHandlerSelectorImpl (支持动态扩容)
///
/// 参考 `dev.scx.object.x.mapper.TypeNodeMapperSelectorImpl`
///
/// @author scx567888
/// @version 0.0.1
public final class TypeSQLHandlerSelectorImpl implements TypeSQLHandlerSelector {

    // 同时缓存 Class 和 TypeInfo, 加速查找
    private final Map<Object, TypeSQLHandler<?>> handlers;

    private final List<TypeSQLHandlerFactory> handlerFactories;

    private final Lock lock;

    public TypeSQLHandlerSelectorImpl() {
        this.handlers = new ConcurrentHashMap<>();
        this.handlerFactories = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    @Override
    public void registerHandler(TypeSQLHandler<?> handler) {
        var typeInfo = handler.valueType();
        handlers.put(typeInfo, handler);
        // 这里对可以无损 映射到 class 的进行双重 key 缓存.
        if (typeInfo.isRaw()) {
            handlers.put(typeInfo.rawClass(), handler);
        }
    }

    @Override
    public void registerHandlerFactory(TypeSQLHandlerFactory handlerFactory) {
        handlerFactories.add(handlerFactory);
    }

    @Override
    public void registerHandlerFactory(TypeSQLHandlerFactory handlerFactory, int order) {
        handlerFactories.add(order, handlerFactory);
    }

    @Override
    public TypeSQLHandler<?> findHandler(TypeInfo type) {
        var handler = handlers.get(type);
        if (handler != null) {
            return handler;
        }
        lock.lock();
        try {
            // 双重检查
            handler = handlers.get(type);
            if (handler != null) {
                return handler;
            }
            // 尝试创建 handler
            handler = this.tryCreateHandler(type);
            if (handler != null) {
                // 注册到 handlers 中.
                registerHandler(handler);
                return handler;
            }
            // createHandler 也没有就是彻底没有了.
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public TypeSQLHandler<?> findHandler(Class<?> type) {
        var handler = handlers.get(type);
        if (handler != null) {
            return handler;
        }
        lock.lock();
        try {
            // 双重检查
            handler = handlers.get(type);
            if (handler != null) {
                return handler;
            }
            // 尝试创建 handler
            handler = this.tryCreateHandler(typeOf(type));
            if (handler != null) {
                // 注册到 handlers 中.
                registerHandler(handler);
                return handler;
            }
            // createHandler 也没有就是彻底没有了.
            return null;
        } finally {
            lock.unlock();
        }
    }

    /// 创建新的 TypeSQLHandler
    private TypeSQLHandler<?> tryCreateHandler(TypeInfo typeInfo) {
        for (var factory : handlerFactories) {
            var handler = factory.createHandler(typeInfo);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

}
