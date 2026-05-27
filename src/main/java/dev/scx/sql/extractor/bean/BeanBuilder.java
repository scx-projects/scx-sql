package dev.scx.sql.extractor.bean;

import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ClassKind;
import dev.scx.reflect.FieldInfo;
import dev.scx.sql.TypeSQLResolver;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.function.Function;

import static dev.scx.reflect.ScxReflect.typeOf;

/// BeanBuilder
///
/// 此处的 Bean, 我们泛指 Class 和 Record, 这点和 序列化库中的设计不同.
/// 因为 在用户视角看, 并不关心 到底是 Class 还是 Record.
///
/// 关于 bean 类型的分流逻辑, 参考
/// `dev.scx.object.x.mapper.bean.BeanNodeMapperFactory`
/// 和
/// `dev.scx.object.x.mapper.record.RecordNodeMapperFactory`.
///
/// 当前 SQL 对象映射在列标签映射这一层统一以 [FieldInfo] 为依据.
/// 对于 record 类型, 虽然从语义上更适合基于
/// [dev.scx.reflect.RecordComponentInfo] 建模,
/// 但在当前实现中, 仍暂按其对应 field 参与列标签映射.
///
/// @author scx567888
/// @version 0.0.1
public interface BeanBuilder<T> {

    static <T> BeanBuilder<T> of(Class<T> type) {
        return of(type, FieldInfo::name);
    }

    /// @param fieldColumnLabelMapping 列标签映射, 方向为 FieldInfo -> columnLabel
    static <T> BeanBuilder<T> of(Class<T> type, Function<FieldInfo, String> fieldColumnLabelMapping) {
        var typeInfo = typeOf(type);
        if (typeInfo instanceof ClassInfo classInfo) {
            if (classInfo.classKind() == ClassKind.CLASS) {
                if (classInfo.isAbstract()) {
                    throw new IllegalArgumentException("Abstract class 无法被实例化: " + classInfo);
                }
                return new NormalBeanBuilder<>(classInfo, fieldColumnLabelMapping);
            } else if (classInfo.classKind() == ClassKind.RECORD) {
                return new RecordBeanBuilder<>(classInfo, fieldColumnLabelMapping);
            }
        }
        throw new IllegalArgumentException("不支持的类型: " + type);
    }

    BeanMappingPlan createMappingPlan(ResultSetMetaData rsm, TypeSQLResolver resolver) throws SQLException;

    T build(ResultSet rs, BeanMappingPlan mappingPlan) throws SQLException;

}
