package gov.epa.owm.mtb.cwns.common.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import oracle.portal.provider.v2.render.PortletRenderRequest;
import oracle.portal.provider.v2.render.PortletRendererUtil;
import oracle.webdb.provider.v2.struts.StrutsUtils;

import org.apache.struts.taglib.html.CancelTag;
import org.apache.struts.util.ResponseUtils;

public class PDKCancelTag extends CancelTag {

	    public int doEndTag()
	        throws JspException
	    {
	    	if(!StrutsUtils.isPortalRequest((HttpServletRequest)pageContext.getRequest()))
	           return super.doEndTag();	    	
	        String label = value;
	        if(label == null && text != null)
	            label = text;
	        if(label == null || label.trim().length() < 1)
	            label = "Cancel";
	        StringBuffer results = new StringBuffer();
	        results.append("<input type=\"submit\"");
	        results.append(" name=\"");
	        HttpServletRequest httpservletrequest = (HttpServletRequest)pageContext.getRequest();
	        PortletRenderRequest portletrenderrequest = (PortletRenderRequest)httpservletrequest.getAttribute("oracle.portal.PortletRenderRequest");
	        String s = PortletRendererUtil.portletParameter(portletrenderrequest, property);
	        results.append(s);
	        results.append("\"");
	        if(accesskey != null)
	        {
	            results.append(" accesskey=\"");
	            results.append(accesskey);
	            results.append("\"");
	        }
	        if(tabindex != null)
	        {
	            results.append(" tabindex=\"");
	            results.append(tabindex);
	            results.append("\"");
	        }
	        results.append(" value=\"");
	        results.append(label);
	        results.append("\"");
	        results.append(prepareEventHandlers());
	        results.append(prepareStyles());
	        if(results.toString().indexOf("onclick=") == -1)
	            results.append(" onclick=\"bCancel=true;\"");
	        results.append(getElementClose());
	        ResponseUtils.write(pageContext, results.toString());
	        return 6;
	    }
}
