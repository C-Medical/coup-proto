package jp.co.medicoup.base.front.provide;

import java.util.Collections;
import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class HtmlFragment {

    public static HtmlFragment create(String templateName) {
        return new HtmlFragment(templateName, null);
    }

    public static HtmlFragment create(String templateName, Map<String, ?> variables) {
        return new HtmlFragment(templateName, variables);
    }

    private static final Map<String, ?> EMPTY_MAP = Collections.emptyMap();

    private final String templateName;
    private final Map<String, ?> variables;

    private HtmlFragment(String templateName, Map<String, ?> variables) {
        this.templateName = templateName;
        this.variables = variables != null ? Collections.unmodifiableMap(variables) : EMPTY_MAP;
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, ?> getVariables() {
        return variables;
    }

}
