/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.database;

import com.foodyshop.database.DBQuery.DBSelect;
import com.foodyshop.database.DBQuery.DBInsert;
import com.foodyshop.database.DBQuery.DBUpdate;
import com.foodyshop.database.DBQuery.DBDelete;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author WIN1064
 */
public class DBQueryBuilder implements DBQuery, DBSelect, DBInsert, DBUpdate, DBDelete {

    private ArrayList<String> qbSelect = new ArrayList<>();
    private boolean qbDistinct = false;
    private ArrayList<String> qbFrom = new ArrayList<>();
    private ArrayList<String> qbJoin = new ArrayList<>();
    private ArrayList<String> qbWhere = new ArrayList<>();
    private ArrayList<String> qbGroupBy = new ArrayList<>();
    private ArrayList<String> qbHaving = new ArrayList<>();
    private ArrayList<String> qbOrderBy = new ArrayList<>();
    private int qbLimit;
    private int qbOffset;

    private HashMap<String, String> qbSet = new HashMap<>();

    private boolean qbWhereGroupStart = false;

    private static DBQueryBuilder instance = null;

    private DBQueryBuilder() {
    }

    /**
     * <strong>getDBQuery:</strong><br>
     * Get instance if available
     *
     * @return instance
     */
    public static DBQuery getDBQuery() {
        if (instance == null) {
            instance = new DBQueryBuilder();
        }
        return instance;
    }

    /**
     * <strong>newDBQuery:</strong><br>
     * Get new OBJ
     *
     * @return new DBQueryBuilder();
     */
    public static DBQuery newDBQuery() {
        return new DBQueryBuilder();
    }

    /**
     * <strong>Select:</strong><br>
     *
     * @return this
     */
    @Override
    public DBQueryBuilder select() {
        return select("*");
    }

    /**
     * <strong>Select:</strong><br>
     * Decide which columns the statement returns.<br>
     * -VD: <br>&nbsp;&nbsp; db.select("id, name").from("student")<br>
     * -Produces: <br>&nbsp;&nbsp; SELECT id, name FROM student
     *
     * @param selects
     * @return this
     */
    @Override
    public DBQueryBuilder select(String selects) {
        for (String val : selects.split(",")) {
            val = val.trim();
            if (!val.equals("")) {
                this.qbSelect.add(val);
            }
        }
        return this;
    }

    /**
     * <strong>Distinct</strong><br>
     * Distinct make sentence <strong>SELECT</strong> has DISTINCT.<br>
     * -VD: <br>&nbsp;&nbsp; db.select().distinct().from("student")<br>
     * -Produces: <br>&nbsp;&nbsp; SELECT DISTINCT * FROM student
     *
     * @return this
     */
    @Override
    public DBQueryBuilder distinct() {
        this.qbDistinct = true;
        return this;
    }

    /**
     * <strong>From:</strong><br>
     * The table you want query.<br>
     * -VD: <br>&nbsp;&nbsp; db.select().from("student").from("mark")<br>
     * -Produces: <br>&nbsp;&nbsp; SELECT * FROM student, mark
     *
     * @param from
     * @return this
     */
    @Override
    public DBQueryBuilder from(String from) {
        for (String val : from.split(",")) {
            this.qbFrom.add(val.trim());
        }
        return this;
    }

    /**
     * <strong>Join:</strong><br>
     *
     * Generates the JOIN portion of the query.<br>
     * -VD: <br>&nbsp;&nbsp; db.join("comments", "comments.id =
     * student.id").<br>
     * -Produces: <br>&nbsp;&nbsp; JOIN comments ON comments.id = student.id.
     *
     * @param table Table name
     * @param cond Condition
     * @return this
     *
     */
    @Override
    public DBQueryBuilder join(String table, String cond) {
        return join(table, cond, "");
    }

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
    @Override
    public DBQueryBuilder join(String table, String cond, String type) {

        ArrayList<String> listType = new ArrayList<>(6);
        listType.add("LEFT");
        listType.add("RIGHT");
        listType.add("OUTER");
        listType.add("INNER");
        listType.add("LEFT OUTER");
        listType.add("RIGHT OUTER");

        type = type.trim().toUpperCase();
        if (listType.contains(type)) {
            type += " ";
        } else {
            type = "";
        }

        String join = type + "JOIN " + table + " ON " + cond;
        this.qbJoin.add(join);

        return this;
    }

