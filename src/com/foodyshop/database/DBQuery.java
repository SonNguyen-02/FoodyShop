/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.database;

import java.util.HashMap;

/**
 *
 * @author WIN1064
 */
public interface DBQuery {

    /**
     * <strong>Select:</strong><br>
     *
     * @return this
     */
    DBSelect select();

    /**
     * <strong>Select:</strong><br>
     * Decide which columns the statement returns.<br>
     * -VD: <br>&nbsp;&nbsp; db.select("id, name").from("student")<br>
     * -Produces: <br>&nbsp;&nbsp; SELECT id, name FROM student
     *
     * @param selects
     * @return this
     */
    DBSelect select(String selects);

    /**
     * <strong>Get Compiled Select:</strong><br>
     * Get SELECT query string.<br>
     * Compiles a SELECT query string and returns the query.
     *
     * @param reset If enabled, the data will be cleared
     * @return query
     */
    String getCompiledSelect(boolean reset);

    /**
     * <strong>Insert:</strong><br>
     * The method start the insert query.<br>
     * If you call back many times, the last <strong>tableName</strong> record
     * will be recorded.
     *
     * @param tableName
     * @return this
     */
    DBInsert insert(String tableName);

    /**
     * <strong>Insert:</strong><br>
     * The method start the insert query.<br>
     * If you call back many times, the last <strong>tableName</strong> record
     * will be recorded.
     *
     * @param tableName
     * @param dataSet
     * @return this
     */
    DBInsert insert(String tableName, HashMap<String, String> dataSet);

    /**
     * <strong>Get Compiled Insert:</strong><br>
     * Get INSERT query string<br>
     * Compiles an insert query and returns the query
     *
     * @param reset If enabled, the data will be cleared
     * @return query
     */
    String getCompiledInsert(boolean reset);

    /**
     * <strong>Update:</strong><br>
     * The method start the update query.<br>
     * If you call back many times, the last <strong>tableName</strong> record
     * will be recorded.
     *
     * @param tableName
     * @return this
     */
    DBUpdate update(String tableName);

    /**
     * <strong>Update:</strong><br>
     * The method start the update query.<br>
     * If you call back many times, the last <strong>tableName</strong> record
     * will be recorded.
     *
     * @param tableName
     * @param dataSet
     * @return this
     */
    DBUpdate update(String tableName, HashMap<String, String> dataSet);

    /**
     * <strong>Get Compiled Update:</strong><br>
     * Get UPDATE query string<br>
     * Compiles an update query and returns the query
     *
     * @param reset If enabled, the data will be cleared
     * @return query
     */
    String getCompiledUpdate(boolean reset);

    /**
     * <strong>Delete:</strong><br>
     * The method start the delete query.<br>
     * If you call back many times, the last <strong>tableName</strong> record
     * will be recorded.
     *
     * @param tableName
     * @return this
     */
    DBDelete delete(String tableName);

    /**
     * <strong>Get Compiled Delete:</strong><br>
     * Get DELETE query string<br>
     * Compiles an delete query and returns the query
     *
     * @param reset If enabled, the data will be cleared
     * @return query
     */
    String getCompiledDelete(boolean reset);

    /**
     * <strong>Empty table:</strong><br>
     *
     * @param tableName
     * @return
     */
    String emptyTable(String tableName);

    /**
     * <strong>Truncate table:</strong><br>
     *
     * @param tableName
     * @return
     */
    String truncate(String tableName);

    /**
     * <strong>Clear:</strong><br>
     * Resets the query builder.<br>
     */
    void clear();

    public interface DBInsert {

        /**
         * <strong>Set:</strong><br>
         * Allows key/value pairs to be set for inserting or updating.
         *
         * @param dataSet
         * @return this
         */
        DBInsert set(HashMap<String, String> dataSet);

        /**
         * <strong>Set:</strong><br>
         * Allows key/value pairs to be set for inserting or updating.
         *
         * @param key
         * @param value
         * @return this
         */
        DBInsert set(String key, String value);

        /**
         * <strong>Get Compiled Insert:</strong><br>
         * Get INSERT query string<br>
         * Compiles an insert query and returns the query
         *
         * @param reset If enabled, the data will be cleared
         * @return query
         */
        String getCompiledInsert(boolean reset);

    }

    public interface DBSelect {

