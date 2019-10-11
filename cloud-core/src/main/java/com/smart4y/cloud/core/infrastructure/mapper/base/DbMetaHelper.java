package com.smart4y.cloud.core.infrastructure.mapper.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * JDBC操作元数据 -- DatabaseMetaData接口
 *
 * @author Youtao
 *         Created by youtao on 2018/12/27.
 */
@Slf4j
@SuppressWarnings("all")
public class DbMetaHelper {

    private static final String TYPE = "TYPE";                          // 类型
    private static final String TABLE = "TABLE";                        // 表
    private static final String TABLE_TYPE = "TABLE_TYPE";              // 表类型
    private static final String TABLE_NAME = "TABLE_NAME";              // 表名
    private static final String REMARKS = "REMARKS";                    // 表备注、列备注
    private static final String COLUMN_NAME = "COLUMN_NAME";            // 列名
    private static final String KEY_SEQ = "KEY_SEQ";                    // 序列号(主键内值1表示第一列的主键，值2代表主键内的第二列)
    private static final String PK_NAME = "PK_NAME";                    // 主键名
    private static final String NON_UNIQUE = "NON_UNIQUE";              // 索引值是否可以不唯一,TYPE为 tableIndexStatistic时索引值为 false;
    private static final String INDEX_NAME = "INDEX_NAME";              // 索引的名称 ;TYPE为 tableIndexStatistic 时索引名称为 null;
    private static final String ORDINAL_POSITION = "ORDINAL_POSITION";  // 在索引列顺序号;TYPE为 tableIndexStatistic 时该序列号为零，其他重1开始;
    private static final String ASC_OR_DESC = "ASC_OR_DESC";            // 列排序顺序:升序还是降序[A:升序; B:降序];如果排序序列不受支持,可能为 null;TYPE为 tableIndexStatistic时排序序列为 null;
    private static final String TYPE_NAME = "TYPE_NAME";                // 列类型名称java.sql.Types类型名称(列类型名称)
    private static final String COLUMN_SIZE = "COLUMN_SIZE";            // 列大小
    private static final String DECIMAL_DIGITS = "DECIMAL_DIGITS";      // 小数位数
    private static final String NULLABLE = "NULLABLE";                  // 是否允许为NULL
    private static final String COLUMN_DEF = "COLUMN_DEF";              // 默认值
    private static final String CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH";// 对于 char 类型，该长度是列中的最大字节数

    private String URL;
    private String USER;
    private String PASSWORD;

    private DbMetaHelper(String url, String user, String password) {
        this.URL = url;
        this.USER = user;
        this.PASSWORD = password;
    }

    public static DbMetaHelper create(String url, String user, String password) {
        return new DbMetaHelper(url, user, password);
    }

