package gov.epa.owm.mtb.cwns.common.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import oracle.portal.provider.v2.render.PortletRenderRequest;
import oracle.portal.provider.v2.render.PortletRendererUtil;
import oracle.webdb.provider.v2.struts.StrutsUtils;

import org.apache.struts.taglib.html.CheckboxTag;
import org.apache.struts.util.*;

/**
 *
 * This class overrides the default struts TextareaTag implementation to work with oracle PDK
 *
 * @author Raj Lingam
 *
 */
public class PDKCheckboxTag extends CheckboxTag {

    public int doStartTag()
    throws JspException
	{
	    StringBuffer results = new StringBuffer("<input type=\"checkbox\"");
	    results.append(" name=\"");
	    if(indexed)
	        prepareIndex(results, name);
	
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
	    if(value == null)
	        results.append("on");
	    else
	        results.append(value);
	    results.append("\"");
	    Object result = RequestUtils.lookup(pageContext, name, property, null);
	    if(result == null)
	        result = "";
	    if(!(result instanceof String))
	        result = result.toString();
	    String checked = (String)result;
	    if(checked.equalsIgnoreCase(value) || checked.equalsIgnoreCase("true") || checked.equalsIgnoreCase("yes") || checked.equalsIgnoreCase("on"))
	        results.append(" checked=\"checked\"");
	    results.append(prepareEventHandlers());
	    results.append(prepareStyles());
	    results.append(getElementClose());
	    ResponseUtils.write(pageContext, results.toString());
	    text = null;
	    return 2;
	}

}
