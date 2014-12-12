package com.eriksuta.page;

import com.eriksuta.search.SearchService;
import com.eriksuta.search.SearchServiceImpl;
import com.eriksuta.data.search.SearchResultType;
import com.eriksuta.page.component.panel.SearchOptionsPanel;
import com.eriksuta.page.component.panel.SearchResultPanel;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import com.eriksuta.page.component.behavior.VisibleEnableBehavior;

/**
 *  @author Erik Suta
 * */
public class SearchPage extends WebPage {

    private static Logger LOGGER = Logger.getLogger(SearchPage.class);

    /**
     *  A SearchService instance. This is used for all searches.
     * */
    private transient SearchService searchService;

    private static final String ID_FEEDBACK = "feedback";
    private static final String ID_MAIN_FORM = "mainForm";
    private static final String ID_SEARCH_TEXT = "searchText";
    private static final String ID_SEARCH_BUTTON = "searchButton";
    private static final String ID_SEARCH_OPTIONS_LINK = "searchOptionsLink";
    private static final String ID_SEARCH_OPTIONS_TEXT = "searchOptionsText";
    private static final String ID_SEARCH_OPTIONS_PANEL = "searchOptions";
    private static final String ID_SEARCH_RESULT = "searchResult";

    private String searchText;
    private boolean searchOptionsVisible = false;
    private SearchResultType result;

	public SearchPage(final PageParameters parameters) {
		super(parameters);

        searchService = SearchServiceImpl.getInstance();

        initLayout();
    }

    private void initLayout(){
        Form form = new Form(ID_MAIN_FORM);
        form.setOutputMarkupId(true);
        add(form);

        FeedbackPanel feedback = new FeedbackPanel(ID_FEEDBACK);
        feedback.setOutputMarkupId(true);
        form.add(feedback);

        TextField searchField = new TextField<String>(ID_SEARCH_TEXT, new PropertyModel<String>(this, "searchText"));
        form.add(searchField);

        AjaxSubmitLink searchButton = new AjaxSubmitLink(ID_SEARCH_BUTTON) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if(searchText == null || searchText.isEmpty() || searchText.length() <= 3){
                    error("Short or non-existing search term. Please enter search term and continue.");
                    target.add(getFeedbackPanel());
                    return;
                }

                searchPerformed(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(getFeedbackPanel());
            }
        };
        form.add(searchButton);

        AjaxLink searchOptionsLink = new AjaxLink(ID_SEARCH_OPTIONS_LINK) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                showSearchOptionsPerformed(target);
            }
        };
        searchOptionsLink.add(new Label(ID_SEARCH_OPTIONS_TEXT, new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                if(searchOptionsVisible){
                    return "Hide Search Options";
                } else {
                    return "Show Search Options";
                }
            }
        }));
        searchOptionsLink.setOutputMarkupId(true);
        form.add(searchOptionsLink);

        WebMarkupContainer searchOptionsPanel = new SearchOptionsPanel(ID_SEARCH_OPTIONS_PANEL);
        searchOptionsPanel.setOutputMarkupId(true);
        searchOptionsPanel.setOutputMarkupPlaceholderTag(true);
        searchOptionsPanel.add(new VisibleEnableBehavior() {

            @Override
            public boolean isVisible() {
                return searchOptionsVisible;
            }
        });
        form.add(searchOptionsPanel);

        WebMarkupContainer searchResultPanel = new SearchResultPanel(ID_SEARCH_RESULT, result);
        searchResultPanel.setOutputMarkupId(true);
        searchResultPanel.setOutputMarkupPlaceholderTag(true);
        searchResultPanel.add(new VisibleEnableBehavior(){

            @Override
            public boolean isVisible() {
                return result != null;
            }
        });
        add(searchResultPanel);
    }

    private FeedbackPanel getFeedbackPanel(){
        return (FeedbackPanel) get(ID_MAIN_FORM + ":" + ID_FEEDBACK);
    }

    private SearchOptionsPanel getSearchOptionsContainer(){
        return (SearchOptionsPanel) get(ID_MAIN_FORM + ":" + ID_SEARCH_OPTIONS_PANEL);
    }

    private Component getSearchOptionsLink(){
        return get(ID_MAIN_FORM + ":" + ID_SEARCH_OPTIONS_LINK);
    }

    private SearchResultPanel getSearchResultPanel(){
        return (SearchResultPanel) get(ID_SEARCH_RESULT);
    }

    private void showSearchOptionsPerformed(AjaxRequestTarget target){
        searchOptionsVisible = !searchOptionsVisible;

        target.add(getSearchOptionsContainer(), getSearchOptionsLink());
    }

    private void searchPerformed(AjaxRequestTarget target){
        if(getSearchOptionsContainer() == null || getSearchOptionsContainer().getModel() == null ||
                getSearchOptionsContainer().getModel().getObject() == null){

            LOGGER.error("Search operation could not be performed. Search options not found. Try again later, please.");
            error("Search operation could not be performed. Search options not found. Try again later, please.");
            target.add(getFeedbackPanel());
            return;
        }

        SearchOptions options = getSearchOptionsContainer().getModel().getObject();
        SearchResultType result = searchService.search(searchText, options);

        if(result != null){
            this.result = result;
            getSearchResultPanel().updateModel(result, target, options);
        } else {
            LOGGER.error("Search operation could not be performed. Something went terribly wrong. Try again later, please.");
            error("Search operation could not be performed. Something went terribly wrong. Try again later, please.");
        }

        target.add(getFeedbackPanel(),
                getSearchOptionsLink(),
                getSearchOptionsContainer(),
                getSearchResultPanel());
    }
}
