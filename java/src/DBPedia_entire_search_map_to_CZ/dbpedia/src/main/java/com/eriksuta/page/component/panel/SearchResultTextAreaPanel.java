package com.eriksuta.page.component.panel;

import com.eriksuta.data.search.InfoboxPropertyType;
import com.eriksuta.data.search.SearchResultType;
import com.eriksuta.page.component.model.LoadableModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

/**
 *  @author Erik Suta
 * */
public class SearchResultTextAreaPanel extends Panel {

    public enum ResultOutputType{
        XML,
        JSON,
        RAW
    }

    private static final String ID_EDITOR = "editor";

    private IModel<SearchResultType> model;
    private ResultOutputType outputType = ResultOutputType.RAW;

    public SearchResultTextAreaPanel(String id, final SearchResultType result, ResultOutputType outputType){
        super(id);

        model = new LoadableModel<SearchResultType>() {

            @Override
            protected SearchResultType load() {
                return result != null ? result : null;
            }
        };
        this.outputType = outputType;

        initLayout();
    }

    public void updateModel(SearchResultType result){
        //TODO
    }

    private void initLayout(){
        TextArea editor = new TextArea<String>(ID_EDITOR, prepareSearchResultModel());
        editor.setEnabled(false);
        editor.setOutputMarkupId(true);
        add(editor);
    }

    private IModel<String> prepareSearchResultModel(){
        return new AbstractReadOnlyModel<String>() {

            @Override
            public String getObject() {
                if(outputType.equals(ResultOutputType.XML)){
                    return prepareResultXML();
                } else if(outputType.equals(ResultOutputType.JSON)){
                    return prepareResultJSON();
                } else {
                    return prepareResultRaw();
                }
            }
        };
    }

    private String prepareResultXML(){
        XStream xStream = new XStream(new StaxDriver());
        xStream.alias("result", SearchResultType.class);
        xStream.alias("property", InfoboxPropertyType.class);

        if(model != null && model.getObject() != null){
            String uglyXml = xStream.toXML(model.getObject());

            try {
                Document document = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder()
                        .parse(new InputSource(new ByteArrayInputStream(uglyXml.getBytes("utf-8"))));

                XPath xPath = XPathFactory.newInstance().newXPath();
                NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
                        document,
                        XPathConstants.NODESET);

                for (int i = 0; i < nodeList.getLength(); ++i) {
                    Node node = nodeList.item(i);
                    node.getParentNode().removeChild(node);
                }

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                StringWriter stringWriter = new StringWriter();
                StreamResult streamResult = new StreamResult(stringWriter);

                transformer.transform(new DOMSource(document), streamResult);

                return  stringWriter.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "Result is NOT valid";
    }

    private String prepareResultJSON(){
        Gson gson = new Gson();

        if(model != null && model.getObject() != null){
            String uglyJson = gson.toJson(model.getObject());

            gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(uglyJson);

            return gson.toJson(je);
        }

        return "Result is NOT valid";
    }

    private String prepareResultRaw(){
        if(model != null && model.getObject() != null){
            return model.getObject().toString();
        }

        return "Result is NOT valid";
    }

}
