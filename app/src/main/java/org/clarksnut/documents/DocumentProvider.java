package org.clarksnut.documents;

import org.clarksnut.documents.exceptions.UnreadableDocumentException;
import org.clarksnut.documents.exceptions.UnsupportedDocumentTypeException;
import org.clarksnut.files.XmlUBLFileModel;

public interface DocumentProvider {

    /**
     * @param file that contains xml file to be persisted
     */
    DocumentModel addDocument(XmlUBLFileModel file, String fileProvider, boolean isVerified, DocumentProviderType providerType)
            throws UnsupportedDocumentTypeException, UnreadableDocumentException;

    /**
     * @param id unique identity generated by the system
     * @return document
     */
    DocumentModel getDocument(String id);

    /**
     * @param type               document type
     * @param assignedId         document assigned id
     * @param supplierAssignedId supplier assigned id
     * @return document
     */
    DocumentModel getDocument(String type, String assignedId, String supplierAssignedId);

    /**
     * @param document document to be removed
     * @return true if document was removed
     */
    boolean removeDocument(DocumentModel document);

}
