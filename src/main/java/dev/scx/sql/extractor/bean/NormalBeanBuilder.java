package dev.scx.sql.extractor.bean;

import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ConstructorInfo;
import dev.scx.reflect.FieldInfo;
import dev.scx.sql.TypeSQLResolver;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.function.Function;

import static dev.scx.sql.extractor.bean.BeanBuilderHelper.*;

/// NormalBeanBuilder
///
/// 参考 `dev.scx.object.x.mapper.bean.BeanNodeMapperFactory` 和 `dev.scx.object.x.mapper.bean.BeanNodeMapper`
///
/// @author scx567888
/// @version 0.0.1
final class NormalBeanBuilder<T> implements BeanBuilder<T> {

    private final ConstructorInfo defaultConstructor;
    // 可写的 field 列表.
    private final FieldInfo[] fields;
    // fields 对应的列标签
    private final String[] fieldColumnLabels;

    public NormalBeanBuilder(ClassInfo classInfo, Function<FieldInfo, String> fieldColumnLabelMapping) {
        this.defaultConstructor = checkDefaultConstructor(classInfo);
        this.fields = initBeanFields(classInfo);
        this.fieldColumnLabels = initFieldColumnLabels(fields, fieldColumnLabelMapping);

        // 这里尝试开放反射访问权限, 失败忽略.
        trySetAccessible(this.defaultConstructor, this.fields);
    }

    /// 检查 无参构造函数 (不支持成员类)
    private static ConstructorInfo checkDefaultConstructor(ClassInfo classInfo) {
        var defaultConstructor = classInfo.defaultConstructor();
        if (defaultConstructor == null) {
            throw new IllegalArgumentException("未找到可用的无参构造函数: " + classInfo);
        }
        return defaultConstructor;
    }

    private static void setFieldValue(FieldInfo fieldInfo, Object object, Object value) throws IllegalStateException {
        try {
            fieldInfo.set(object, value);
        } catch (IllegalAccessException e) {
            // 因为我们 使用的都是 public 字段 理论上不会出现 这种异常
            throw new IllegalStateException(e);
        }
    }

    @Override
    public BeanMappingPlan createMappingPlan(ResultSetMetaData rsm, TypeSQLResolver resolver) throws SQLException {
        return new BeanMappingPlan(initFieldColumnIndexes(rsm, fieldColumnLabels), initFieldHandlers(fields, resolver));
    }

    @Override
    public T build(ResultSet rs, BeanMappingPlan mappingPlan) throws SQLException {
        T t = newInstance();

        var fieldColumnIndexes = mappingPlan.fieldColumnIndexes();
        var handlers = mappingPlan.handlers();

        for (int i = 0; i < fields.length; i = i + 1) {
            int fieldColumnIndex = fieldColumnIndexes[i];
            // -1 需要跳过
            if (fieldColumnIndex == -1) {
                continue;
            }
            var value = handlers[i].read(rs, fieldColumnIndex);
            setFieldValue(fields[i], t, value);
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    private T newInstance() throws IllegalStateException {
        try {
            return (T) defaultConstructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // 因为我们使用的都是 public 构造函数, 所以这里理论上不会出现异常, 除非构造函数方法内部逻辑异常
            throw new IllegalStateException(e);
        }
    }

}