    /**
     * <strong>_wh:</strong><br>
     *
     * Called by where() || orWhere() || having() || orHaving()
     *
     * @param qbKey 'qbWhere' or 'qbHaving'
     * @param key
     * @param value
     * @param type
     * @return this
     * @throws DBError
     */
    private DBQueryBuilder _wh(String qbKey, String key, String value, String type) {
        if (key == null || key.isEmpty()) {
            throw new DBError("The key is null");
        }
        if (value == null) {
            throw new DBError("The value is null");
        }
        ArrayList<String> arrTmp = qbKey.equals("qbWhere") ? qbWhere : qbHaving;
        String prefix = arrTmp.isEmpty() ? _groupGetType("") : _groupGetType(type);
        key = key.trim();
        value = value.trim();
        if (value.equals("") && !_hasOperator(key)) {
            key += " IS NULL";
        } else {
            if (!_hasOperator(key)) {
                key += " = ";
                value = escape(value);
            } else {
                if (key.toLowerCase().contains("is null") || key.toLowerCase().contains("is not null")) {
                    if (!value.isEmpty()) {
                        throw new DBError("Key has Operator 'is null' | 'is not null' so the value must be equal to \"\"");
                    }
                } else {
                    key += " ";
                    value = escape(value);
                }
            }
        }
        arrTmp.add(prefix + key + value);

        return this;
    }

    /**
     * <strong>_wh:</strong><br>
     *
     * Called by where() || orWhere() || having() || orHaving()
     *
     * @param qbKey 'qbWhere' or 'qbHaving'
     * @param whereOrHaving
     * @param type
     * @return
     */
    private DBQueryBuilder _wh(String qbKey, String whereOrHaving, String type) {
        if (whereOrHaving == null || whereOrHaving.trim().isEmpty()) {
            throw new DBError("Having is empty");
        }
        ArrayList<String> arrTmp = qbKey.equals("qbWhere") ? qbWhere : qbHaving;

        String prefix = arrTmp.isEmpty() ? _groupGetType("") : _groupGetType(type);

        arrTmp.add(prefix + whereOrHaving.trim());

        return this;
    }

    /**
     * <strong>Where:</strong><br>
     * Generates the WHERE portion of the query. Separates multiple calls with
     * AND.<br>
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
     * @throws DBError
     *
     */
    @Override
    public DBQueryBuilder where(String key, String value) {
        return _wh("qbWhere", key, value, "AND ");
    }

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
     * @throws DBError
     */
    @Override
    public DBQueryBuilder where(String where) {
        return _wh("qbWhere", where, "AND ");
    }

    /**
     * <strong>Or Where:</strong><br>
     * Generates the WHERE portion of the query. Separates multiple calls with
     * OR.<br>
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
     * @throws DBError
     */
    @Override
    public DBQueryBuilder orWhere(String key, String value) {
        return _wh("qbWhere", key, value, "OR ");
    }

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
     * @throws DBError
     */
    @Override
    public DBQueryBuilder orWhere(String where) {
        return _wh("qbWhere", where, "OR ");
    }

    /**
     * <strong>_whereIn:</strong><br>
     * Called by whereIn, orWhereIn, whereNotIn, orWhereNotIn
     *
     * @param key The field to search
     * @param values The values searched on
     * @param not If the statement would be IN or NOT IN
     * @param type AND || OR
     * @return this
     * @throws DBError
     */
    private DBQueryBuilder _whereIn(String key, String[] values, boolean not, String type) {
        if (key == null || key.isEmpty()) {
            throw new DBError("Key is empty");
        }
        if (values == null || values.length == 0) {
            throw new DBError("Value is empty");
        }

        String prefix = this.qbWhere.isEmpty() ? _groupGetType("") : _groupGetType(type);
        String nots = not ? " NOT" : "";
        ArrayList<String> arrTmp = new ArrayList<>();
        for (String value : values) {
            value = value.trim();
            if (!value.equals("")) {
                arrTmp.add(escape(value));
            }
        }

        String whereIn = prefix + key.trim() + nots + " IN (" + implode(arrTmp.toArray(new String[arrTmp.size()]), ", ") + ")";
        this.qbWhere.add(whereIn);

        return this;
    }

