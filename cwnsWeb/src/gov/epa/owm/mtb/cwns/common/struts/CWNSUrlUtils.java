/* Copyright (c) 2001, 2005, Oracle. All rights reserved.  */

package gov.epa.owm.mtb.cwns.common.struts;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import javax.servlet.http.HttpServletResponse;

import oracle.portal.log.LogManager;
import oracle.portal.provider.v2.PortletException;
import oracle.portal.provider.v2.render.PortletRenderer;
import oracle.portal.provider.v2.render.PortletRendererUtil;
import oracle.portal.provider.v2.render.PortletRenderRequest;
import oracle.portal.provider.v2.render.RenderContext;
import oracle.portal.provider.v2.http.HttpCommonConstants;
import oracle.portal.utils.NameValue;
import oracle.webdb.utils.SimpleStringBuffer;
import oracle.webdb.utils.HTTPUtils;
import oracle.webdb.utils.HmacAuthenticater;
import oracle.portal.utils.xml.v2.XMLUtil;
import oracle.portal.provider.v2.url.UrlUtils;

/**
 * Contains utility methods and constants for urls.
 * 
 * There are utility methods in this class that are suited to rendering HTML,
 * and others that are suited to rendering Oracle mobile XML (MXML) i.e. the
 * markup used to satisfy requests for mime-type of
 * oracle.portal.provider.v2.render.PortletRenderer.TYPE_MOBILE.
 * 
 * Those methods that are safe for use in a MXML context are explitly labeled as
 * such.
 */
public class CWNSUrlUtils extends UrlUtils {
	private static String emitJavascriptHiddenFields(PortletRenderRequest pr,
			int linkType, String formName) throws IllegalArgumentException {
		SimpleStringBuffer newJavascript = new SimpleStringBuffer(100);
		String portletRef = getPortletPageReference(pr);

		newJavascript.append("<SCRIPT>p");
		newJavascript.append(portletRef);
		newJavascript.append(".setFormAction(");

		switch (linkType) {
		case PAGE_LINK:
			newJavascript.append("'p'");
			break;
		case EVENT_LINK:
			newJavascript.append("'e'");
			break;
		case DESIGN_LINK:
			newJavascript.append("'d'");
			break;
		case BACK_LINK:
			newJavascript.append("'b'");
			break;
		default:
			throw new IllegalArgumentException("Invalid linkType: " + linkType);
		}

		newJavascript.append(", '");
		newJavascript.append(formName);
		newJavascript.append("');</SCRIPT>");

		return newJavascript.toString();
	}

	/**
	 * Enables the parameters in the query string to be POSTED when the form is
	 * submited. This code generates hidden parameters when rewriteUrls is false
	 * or by setting the action value of the form to be the whole url when
	 * rewriteUrls is true.
	 * 
	 * This function can only be used if there are only one form on a portlet.
	 * 
	 * This utility method either generates the hidden field tags from this that
	 * will alter the forms action to be the URL and returns the javascript as a
	 * String.
	 * 
	 * @param pr
	 *            the request
	 * @param linkType
	 *            the link to use. Constants representing these types are
	 *            defined in UrlUtils.
	 */
	public static String htmlFormHiddenFields(PortletRenderRequest pr,
			int linkType) throws IllegalArgumentException {
		String formName = htmlFormName(pr, null);
		return htmlFormHiddenFields(pr, linkType, formName);
	}