        /**
         * <strong>Distinct</strong><br>
         * Distinct make sentence <strong>SELECT</strong> has DISTINCT.<br>
         * -VD: <br>&nbsp;&nbsp; db.select().distinct().from("student")<br>
         * -Produces: <br>&nbsp;&nbsp; SELECT DISTINCT * FROM student
         *
         * @return this
         */
        DBSelect distinct();

        /**
         * <strong>From:</strong><br>
         * The table you want query.<br>
         * -VD: <br>&nbsp;&nbsp; db.select().from("student").from("mark")<br>
         * -Produces: <br>&nbsp;&nbsp; SELECT * FROM student, mark
         *
         * @param from
         * @return this
         */
        DBSelect from(String from);

        /**
         * <strong>Join:</strong><br>
         *
         * Generates the JOIN portion of the query.<br>
         * -VD: <br>&nbsp;&nbsp; db.join("comments", "comments.id =
         * student.id").<br>
         * -Produces: <br>&nbsp;&nbsp; JOIN comments ON comments.id =
         * student.id.
         *
         * @param table Table name
         * @param cond Condition
         * @return this
         *
         */
        DBSelect join(String table, String cond);

        /**
         * <strong>Join:</strong><br>
         * Generates the JOIN portion of the query.<br>
         * -VD: <br>&nbsp;&nbsp; join("comments", "comments.id = student.id",
         * "left").<br>
         * -Produces: <br>&nbsp;&nbsp; LEFT JOIN comments ON comments.id =
         * student.id.
         *
         * @param table Table name
         * @param cond Condition
         * @param type type in('LEFT', 'RIGHT', 'OUTER', 'INNER', 'LEFT OUTER',
         * 'RIGHT OUTER')
         * @return this
         *
         */
        DBSelect join(String table, String cond, String type);

        /**
         * <strong>Group Start:</strong><br>
         * -VD: <br>
         * <pre>
         * groupStart()
         *     .where('name', 'Tom')
         *     .orGroupStart()
         *         .where('age', '10')
         *         .where('gender', 'male')
         *     .group_end()
         * .group_end()
         * .like("email", "admin", "both")
         * </pre> -Produces: <br>
         * ( name = 'Tom' OR ( age = '10' AND gender = male ) ) AND email
         * like('%admin%')
         *
         * @return this
         */
        DBSelect groupStart();

        /**
         * <strong>Or Group Start:</strong><br>
         * -VD: <br>
         * <pre>
         * groupStart()
         *     .where('name', 'Tom')
         *     .orGroupStart()
         *         .where('age', '10')
         *         .where('gender', 'male')
         *     .group_end()
         * .group_end()
         * .like("email", "admin", "both")
         * </pre> -Produces: <br>
         * ( name = 'Tom' OR ( age = '10' AND gender = male ) ) AND email
         * like('%admin%')
         *
         * @return this
         */
        DBSelect orGroupStart();

        /**
         * <strong>Not Group Start:</strong><br>
         * -VD: <br>
         * <pre>
         * groupStart()
         *     .where('name', 'Tom')
         *     .orGroupStart()
         *         .where('age', '10')
         *         .where('gender', 'male')
         *     .group_end()
         * .group_end()
         * .like("email", "admin", "both")
         * </pre> -Produces: <br>
         * ( name = 'Tom' OR ( age = '10' AND gender = male ) ) AND email
         * like('%admin%')
         *
         * @return this
         */
        DBSelect notGroupStart();

        /**
         * <strong>Or Not Group Start:</strong><br>
         * -VD: <br>
         * <pre>
         * groupStart()
         *     .where('name', 'Tom')
         *     .orGroupStart()
         *         .where('age', '10')
         *         .where('gender', 'male')
         *     .group_end()
         * .group_end()
         * .like("email", "admin", "both")
         * </pre> -Produces: <br>
         * ( name = 'Tom' OR ( age = '10' AND gender = male ) ) AND email
         * like('%admin%')
         *
         * @return this
         */
        DBSelect orNotGroupStart();

        /**
         * <strong>Group End:</strong><br>
         * End of group.
         *
         * @return
         */
        DBSelect GroupEnd();

        /**
         * <strong>Where:</strong><br>
         * Generates the WHERE portion of the query. Separates multiple calls
         * with AND.<br>
         *
         * -VD: <br>
         * &nbsp;&nbsp;where("id", "1");<br>
         * &nbsp;&nbsp;where("age !=", "15");<br>
         * &nbsp;&nbsp;where("age is null", "");<br>
         * -Produces: <br>
         * &nbsp;&nbsp;WHERE id = '1';<br>
         * &nbsp;&nbsp;AND age != '15';<br>
         * &nbsp;&nbsp;AND age is null;
         *
         * @param key The field to search
         * @param value The value searched on
         * @return this
         *
         */
        DBSelect where(String key, String value);

