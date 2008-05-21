package gov.epa.owm.mtb.cwns.common.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import oracle.webdb.provider.v2.struts.StrutsUtils;
import oracle.webdb.utils.SimpleStringBuffer;

import org.apache.struts.taglib.html.ImageTag;

public class PDKImageTag extends ImageTag {

    public PDKImageTag()
    {
    }

    protected String src()
        throws JspException
    {
        HttpServletRequest httpservletrequest = (HttpServletRequest)pageContext.getRequest();
        if(!StrutsUtils.isPortalRequest(httpservletrequest))
            return super.src();
        String s = null;
        if(getPage() != null || getPageKey() != null)
        {
            SimpleStringBuffer simplestringbuffer = (new SimpleStringBuffer(50)).append(httpservletrequest.getScheme()).append("://").append(httpservletrequest.getServerName()).append(":").append(httpservletrequest.getServerPort()).append(super.src());
            s = simplestringbuffer.toString();
        } else
        {
            s = super.src();
        }
        return s;
    }

}