    /**
     * <strong>Where In:</strong><br>
     * Generates a WHERE field IN ('item', 'item') SQL query joined with AND if
     * appropriate<br>
     * -VD: <br>
     * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd", "James"};<br>
     * &nbsp;&nbsp;where_in('username', names);<br>
     * -Produces: <br>&nbsp;&nbsp; WHERE username IN ('Frank', 'Todd', 'James')
     *
     * @param key The field to search
     * @param value The values searched on
     * @return this
     * @throws DBError
     */
    @Override
    public DBQueryBuilder whereIn(String key, String[] value) {
        return _whereIn(key, value, false, "AND ");
    }

    /**
     * <strong>Or Where In:</strong><br>
     * Generates a WHERE field IN ('item', 'item') SQL query joined with OR if
     * appropriate<br>
     * -VD: <br>
     * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd", "James"};<br>
     * &nbsp;&nbsp;where_in('username', names);<br>
     * -Produces: <br>&nbsp;&nbsp; OR username IN ('Frank', 'Todd', 'James')
     *
     * @param key The field to search
     * @param value The values searched on
     * @return this
     * @throws DBError
     */
    @Override
    public DBQueryBuilder orWhereIn(String key, String[] value) {
        return _whereIn(key, value, false, "OR ");
    }

    /**
     * <strong>Where Not In:</strong><br>
     * Generates a WHERE field IN ('item', 'item') SQL query joined with AND if
     * appropriate<br>
     * -VD: <br>
     * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd", "James"};<br>
     * &nbsp;&nbsp;where_in('username', names);<br>
     * -Produces: <br>&nbsp;&nbsp; WHERE username NOT IN ('Frank', 'Todd',
     * 'James')
     *
     * @param key The field to search
     * @param value The values searched on
     * @return this
     * @throws DBError
     */
    @Override
    public DBQueryBuilder whereNotIn(String key, String[] value) {
        return _whereIn(key, value, true, "AND ");
    }

    /**
     * <strong>Or Where Not In:</strong><br>
     * Generates a WHERE field IN ('item', 'item') SQL query joined with OR if
     * appropriate<br>
     * -VD: <br>
     * &nbsp;&nbsp;String[] names = new String[]{"Frank", "Todd", "James"};<br>
     * &nbsp;&nbsp;where_in('username', names);<br>
     * -Produces: <br>&nbsp;&nbsp; OR username NOT IN ('Frank', 'Todd', 'James')
     *
     * @param key The field to search
     * @param value The values searched on
     * @return this
     * @throws DBError
     */
    @Override
    public DBQueryBuilder orWhereNotIn(String key, String[] value) {
        return _whereIn(key, value, true, "OR ");
    }

    /**
     * <strong>_like:</strong><br>
     *
     * Called by like() | orLike() | notLike() | orNotLike()
     *
     * @param field The column to search
     * @param match The search value
     * @param type AND | OR
     * @param side none | before | after | both
     * @param not NOT | ''
     * @return this
     */
    private DBQueryBuilder _like(String field, String match, String type, String side, String not) {

        List<String> listSide = Arrays.asList("none", "before", "after", "both");
        side = side.trim().toLowerCase();
        if (!listSide.contains(side)) {
            throw new DBError("Invalid side: " + side + "\nSide in: " + listSide.toString());
        }
        String likeStament = "";
        field = field.trim();
        match = escapeLikeStr(match.trim());
        String prefix = qbWhere.isEmpty() ? _groupGetType("") : _groupGetType(type);
        switch (side) {
            case "none":
                match = "'" + match + "'";
                break;
            case "before":
                match = "'%" + match + "'";
                break;
            case "after":
                match = "'" + match + "%'";
                break;
            case "both":
            default:
                match = "'%" + match + "%'";
                break;
        }
        likeStament = prefix + field + not + " LIKE " + match;
        qbWhere.add(likeStament);

        return this;
    }

