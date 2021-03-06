/*
 * Microsoft JDBC Driver for SQL Server
 * 
 * Copyright(c) Microsoft Corporation All rights reserved.
 * 
 * This program is made available under the terms of the MIT License. See the LICENSE file in the project root for more information.
 */

package com.microsoft.sqlserver.testframework;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerException;

/**
 * wrapper method for Statement object
 * 
 * @author Microsoft
 *
 */
public class DBStatement extends AbstractParentWrapper {

    // TODO: support PreparedStatement and CallableStatement
    // TODO: add stmt level holdability
    // TODO: support IDENTITY column and stmt.getGeneratedKeys()

    Statement statement = null;
    DBResultSet dbresultSet = null;

    DBStatement(DBConnection dbConnection) {
        super(dbConnection, null, "statement");
    }

    DBStatement statement() {
        return this;
    }

    DBStatement createStatement() throws SQLServerException {
        // TODO: add cursor and holdability
        statement = ((SQLServerConnection) parent().product()).createStatement();
        setInternal(statement);
        return this;
    }

    DBStatement createStatement(int type,
            int concurrency) throws SQLServerException {
        // TODO: add cursor and holdability
        statement = ((SQLServerConnection) parent().product()).createStatement(type, concurrency);
        setInternal(statement);
        return this;
    }

    /**
     * 
     * @param sql
     *            query to execute
     * @return DBResultSet
     * @throws SQLException
     */
    public DBResultSet executeQuery(String sql) throws SQLException {
        ResultSet rs = null;
        rs = statement.executeQuery(sql);
        dbresultSet = new DBResultSet(this, rs);
        return dbresultSet;
    }

    /**
     * 
     * @param sql
     *            query to execute
     * @return <code>true</code> if ResultSet is returned
     * @throws SQLException
     */
    public boolean execute(String sql) throws SQLException {
        return statement.execute(sql);
    }

    /**
     * Close the <code>Statement</code> and <code>ResultSet</code> associated with it
     * 
     * @throws SQLException
     */
    public void close() throws SQLException {
        if ((null != dbresultSet) && null != ((ResultSet) dbresultSet.product())) {
            ((ResultSet) dbresultSet.product()).close();
        }
        statement.close();
    }

    /**
     * create table
     * 
     * @param table
     * @return <code>true</code> if table is created
     */
    public boolean createTable(DBTable table) {
        return table.createTable(this);
    }

    /**
     * populate table with values
     * 
     * @param table
     * @return <code>true</code> if table is populated
     */
    public boolean populateTable(DBTable table) {
        return table.populateTable(this);
    }

    /**
     * Drop table from Database
     * 
     * @param dbstatement
     * @return true if table dropped
     */
    public boolean dropTable(DBTable table) {
        return table.dropTable(this);
    }

    @Override
    void setInternal(Object internal) {
        this.internal = internal;
    }

    /**
     * 
     * @return
     * @throws SQLException
     */
    public int getQueryTimeout() throws SQLException {
        int current = ((Statement) product()).getQueryTimeout();
        return current;
    }
}
