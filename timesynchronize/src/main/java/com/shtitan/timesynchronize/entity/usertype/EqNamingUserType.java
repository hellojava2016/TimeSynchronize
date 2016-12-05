package com.shtitan.timesynchronize.entity.usertype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import com.shtitan.timesynchronize.entity.eqm.naming.EqNaming;

public class EqNamingUserType implements UserType,Serializable {

    private static final long serialVersionUID = -3968937974301097953L;

    @Override
    public Object assemble(Serializable arg0, Object arg1)
            throws HibernateException {
        return arg0;
    }

    @Override
    public Object deepCopy(Object arg0) throws HibernateException {
        return arg0;
    }

    @Override
    public Serializable disassemble(Object arg0) throws HibernateException {
        return (Serializable) arg0;
    }

    @Override
    public boolean equals(Object arg0, Object arg1) throws HibernateException {
        return arg0 == arg1;

    }

    @Override
    public int hashCode(Object arg0) throws HibernateException {
        return arg0.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }


    @Override
    public Object replace(Object arg0, Object arg1, Object arg2)
            throws HibernateException {
        return arg0;
    }

    @Override
    public Class returnedClass() {
        return EqNaming.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.VARCHAR };
    }

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor arg2, Object owner)
			throws HibernateException, SQLException {
		 if (names[0] == null)
	            return null;
	        String name = rs.getString(names[0]);
	        if(StringUtils.isEmpty(name))
	        	return null;
	        return new EqNaming(name);
	}

	@Override
	public void nullSafeSet(PreparedStatement ps, Object value, int index, SessionImplementor arg3)
			throws HibernateException, SQLException {
		  if (value == null)
	            ps.setString(index, "");
	        else
	            ps.setString(index, (new EqNaming(value.toString())).toString());
		
	}

}
