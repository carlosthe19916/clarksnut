package org.openfact.documents.reader.basic.invoice;

import com.helger.ubl21.UBL21Reader;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import org.jboss.logging.Logger;
import org.openfact.documents.DocumentReader;
import org.openfact.documents.GenericDocument;
import org.openfact.documents.InteractType;
import org.openfact.documents.jpa.entity.DocumentEntity;
import org.openfact.documents.jpa.entity.DocumentSpaceEntity;
import org.openfact.documents.reader.SupportedType;
import org.openfact.documents.reader.basic.common.BasicUtils;
import org.openfact.files.XmlUBLFileModel;
import org.openfact.models.db.jpa.entity.SpaceEntity;
import org.openfact.models.utils.OpenfactModelUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Stateless
@SupportedType(value = "Invoice")
public class BasicInvoiceReader implements DocumentReader {

    private static final Logger logger = Logger.getLogger(BasicInvoiceReader.class);

    @Inject
    private BasicUtils basicUtils;

    @Override
    public String getSupportedDocumentType() {
        return "Invoice";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public GenericDocument read(XmlUBLFileModel file) {
        InvoiceType invoiceType = UBL21Reader.invoice().read(file.getDocument());
        if (invoiceType == null) {
            return null;
        }

        SpaceEntity senderSpaceEntity = basicUtils.getSenderSpace(invoiceType.getAccountingSupplierParty());
        SpaceEntity receiverSpaceEntity = basicUtils.getReceiverSpace(invoiceType.getAccountingCustomerParty());


        DocumentEntity documentEntity = new DocumentEntity();

        DocumentSpaceEntity documentSpaceSenderEntity = new DocumentSpaceEntity();
        documentSpaceSenderEntity.setId(OpenfactModelUtils.generateId());
        documentSpaceSenderEntity.setType(InteractType.SENDER);
        documentSpaceSenderEntity.setSpace(senderSpaceEntity);
        documentSpaceSenderEntity.setDocument(documentEntity);

        DocumentSpaceEntity documentSpaceReceiverEntity = new DocumentSpaceEntity();
        documentSpaceReceiverEntity.setId(OpenfactModelUtils.generateId());
        documentSpaceReceiverEntity.setType(InteractType.RECEIVER);
        documentSpaceReceiverEntity.setSpace(receiverSpaceEntity);
        documentSpaceReceiverEntity.setDocument(documentEntity);

        documentEntity.setAssignedId(invoiceType.getIDValue());
        documentEntity.setSpaces(new HashSet<>(Arrays.asList(documentSpaceSenderEntity, documentSpaceReceiverEntity)));
        documentEntity.setSupplierAssignedId(invoiceType.getAccountingSupplierParty().getCustomerAssignedAccountID().getValue());
        documentEntity.setSupplierName(invoiceType.getAccountingSupplierParty().getParty().getPartyLegalEntity().get(0).getRegistrationName().getValue());
        documentEntity.setCustomerAssignedId(invoiceType.getAccountingCustomerParty().getCustomerAssignedAccountID().getValue());
        documentEntity.setCustomerName(invoiceType.getAccountingCustomerParty().getParty().getPartyLegalEntity().get(0).getRegistrationName().getValue());
        documentEntity.setCurrency(invoiceType.getLegalMonetaryTotal().getPayableAmount().getCurrencyID());
        documentEntity.setAmount(invoiceType.getLegalMonetaryTotal().getPayableAmount().getValue().floatValue());
        documentEntity.setIssueDate(invoiceType.getIssueDate().getValue().toGregorianCalendar().getTime());

        Map<String, String> tags = new HashMap<>();
        tags.put("reader", "basic");
        documentEntity.setTags(tags);

        return new GenericDocument() {
            @Override
            public DocumentEntity getEntity() {
                return documentEntity;
            }

            @Override
            public Object getJaxb() {
                return invoiceType;
            }
        };
    }

}