    /**
     * <strong>Like:</strong><br>
     * Generates a LIKE portion of the query. Separates multiple calls with AND
     *
     * @param field The column to search
     * @param match The search value
     * @param side none | before | after | both
     * @return this
     * @throws DBError
     */
    @Override
    public DBQueryBuilder like(String field, String match, String side) {
        return _like(field, match, "AND ", side, "");
    }

    /**
     * <strong>Or Like:</strong><br>
     * Generates a LIKE portion of the query. Separates multiple calls with OR
     *
     * @param field The column to search
     * @param match The search value
     * @param side none | before | after | both
     * @return this
     * @throws DBError
     */
    @Override
    public DBQueryBuilder orLike(String field, String match, String side) {
        return _like(field, match, "OR ", side, "");
    }

    /**
     * <strong>Not Like:</strong><br>
     * Generates a NOT LIKE portion of the query. Separates multiple calls with
     * AND
     *
     * @param field The column to search
     * @param match The search value
     * @param side none | before | after | both
     * @return this
     * @throws DBError
     */
    @Override
    public DBQueryBuilder notLike(String field, String match, String side) {
        return _like(field, match, "AND ", side, " NOT");
    }

    /**
     * <strong>Or Not Like:</strong><br>
     * Generates a NOT LIKE portion of the query. Separates multiple calls with
     * OR
     *
     * @param field The column to search
     * @param match The search value
     * @param side none | before | after | both
     * @return this
     * @throws DBError
     */
    @Override
    public DBQueryBuilder orNotLike(String field, String match, String side) {
        return _like(field, match, "OR ", side, " NOT");
    }

    /**
     * <strong>Group By:</strong><br>
     *
     * @param groupBy
     * @return this
     */
    @Override
    public DBQueryBuilder groupBy(String groupBy) {
        return groupBy(groupBy.split(","));
    }

    /**
     * <strong>Group By:</strong><br>
     *
     * @param groupBy
     * @return this
     */
    @Override
    public DBQueryBuilder groupBy(String[] groupBy) {
        for (String val : groupBy) {
            if (!val.trim().equals("")) {
                qbGroupBy.add(val.trim());
            }
        }
        return this;
    }

    /**
     * <strong>_orderBy:</strong><br>
     *
     * @param orderBy
     * @param direction ASC or DESC
     * @return this
     */
    private DBQueryBuilder _orderBy(String[] orderBy, String direction) {
        for (String val : orderBy) {
            if (!val.trim().equals("")) {
                this.qbOrderBy.add(val.trim() + direction);
            }
        }
        return this;
    }

    /**
     * <strong>Order By ASC:</strong><br>
     * Sets the ORDER BY value
     *
     * @param orderBy
     * @return this
     */
    @Override
    public DBQueryBuilder orderByASC(String orderBy) {
        return _orderBy(orderBy.split(","), " ASC");
    }

    /**
     * <strong>Order By ASC:</strong><br>
     * Sets the ORDER BY value
     *
     * @param orderBy
     * @return this
     */
    @Override
    public DBQueryBuilder orderByASC(String[] orderBy) {
        return _orderBy(orderBy, " ASC");
    }

    /**
     * <strong>Order By DESC:</strong><br>
     * Sets the ORDER BY value
     *
     * @param orderBy
     * @return this
     */
    @Override
    public DBQueryBuilder orderByDESC(String orderBy) {
        return _orderBy(orderBy.split(","), " DESC");
    }

    /**
     * <strong>Order By DESC:</strong><br>
     * Sets the ORDER BY value
     *
     * @param orderBy
     * @return this
     */
    @Override
    public DBQueryBuilder orderByDESC(String[] orderBy) {
        return _orderBy(orderBy, " DESC");
    }

    /**
     * <strong>Having:</strong><br>
     * Sets the HAVING value. Separates multiple calls with AND
     *
     * @param key
     * @param value
     * @return this
     */
    @Override
    public DBQueryBuilder having(String key, String value) {
        return _wh("qbHaving", key, value, "AND ");
    }

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
     * @throws DBError
     */
    @Override
    public DBQueryBuilder having(String having) {
        return _wh("qbHaving", having, "AND ");
    }

