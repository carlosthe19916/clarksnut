package org.clarksnut.managers;

import org.clarksnut.documents.DocumentModel;
import org.clarksnut.documents.DocumentProvider;
import org.clarksnut.documents.DocumentVersionModel;
import org.clarksnut.documents.ImportedDocumentProvider;
import org.clarksnut.files.FileProvider;
import org.clarksnut.models.SpaceModel;
import org.clarksnut.models.UserModel;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class DocumentManager {

    private static final Logger logger = Logger.getLogger(DocumentManager.class);

    @Inject
    private FileProvider fileProvider;

    @Inject
    private ImportedDocumentProvider importedDocumentProvider;

    @Inject
    private DocumentProvider documentProvider;

    public DocumentModel getDocumentById(UserModel user, String documentId) {
        DocumentModel document = documentProvider.getDocument(documentId);
        if (document == null) return null;

        DocumentVersionModel documentCurrentVersion = document.getCurrentVersion();
        Set<String> permittedSpacesIds = user.getAllPermitedSpaces().stream()
                .map(SpaceModel::getAssignedId)
                .collect(Collectors.toSet());
        if (permittedSpacesIds.contains(document.getSupplierAssignedId()) || permittedSpacesIds.contains(documentCurrentVersion.getCustomerAssignedId())) {
            return document;
        }

        return null;
    }

}
