package org.clarksnut.mapper.document.pe.perception;

import org.clarksnut.files.XmlUBLFileModel;
import org.clarksnut.mapper.document.DocumentMapped;
import org.clarksnut.mapper.document.DocumentMapperProvider;
import org.clarksnut.models.utils.ClarksnutModelUtils;
import org.jboss.logging.Logger;
import org.openfact.perception.PerceptionType;

import javax.xml.bind.JAXBException;

public class PEPerceptionParsedDocumentProvider implements DocumentMapperProvider {

    private static final Logger logger = Logger.getLogger(PEPerceptionParsedDocumentProvider.class);

    @Override
    public String getGroup() {
        return "peru";
    }

    @Override
    public String getSupportedDocumentType() {
        return "Perception";
    }

    @Override
    public DocumentMapped map(XmlUBLFileModel file) {
        PerceptionType perceptionType;
        try {
            perceptionType = ClarksnutModelUtils.unmarshall(file.getDocument(), PerceptionType.class);
        } catch (JAXBException e) {
            return null;
        }

        return new DocumentMapped() {
            @Override
            public DocumentBean getBean() {
                return new PEPerceptionBeanAdapter(perceptionType);
            }

            @Override
            public Object getType() {
                return perceptionType;
            }
        };
    }

}
