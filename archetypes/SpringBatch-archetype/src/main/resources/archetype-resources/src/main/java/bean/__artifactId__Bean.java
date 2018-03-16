package ${package}.bean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
/**
 * @author ${devName}
 *
 *	base 
 */
public class ${artifactId}Bean {

	
	public String getTEKey() {
		/* TODO:  Create the key based on deduplication of data.  
		 *        So the key is unique and should parse out dups.  
		 *        Ex included from another job.  */
		return this.toString(); 
		//this.getINSTCODEC() + this.getPRGMC() + this.getTLVLC() + this.getTRNSFSUBJC() + this.getTRNSFCRSEC() + this.getGRPC();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	public void setBeanFromString(String string) {
		
	}
}