/*
 * xScript.database ( Database for XML Script Language)
 * Copyright 2015 and beyond, Kenneth Huang
 * 
 * This file is part of xScript.database.
 * 
 * xScript.database is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * xScript.database is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with xScript.database.  If not, see <http://www.gnu.org/licenses/>. 
 */

package kenh.xscript.database.beans;

import java.util.*;
import java.sql.*;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaClass;

/**
 * The bean of result set.
 * This class do not call close method for result set.
 * 
 * @author Kenneth
 * @version 1.0
 *
 */
public class ResultSetBean implements Iterable<DynaBean> {
	
	private List<DynaBean> rows = new ArrayList<DynaBean>();  // each row of result set
	private List<Column> cols = new ArrayList<Column>();  // columns
	
	private boolean include_field_name = true; // include field name
	
	/**
	 * Use result set to initial a bean. The bean include fields' name.
	 * 
	 * @param rs
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public ResultSetBean(ResultSet rs) throws SQLException, IllegalAccessException, InstantiationException {
		this(rs, true);
		
	}
	
	/**
	 * Use result set to initial a bean.
	 * 
	 * @param rs
	 * @param includeFieldName
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public ResultSetBean(ResultSet rs, boolean includeFieldName) throws SQLException, IllegalAccessException, InstantiationException {
		include_field_name = includeFieldName;
		
		LazyDynaClass beanClass = new LazyDynaClass();
		
		ResultSetMetaData m = rs.getMetaData();
		for(int i=1; i<= m.getColumnCount(); i++) {
			Column c = new Column();
			
			try {
				c.catalogName = m.getCatalogName(i);
			} catch(SQLException e) {}
			try {
				c.className = m.getColumnClassName(i);
			} catch(SQLException e) {}
			try {
				c.displaySize = m.getColumnDisplaySize(i);
			} catch(SQLException e) {}
			try {
				c.label = m.getColumnLabel(i);
			} catch(SQLException e) {}
			try {
				c.name = m.getColumnName(i);
			} catch(SQLException e) {}
			try {
				c.type = m.getColumnType(i);
			} catch(SQLException e) {}
			try {
				c.typeName = m.getColumnTypeName(i);
			} catch(SQLException e) {}
			try {
				c.precision = m.getPrecision(i);
			} catch(SQLException e) {}
			try {
				c.scale = m.getScale(i);
			} catch(SQLException e) {}
			try {
				c.schemaName = m.getSchemaName(i);
			} catch(SQLException e) {}
			try {
				c.tableName = m.getTableName(i);
			} catch(SQLException e) {}
			
			beanClass.add(m.getColumnLabel(i).toLowerCase());
			beanClass.add("" + i);
			
			cols.add(c);
		}
		
		DynaBean colBean = beanClass.newInstance();
		int i = 1;
		for(Column col: cols) {
			String field = col.getLabel().toLowerCase();
			colBean.set(field, col.getLabel());
			colBean.set("" + i, col.getLabel());
			i++;
		}
		
		if(include_field_name) rows.add(colBean);
		
		while(rs.next()) {
			DynaBean bean = beanClass.newInstance();
			i = 1;
			for(Column c: cols) {
				String field = c.getLabel().toLowerCase();
				Object obj = rs.getObject(field);
				bean.set(field, obj);
				bean.set("" + i, obj);
				i++;
			}
			rows.add(bean);
		}
	
	}
	
	/**
	 * Return all rows.
	 * @return
	 */
	public List<DynaBean> getRows() {
		return rows;
	}
	
	/**
	 * Get the size (without field row).
	 * @return
	 */
	public int getSize() {
		return include_field_name? (rows.size() - 1):rows.size();
	}
	
	/**
	 * Get the length (with field row if has).
	 * @return
	 */
	public int getLength() {
		return rows.size();
	}
	
	/**
	 * Get columns.
	 * @return
	 */
	public List<Column> getCols() {
		return cols;
	}
	
	
	/**
	 * Get the column size.
	 * @return
	 */
	public int getWidth() {
		return cols.size();
	}
	
	@Override
	public Iterator<DynaBean> iterator() {
		List<DynaBean> returnRows = new ArrayList<DynaBean>();
		returnRows.addAll(rows);
		
		if(include_field_name) returnRows.remove(0);
		return returnRows.iterator();
	}
	
	
	/**
	 * Column bean, it list field's meta data.
	 * @author Kenneth
	 *
	 */
	public class Column {
		String catalogName;
		String className;
		int displaySize;
		String label;
		String name;
		int type;
		String typeName;
		int precision;
		int scale;
		String schemaName;
		String tableName;
		
		public String getCatalogName() {
			return catalogName;
		}
		public String getClassName() {
			return className;
		}
		public int getDisplaySize() {
			return displaySize;
		}
		public String getLabel() {
			return label;
		}
		public String getName() {
			return name;
		}
		public int getType() {
			return type;
		}
		public String getTypeName() {
			return typeName;
		}
		public int getPrecision() {
			return precision;
		}
		public int getScale() {
			return scale;
		}
		public String getSchemaName() {
			return schemaName;
		}
		public String getTableName() {
			return tableName;
		}
		
	}
}
