package gov.epa.owm.mtb.cwns.common.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import oracle.portal.provider.v2.render.PortletRenderRequest;
import oracle.portal.provider.v2.render.PortletRendererUtil;

import org.apache.struts.taglib.html.BaseFieldTag;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

public class PDKBaseFieldTag extends BaseFieldTag {
	public int doStartTag() throws JspException {
		StringBuffer results = new StringBuffer("<input type=\"");
		results.append(type);
		results.append("\" name=\"");
		if (indexed)
			prepareIndex(results, name);
		HttpServletRequest httpservletrequest = (HttpServletRequest) pageContext
				.getRequest();
		PortletRenderRequest portletrenderrequest = (PortletRenderRequest) httpservletrequest
				.getAttribute("oracle.portal.PortletRenderRequest");
		String s = PortletRendererUtil.portletParameter(portletrenderrequest,
				property);
		results.append(s);
		results.append("\"");
		if (accesskey != null) {
			results.append(" accesskey=\"");
			results.append(accesskey);
			results.append("\"");
		}
		if (accept != null) {
			results.append(" accept=\"");
			results.append(accept);
			results.append("\"");
		}
		if (maxlength != null) {
			results.append(" maxlength=\"");
			results.append(maxlength);
			results.append("\"");
		}
		if (cols != null) {
			results.append(" size=\"");
			results.append(cols);
			results.append("\"");
		}
		if (tabindex != null) {
			results.append(" tabindex=\"");
			results.append(tabindex);
			results.append("\"");
		}
		results.append(" value=\"");
		if (this.value != null)
			results.append(ResponseUtils.filter(this.value));
		else if (redisplay || !"password".equals(type)) {
			Object value = RequestUtils.lookup(pageContext, name, property,
					null);
			if (value == null)
				value = "";
			results.append(ResponseUtils.filter(value.toString()));
		}
		results.append("\"");
		results.append(prepareEventHandlers());
		results.append(prepareStyles());
		results.append(getElementClose());
		ResponseUtils.write(pageContext, results.toString());
		return 2;
	}

}
