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

/// RecordBeanBuilder
///
/// 参考 `dev.scx.object.x.mapper.record.RecordNodeMapperFactory` 和 `dev.scx.object.x.mapper.record.RecordNodeMapper`
///
/// @author scx567888
/// @version 0.0.1
final class RecordBeanBuilder<T> implements BeanBuilder<T> {

    private final ConstructorInfo recordConstructor;
    // 可写的 field 列表.
    private final FieldInfo[] fields;
    // fields 对应的列标签
    private final String[] fieldColumnLabels;

    public RecordBeanBuilder(ClassInfo classInfo, Function<FieldInfo, String> fieldColumnLabelMapping) {
        this.recordConstructor = classInfo.recordConstructor();
        this.fields = initRecordFields(classInfo, this.recordConstructor);
        this.fieldColumnLabels = initFieldColumnLabels(fields, fieldColumnLabelMapping);

        // 这里尝试开放反射访问权限, 失败忽略.
        trySetAccessible(this.recordConstructor, this.fields);
    }

    @Override
    public BeanMappingPlan createMappingPlan(ResultSetMetaData rsm, TypeSQLResolver resolver) throws SQLException {
        return new BeanMappingPlan(initFieldColumnIndexes(rsm, fieldColumnLabels), initFieldHandlers(fields, resolver));
    }

    @Override
    public T build(ResultSet rs, BeanMappingPlan mappingPlan) throws SQLException {
        var params = new Object[fields.length];

        var fieldColumnIndexes = mappingPlan.fieldColumnIndexes();
        var handlers = mappingPlan.handlers();

        for (int i = 0; i < fields.length; i = i + 1) {
            int fieldColumnIndex = fieldColumnIndexes[i];
            // -1 需要跳过
            if (fieldColumnIndex == -1) {
                params[i] = handlers[i].missingValue();
            } else {
                params[i] = handlers[i].read(rs, fieldColumnIndex);
            }
        }
        return newInstance(params);
    }

    @SuppressWarnings("unchecked")
    private T newInstance(Object... params) {
        try {
            return (T) this.recordConstructor.newInstance(params);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

}
