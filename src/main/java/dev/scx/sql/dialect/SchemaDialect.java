package dev.scx.sql.dialect;

import dev.scx.sql.schema.Column;
import dev.scx.sql.schema.DataTypeKind;
import dev.scx.sql.schema.Index;
import dev.scx.sql.schema.Table;

import java.util.List;

/// Schema 差异 相关方言
///
/// 该接口用于描述明确的 schema 动作在不同数据库方言下应生成哪些 DDL。
///
/// @author scx567888
/// @version 0.0.1
public interface SchemaDialect extends SyntaxDialect {

    // ************************* 类型处理相关 *************************

    /// 将数据库返回或方言定义的类型名归一为标准数据类型种类
    DataTypeKind dialectTypeNameToDataTypeKind(String dialectTypeName);

    /// 将标准数据类型种类映射为当前方言使用的类型名
    String dataTypeKindToDialectTypeName(DataTypeKind dataTypeKind);

    //**************************** DDL 相关 *********************************

    /// 获取创建指定表所需的 DDL 列表。
    ///
    /// 示例:
    ///
    /// - MySQL:
    ///
    /// ```sql
    /// CREATE TABLE `user`
    /// (
    ///     `id`      BIGINT       NOT NULL AUTO_INCREMENT,
    ///     `name`    VARCHAR(128) NOT NULL,
    ///     `id_card` VARCHAR(128) NOT NULL,
    ///     PRIMARY KEY (`id`),
    ///     UNIQUE KEY `unique_id_card` (`id_card`),
    ///     KEY `index_name` (`name`)
    /// );
    /// ```
    ///
    /// - SQLite:
    ///
    /// ```sql
    /// CREATE TABLE "user"
    /// (
    ///     "id"      INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    ///     "name"    TEXT    NOT NULL,
    ///     "id_card" TEXT    NOT NULL
    /// );
    ///
    /// CREATE UNIQUE INDEX "unique_id_card" ON "user" ("id_card");
    ///
    /// CREATE INDEX "index_name" ON "user" ("name");
    /// ```
    ///
    List<String> getCreateTableDDLs(Table table);

    /// 获取为指定表添加指定列所需的 DDL 列表。
    ///
    /// 示例:
    ///
    /// - MySQL:
    ///
    /// ```sql
    /// ALTER TABLE `user` ADD COLUMN `age` INT;
    /// ```
    ///
    /// - SQLite:
    ///
    /// ```sql
    /// ALTER TABLE "user" ADD COLUMN "age" INTEGER;
    /// ```
    ///
    List<String> getAddColumnDDLs(Table table, Column column);

    /// 获取为指定表删除指定列所需的 DDL 列表。
    ///
    /// 示例:
    ///
    /// - MySQL:
    ///
    /// ```sql
    /// ALTER TABLE `user` DROP COLUMN `age`;
    /// ```
    /// - SQLite:
    ///
    /// ```sql
    /// ALTER TABLE "user" DROP COLUMN "age";
    /// ```
    List<String> getDropColumnDDLs(Table table, Column column);

    /// 获取为指定表添加指定索引所需的 DDL 列表。
    ///
    /// 示例:
    ///
    /// - MySQL:
    ///
    /// ```sql
    /// CREATE INDEX `index_name` ON `user` (`name`);
    /// ```
    ///
    /// - SQLite:
    ///
    /// ```sql
    /// CREATE INDEX "index_name" ON "user" ("name");
    /// ```
    List<String> getAddIndexDDLs(Table table, Index index);

    /// 获取为指定表删除指定索引所需的 DDL 列表。
    ///
    /// 示例:
    ///
    /// - MySQL:
    ///
    /// ```sql
    /// DROP INDEX `index_name` ON `user`;
    /// ```
    ///
    /// - SQLite:
    ///
    /// ```sql
    /// DROP INDEX "index_name";
    /// ```
    List<String> getDropIndexDDLs(Table table, Index index);

}
