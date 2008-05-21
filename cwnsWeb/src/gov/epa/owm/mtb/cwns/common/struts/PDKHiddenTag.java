package gov.epa.owm.mtb.cwns.common.struts;

import javax.servlet.jsp.JspException;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;


public class PDKHiddenTag extends PDKBaseFieldTag{

	    public PDKHiddenTag()
	    {
	        write = false;
	        type = "hidden";
	    }

	    public boolean getWrite()
	    {
	        return write;
	    }

	    public void setWrite(boolean write)
	    {
	        this.write = write;
	    }

	    public int doStartTag()
	        throws JspException
	    {
	        super.doStartTag();
	        if(!write)
	            return 2;
	        String results = null;
	        if(this.value != null)
	        {
	            results = ResponseUtils.filter(this.value);
	        } else
	        {
	            Object value = RequestUtils.lookup(pageContext, name, property, null);
	            if(value == null)
	                results = "";
	            else
	                results = ResponseUtils.filter(value.toString());
	        }
	        ResponseUtils.write(pageContext, results);
	        return 2;
	    }

	    public void release()
	    {
	        super.release();
	        write = false;
	    }

	    protected boolean write;
}
