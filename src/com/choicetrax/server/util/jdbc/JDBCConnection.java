package com.choicetrax.server.util.jdbc;

import java.sql.*;

import org.apache.log4j.Logger;

import com.choicetrax.client.util.exception.ChoicetraxDatabaseException;

public class JDBCConnection implements DBResource 
{

	private Connection conn 		= null;
	private Statement statement 	= null;
	private PreparedStatement ps	= null;
	
	private boolean inuse = false;
	private long timestamp;
	
	private boolean resinConn = false;
	
	private static Logger logger = Logger.getLogger( JDBCConnection.class );
	
	
	public JDBCConnection( Connection conn ) 
	{
		this.conn = conn;		
		this.inuse = false;
		this.timestamp = 0;
		
		resinConn = true;
	}

	
	public void clearParameters() throws ChoicetraxDatabaseException
	{
		try {
			if ( ps != null )	ps.clearParameters();
		}
		catch ( SQLException sqle ) {
			throw new ChoicetraxDatabaseException( "Error in clearing PreparedStatement parameters: " 
											+ sqle,
											this.getClass().getName() + ".clearParameters()" );

		}
	}
	
	public ResultSet executeQuery() throws ChoicetraxDatabaseException
	{
		ResultSet rs = null;
		
		if ( ps != null )
		{
			try
			{
				rs = ps.executeQuery();
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in executing PreparedStatement query: " 
												+ sqle,
												this.getClass().getName() + ".executeQuery()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Unable to execute PreparedStatement query, "
											+ "PreparedStatement is null",
											this.getClass().getName() + ".executeQuery()" );
		}
		
		return rs;
	}
	
	public ResultSet executeQuery( String sql ) throws ChoicetraxDatabaseException
	{
		return executeQuery( sql, 1 );
	}
	
	
	private ResultSet executeQuery( String sql, int attemptNum ) throws ChoicetraxDatabaseException
	{
		ResultSet rs = null;
		
		if ( conn != null )
		{
			try
			{
				statement = conn.createStatement();
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in creating Statement obj: " + sqle,
												this.getClass().getName() 
													+ ".executeQuery( sql )" );
			}
			
			try
			{
				rs = statement.executeQuery( sql );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in executing Statement query "
													+ "with SQL [" + sql + "]: " + sqle,
												this.getClass().getName() 
													+ ".executeQuery( sql )" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Unable to execute query [" + sql + "], "
											+ "no database connection exists",
											this.getClass().getName() + ".executeQuery( sql )" );
		}
		
