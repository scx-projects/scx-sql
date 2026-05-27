package dev.scx.sql.extractor.bean;

import dev.scx.reflect.AccessModifier;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ConstructorInfo;
import dev.scx.reflect.FieldInfo;
import dev.scx.sql.TypeSQLResolver;
import dev.scx.sql.handler.TypeSQLHandler;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

/// 参考 `BeanNodeMapperFactory`
///
/// @author scx567888
/// @version 0.0.1
final class BeanBuilderHelper {

    /// 针对某些非 public 的 class/record, 提前尝试开放其构造函数 和 字段 的反射访问权限.
    ///
    /// 这里只做尝试, 失败时忽略.
    /// 因为有些场景下可能只会走序列化或只会走反序列化,
    /// 没有必要在 mapper 创建阶段因为访问权限问题直接失败.
    /// 真正需要调用时, 再由具体读写流程决定是否抛出异常.
    public static void trySetAccessible(ConstructorInfo constructor, FieldInfo[] fields) {
        constructor.rawConstructor().trySetAccessible();
        for (var field : fields) {
            field.rawField().trySetAccessible();
        }
    }

    private static FieldInfo[] filterReadableFields(ClassInfo classInfo) {
        // 因为 BeanBuilder 每种类型的对象只会创建很少次, 所以这里 使用 Stream 并没有什么性能问题
        // 注意我们这里需要连父级的字段也带上
        return Arrays.stream(classInfo.allFields())
            .filter(c -> !c.isStatic() && c.accessModifier() == AccessModifier.PUBLIC)
            .toArray(FieldInfo[]::new);
    }

    private static FieldInfo[] filterWritableFields(FieldInfo[] readableFields) {
        // 因为 BeanBuilder 每种类型的对象只会创建很少次, 所以这里 使用 Stream 并没有什么性能问题
        return Arrays.stream(readableFields).filter(c -> !c.isFinal()).toArray(FieldInfo[]::new);
    }

    /// 这里只 保留 可写 的 field
    public static FieldInfo[] initBeanFields(ClassInfo classInfo) {
        // 可读的字段, 这里只要 public 的实例字段
        var readableFields = filterReadableFields(classInfo);
        // 可写的字段, 相较于可读 我们过滤掉 final
        return filterWritableFields(readableFields);
    }

    /// 这里会根据 Record 标准构造函数的参数顺序重新排序 fields
    public static FieldInfo[] initRecordFields(ClassInfo classInfo, ConstructorInfo recordConstructor) {
        // 使用 map 加速查找
        var map = new HashMap<String, FieldInfo>();
        // record 我们取 fields 即可, 不用 allFields
        for (var field : classInfo.fields()) {
            map.put(field.name(), field);
        }
        // 根据 parameters 的参数顺序重排 fields
        var parameters = recordConstructor.parameters();
        var fields = new FieldInfo[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            //这里直接使用 getName 是安全的, 因为 Record 中规范构造函数的参数名称是固定的
            fields[i] = map.get(parameters[i].name());
        }
        return fields;
    }

    public static String[] initFieldColumnLabels(FieldInfo[] fields, Function<FieldInfo, String> fieldColumnLabelMapping) {
        var fieldColumnLabels = new String[fields.length];
        for (int i = 0; i < fields.length; i = i + 1) {
            var field = fields[i];
            var columnLabel = fieldColumnLabelMapping.apply(field);
            // 若 fieldColumnLabelMapping 提供空值, 则回退到 field.name()
            if (columnLabel == null) {
                columnLabel = field.name();
            }
            fieldColumnLabels[i] = columnLabel;
        }
        return fieldColumnLabels;
    }


    /// 根据 [ResultSetMetaData] 生成与字段数组同下标对齐的列索引数组.
    ///
    /// 返回结果中的每一项都与 `fieldColumnLabels` 中对应位置的字段一一对应:
    ///
    /// - `indexInfo[i] >= 1` 表示第 `i` 个字段在当前结果集中对应的列索引
    /// - `indexInfo[i] == -1` 表示当前结果集中不存在该字段对应的列
    ///
    /// 这里使用 [ResultSetMetaData#getColumnLabel(int)] 作为列标签来源,
    /// 因此会优先按 SQL 中的列别名进行匹配.
    ///
    /// 该方法的目的, 是在真正读取每一行数据之前, 先完成一次
    /// “字段 -> 结果集列索引” 的预解析, 避免在逐行构建对象时重复按列名查找.
    ///
    /// @param rsm 当前结果集的元数据
    /// @return 与 `fieldColumnLabels` 同下标对齐的列索引数组
    /// @throws SQLException 当读取结果集元数据失败时抛出
    public static int[] initFieldColumnIndexes(ResultSetMetaData rsm, String[] fieldColumnLabels) throws SQLException {
        var count = rsm.getColumnCount();
        var labelIndexMap = new HashMap<String, Integer>();
        for (int i = 1; i <= count; i = i + 1) {
            labelIndexMap.put(rsm.getColumnLabel(i), i);
        }

        var fieldColumnIndexes = new int[fieldColumnLabels.length];
        for (int i = 0; i < fieldColumnLabels.length; i = i + 1) {
            fieldColumnIndexes[i] = labelIndexMap.getOrDefault(fieldColumnLabels[i], -1);
        }
        return fieldColumnIndexes;
    }

    public static TypeSQLHandler<?>[] initFieldHandlers(FieldInfo[] fields, TypeSQLResolver resolver) {
        var handlers = new TypeSQLHandler<?>[fields.length];
        for (int i = 0; i < fields.length; i = i + 1) {
            var field = fields[i];
            handlers[i] = resolver.findHandler(field.fieldType());
        }
        return handlers;
    }

}
