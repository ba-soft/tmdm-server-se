package com.amalto.core.util;


import com.amalto.core.ejb.ItemPOJOPK;
import com.amalto.core.ejb.ObjectPOJOPK;
import com.mysql.jdbc.StringUtils;

public class EntityNotFoundException extends XtentisException {

    private static final long serialVersionUID = -5541352366654335876L;

    private ObjectPOJOPK objectPK;

    private ItemPOJOPK itemPK;

    public EntityNotFoundException(ItemPOJOPK pk) {
        this.itemPK = pk;
    }

    public EntityNotFoundException(ObjectPOJOPK pk) {
        this.objectPK = pk;
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        if (itemPK != null) {
            if (StringUtils.isNullOrEmpty(itemPK.getDataClusterPOJOPK().getUniqueId())) {
                return "No container specified in id '" + itemPK.getUniqueID() + "'.";
            }
            if (StringUtils.isNullOrEmpty(itemPK.getConceptName())) {
                return "No data model specified in '" + itemPK.getUniqueID() + "'.";
            } else {
                return "Could not find item '" + itemPK.getUniqueID() + "'.";
            }
        } else if (objectPK != null) {
            return "Could not find object '" + objectPK.getUniqueId() + "'.";
        } else {
            return "Could not find item."; // Generic error message.
        }
    }
}