	/**
	 * Enables the parameters in the query string to be POSTED when the form is
	 * submited. This code generates hidden parameters when rewriteUrls is false
	 * or by setting the action value of the form to be the whole url when
	 * rewriteUrls is true.
	 * 
	 * This utility method either generates the hidden field tags from this URL
	 * and returns them as a String. Or generates a javascript fragment that
	 * will alter the forms action to be the URL and returns the javascript as a
	 * String.
	 * 
	 * @param pr
	 *            the request
	 * @param linkType
	 *            the link to use. Constants representing these types are
	 *            defined in PortletRendererUtil.
	 * @param formName
	 *            unique name of a form within a page. Use htmlFormName() to
	 *            generate this name.
	 */
	public static String htmlFormHiddenFields(PortletRenderRequest pr,
			int linkType, String formName) throws IllegalArgumentException {
		LogManager.debug("Enter htmlFormHiddenFields");

		RenderContext renderContext = pr.getRenderContext();
		if (renderContext.isRewriteUrls()) {
			switch (linkType) {
			case REQUEST_LINK:
				return htmlFormHiddenFields(pr, pr.getRequestURL());
			case LOGIN_LINK:
				return htmlFormHiddenFields(pr, pr.getRenderContext()
						.getLoginServerURL());
			case PAGE_LINK:
			case EVENT_LINK:
			case DESIGN_LINK:
			case BACK_LINK:
				return emitJavascriptHiddenFields(pr, linkType, formName);
			default:
				throw new IllegalArgumentException("Invalid linkType: "
						+ linkType);
			}
		} else {
			switch (linkType) {
			case PAGE_LINK:
				return htmlFormHiddenFields(pr, pr.getRenderContext()
						.getPageURL());
			case EVENT_LINK:
				return htmlFormHiddenFields(pr, pr.getRenderContext()
						.getEventURL());
			case DESIGN_LINK:
				return htmlFormHiddenFields(pr, pr.getRenderContext()
						.getDesignURL());
			case REQUEST_LINK:
				return htmlFormHiddenFields(pr, pr.getRequestURL());
			case LOGIN_LINK:
				return htmlFormHiddenFields(pr, pr.getRenderContext()
						.getLoginServerURL());
			case BACK_LINK:
				return htmlFormHiddenFields(pr, pr.getRenderContext()
						.getBackURL());
			default:
				throw new IllegalArgumentException("Invalid linkType: "
						+ linkType);
			}
		}
	}

	/**
	 * Encodes the parameters in the query string of the passed URL as hidden
	 * form fields. When "linking" back to the portlet via a form, the URI
	 * parameters must be added as hidden fields. These parameters are found in
	 * the querystring of your URI. This utility method generates the hidden
	 * field tags from this URL and returns them as a String.
	 * 
	 * @param ref
	 *            the URI
	 */
	public static String htmlFormHiddenFields(PortletRenderRequest pr,
			String ref) {
		String hidden = "";

		if (ref != null) {

			int last = ref.indexOf('?');

			if (last != -1) {

				ref = removeForeignPortletFieldsFromUrl(pr, ref);

				hidden = emitHiddenFields(ref.substring(last + 1));
			}
		}

		return hidden;
	}

	private static String emitHiddenFields(String queryString) {
		String hidden = "";
		StringTokenizer qsST = new StringTokenizer(queryString, "&");

		while (qsST.hasMoreTokens()) {
			String param = qsST.nextToken();
			int i = param.indexOf('=');
			hidden = hidden
					+ emitHiddenField(param.substring(0, i), param
							.substring(i + 1));
		}

		return hidden;
	}

	public static String removeForeignPortletFieldsFromUrl(
			PortletRenderRequest pr, String urlString) {
		if (urlString == null || urlString.trim().length() == 0) {
			return urlString;
		}

		int last = urlString.indexOf('?');

		if (last == -1) {
			return urlString;
		}

		String resultStr = urlString.substring(0, last + 1);

		StringTokenizer qsST = new StringTokenizer(urlString
				.substring(last + 1), "&");

		while (qsST.hasMoreTokens()) {
			String param = qsST.nextToken();

			if (param.startsWith(PortletRendererUtil.PORTLET_PARAMETER_PREFIX)) {
				if (param.startsWith(PortletRendererUtil.portletParameter(pr,
						""))) {
					resultStr = resultStr + param + "&";
				}
			} else {
				resultStr = resultStr + param + "&";
			}
		}

		return resultStr.substring(0, resultStr.length() - 1);
	}

