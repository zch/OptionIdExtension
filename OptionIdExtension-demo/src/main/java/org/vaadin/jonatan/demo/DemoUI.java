package org.vaadin.jonatan.demo;

import com.vaadin.ui.OptionGroup;
import org.vaadin.jonatan.OptionIdExtension;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("OptionIdExtension Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "org.vaadin.jonatan.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        OptionGroup og = new OptionGroup();

        og.addItem("First");
        og.addItem("Second");

        OptionIdExtension oie = new OptionIdExtension();
        oie.extend(og);
        oie.setIdForOption("First", "foo");
        oie.setIdForOption("Second", "bar");

        // Show it in the middle of the screen
        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.addComponent(og);
        layout.setComponentAlignment(og, Alignment.MIDDLE_CENTER);
        setContent(layout);
    }

}
