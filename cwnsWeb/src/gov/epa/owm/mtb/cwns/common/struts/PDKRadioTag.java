package gov.epa.owm.mtb.cwns.common.struts;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import oracle.portal.provider.v2.render.PortletRenderRequest;
import oracle.portal.provider.v2.render.PortletRendererUtil;
import oracle.webdb.provider.v2.struts.StrutsUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.taglib.html.RadioTag;
import org.apache.struts.util.RequestUtils;

/**
 *
 * This class overrides the default struts TextareaTag implementation to work with oracle PDK
 *
 * @author Raj Lingam
 *
 */
public class PDKRadioTag extends RadioTag {

    protected String renderRadioElement(String serverValue, String checkedValue)
    throws JspException
	{
    	StringBuffer results = new StringBuffer("<input type=\"radio\"");
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
	    results.append(serverValue);
	    results.append("\"");
	    if(serverValue.equals(checkedValue))
	        results.append(" checked=\"checked\"");
	    results.append(prepareEventHandlers());
	    results.append(prepareStyles());
	    results.append(getElementClose());
	    return results.toString();
	}
}