        /**
         * <strong>Where:</strong><br>
         * SQL query joined with AND if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String whereStr = "name='Joe' AND status='boss'";<br>
         * &nbsp;&nbsp;where(whereStr);<br>
         * -Produces: <br>
         * &nbsp;&nbsp;WHERE name='Joe' AND status='boss';
         *
         * @param where
         * @return this
         */
        DBSelect where(String where);

        /**
         * <strong>Or Where:</strong><br>
         * Generates the WHERE portion of the query. Separates multiple calls
         * with OR.<br>
         *
         * -VD: <br>
         * &nbsp;&nbsp;orWhere("id", "1");<br>
         * &nbsp;&nbsp;orWhere("age !=", "15");<br>
         * &nbsp;&nbsp;orWhere("age is null", "");<br>
         * -Produces: <br>
         * &nbsp;&nbsp;WHERE id = '1';<br>
         * &nbsp;&nbsp;OR age != '15';<br>
         * &nbsp;&nbsp;OR age is null;
         *
         * @param key The field to search
         * @param value The value searched on
         * @return this
         *
         */
        DBSelect orWhere(String key, String value);

        /**
         * <strong>Or Where:</strong><br>
         * SQL query joined with OR if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String whereStr = "name='Joe' AND status='boss'";<br>
         * &nbsp;&nbsp;where(whereStr);<br>
         * -Produces: <br>
         * &nbsp;&nbsp;WHERE name='Joe' AND status='boss';
         *
         * @param where
         * @return this
         *
         */
        DBSelect orWhere(String where);

        /**
         * <strong>Where In:</strong><br>
         * Generates a WHERE field IN ('item', 'item') SQL query joined with AND
         * if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd",
         * "James"};<br>
         * &nbsp;&nbsp;where_in('username', names);<br>
         * -Produces: <br>&nbsp;&nbsp; WHERE username IN ('Frank', 'Todd',
         * 'James')
         *
         * @param key The field to search
         * @param value The values searched on
         * @return this
         *
         */
        DBSelect whereIn(String key, String[] value);

        /**
         * <strong>Or Where In:</strong><br>
         * Generates a WHERE field IN ('item', 'item') SQL query joined with OR
         * if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd",
         * "James"};<br>
         * &nbsp;&nbsp;where_in('username', names);<br>
         * -Produces: <br>&nbsp;&nbsp; OR username IN ('Frank', 'Todd', 'James')
         *
         * @param key The field to search
         * @param value The values searched on
         * @return this
         *
         */
        DBSelect orWhereIn(String key, String[] value);

        /**
         * <strong>Where Not In:</strong><br>
         * Generates a WHERE field IN ('item', 'item') SQL query joined with AND
         * if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd",
         * "James"};<br>
         * &nbsp;&nbsp;where_in('username', names);<br>
         * -Produces: <br>&nbsp;&nbsp; WHERE username NOT IN ('Frank', 'Todd',
         * 'James')
         *
         * @param key The field to search
         * @param value The values searched on
         * @return this
         *
         */
        DBSelect whereNotIn(String key, String[] value);

        /**
         * <strong>Or Where Not In:</strong><br>
         * Generates a WHERE field IN ('item', 'item') SQL query joined with OR
         * if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd",
         * "James"};<br>
         * &nbsp;&nbsp;where_in('username', names);<br>
         * -Produces: <br>&nbsp;&nbsp; OR username NOT IN ('Frank', 'Todd',
         * 'James')
         *
         * @param key The field to search
         * @param value The values searched on
         * @return this
         *
         */
        DBSelect orWhereNotIn(String key, String[] value);

        /**
         * <strong>Like:</strong><br>
         * Generates a LIKE portion of the query. Separates multiple calls with
         * AND
         *
         * @param field The column to search
         * @param match The search value
         * @param side none | before | after | both
         * @return this
         *
         */
        DBSelect like(String field, String match, String side);

        /**
         * <strong>Or Like:</strong><br>
         * Generates a LIKE portion of the query. Separates multiple calls with
         * OR
         *
         * @param field The column to search
         * @param match The search value
         * @param side none | before | after | both
         * @return this
         *
         */
        DBSelect orLike(String field, String match, String side);

