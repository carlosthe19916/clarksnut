package org.openfact.models.db.es;

import org.openfact.models.DocumentModel;
import org.openfact.models.SpaceModel;
import org.openfact.models.db.es.entity.DocumentEntity;
import org.openfact.models.db.JpaModel;
import org.openfact.models.db.jpa.SpaceAdapter;
import org.openfact.models.db.jpa.entity.SpaceEntity;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DocumentAdapter implements DocumentModel, JpaModel<DocumentEntity> {

    private final EntityManager em;
    private final DocumentEntity document;

    public DocumentAdapter(EntityManager em, DocumentEntity document) {
        this.em = em;
        this.document = document;
    }

    public static DocumentEntity toEntity(DocumentModel model, EntityManager em) {
        if (model instanceof DocumentAdapter) {
            return ((DocumentAdapter) model).getEntity();
        }
        return em.getReference(DocumentEntity.class, model.getId());
    }

    @Override
    public DocumentEntity getEntity() {
        return document;
    }

    @Override
    public String getId() {
        return document.getId();
    }

    @Override
    public String getType() {
        return document.getType();
    }

    @Override
    public String getAssignedId() {
        return document.getAssignedId();
    }

    @Override
    public String getFileId() {
        return document.getFileId();
    }

    @Override
    public Map<String, String> getTags() {
        Map<String, String> config = new HashMap<>();
        config.putAll(document.getTags());
        return Collections.unmodifiableMap(config);
    }

    @Override
    public Date getCreatedAt() {
        return document.getCreatedAt();
    }

    @Override
    public Date getUpdatedAt() {
        return document.getUpdatedAt();
    }

    @Override
    public SpaceModel getSpace() {
        return new SpaceAdapter(em, document.getSpace());
    }

}