    /**
     * <strong>Or Having:</strong><br>
     * Sets the HAVING value. Separates multiple calls with OR
     *
     * @param key
     * @param value
     * @return this
     */
    @Override
    public DBQueryBuilder orHaving(String key, String value) {
        return _wh("qbHaving", key, value, "OR ");
    }

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
     * @throws DBError
     */
    @Override
    public DBQueryBuilder orHaving(String having) {
        return _wh("qbHaving", having, "OR ");
    }

    /**
     * <strong>Limit:</strong><br>
     * Sets the LIMIT and offset values
     *
     * @param limit
     * @param offset
     * @return
     */
    @Override
    public DBQueryBuilder limit(int limit, int offset) {
        if (limit < 0 || offset < 0) {
            throw new IllegalAccessError("Limit and offset >= 0");
        }
        this.qbLimit = limit;
        this.qbOffset = offset;
        return this;
    }

    /**
     * <strong>Limit:</strong><br>
     * Sets the LIMIT value and offset = 0
     *
     * @param limit
     * @return this
     */
    @Override
    public DBQueryBuilder limit(int limit) {
        return limit(limit, 0);
    }

    /**
     * <strong>_grStart:</strong><br>
     * Build query group for where condition
     *
     * @param not
     * @param type
     * @return
     */
    private DBQueryBuilder _grStart(String not, String type) {
        type = _groupGetType(type);
        qbWhereGroupStart = true;
        String prefix = qbWhere.isEmpty() ? "" : type;
        String whereCondition = prefix + not + "(";
        qbWhere.add(whereCondition);
        return this;
    }

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
    @Override
    public DBQueryBuilder groupStart() {
        return _grStart("", "AND ");
    }

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
    @Override
    public DBQueryBuilder orGroupStart() {
        return _grStart("", "OR ");
    }

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
    @Override
    public DBQueryBuilder notGroupStart() {
        return _grStart("NOT ", "AND ");
    }

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
    @Override
    public DBQueryBuilder orNotGroupStart() {
        return _grStart("NOT ", "OR ");
    }

    /**
     * <strong>Group End:</strong><br>
     * End of group.
     *
     * @return
     */
    @Override
    public DBQueryBuilder GroupEnd() {
        qbWhereGroupStart = false;
        qbWhere.add(")");
        return this;
    }

    private String _groupGetType(String type) {
        if (qbWhereGroupStart) {
            type = "";
            qbWhereGroupStart = false;
        }
        return type;
    }

    /**
     * <strong>Set:</strong><br>
     * Allows key/value pairs to be set for inserting or updating.
     *
     * @param dataSet
     * @return this
     */
    @Override
    public DBQueryBuilder set(HashMap<String, String> dataSet) {
        dataSet.forEach((key, value) -> {
            if (key.equals("")) {
                throw new DBError("Key is empty");
            }
            if (value != null) {
                dataSet.replace(key, escape(value));
            }
        });
        qbSet.putAll(dataSet);
        return this;
    }

    /**
     * <strong>Set:</strong><br>
     * Allows key/value pairs to be set for inserting or updating.
     *
     * @param key
     * @param value
     * @return this
     */
    @Override
    public DBQueryBuilder set(String key, String value) {
        if (key.equals("")) {
            throw new DBError("Key is empty");
        }
        HashMap<String, String> dataSet = new HashMap<>();
        dataSet.put(key, value);
        return set(dataSet);
    }

    //===================================================================
    /**
     * <strong>Get Compiled Select:</strong><br>
     * Get SELECT query string.<br>
     * Compiles a SELECT query string and returns the query.
     *
     * @param reset If enabled, the data will be cleared
     * @return query
     */
    @Override
    public String getCompiledSelect(boolean reset) {
        String select = _compileSelect();
        if (reset) {
            _resetSelect();
        }
        return select;
    }