        /**
         * <strong>Not Like:</strong><br>
         * Generates a NOT LIKE portion of the query. Separates multiple calls
         * with AND
         *
         * @param field The column to search
         * @param match The search value
         * @param side none | before | after | both
         * @return this
         *
         */
        DBSelect notLike(String field, String match, String side);

        /**
         * <strong>Or Not Like:</strong><br>
         * Generates a NOT LIKE portion of the query. Separates multiple calls
         * with OR
         *
         * @param field The column to search
         * @param match The search value
         * @param side none | before | after | both
         * @return this
         *
         */
        DBSelect orNotLike(String field, String match, String side);

        /**
         * <strong>Group By:</strong><br>
         *
         * @param groupBy
         * @return this
         */
        DBSelect groupBy(String groupBy);

        /**
         * <strong>Group By:</strong><br>
         *
         * @param groupBy
         * @return this
         */
        DBSelect groupBy(String[] groupBy);

        /**
         * <strong>Having:</strong><br>
         * Sets the HAVING value. Separates multiple calls with AND
         *
         * @param key
         * @param value
         * @return this
         */
        DBSelect having(String key, String value);

        /**
         * <strong>Having:</strong><br>
         * SQL query joined with AND if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String havingStr = "name='Joe' AND status='boss'";<br>
         * &nbsp;&nbsp;having(havingStr);<br>
         * -Produces: <br>
         * &nbsp;&nbsp;HAVING name='Joe' AND status='boss';
         *
         * @param having
         * @return this
         *
         */
        DBSelect having(String having);

        /**
         * <strong>Or Having:</strong><br>
         * Sets the HAVING value. Separates multiple calls with OR
         *
         * @param key
         * @param value
         * @return this
         */
        DBSelect orHaving(String key, String value);

        /**
         * <strong>Having:</strong><br>
         * SQL query joined with OR if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String havingStr = "name='Joe' AND status='boss'";<br>
         * &nbsp;&nbsp;having(havingStr);<br>
         * -Produces: <br>
         * &nbsp;&nbsp;OR name='Joe' AND status='boss';
         *
         * @param having
         * @return this
         *
         */
        DBSelect orHaving(String having);

        /**
         * <strong>Order By ASC:</strong><br>
         * Sets the ORDER BY value
         *
         * @param orderBy
         * @return this
         */
        DBSelect orderByASC(String orderBy);

        /**
         * <strong>Order By ASC:</strong><br>
         * Sets the ORDER BY value
         *
         * @param orderBy
         * @return this
         */
        DBSelect orderByASC(String[] orderBy);

        /**
         * <strong>Order By DESC:</strong><br>
         * Sets the ORDER BY value
         *
         * @param orderBy
         * @return this
         */
        DBSelect orderByDESC(String orderBy);

        /**
         * <strong>Order By DESC:</strong><br>
         * Sets the ORDER BY value
         *
         * @param orderBy
         * @return this
         */
        DBSelect orderByDESC(String[] orderBy);

        /**
         * <strong>Limit:</strong><br>
         * Sets the LIMIT and offset values
         *
         * @param limit
         * @param offset
         * @return
         */
        DBSelect limit(int limit, int offset);

        /**
         * <strong>Limit:</strong><br>
         * Sets the LIMIT value and offset = 0
         *
         * @param limit
         * @return this
         */
        DBSelect limit(int limit);

        /**
         * <strong>Get Compiled Select:</strong><br>
         * Get SELECT query string.<br>
         * Compiles a SELECT query string and returns the query.
         *
         * @param reset If enabled, the data will be cleared
         * @return query
         */
        String getCompiledSelect(boolean reset);

    }

    public interface DBUpdate {

        /**
         * <strong>Set:</strong><br>
         * Allows key/value pairs to be set for inserting or updating.
         *
         * @param dataSet
         * @return this
         */
        DBUpdate set(HashMap<String, String> dataSet);

        /**
         * <strong>Set:</strong><br>
         * Allows key/value pairs to be set for inserting or updating.
         *
         * @param key
         * @param value
         * @return this
         */
        DBUpdate set(String key, String value);