		return rs;
	}
	
	
	public int executeUpdate() throws ChoicetraxDatabaseException
	{
		int rc = -1;
		
		if ( ps != null )
		{
			try 
			{
				rc = ps.executeUpdate();
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error executing PreparedStatement update: " + sqle,
												this.getClass().getName() + ".executeUpdate()" );
				
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Unable to execute PreparedStatement update: "
											+ "PreparedStatement is null",
											this.getClass().getName() + ".executeUpdate()" );
		}
		
		return rc;
	}
	
	public int executeUpdate( String sql ) throws ChoicetraxDatabaseException
	{
		int rowCount = 0;
		
		if ( conn != null )
		{
			try
			{
				statement = conn.createStatement();
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in populating PreparedStatement: " + sqle,
												this.getClass().getName() 
													+ ".executeUpdate( sql ) " );
			}
			
			try
			{
				rowCount = statement.executeUpdate( sql );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in executing database update "
													+ "with SQL [" + sql + "]: " + sqle, 
												this.getClass().getName() + ".executeUpdate( " 
													+ sql + " )" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Unable to execute update [" + sql + "], "
											+ "no database connection exists",
											this.getClass().getName() + ".executeUpdate( sql )" );
		}
		
		return rowCount;
	}
	
	public int executeUpdate( String sql, int autoGeneratedKeys ) throws ChoicetraxDatabaseException
	{
		int rowCount = 0;
		
		if ( conn != null )
		{
			try
			{
				statement = conn.createStatement();
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in populating PreparedStatement: " + sqle,
												this.getClass().getName() 
													+ ".executeUpdate( sql ) " );
			}
			
			try
			{
				rowCount = statement.executeUpdate( sql, autoGeneratedKeys );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in executing database update: " + sqle, 
												this.getClass().getName() 
												+ ".executeUpdate( sql )" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Unable to execute update [" + sql + "], "
											+ "no database connection exists",
											this.getClass().getName() + ".executeUpdate( sql )" );
		}
		
		return rowCount;
	}
	
	public ResultSet getGeneratedKeys() throws ChoicetraxDatabaseException
	{
		if ( statement != null )
		{
			try
			{
				return statement.getGeneratedKeys();
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Unable to get DB generated keys: " + sqle,
												this.getClass().getName() + ".getGeneratedKeys()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Unable to get DB generated keys: "
											+ "statement obj is null",
											this.getClass().getName() + ".getGeneratedKeys()" );
		}
	}

	public synchronized boolean lease() 
	{
		if (inuse) {
			return false;
		} else {
			inuse = true;
			timestamp = System.currentTimeMillis();
			return true;
		}
	}

	public boolean validate() 
	{
		try {
			conn.getMetaData();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean inUse() {
		return inuse;
	}

	public long getLastUse() {
		return timestamp;
	}

	public void close() throws ChoicetraxDatabaseException 
	{
		try
		{
			if ( ps != null )			ps.close();
			if ( statement != null )	statement.close();
		}
		catch ( SQLException sqle ) 
		{
			logger.error( "error closing JDBCConnection ps or statement", sqle );
		}
		finally 
		{
			if ( resinConn ) 
			{
				try 
				{
					conn.close();
				} 
				catch ( SQLException e ) 
				{ 
					logger.error( "error closing resin connection", e );
				}
			}
		}
	}

	protected void expireLease() {
		inuse = false;
	}

	protected Connection getConnection() {
		return conn;
	}

	public void prepareStatement(String sql) throws ChoicetraxDatabaseException 
	{
		if ( conn != null )
		{
			try
			{
				if ( ps != null )	ps.close();
				
				ps = conn.prepareStatement(sql);
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in creating PreparedStatement: " + sqle,
												this.getClass().getName() 
												+ ".prepareStatement()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Unable to create PreparedStatement - no DB connection",
											this.getClass().getName() + ".prepareStatement()" );
		}
	}
	
	public PreparedStatement prepareStatement(String sql, String[] columnNames) 
		throws SQLException 
	{
		return conn.prepareStatement(sql, columnNames);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return conn.prepareCall(sql);
	}

	public Statement createStatement() throws SQLException {
		return conn.createStatement();
	}

	public void setAutoCommit(boolean autoCommit) throws ChoicetraxDatabaseException 
	{
		if ( conn != null )
		{
			try
			{
				conn.setAutoCommit(autoCommit);
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Unable to set DB auto-commit state: " + sqle,
												this.getClass().getName() + ".setAutoCommit()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Unable to set DB auto-commit state, "
											+ "no DB connection exists",
											this.getClass().getName() + ".setAutoCommit()" );
		}
	}

	public boolean getAutoCommit() throws ChoicetraxDatabaseException 
	{
		if ( conn != null )
		{
			try
			{
				return conn.getAutoCommit();
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Unable to determine DB auto-commit state: " + sqle,
												this.getClass().getName() + ".getAutoCommit()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Unable to determine DB auto-commit state: "
											+ "no DB connection",
											this.getClass().getName() + ".getAutoCommit()" );
		}
	}

	public void commit() throws ChoicetraxDatabaseException 
	{
		if ( conn != null )
		{
			try {
				conn.commit();
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Unable to commit DB: " + sqle,
												this.getClass().getName() + ".commit()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Unable to commit DB, no database connection exists",
											this.getClass().getName() + ".commit()" );
		}
	}

	public void rollback() throws ChoicetraxDatabaseException 
	{
		if ( conn != null )
		{
			try
			{
				conn.rollback();
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Unable to rollback DB: " + sqle,
												this.getClass().getName() + ".rollback()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Unable to rollback DB, no DB connection exists",
											this.getClass().getName() + ".rollback()" );
		}
	}
	
	public void setBigDecimal( int i, java.math.BigDecimal value ) throws ChoicetraxDatabaseException
	{
		if ( ps != null )
		{
			try 
			{
				ps.setBigDecimal( i, value );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "] "
												+ "with index [" + i + "]: " + sqle,
												this.getClass().getName() + ".setBigDecimal()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "], "
											+ "PreparedStatement has not been initialized",
											this.getClass().getName() + ".setBigDecimal()" );
		}
	}
	
	public void setBoolean( int i, boolean value ) throws ChoicetraxDatabaseException
	{
		if ( ps != null )
		{
			try 
			{
				ps.setBoolean( i, value );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "] "
												+ "with index [" + i + "]: " + sqle,
												this.getClass().getName() + ".setBoolean()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "], "
											+ "PreparedStatement has not been initialized",
											this.getClass().getName() + ".setBoolean()" );
		}
	}
	
	public void setDate( int i, java.sql.Date value ) throws ChoicetraxDatabaseException
	{
		if ( ps != null )
		{
			try 
			{
				ps.setDate( i, value );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "] "
												+ "with index [" + i + "]: " + sqle,
												this.getClass().getName() + ".setDate()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "], "
											+ "PreparedStatement has not been initialized",
											this.getClass().getName() + ".setDate()" );
		}
	}
	
	public void setDouble( int i, double value ) throws ChoicetraxDatabaseException
	{
		if ( ps != null )
		{
			try 
			{
				ps.setDouble( i, value );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "] "
												+ "with index [" + i + "]: " + sqle,
												this.getClass().getName() + ".setDouble()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "], "
											+ "PreparedStatement has not been initialized",
											this.getClass().getName() + ".setDouble()" );
		}
	}
	
	public void setFloat( int i, float value ) throws ChoicetraxDatabaseException
	{
		if ( ps != null )
		{
			try 
			{
				ps.setFloat( i, value );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "] "
												+ "with index [" + i + "]: " + sqle,
												this.getClass().getName() + ".setFloat()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "], "
											+ "PreparedStatement has not been initialized",
											this.getClass().getName() + ".setFloat()" );
		}
	}
	
	public void setInt( int i, int value ) throws ChoicetraxDatabaseException
	{
		if ( ps != null )
		{
			try 
			{
				ps.setInt( i, value );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "] "
												+ "with index [" + i + "]: " + sqle,
												this.getClass().getName() + ".setInt()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "], "
											+ "PreparedStatement has not been initialized",
											this.getClass().getName() + ".setInt()" );
		}
	}
	
	public void setLong( int i, long value ) throws ChoicetraxDatabaseException
	{
		if ( ps != null )
		{
			try 
			{
				ps.setLong( i, value );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "] "
												+ "with index [" + i + "]: " + sqle,
												this.getClass().getName() + ".setLong()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "], "
											+ "PreparedStatement has not been initialized",
											this.getClass().getName() + ".setLong()" );
		}
	}
	
	public void setObject( int i, Object o, int sqlType, int scale ) throws ChoicetraxDatabaseException
	{
		if ( ps != null )
		{
			try 
			{
				ps.setObject( i, o, sqlType, scale );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in setting value [" + o + "] "
												+ "with index [" + i + "]: " + sqle,
												this.getClass().getName() + ".setObject()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Error in setting value [" + o + "], "
											+ "PreparedStatement has not been initialized",
											this.getClass().getName() + ".setObject()" );
		}
	}
	
	public void setString( int i, String value ) throws ChoicetraxDatabaseException
	{
		if ( ps != null )
		{
			try 
			{
				ps.setString( i, value );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "] " 
												+ "with index [" + i + "]: " + sqle,
												this.getClass().getName() + ".setString()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "], "
											+ "PreparedStatement has not been initialized",
											this.getClass().getName() + ".setString()" );
		}
	}
	
	public void setTimestamp( int i, java.sql.Timestamp value ) throws ChoicetraxDatabaseException
	{
		if ( ps != null )
		{
			try 
			{
				ps.setTimestamp( i, value );
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "] "
												+ "with index [" + i + "]: " + sqle,
												this.getClass().getName() + ".setTimestamp()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Error in setting value [" + value + "], "
											+ "PreparedStatement has not been initialized",
											this.getClass().getName() + ".setTimestamp()" );
		}
	}
	
	public void shutdown() throws ChoicetraxDatabaseException
	{
		try
		{
			if ( conn != null )	conn.close();
			conn = null;
		}
		catch ( SQLException sqle )
		{
			throw new ChoicetraxDatabaseException( "Error closing JDBC connection: " + sqle, 
											this.getClass().getName() + ".shutdown()" );
		}
	}

	public boolean isClosed() throws SQLException {
		return conn.isClosed();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return conn.getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws ChoicetraxDatabaseException 
	{
		try
		{
			if ( conn != null )	conn.setReadOnly(readOnly);
		}
		catch ( SQLException sqle )
		{
			throw new ChoicetraxDatabaseException( "Error setting read only state: " + sqle,
											this.getClass().getName() + ".setReadOnly()" );
		}
	}

	public boolean isReadOnly() throws ChoicetraxDatabaseException 
	{
		if ( conn != null )
		{
			try
			{
				return conn.isReadOnly();
			}
			catch ( SQLException sqle )
			{
				throw new ChoicetraxDatabaseException( "Error getting read only value: " + sqle,
												this.getClass().getName() + ".isReadOnly()" );
			}
		}
		else
		{
			throw new ChoicetraxDatabaseException( "Unable to get read only state, no DB connection exists",
											this.getClass().getName() + ".isReadOnly()" );
		}		
	}

	public void setTransactionIsolation(int level) throws SQLException {
		conn.setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException {
		return conn.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		return conn.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		conn.clearWarnings();
	}
	
	
	
}