package org.vaadin.jonatan.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import org.vaadin.jonatan.OptionIdExtension;

import com.google.gwt.user.client.ui.CheckBox;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VOptionGroup;
import com.vaadin.client.ui.optiongroup.OptionGroupConnector;
import com.vaadin.shared.ui.Connect;

@Connect(OptionIdExtension.class)
public class OptionIdExtensionConnector extends AbstractExtensionConnector {

    private OptionGroupConnector optionGroupConnector;

    @Override
    protected void extend(ServerConnector serverConnector) {
        this.optionGroupConnector = (OptionGroupConnector) serverConnector;
    }

    // We must implement getState() to cast to correct type
    @Override
    public OptionIdExtensionState getState() {
        return (OptionIdExtensionState) super.getState();
    }

    private native Map<CheckBox, String> getOptionsToKeys(VOptionGroup from) /*-{
        return from.@com.vaadin.client.ui.VOptionGroup::optionsToKeys;
    }-*/;

    // Whenever the state changes in the server-side, this method is called
    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        // Deferred to make sure our VOptionGroup has been rendered first.
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                Map<String, CheckBox> keysToOptionsMap = new HashMap<String, CheckBox>();
                for (Map.Entry<CheckBox, String> entry : getOptionsToKeys(
                        optionGroupConnector.getWidget()).entrySet()) {
                    keysToOptionsMap.put(entry.getValue(), entry.getKey());
                }

                for (Map.Entry<String, String> entry : getState().optionIds.entrySet()) {
                    Object key = entry.getKey();
                    String id = entry.getValue();
                    keysToOptionsMap.get(key).getElement().setId(id);
                }
            }
        });
    }

}