        /**
         * <strong>Group Start:</strong><br>
         * -VD: <br>
         * <pre>
         * groupStart()
         *     .where('name', 'Tom')
         *     .orGroupStart()
         *         .where('age', '10')
         *         .where('gender', 'male')
         *     .group_end()
         * .group_end()
         * .like("email", "admin", "both")
         * </pre> -Produces: <br>
         * ( name = 'Tom' OR ( age = '10' AND gender = male ) ) AND email
         * like('%admin%')
         *
         * @return this
         */
        DBUpdate groupStart();

        /**
         * <strong>Or Group Start:</strong><br>
         * -VD: <br>
         * <pre>
         * groupStart()
         *     .where('name', 'Tom')
         *     .orGroupStart()
         *         .where('age', '10')
         *         .where('gender', 'male')
         *     .group_end()
         * .group_end()
         * .like("email", "admin", "both")
         * </pre> -Produces: <br>
         * ( name = 'Tom' OR ( age = '10' AND gender = male ) ) AND email
         * like('%admin%')
         *
         * @return this
         */
        DBUpdate orGroupStart();

        /**
         * <strong>Not Group Start:</strong><br>
         * -VD: <br>
         * <pre>
         * groupStart()
         *     .where('name', 'Tom')
         *     .orGroupStart()
         *         .where('age', '10')
         *         .where('gender', 'male')
         *     .group_end()
         * .group_end()
         * .like("email", "admin", "both")
         * </pre> -Produces: <br>
         * ( name = 'Tom' OR ( age = '10' AND gender = male ) ) AND email
         * like('%admin%')
         *
         * @return this
         */
        DBUpdate notGroupStart();

        /**
         * <strong>Or Not Group Start:</strong><br>
         * -VD: <br>
         * <pre>
         * groupStart()
         *     .where('name', 'Tom')
         *     .orGroupStart()
         *         .where('age', '10')
         *         .where('gender', 'male')
         *     .group_end()
         * .group_end()
         * .like("email", "admin", "both")
         * </pre> -Produces: <br>
         * ( name = 'Tom' OR ( age = '10' AND gender = male ) ) AND email
         * like('%admin%')
         *
         * @return this
         */
        DBUpdate orNotGroupStart();

        /**
         * <strong>Group End:</strong><br>
         * End of group.
         *
         * @return
         */
        DBUpdate GroupEnd();

        /**
         * <strong>Where:</strong><br>
         * Generates the WHERE portion of the query. Separates multiple calls
         * with AND.<br>
         *
         * -VD: <br>
         * &nbsp;&nbsp;where("id", "1");<br>
         * &nbsp;&nbsp;where("age !=", "15");<br>
         * &nbsp;&nbsp;where("age is null", "");<br>
         * -Produces: <br>
         * &nbsp;&nbsp;WHERE id = '1';<br>
         * &nbsp;&nbsp;AND age != '15';<br>
         * &nbsp;&nbsp;AND age is null;
         *
         * @param key The field to search
         * @param value The value searched on
         * @return this
         *
         */
        DBUpdate where(String key, String value);

        /**
         * <strong>Where:</strong><br>
         * SQL query joined with AND if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String whereStr = "name='Joe' AND status='boss'";<br>
         * &nbsp;&nbsp;where(whereStr);<br>
         * -Produces: <br>
         * &nbsp;&nbsp;WHERE name='Joe' AND status='boss';
         *
         * @param where
         * @return this
         */
        DBUpdate where(String where);

        /**
         * <strong>Or Where:</strong><br>
         * Generates the WHERE portion of the query. Separates multiple calls
         * with OR.<br>
         *
         * -VD: <br>
         * &nbsp;&nbsp;orWhere("id", "1");<br>
         * &nbsp;&nbsp;orWhere("age !=", "15");<br>
         * &nbsp;&nbsp;orWhere("age is null", "");<br>
         * -Produces: <br>
         * &nbsp;&nbsp;WHERE id = '1';<br>
         * &nbsp;&nbsp;OR age != '15';<br>
         * &nbsp;&nbsp;OR age is null;
         *
         * @param key The field to search
         * @param value The value searched on
         * @return this
         *
         */
        DBUpdate orWhere(String key, String value);

        /**
         * <strong>Or Where:</strong><br>
         * SQL query joined with OR if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String whereStr = "name='Joe' AND status='boss'";<br>
         * &nbsp;&nbsp;where(whereStr);<br>
         * -Produces: <br>
         * &nbsp;&nbsp;WHERE name='Joe' AND status='boss';
         *
         * @param where
         * @return this
         *
         */
        DBUpdate orWhere(String where);

