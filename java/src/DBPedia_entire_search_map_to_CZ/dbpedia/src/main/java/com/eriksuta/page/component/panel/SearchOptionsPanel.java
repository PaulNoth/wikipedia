package com.eriksuta.page.component.panel;

import com.eriksuta.page.SearchOptions;
import com.eriksuta.page.component.model.LoadableModel;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *  @author Erik Suta
 * */
public class SearchOptionsPanel extends Panel {

//    public static final String ID_MATCH_CASE = "matchCase";
//    public static final String ID_WHOLE_WORD = "wholeWord";
//    public static final String ID_REGEX = "asRegex";
    public static final String ID_LINKS = "links";
    public static final String ID_ABSTRACTS = "abstracts";
    public static final String ID_CATEGORIES = "categories";
    public static final String ID_INFO_PROPERTIES = "infoboxProperties";
    public static final String ID_MAPPING_CZ = "mappingCz";

    private IModel<SearchOptions> model;

    public SearchOptionsPanel(String id){
        super(id);

        model = new LoadableModel<SearchOptions>(false) {

            @Override
            protected SearchOptions load() {
                return new SearchOptions();
            }
        };

        initLayout();
    }

    public IModel<SearchOptions> getModel(){
        return model;
    }

    private void initLayout(){
//        CheckBox matchCase = new CheckBox(ID_MATCH_CASE, new PropertyModel<Boolean>(model, SearchOptions.F_MATCH_CASE));
//        add(matchCase);
//
//        CheckBox wholeWord = new CheckBox(ID_WHOLE_WORD, new PropertyModel<Boolean>(model, SearchOptions.F_WHOLE_WORD));
//        add(wholeWord);
//
//        CheckBox asRegex = new CheckBox(ID_REGEX, new PropertyModel<Boolean>(model, SearchOptions.F_REGEX));
//        add(asRegex);

        CheckBox links = new CheckBox(ID_LINKS, new PropertyModel<Boolean>(model, SearchOptions.F_LINKS));
        add(links);

        CheckBox abstracts = new CheckBox(ID_ABSTRACTS, new PropertyModel<Boolean>(model, SearchOptions.F_ABSTRACTS));
        add(abstracts);

        CheckBox categories = new CheckBox(ID_CATEGORIES, new PropertyModel<Boolean>(model, SearchOptions.F_CATEGORIES));
        add(categories);

        CheckBox infoboxProperties = new CheckBox(ID_INFO_PROPERTIES, new PropertyModel<Boolean>(model, SearchOptions.F_INFO_PROPERTIES));
        add(infoboxProperties);

        CheckBox linkMappingCz = new CheckBox(ID_MAPPING_CZ, new PropertyModel<Boolean>(model, SearchOptions.F_LINK_MAPPING_CZ));
        add(linkMappingCz);
    }
}
