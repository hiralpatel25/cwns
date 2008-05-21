package gov.epa.owm.mtb.cwns.common.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import oracle.portal.provider.v2.render.PortletRenderRequest;
import oracle.portal.provider.v2.render.PortletRendererUtil;

import org.apache.struts.taglib.html.SelectTag;

public class PDKSelectTag extends SelectTag {
	
	protected String renderSelectStartElement() throws JspException {
		StringBuffer results = new StringBuffer("<select");
		results.append(" name=\"");
		if (indexed)
			prepareIndex(results, name);
        HttpServletRequest httpservletrequest = (HttpServletRequest)pageContext.getRequest();
        PortletRenderRequest portletrenderrequest = (PortletRenderRequest)httpservletrequest.getAttribute("oracle.portal.PortletRenderRequest");
        String s = PortletRendererUtil.portletParameter(portletrenderrequest, property);
		results.append(s);
		results.append("\"");
		if (accesskey != null) {
			results.append(" accesskey=\"");
			results.append(accesskey);
			results.append("\"");
		}
		if (multiple != null)
			results.append(" multiple=\"multiple\"");
		if (size != null) {
			results.append(" size=\"");
			results.append(size);
			results.append("\"");
		}
		if (tabindex != null) {
			results.append(" tabindex=\"");
			results.append(tabindex);
			results.append("\"");
		}
		results.append(prepareEventHandlers());
		results.append(prepareStyles());
		results.append(">");
		return results.toString();
	}
}