        /**
         * <strong>Where In:</strong><br>
         * Generates a WHERE field IN ('item', 'item') SQL query joined with AND
         * if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd",
         * "James"};<br>
         * &nbsp;&nbsp;where_in('username', names);<br>
         * -Produces: <br>&nbsp;&nbsp; WHERE username IN ('Frank', 'Todd',
         * 'James')
         *
         * @param key The field to search
         * @param value The values searched on
         * @return this
         *
         */
        DBUpdate whereIn(String key, String[] value);

        /**
         * <strong>Or Where In:</strong><br>
         * Generates a WHERE field IN ('item', 'item') SQL query joined with OR
         * if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd",
         * "James"};<br>
         * &nbsp;&nbsp;where_in('username', names);<br>
         * -Produces: <br>&nbsp;&nbsp; OR username IN ('Frank', 'Todd', 'James')
         *
         * @param key The field to search
         * @param value The values searched on
         * @return this
         *
         */
        DBUpdate orWhereIn(String key, String[] value);

        /**
         * <strong>Where Not In:</strong><br>
         * Generates a WHERE field IN ('item', 'item') SQL query joined with AND
         * if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd",
         * "James"};<br>
         * &nbsp;&nbsp;where_in('username', names);<br>
         * -Produces: <br>&nbsp;&nbsp; WHERE username NOT IN ('Frank', 'Todd',
         * 'James')
         *
         * @param key The field to search
         * @param value The values searched on
         * @return this
         *
         */
        DBUpdate whereNotIn(String key, String[] value);

        /**
         * <strong>Or Where Not In:</strong><br>
         * Generates a WHERE field IN ('item', 'item') SQL query joined with OR
         * if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd",
         * "James"};<br>
         * &nbsp;&nbsp;where_in('username', names);<br>
         * -Produces: <br>&nbsp;&nbsp; OR username NOT IN ('Frank', 'Todd',
         * 'James')
         *
         * @param key The field to search
         * @param value The values searched on
         * @return this
         *
         */
        DBUpdate orWhereNotIn(String key, String[] value);

        /**
         * <strong>Like:</strong><br>
         * Generates a LIKE portion of the query. Separates multiple calls with
         * AND
         *
         * @param field The column to search
         * @param match The search value
         * @param side none | before | after | both
         * @return this
         *
         */
        DBUpdate like(String field, String match, String side);

        /**
         * <strong>Or Like:</strong><br>
         * Generates a LIKE portion of the query. Separates multiple calls with
         * OR
         *
         * @param field The column to search
         * @param match The search value
         * @param side none | before | after | both
         * @return this
         *
         */
        DBUpdate orLike(String field, String match, String side);

        /**
         * <strong>Not Like:</strong><br>
         * Generates a NOT LIKE portion of the query. Separates multiple calls
         * with AND
         *
         * @param field The column to search
         * @param match The search value
         * @param side none | before | after | both
         * @return this
         *
         */
        DBUpdate notLike(String field, String match, String side);

        /**
         * <strong>Or Not Like:</strong><br>
         * Generates a NOT LIKE portion of the query. Separates multiple calls
         * with OR
         *
         * @param field The column to search
         * @param match The search value
         * @param side none | before | after | both
         * @return this
         *
         */
        DBUpdate orNotLike(String field, String match, String side);

        /**
         * <strong>Order By ASC:</strong><br>
         * Sets the ORDER BY value
         *
         * @param orderBy
         * @return this
         */
        DBUpdate orderByASC(String orderBy);

        /**
         * <strong>Order By ASC:</strong><br>
         * Sets the ORDER BY value
         *
         * @param orderBy
         * @return this
         */
        DBUpdate orderByASC(String[] orderBy);

        /**
         * <strong>Order By DESC:</strong><br>
         * Sets the ORDER BY value
         *
         * @param orderBy
         * @return this
         */
        DBUpdate orderByDESC(String orderBy);

        /**
         * <strong>Order By DESC:</strong><br>
         * Sets the ORDER BY value
         *
         * @param orderBy
         * @return this
         */
        DBUpdate orderByDESC(String[] orderBy);

        /**
         * <strong>Limit:</strong><br>
         * Sets the LIMIT value and offset = 0
         *
         * @param limit
         * @return this
         */
        DBUpdate limit(int limit);

        /**
         * <strong>Get Compiled Update:</strong><br>
         * Get UPDATE query string<br>
         * Compiles an update query and returns the query
         *
         * @param reset If enabled, the data will be cleared
         * @return query
         */
        String getCompiledUpdate(boolean reset);

    }

