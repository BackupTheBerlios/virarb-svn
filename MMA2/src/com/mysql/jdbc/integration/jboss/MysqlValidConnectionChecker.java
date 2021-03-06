/*
      Copyright (C) 2002-2005 MySQL AB

      This program is free software; you can redistribute it and/or modify
      it under the terms of version 2 of the GNU General Public License as 
      published by the Free Software Foundation.

      There are special exceptions to the terms and conditions of the GPL 
      as it is applied to this software. View the full text of the 
      exception in file EXCEPTIONS-CONNECTOR-J in the directory of this 
      software distribution.

      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU General Public License for more details.

      You should have received a copy of the GNU General Public License
      along with this program; if not, write to the Free Software
      Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.mysql.jdbc.integration.jboss;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.jboss.resource.adapter.jdbc.ValidConnectionChecker;

/**
 * A more efficient connection checker for JBoss.
 * 
 * @version $Id: MysqlValidConnectionChecker.java,v 1.1.2.1 2005/03/22 00:45:07 mmatthews Exp $
 */
public final class MysqlValidConnectionChecker 
	implements ValidConnectionChecker, Serializable {

	private static final long serialVersionUID = 3258689922776119348L;

	private Method pingMethod;

	private final static Object[] NO_ARGS_OBJECT_ARRAY = new Object[0];

	public MysqlValidConnectionChecker() {
		try {
			// Avoid classloader goofiness
			Class mysqlConnection = Thread.currentThread()
					.getContextClassLoader().loadClass(
							"com.mysql.jdbc.Connection");

			pingMethod = mysqlConnection.getMethod("ping", null);
		} catch (Exception ex) {
			// Punt, we'll use 'SELECT 1' to do the check
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.resource.adapter.jdbc.ValidConnectionChecker#isValidConnection(java.sql.Connection)
	 */
	public SQLException isValidConnection(Connection conn) {
		if (pingMethod != null) {
			try {
				this.pingMethod.invoke(conn, NO_ARGS_OBJECT_ARRAY);

				return null;
			} catch (Exception ex) {
				if (ex instanceof SQLException) {
					return (SQLException) ex;
				}

				return new SQLException("Ping failed: " + ex.toString());
			}
		}
		
		// Punt and use 'SELECT 1'

		Statement pingStatement = null;

		try {
			pingStatement.executeQuery("SELECT 1").close();

			return null;
		} catch (SQLException sqlEx) {
			return sqlEx;
		} finally {
			if (pingStatement != null) {
				try {
					pingStatement.close();
				} catch (SQLException sqlEx) {
					// can't do anything about it here
				}
			}
		}
	}
}
