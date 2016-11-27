package com.shtitan.timesynchronize.entity.usertype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

/**
 * Hibernate UserType，用于BO的List与字段的VARCHAR之间互转
 * @author pl
 *
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class ListUserType implements UserType, Serializable {

	private static final long serialVersionUID = -49850477878L;
	
	/**
	 * 分隔符
	 */
	public static final String LINE_DISCRIMINATOR = ",";

	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return cached;
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y)
			return true;
		if (x == null || y == null)
			return false;
		return x.equals(y);
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	public boolean isMutable() {
		return false;
	}

	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	public Class returnedClass() {
		return String.class;
	}

	public int[] sqlTypes() {
		return new int[] { Types.VARCHAR };
	}
	
	private List fromString(String value) {
		StringTokenizer tokenizer = new StringTokenizer(value,
				LINE_DISCRIMINATOR);
		String line;
		List list = new ArrayList<String>();

		while (tokenizer.hasMoreElements()) {
			line = tokenizer.nextToken();
			list.add(line);
		}

		return list;
	}

	private String toString(List list) {
		StringBuffer buf = new StringBuffer();

		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			String str = (String) iterator.next();
			buf.append(str).append(LINE_DISCRIMINATOR);
		}
		return buf.toString();
	}

	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor arg2, Object arg3) throws HibernateException,
			SQLException {
		String value = rs.getString(names[0]);
		if (rs.wasNull())
			return null;
		return fromString(value);
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor arg3) throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, Types.VARCHAR);
		} else {
			st.setString(index, toString((List) value));
		}

	}
}