    public interface DBDelete {

        /**
         * <strong>From:</strong><br>
         * The table you want query.<br>
         * -VD: <br>&nbsp;&nbsp; db.select().from("student").from("mark")<br>
         * -Produces: <br>&nbsp;&nbsp; SELECT * FROM student, mark
         *
         * @param from
         * @return this
         */
        DBDelete from(String from);

        /**
         * <strong>Group Start:</strong><br>
         * -VD: <br>
         * <pre>
         * groupStart()
         *     .where('name', 'Tom')
         *     .orGroupStart()
         *         .where('age', '10')
         *         .where('gender', 'male')
         *     .group_end()
         * .group_end()
         * .like("email", "admin", "both")
         * </pre> -Produces: <br>
         * ( name = 'Tom' OR ( age = '10' AND gender = male ) ) AND email
         * like('%admin%')
         *
         * @return this
         */
        DBDelete groupStart();

        /**
         * <strong>Or Group Start:</strong><br>
         * -VD: <br>
         * <pre>
         * groupStart()
         *     .where('name', 'Tom')
         *     .orGroupStart()
         *         .where('age', '10')
         *         .where('gender', 'male')
         *     .group_end()
         * .group_end()
         * .like("email", "admin", "both")
         * </pre> -Produces: <br>
         * ( name = 'Tom' OR ( age = '10' AND gender = male ) ) AND email
         * like('%admin%')
         *
         * @return this
         */
        DBDelete orGroupStart();

        /**
         * <strong>Not Group Start:</strong><br>
         * -VD: <br>
         * <pre>
         * groupStart()
         *     .where('name', 'Tom')
         *     .orGroupStart()
         *         .where('age', '10')
         *         .where('gender', 'male')
         *     .group_end()
         * .group_end()
         * .like("email", "admin", "both")
         * </pre> -Produces: <br>
         * ( name = 'Tom' OR ( age = '10' AND gender = male ) ) AND email
         * like('%admin%')
         *
         * @return this
         */
        DBDelete notGroupStart();

        /**
         * <strong>Or Not Group Start:</strong><br>
         * -VD: <br>
         * <pre>
         * groupStart()
         *     .where('name', 'Tom')
         *     .orGroupStart()
         *         .where('age', '10')
         *         .where('gender', 'male')
         *     .group_end()
         * .group_end()
         * .like("email", "admin", "both")
         * </pre> -Produces: <br>
         * ( name = 'Tom' OR ( age = '10' AND gender = male ) ) AND email
         * like('%admin%')
         *
         * @return this
         */
        DBDelete orNotGroupStart();

        /**
         * <strong>Group End:</strong><br>
         * End of group.
         *
         * @return
         */
        DBDelete GroupEnd();

        /**
         * <strong>Where:</strong><br>
         * Generates the WHERE portion of the query. Separates multiple calls
         * with AND.<br>
         *
         * -VD: <br>
         * &nbsp;&nbsp;where("id", "1");<br>
         * &nbsp;&nbsp;where("age !=", "15");<br>
         * &nbsp;&nbsp;where("age is null", "");<br>
         * -Produces: <br>
         * &nbsp;&nbsp;WHERE id = '1';<br>
         * &nbsp;&nbsp;AND age != '15';<br>
         * &nbsp;&nbsp;AND age is null;
         *
         * @param key The field to search
         * @param value The value searched on
         * @return this
         *
         */
        DBDelete where(String key, String value);

        /**
         * <strong>Where:</strong><br>
         * SQL query joined with AND if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String whereStr = "name='Joe' AND status='boss'";<br>
         * &nbsp;&nbsp;where(whereStr);<br>
         * -Produces: <br>
         * &nbsp;&nbsp;WHERE name='Joe' AND status='boss';
         *
         * @param where
         * @return this
         */
        DBDelete where(String where);

        /**
         * <strong>Or Where:</strong><br>
         * Generates the WHERE portion of the query. Separates multiple calls
         * with OR.<br>
         *
         * -VD: <br>
         * &nbsp;&nbsp;orWhere("id", "1");<br>
         * &nbsp;&nbsp;orWhere("age !=", "15");<br>
         * &nbsp;&nbsp;orWhere("age is null", "");<br>
         * -Produces: <br>
         * &nbsp;&nbsp;WHERE id = '1';<br>
         * &nbsp;&nbsp;OR age != '15';<br>
         * &nbsp;&nbsp;OR age is null;
         *
         * @param key The field to search
         * @param value The value searched on
         * @return this
         *
         */
        DBDelete orWhere(String key, String value);