    private String _compileSelect() {
        StringBuilder sql = new StringBuilder();
        sql.append(!qbDistinct ? "SELECT " : "SELECT DISTINCT ");

        if (qbSelect.isEmpty()) {
            sql.append("*");
        } else {
            sql.append(implode(qbSelect, ", "));
        }
        if (!qbFrom.isEmpty()) {
            sql.append("\nFROM ").append(implode(qbFrom, ", "));
        }
        if (!qbJoin.isEmpty()) {
            sql.append("\n").append(implode(qbJoin, "\n"));
        }
        if (!qbWhere.isEmpty()) {
            sql.append("\nWHERE ").append(implode(qbWhere, " "));
        }
        if (!qbGroupBy.isEmpty()) {
            sql.append("\nGROUP BY ").append(implode(qbGroupBy, ", "));
        }
        if (!qbHaving.isEmpty()) {
            sql.append("\nHAVING ").append(implode(qbHaving, "\n"));
        }
        if (!qbOrderBy.isEmpty()) {
            sql.append("\nORDER BY ").append(implode(qbOrderBy, ", "));
        }
        if (qbLimit > 0) {
            sql.append("\nLIMIT ").append(qbLimit).append(" OFFSET ").append(qbOffset).append("");
        }

        return sql.toString();
    }

    //===================================================================
    /**
     * <strong>Insert:</strong><br>
     * The method start the insert query.<br>
     * If you call back many times, the last <strong>tableName</strong> record
     * will be recorded.
     *
     * @param tableName
     * @return this
     */
    @Override
    public DBQueryBuilder insert(String tableName) {
        _setTable(tableName);
        return this;
    }

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
    @Override
    public DBQueryBuilder insert(String tableName, HashMap<String, String> dataSet) {
        _setTable(tableName);
        return set(dataSet);
    }

    /**
     * <strong>Get Compiled Insert:</strong><br>
     * Get INSERT query string<br>
     * Compiles an insert query and returns the query
     *
     * @param reset If enabled, the data will be cleared
     * @return query
     */
    @Override
    public String getCompiledInsert(boolean reset) {
        String sql = _compileInsert();
        if (reset) {
            _resetWrite();
        }
        return sql;
    }

    private String _compileInsert() {
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        qbSet.forEach((k, v) -> {
            key.append(k).append(", ");
            value.append(v).append(", ");
        });
        key.replace(key.length() - 2, key.length(), "");
        value.replace(value.length() - 2, value.length(), "");

        return "INSERT INTO " + qbFrom.get(0) + " (" + key + ") VALUE (" + value + ")";
    }

    //===================================================================
    /**
     * <strong>Update:</strong><br>
     * The method start the update query.<br>
     * If you call back many times, the last <strong>tableName</strong> record
     * will be recorded.
     *
     * @param tableName
     * @return this
     */
    @Override
    public DBQueryBuilder update(String tableName) {
        _setTable(tableName);
        return this;
    }

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
    @Override
    public DBQueryBuilder update(String tableName, HashMap<String, String> dataSet) {
        _setTable(tableName);
        return this.set(dataSet);
    }

    /**
     * <strong>Get Compiled Update:</strong><br>
     * Get UPDATE query string<br>
     * Compiles an update query and returns the query
     *
     * @param reset If enabled, the data will be cleared
     * @return query
     */
    @Override
    public String getCompiledUpdate(boolean reset) {
        String sql = _compileUpdate();
        if (reset) {
            _resetWrite();
        }
        return sql;
    }

    private String _compileUpdate() {
        StringBuilder setStr = new StringBuilder();
        qbSet.forEach((k, v) -> {
            setStr.append(k).append(" = ").append(v).append(", ");
        });
        setStr.replace(setStr.length() - 2, setStr.length(), "");
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(qbFrom.get(0)).append(" SET ").append(setStr);
        if (!qbWhere.isEmpty()) {
            sql.append(" WHERE ").append(implode(qbWhere, " "));
        }
        if (!qbOrderBy.isEmpty()) {
            sql.append("\nORDER BY ").append(implode(qbOrderBy, ", "));
        }
        if (qbLimit > 0) {
            sql.append(" LIMIT ").append(qbLimit);
        }
        return sql.toString();
    }

    //===================================================================
    /**
     * <strong>Delete:</strong><br>
     * The method start the delete query.<br>
     * If you call back many times, the last <strong>tableName</strong> record
     * will be recorded.
     *
     * @param tableName
     * @return this
     */
    @Override
    public DBQueryBuilder delete(String tableName) {
        _setTable(tableName);
        return this;
    }

    /**
     * <strong>Get Compiled Delete:</strong><br>
     * Get DELETE query string<br>
     * Compiles an delete query and returns the query
     *
     * @param reset If enabled, the data will be cleared
     * @return query
     */
    @Override
    public String getCompiledDelete(boolean reset) {
        String sql = _compileDelete();
        if (reset) {
            _resetWrite();
        }
        return sql;
    }

