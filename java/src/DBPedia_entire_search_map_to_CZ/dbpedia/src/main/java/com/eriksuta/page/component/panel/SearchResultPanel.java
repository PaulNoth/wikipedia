package com.eriksuta.page.component.panel;

import com.eriksuta.data.search.SearchResultType;
import com.eriksuta.page.SearchOptions;
import com.eriksuta.page.component.model.LoadableModel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 *  @author Erik Suta
 * */
public class SearchResultPanel extends Panel {

    private static final String ID_CLASSIC = "classic";
    private static final String ID_CLASSIC_CONTAINER = "classicContainer";
    private static final String ID_XML_CONTAINER = "xmlContainer";
    private static final String ID_JSON_CONTAINER = "jsonContainer";
    private static final String ID_RAW_CONTAINER = "rawContainer";
    private static final String ID_XML = "xml";
    private static final String ID_JSON = "json";
    private static final String ID_RAW = "raw";
    private static final String ID_RESULT = "result";

    private IModel<SearchResultType> model;
    private SearchOptions options;

    public SearchResultPanel(String id, final SearchResultType result){
        super(id);

        model = new LoadableModel<SearchResultType>(false) {

            @Override
            protected SearchResultType load() {
                if(result != null){
                    return result;
                } else {
                    return new SearchResultType();
                }
            }
        };

        initLayout();
    }

    public IModel<SearchResultType> getModel(){
        return model;
    }

    public void updateModel(SearchResultType result, AjaxRequestTarget target, SearchOptions options){
        model.setObject(result);
        this.options = options;

        showClassicPerformed(target);

        target.add(getResultPanel());
    }

    private void initLayout(){
        WebMarkupContainer classicContainer = new WebMarkupContainer(ID_CLASSIC_CONTAINER);
        classicContainer.setOutputMarkupId(true);
        add(classicContainer);

        WebMarkupContainer xmlContainer = new WebMarkupContainer(ID_XML_CONTAINER);
        xmlContainer.setOutputMarkupId(true);
        add(xmlContainer);

        WebMarkupContainer jsonContainer = new WebMarkupContainer(ID_JSON_CONTAINER);
        jsonContainer.setOutputMarkupId(true);
        add(jsonContainer);

        WebMarkupContainer rawContainer = new WebMarkupContainer(ID_RAW_CONTAINER);
        rawContainer.setOutputMarkupId(true);
        add(rawContainer);

        AjaxLink classic = new AjaxLink(ID_CLASSIC) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                showClassicPerformed(target);
            }
        };
        classicContainer.add(classic);

        AjaxLink xml = new AjaxLink(ID_XML) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                showXmlPerformed(target);
            }
        };
        xmlContainer.add(xml);

        AjaxLink json = new AjaxLink(ID_JSON) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                showJsonPerformed(target);
            }
        };
        jsonContainer.add(json);

        AjaxLink raw = new AjaxLink(ID_RAW) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                showRawPerformed(target);
            }
        };
        rawContainer.add(raw);

        WebMarkupContainer resultPanel = new SearchResultClassicPanel(ID_RESULT, model.getObject(), options);
        resultPanel.setOutputMarkupId(true);
        resultPanel.setOutputMarkupPlaceholderTag(true);
        add(resultPanel);
    }

    private WebMarkupContainer getResultPanel(){
        return (WebMarkupContainer) get(ID_RESULT);
    }

    private void showClassicPerformed(AjaxRequestTarget target){
        getResultPanel().replaceWith(new SearchResultClassicPanel(ID_RESULT, model.getObject(), options));
        get(ID_CLASSIC_CONTAINER).add(new AttributeModifier("class", "active"));
        get(ID_XML_CONTAINER).add(new AttributeModifier("class", ""));
        get(ID_JSON_CONTAINER).add(new AttributeModifier("class", ""));
        get(ID_RAW_CONTAINER).add(new AttributeModifier("class", ""));

        target.add(this);
    }

    private void showXmlPerformed(AjaxRequestTarget target){
        getResultPanel().replaceWith(new SearchResultTextAreaPanel(ID_RESULT, model.getObject(), SearchResultTextAreaPanel.ResultOutputType.XML));
        get(ID_CLASSIC_CONTAINER).add(new AttributeModifier("class", ""));
        get(ID_XML_CONTAINER).add(new AttributeModifier("class", "active"));
        get(ID_JSON_CONTAINER).add(new AttributeModifier("class", ""));
        get(ID_RAW_CONTAINER).add(new AttributeModifier("class", ""));

        target.add(this);
    }

    private void showJsonPerformed(AjaxRequestTarget target){
        getResultPanel().replaceWith(new SearchResultTextAreaPanel(ID_RESULT, model.getObject(), SearchResultTextAreaPanel.ResultOutputType.JSON));
        get(ID_CLASSIC_CONTAINER).add(new AttributeModifier("class", ""));
        get(ID_XML_CONTAINER).add(new AttributeModifier("class", ""));
        get(ID_JSON_CONTAINER).add(new AttributeModifier("class", "active"));
        get(ID_RAW_CONTAINER).add(new AttributeModifier("class", ""));

        target.add(this);
    }

    private void showRawPerformed(AjaxRequestTarget target){
        getResultPanel().replaceWith(new SearchResultTextAreaPanel(ID_RESULT, model.getObject(), SearchResultTextAreaPanel.ResultOutputType.RAW));
        get(ID_CLASSIC_CONTAINER).add(new AttributeModifier("class", ""));
        get(ID_XML_CONTAINER).add(new AttributeModifier("class", ""));
        get(ID_JSON_CONTAINER).add(new AttributeModifier("class", ""));
        get(ID_RAW_CONTAINER).add(new AttributeModifier("class", "active"));

        target.add(this);
    }
}