        /**
         * <strong>Or Where:</strong><br>
         * SQL query joined with OR if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String whereStr = "name='Joe' AND status='boss'";<br>
         * &nbsp;&nbsp;where(whereStr);<br>
         * -Produces: <br>
         * &nbsp;&nbsp;WHERE name='Joe' AND status='boss';
         *
         * @param where
         * @return this
         *
         */
        DBDelete orWhere(String where);

        /**
         * <strong>Where In:</strong><br>
         * Generates a WHERE field IN ('item', 'item') SQL query joined with AND
         * if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd",
         * "James"};<br>
         * &nbsp;&nbsp;where_in('username', names);<br>
         * -Produces: <br>&nbsp;&nbsp; WHERE username IN ('Frank', 'Todd',
         * 'James')
         *
         * @param key The field to search
         * @param value The values searched on
         * @return this
         *
         */
        DBDelete whereIn(String key, String[] value);

        /**
         * <strong>Or Where In:</strong><br>
         * Generates a WHERE field IN ('item', 'item') SQL query joined with OR
         * if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd",
         * "James"};<br>
         * &nbsp;&nbsp;where_in('username', names);<br>
         * -Produces: <br>&nbsp;&nbsp; OR username IN ('Frank', 'Todd', 'James')
         *
         * @param key The field to search
         * @param value The values searched on
         * @return this
         *
         */
        DBDelete orWhereIn(String key, String[] value);

        /**
         * <strong>Where Not In:</strong><br>
         * Generates a WHERE field IN ('item', 'item') SQL query joined with AND
         * if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd",
         * "James"};<br>
         * &nbsp;&nbsp;where_in('username', names);<br>
         * -Produces: <br>&nbsp;&nbsp; WHERE username NOT IN ('Frank', 'Todd',
         * 'James')
         *
         * @param key The field to search
         * @param value The values searched on
         * @return this
         *
         */
        DBDelete whereNotIn(String key, String[] value);

        /**
         * <strong>Or Where Not In:</strong><br>
         * Generates a WHERE field IN ('item', 'item') SQL query joined with OR
         * if appropriate<br>
         * -VD: <br>
         * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd",
         * "James"};<br>
         * &nbsp;&nbsp;where_in('username', names);<br>
         * -Produces: <br>&nbsp;&nbsp; OR username NOT IN ('Frank', 'Todd',
         * 'James')
         *
         * @param key The field to search
         * @param value The values searched on
         * @return this
         *
         */
        DBDelete orWhereNotIn(String key, String[] value);

        /**
         * <strong>Like:</strong><br>
         * Generates a LIKE portion of the query. Separates multiple calls with
         * AND
         *
         * @param field The column to search
         * @param match The search value
         * @param side none | before | after | both
         * @return this
         *
         */
        DBDelete like(String field, String match, String side);

        /**
         * <strong>Or Like:</strong><br>
         * Generates a LIKE portion of the query. Separates multiple calls with
         * OR
         *
         * @param field The column to search
         * @param match The search value
         * @param side none | before | after | both
         * @return this
         *
         */
        DBDelete orLike(String field, String match, String side);

        /**
         * <strong>Not Like:</strong><br>
         * Generates a NOT LIKE portion of the query. Separates multiple calls
         * with AND
         *
         * @param field The column to search
         * @param match The search value
         * @param side none | before | after | both
         * @return this
         *
         */
        DBDelete notLike(String field, String match, String side);

        /**
         * <strong>Or Not Like:</strong><br>
         * Generates a NOT LIKE portion of the query. Separates multiple calls
         * with OR
         *
         * @param field The column to search
         * @param match The search value
         * @param side none | before | after | both
         * @return this
         *
         */
        DBDelete orNotLike(String field, String match, String side);

        /**
         * <strong>Limit:</strong><br>
         * Sets the LIMIT value and offset = 0
         *
         * @param limit
         * @return this
         */
        DBDelete limit(int limit);

        /**
         * <strong>Get Compiled Delete:</strong><br>
         * Get DELETE query string<br>
         * Compiles an delete query and returns the query
         *
         * @param reset If enabled, the data will be cleared
         * @return query
         */
        String getCompiledDelete(boolean reset);

    }

}
