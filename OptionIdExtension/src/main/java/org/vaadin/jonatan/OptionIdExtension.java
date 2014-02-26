package org.vaadin.jonatan;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.vaadin.jonatan.client.OptionIdExtensionState;

import com.vaadin.server.AbstractExtension;
import com.vaadin.server.KeyMapper;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.OptionGroup;

public class OptionIdExtension extends AbstractExtension {

    private OptionGroup target;
    private HashMap<Object, String> itemIdToIdMap = new HashMap<Object, String>();

    public void extend(OptionGroup target) {
        super.extend(target);
        this.target = target;
    }

    private Object getKeyForOption(Object optionId) {
        try {
            Field itemIdMapper = AbstractSelect.class.getDeclaredField("itemIdMapper");
            itemIdMapper.setAccessible(true);
            KeyMapper<Object> actualMapper = (KeyMapper<Object>) itemIdMapper
                    .get(target);
            return actualMapper.key(optionId.toString());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Couldn't map from itemId to key", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't map from itemId to key", e);
        }
    }

    @Override
    public OptionIdExtensionState getState() {
        return (OptionIdExtensionState) super.getState();
    }

    public void setIdForOption(Object itemId, String id) {
        itemIdToIdMap.put(itemId, id);
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        for (Map.Entry<Object, String> entry : itemIdToIdMap.entrySet()) {
            getState().optionIds.put(getKeyForOption(entry.getKey()).toString(), entry.getValue());
        }
    }
}
