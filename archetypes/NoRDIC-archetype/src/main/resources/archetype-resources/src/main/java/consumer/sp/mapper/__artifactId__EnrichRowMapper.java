/**
 * 
 */
package ${package}.consumer.sp.mapper;


import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

import ${package}.beans.${artifactId}Bean;

/**
 * @author ${devName}
 *
 */
@SuppressWarnings("rawtypes")
public class ${artifactId}EnrichRowMapper implements RowMapper {
	DataSource ds;
	
	public ${artifactId}EnrichRowMapper(DataSource ds) {
		super();
		
		this.ds = ds;
	}
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		${artifactId}Bean bean = new ${artifactId}Bean();
		
		/* TODO:  Add in your translations from Row to Been. */
		//bean.setSomeValue(rs.getString("SOME_VALUE"));
		
		return bean;
	}
}