    private Connection getConnection() {
        Connection conn = null;
        try {
            Properties props = new Properties();
            props.setProperty("user", USER);
            props.setProperty("password", PASSWORD);
            props.setProperty("remarks", "true"); // 设置可以获取remarks信息
            props.setProperty("useInformationSchema", "true");// 设置可以获取tables remarks信息
            conn = DriverManager.getConnection(URL, props);
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 获取数据库信息
     *
     * @return {@link DatabaseMetaData}
     */
    public DatabaseMetaData databaseMetaData() {
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            DatabaseMetaData databaseMetaData = conn.getMetaData();

            StringBuilder builder = new StringBuilder();
            rs = databaseMetaData.getTableTypes();
            while (rs.next()) {
                builder.append(String.format("%s, ", rs.getString(TABLE_TYPE)));
            }

            if (log.isInfoEnabled()) {
                String message = String.format(
                        "%n>>>>>>>>>>>>>>> 数据库信息 >>>>>>>>>>>>>>>%n" +
                                "数据库已知的用户：\t%s%n" +
                                "数据库的系统函数的逗号分隔列表：\t%s%n" +
                                "数据库的时间和日期函数的逗号分隔列表：\t%s%n" +
                                "数据库的字符串函数的逗号分隔列表：\t%s%n" +
                                "数据库供应商用于Schema的首选术语：\t%s%n" +
                                "数据库URL：\t%s%n" +
                                "是否允许只读：\t%s%n" +
                                "数据库的产品名称：\t%s%n" +
                                "数据库的版本：\t%s%n" +
                                "驱动程序的名称：\t%s%n" +
                                "驱动程序的版本：\t%s%n" +
                                "数据库中使用的表类型：\t%s%n",
                        databaseMetaData.getUserName(),
                        databaseMetaData.getSystemFunctions(),
                        databaseMetaData.getTimeDateFunctions(),
                        databaseMetaData.getStringFunctions(),
                        databaseMetaData.getSchemaTerm(),
                        databaseMetaData.getURL(),
                        databaseMetaData.isReadOnly(),
                        databaseMetaData.getDatabaseProductName(),
                        databaseMetaData.getDatabaseProductVersion(),
                        databaseMetaData.getDriverName(),
                        databaseMetaData.getDriverVersion(),
                        builder.toString());
                log.info(message);
            }
            return databaseMetaData;
        } catch (Exception e) {
            throw new RuntimeException("获取库信息错误");
        } finally {
            closeConn(rs, conn);
        }
    }

    /**
     * 获取 表信息
     * <p>
     * 输入参数：带Pattern表示可包含单字符通配符"_"或多字符通配符"%"
     * 方法原型：{@link DatabaseMetaData#getTables(String, String, String, String[])}
     * </p>
     *
     * @param schema           表模式（数据库名）
     * @param tableNamePattern 表名称（""，null，"sys_%"，"%"）
     * @return 表名 ==> {@link TableInfo}
     */
    public Map<String, TableInfo> getTables(String schema, String tableNamePattern) {
        Map<String, TableInfo> result = new HashMap<>();

        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            // types - 表类型数组; "TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"、"LOCAL TEMPORARY"、"ALIAS" 和 "SYNONYM";null表示包含所有的表类型;可包含单字符通配符("_"),或多字符通配符("%");
            String[] types = {TABLE};
            rs = databaseMetaData.getTables(schema, schema, tableNamePattern, types);
            while (rs.next()) {
                // 表名
                String tableName = rs.getString(TABLE_NAME);
                // 表备注
                String remarks = rs.getString(REMARKS);

                result.put(tableName,
                        new TableInfo(tableName, remarks));
            }
            if (log.isInfoEnabled()) {
                String message = String.format(
                        "%n>>>>>>>>>>>>>>> 表信息[catalog=%s, schema=%s, tableName=%s] >>>>>>>>>>>>>>>%n" +
                                "表名|表备注%n",
                        schema, schema, tableNamePattern);
                log.info(message + result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取表信息错误");
        } finally {
            closeConn(rs, conn);
        }
    }

    /**
     * 获取 表主键信息
     * <p>
     * 方法原型：{@link DatabaseMetaData#getPrimaryKeys(String, String, String)}
     * </p>
     *
     * @param schema    表模式（数据库名）
     * @param tableName 表名称
     * @return 列名 ==> {@link PrimaryKeysInfo}
     */
    public Map<String, PrimaryKeysInfo> getPrimaryKeysInfo(String schema, String tableName) {
        Map<String, PrimaryKeysInfo> result = new HashMap<>();

        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            rs = databaseMetaData.getPrimaryKeys(schema, schema, tableName);
            while (rs.next()) {
                String columnName = rs.getString(COLUMN_NAME);  // 列名
                short keySeq = rs.getShort(KEY_SEQ);            // 序列号(主键内值1表示第一列的主键，值2代表主键内的第二列)
                String pkName = rs.getString(PK_NAME);          // 主键名称

                result.put(columnName,
                        new PrimaryKeysInfo(columnName, keySeq, pkName));
            }
            if (log.isInfoEnabled()) {
                String message = String.format(
                        "%n>>>>>>>>>>>>>>> 表主键信息[catalog=%s, schema=%s, tableName=%s] >>>>>>>>>>>>>>>%n" +
                                "列名|序列号|主键名称%n",
                        schema, schema, tableName);
                log.info(message + result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取表主键信息错误");
        } finally {
            closeConn(rs, conn);
        }
    }

    /**
     * 获取 表索引信息
     * <p>
     * 输入参数：带Pattern表示可包含单字符通配符"_"或多字符通配符"%"
     * 方法原型：{@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
     * </p>
     *
     * @param schema           表模式（数据库名）
     * @param tableNamePattern 表名称（""，null，"sys_%"，"%"）
     * @return 索引名 ==> {@link IndexInfo}
     */
    public Map<String, IndexInfo> getIndexInfo(String schema, String tableNamePattern) {
        Map<String, IndexInfo> result = new HashMap<>();

        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            // unique - 该参数为 true时,仅返回唯一值的索引; 该参数为 false时,返回所有索引;
            // approximate - 该参数为true时,允许结果是接近的数据值或这些数据值以外的值;该参数为 false时,要求结果是精确结果;
            rs = databaseMetaData.getIndexInfo(schema, schema, tableNamePattern, Boolean.FALSE, Boolean.TRUE);

            while (rs.next()) {
                String indexName = rs.getString(INDEX_NAME);//索引的名称 ;TYPE为 tableIndexStatistic 时索引名称为 null;
                boolean nonUnique = rs.getBoolean(NON_UNIQUE);// 索引值是否可以不唯一,TYPE为 tableIndexStatistic时索引值为 false;
                short type = rs.getShort(TYPE);//索引类型;
                short ordinalPosition = rs.getShort(ORDINAL_POSITION);//在索引列顺序号;TYPE为 tableIndexStatistic 时该序列号为零;
                String columnName = rs.getString(COLUMN_NAME);//列名;TYPE为 tableIndexStatistic时列名称为 null;
                String ascOrDesc = rs.getString(ASC_OR_DESC);//列排序顺序:升序还是降序[A:升序; B:降序];如果排序序列不受支持,可能为 null;TYPE为 tableIndexStatistic时排序序列为 null;

                result.put(indexName,
                        new IndexInfo(indexName, nonUnique, type, ordinalPosition, columnName, ascOrDesc));
            }
            if (log.isInfoEnabled()) {
                String message = String.format(
                        "%n>>>>>>>>>>>>>>> 表索引信息[catalog=%s, schema=%s, tableName=%s] >>>>>>>>>>>>>>>%n" +
                                "索引名|索引值是否可以不唯一|索引类型|在索引列顺序号|列名|列排序顺序%n",
                        schema, schema, tableNamePattern);
                log.info(message + result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取表索引信息错误");
        } finally {
            closeConn(rs, conn);
        }
    }

    /**
     * 获取 表列信息
     * <p>
     * 输入参数：带Pattern表示可包含单字符通配符"_"或多字符通配符"%"
     * 方法原型：{@link DatabaseMetaData#getColumns(String, String, String, String)}
     * </p>
     *
     * @param schema            表模式（数据库名）
     * @param tableName         表名称
     * @param columnNamePattern 列名称（""，null，"sys_%"，"%"）
     * @return 引名 ==> {@link ColumnInfo}
     */
    public Map<String, ColumnInfo> getColumnsInfo(String schema, String tableName, String columnNamePattern) {
        Map<String, ColumnInfo> result = new HashMap<>();

        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            rs = databaseMetaData.getColumns(schema, schema, tableName, columnNamePattern);

            while (rs.next()) {
                String columnName = rs.getString(COLUMN_NAME);  // 列名
                String dataTypeName = rs.getString(TYPE_NAME);  // java.sql.Types类型名称(列类型名称)
                int columnSize = rs.getInt(COLUMN_SIZE);        // 列大小
                int decimalDigits = rs.getInt(DECIMAL_DIGITS);  // 小数位数
                int nullAble = rs.getInt(NULLABLE);             // 是否允许为null
                String columnDef = rs.getString(COLUMN_DEF);    // 默认值
                String remarks = rs.getString(REMARKS);         // 列描述
                int charOctetLength = rs.getInt(CHAR_OCTET_LENGTH);    // 对于 char 类型，该长度是列中的最大字节数
                int ordinalPosition = rs.getInt(ORDINAL_POSITION);   //表中列的索引（从1开始）

                result.put(columnName,
                        new ColumnInfo(columnName, dataTypeName, columnSize, decimalDigits, nullAble, columnDef, remarks, charOctetLength, ordinalPosition));
            }
            if (log.isInfoEnabled()) {
                String message = String.format(
                        "%n>>>>>>>>>>>>>>> 表列值信息[catalog=%s, schema=%s, tableName=%s] >>>>>>>>>>>>>>>%n" +
                                "列名|列类型|列大小|小数位数|是否允许为NULL|默认值|列描述|列最大字节数|列的顺序%n",
                        schema, schema, tableName);
                log.info(message + result);
            }
            return result;
        } catch (Exception ex) {
            throw new RuntimeException("获取表列信息错误");
        } finally {
            closeConn(rs, conn);
        }
    }

    private void closeConn(Object o) {
        if (o == null) {
            return;
        }
        if (o instanceof ResultSet) {
            try {
                ((ResultSet) o).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (o instanceof Statement) {
            try {
                ((Statement) o).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (o instanceof Connection) {
            Connection c = (Connection) o;
            try {
                if (!c.isClosed()) {
                    c.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeConn(ResultSet rs, Connection conn) {
        closeConn(rs);
        closeConn(conn);
    }

    /**
     * 表信息
     */
    @Data
    @AllArgsConstructor
    public static class TableInfo {
        /**
         * 表名
         */
        private String tableName;
        /**
         * 表备注
         */
        private String remarks;
    }

    /**
     * 表主键信息
     */
    @Data
    @AllArgsConstructor
    public static class PrimaryKeysInfo {
        /**
         * 列名
         */
        private String columnName;
        /**
         * 序列号(主键内值1表示第一列的主键，值2代表主键内的第二列)
         */
        private short keySeq;
        /**
         * 主键名称
         */
        private String pkName;
    }

    /**
     * 表索引信息
     */
    @Data
    @AllArgsConstructor
    public static class IndexInfo {
        /**
         * 索引的名称 ;TYPE为 tableIndexStatistic 时索引名称为 null
         */
        private String indexName;
        /**
         * 索引值是否可以不唯一,TYPE为 tableIndexStatistic时索引值为 false;
         */
        private boolean nonUnique;
        /**
         * 索引类型：
         * tableIndexStatistic - 此标识与表的索引描述一起返回的表统计信息
         * tableIndexClustered - 此为集群索引
         * tableIndexHashed - 此为散列索引
         * tableIndexOther - 此为某种其他样式的索引
         */
        private short type;
        /**
         * 在索引列顺序号;TYPE为 tableIndexStatistic 时该序列号为零
         */
        private short ordinalPosition;
        /**
         * 列名;TYPE为 tableIndexStatistic时列名称为 null
         */
        private String columnName;
        /**
         * 列排序顺序:升序还是降序[A:升序; B:降序];如果排序序列不受支持,可能为 null;TYPE为 tableIndexStatistic时排序序列为 null
         */
        private String ascOrDesc;
    }

    /**
     * 表列信息
     */
    @Data
    @AllArgsConstructor
    public static class ColumnInfo {
        /**
         * 列名
         */
        private String columnName;
        /**
         * java.sql.Types类型名称(列类型名称)
         */
        private String dataTypeName;
        /**
         * 列大小
         */
        private int columnSize;
        /**
         * 小数位数
         */
        private int decimalDigits;
        /**
         * 是否允许为null
         * 0 (columnNoNulls) - 该列不允许为空
         * 1 (columnNullable) - 该列允许为空
         * 2 (columnNullableUnknown) - 不确定该列是否为空
         */
        private int nullAble;
        /**
         * 默认值
         */
        private String columnDef;
        /**
         * 列描述
         */
        private String remarks;
        /**
         * 对于 char 类型，该长度是列中的最大字节数
         */
        private int charOctetLength;
        /**
         * 表中列的索引（从1开始）
         */
        private int ordinalPosition;
    }
}