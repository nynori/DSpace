/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.app.webui.jsptag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.ResourcePolicy;
import org.dspace.core.Context;

/**
 * Tag to display embargo settings
 *
 * @author Keiji Suzuki
 * @version $Revision$
 */
public class PoliciesListTag extends TagSupport
{
	/** log4j category */
    private static Logger log = Logger.getLogger(PoliciesListTag.class);

    /** Groups to make options list */
    private transient List<ResourcePolicy> policies = null;

    public PoliciesListTag()
    {
        super();
    }

    public int doStartTag() throws JspException
    {
        String label_name = LocaleSupport.getLocalizedMessage(pageContext, "org.dspace.app.webui.jsptag.policies-list.label_name");
        String label_action = LocaleSupport.getLocalizedMessage(pageContext, "org.dspace.app.webui.jsptag.policies-list.label_action");
        String label_group = LocaleSupport.getLocalizedMessage(pageContext, "org.dspace.app.webui.jsptag.policies-list.label_group");
        String label_sdate = LocaleSupport.getLocalizedMessage(pageContext, "org.dspace.app.webui.jsptag.policies-list.label_sdate");
        String label_edate = LocaleSupport.getLocalizedMessage(pageContext, "org.dspace.app.webui.jsptag.policies-list.label_edate");

        JspWriter out = pageContext.getOut();
        StringBuffer sb = new StringBuffer();

        try
        {
            HttpServletRequest hrq = (HttpServletRequest) pageContext.getRequest();
            Context context = UIUtil.obtainContext(hrq);

            sb.append("<center>\n");
            sb.append("<table class=\"miscTable\">\n");
            sb.append("<table class=\"miscTable\">\n");
            sb.append("<tr>\n");
            sb.append("<th class=\"accessHeadOdd\">").append(label_name).append("</th>\n");
            sb.append("<th class=\"accessHeadEven\">").append(label_action).append("</th>\n");
            sb.append("<th class=\"accessHeadOdd\">").append(label_group).append("</th>\n");
            sb.append("<th class=\"accessHeadEven\">").append(label_sdate).append("</th>\n");
            sb.append("<th class=\"accessHeadOdd\">").append(label_edate).append("</th>\n");
            sb.append("<th class=\"accessButton\">&nbsp;</th>\n");
            sb.append("</tr>\n");

            if (policies != null && policies.size() > 0)
            {
                String column1 = "Even";
                String column2 = "Odd";
                for (ResourcePolicy policy : policies)
                {
                    column1 = (column1.equals("Even") ? "Odd" : "Even");
                    column2 = (column2.equals("Even") ? "Odd" : "Even");
                    String rpName = (policy.getRpName() == null ? "" : policy.getRpName());
                    String startDate = (policy.getStartDate() == null ? "" : DateFormatUtils.format(policy.getStartDate(), "yyyy-MM-dd"));
                    String endDate = (policy.getEndDate() == null ? "" : DateFormatUtils.format(policy.getEndDate(), "yyyy-MM-dd"));


                    sb.append("<tr>\n");
                    sb.append("<td class=\"access").append(column1).append("\">").append(rpName).append("</td>\n");
                    sb.append("<td class=\"access").append(column2).append("\">").append(policy.getActionText()).append("</td>\n");
                    sb.append("<td class=\"access").append(column1).append("\">").append(policy.getGroup().getName()).append("</td>\n");
                    sb.append("<td class=\"access").append(column2).append("\">").append(startDate).append("</td>\n");
                    sb.append("<td class=\"access").append(column1).append("\">").append(endDate).append("</td>\n");
                    sb.append("<td class=\"accessButton\">\n");
                    sb.append("<input name=\"submit_edit_edit_policies_").append(policy.getID()).append("\" type=\"submit\" value=\"Edit\" /> <input name=\"submit_delete_edit_policies_").append(policy.getID()).append("\" type=\"submit\" value=\"Remove\" />\n");
                    sb.append("</td></tr>\n");
                }
            }
            sb.append("</table>\n");
            sb.append("</center>\n");

            out.println(sb.toString());
        }
        catch (IOException ie)
        {
            throw new JspException(ie);
        }
        catch (SQLException e)
        {
        	throw new JspException(e);
        }

        return SKIP_BODY;
    }

    /**
     * Get the policies to list
     *
     * @return the policies
     */
    public List<ResourcePolicy> getPolicies()
    {
        return policies;
    }

    /**
     * Set the policies to list
     *
     * @param itemsIn
     *            the policies
     */
    public void setPolicies(List<ResourcePolicy> policies)
    {
        this.policies = policies;
    }


    public void release()
    {
        policies = null;
    }

}