    private String _compileDelete() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(qbFrom.get(0));
        if (qbWhere.isEmpty()) {
            throw new DBError("Delete must use where condition");
        } else {
            sql.append(" WHERE ").append(implode(qbWhere, " "));
        }
        if (qbLimit > 0) {
            sql.append(" LIMIT ").append(qbLimit);
        }
        return sql.toString();
    }

    //===================================================================
    /**
     * <strong>Empty table:</strong><br>
     *
     * @param tableName
     * @return
     */
    @Override
    public String emptyTable(String tableName) {
        return "DELETE FROM " + tableName;
    }

    /**
     * <strong>Truncate table:</strong><br>
     *
     * @param tableName
     * @return
     */
    @Override
    public String truncate(String tableName) {
        return "TRUNCATE " + tableName;
    }

    //===================================================================
    /**
     * <strong>_setTable</strong><br>
     *
     * Resets the query builder values. Called by the insert(), update(),
     * delete() function
     */
    private void _setTable(String tableName) {
        if (tableName.equals("")) {
            throw new DBError("Table name is empty");
        }
        qbFrom.clear();
        qbFrom.add(tableName);
    }

    /**
     * <strong>_resetSelect</strong><br>
     *
     * Resets the query builder values. Called by the _compileSelect() function
     */
    private void _resetSelect() {
        qbSelect = new ArrayList<>();
        qbDistinct = false;
        qbFrom = new ArrayList<>();
        qbJoin = new ArrayList<>();
        qbWhere = new ArrayList<>();
        qbGroupBy = new ArrayList<>();
        qbHaving = new ArrayList<>();
        qbOrderBy = new ArrayList<>();
        qbLimit = 0;
        qbOffset = 0;
        qbWhereGroupStart = false;
    }

    /**
     * <strong>_resetWrite</strong><br>
     * Resets the query builder "write" values.<br>
     * Called by the _compileInsert(), _compileUpdate(), _compileDelete()
     * functions
     */
    private void _resetWrite() {
        qbSet = new HashMap<>();
        qbFrom = new ArrayList<>();
        qbJoin = new ArrayList<>();
        qbWhere = new ArrayList<>();
        qbOrderBy = new ArrayList<>();
        qbLimit = 0;
    }

    /**
     * <strong>Clear:</strong><br>
     * Resets the query builder.<br>
     */
    @Override
    public void clear() {
        _resetSelect();
        qbSet = new HashMap<>();
    }

    //===================================================================
    private String implode(String[] arrayToImplode, String separator) {

        if (arrayToImplode.length == 0) { //empty array return empty string 
            return "";
        }

        if (arrayToImplode.length < 2) { //only 1 item
            return arrayToImplode[0];
        }

        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < arrayToImplode.length; i++) {
            if (i != 0) {
                strBuilder.append(separator);
            }
            strBuilder.append(arrayToImplode[i]);
        }

        //return the implode string
        return strBuilder.toString();
    }

    private String implode(List<String> arrayToImplode, String separator) {
        return implode(arrayToImplode.toArray(new String[arrayToImplode.size()]), separator);
    }

    private boolean _hasOperator(String str) {
        String preg = "\\w+\\s+(<|>|!=|=|>=|<=|is null|is not null){1}";
        return str.matches(preg);
    }

    private String escape(String str) {
        return "'" + escapeStr(str) + "'";
    }

    private String escapeStr(String str) {
        String[] preg = new String[]{"\\\\", "\n", "\r", "'", "\""};
        String[] replace = new String[]{"\\\\\\\\", "\\\\n", "\\\\r", "\\\\'", "\\\\\""};
        for (int i = 0; i < preg.length; i++) {
            str = str.trim().replaceAll(preg[i], replace[i]);
        }
        return str;
    }

    private String escapeLikeStr(String str) {
        return escapeStr(str).replaceAll("%", "\\%").replaceAll("_", "\\_");
    }

    public class DBError extends RuntimeException {

        private String msg;

        public DBError(String msg) {
            this.msg = msg;
        }

        @Override
        public String getMessage() {
            return msg;
        }

    }

}