	/**
	 * Get the portlet Page Reference.
	 * 
	 * The portlet page reference is sent by the PPE to the provider for all
	 * display modes other than REVIEW mode. This function will return the
	 * portlet page reference or an empty string if in the REVIEW mode. The
	 * underlying function call raises an exception if the portlet page is not
	 * set in the RenderComtext.
	 * 
	 * @param pr
	 *            render request
	 * @return the portlet page reference or the empty string in PREVIEW mode
	 */
	private static String getPortletPageReference(PortletRenderRequest pr) {
		String pageRef = "";
		RenderContext renderContext = pr.getRenderContext();
		if (renderContext.isRewriteUrls()
				&& renderContext.getMode() != PortletRenderer.MODE_PREVIEW) {
			// In PREVIEW MODE the PPE does not send a PageReference.
			// it is not yet on a page.
			pageRef = renderContext.getPortletPageReference();
		} else if (renderContext.getMode() != PortletRenderer.MODE_PREVIEW) {
			// the request may come from a 9.0.2 portal or a pre 9.0.2 portal
			// if it is from a pre 9.0.2 portal then the call to get
			// the PortletPageReference will raise an exception
			try {
				pageRef = renderContext.getPortletPageReference();
			} catch (java.lang.IllegalStateException e) {
				pageRef = pr.getPortletInstance().getInstanceName();
			}
		} else {
			pageRef = pr.getPortletInstance().getInstanceName();
		}

		return pageRef;
	}

	/**
	 * Generic utility to append parameters to a link.
	 * 
	 * This method is suitable for use in a MXML context, but only if URL
	 * rewriting is not in operation - in this case no error will be issued.
	 * 
	 * @param pr
	 *            render request
	 * @param linkType
	 *            the link to use. Constants representing these types are
	 *            defined in UrlUtils.
	 * @param params
	 *            array of NameValue objects representing the parameters
	 * @param encodeParams
	 *            flag indicating whether parameter names and values should be
	 *            encoded
	 * @param replaceParams
	 *            flag indicating whether parameters contained in the params
	 *            array should replace existing parameters of the same name.
	 *            true = replace or overwrite existing parameters. false =
	 *            simply add parameters
	 * 
	 * @return original link with additional parameters or a javascript call to
	 *         the original link with additional parameters.
	 */
	public static String constructLink(PortletRenderRequest pr, int linkType,
			NameValue[] params, boolean encodeParams, boolean replaceParams)
			throws IllegalArgumentException, IOException {
		RenderContext renderContext = pr.getRenderContext();
		if (renderContext.isRewriteUrls()
				&& (linkType == PAGE_LINK || linkType == EVENT_LINK
						|| linkType == DESIGN_LINK || linkType == BACK_LINK)) {
			SimpleStringBuffer link = new SimpleStringBuffer(200);
			link.append("javascript:").append(
					constructJavascriptLink(pr, linkType, params, encodeParams,
							replaceParams));
			return link.toString();
		} else {
			switch (linkType) {
			case PAGE_LINK:
				return constructLink(pr, removeForeignPortletFieldsFromUrl(pr,
						pr.getRenderContext().getPageURL()), params,
						encodeParams, replaceParams);
			case EVENT_LINK:
				return constructLink(pr, removeForeignPortletFieldsFromUrl(pr,
						pr.getRenderContext().getEventURL()), params,
						encodeParams, replaceParams);
			case DESIGN_LINK:
				return constructLink(pr, removeForeignPortletFieldsFromUrl(pr,
						pr.getRenderContext().getDesignURL()), params,
						encodeParams, replaceParams);
			case REQUEST_LINK:
				return constructLink(pr, removeForeignPortletFieldsFromUrl(pr,
						pr.getRequestURL()), params, encodeParams,
						replaceParams);
			case LOGIN_LINK:
				return constructLink(pr, removeForeignPortletFieldsFromUrl(pr,
						pr.getRenderContext().getLoginServerURL()), params,
						encodeParams, replaceParams);
			case BACK_LINK:
				return constructLink(pr, removeForeignPortletFieldsFromUrl(pr,
						pr.getRenderContext().getBackURL()), params,
						encodeParams, replaceParams);
			default:
				throw new IllegalArgumentException("Invalid linkType: "
						+ linkType);
			}
		}
	}
}
