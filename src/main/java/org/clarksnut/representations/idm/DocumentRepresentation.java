package org.clarksnut.representations.idm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class DocumentRepresentation {

    private Data data;

    public DocumentRepresentation() {
    }

    public DocumentRepresentation(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String id;
        private String type;
        private Attributes attributes;
        private Relationships relationships;
        private DocumentLink links;

        public DocumentRepresentation toSpaceRepresentation() {
            return new DocumentRepresentation(this);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Attributes getAttributes() {
            return attributes;
        }

        public void setAttributes(Attributes attributes) {
            this.attributes = attributes;
        }

        public Relationships getRelationships() {
            return relationships;
        }

        public void setRelationships(Relationships relationships) {
            this.relationships = relationships;
        }

        public DocumentLink getLinks() {
            return links;
        }

        public void setLinks(DocumentLink links) {
            this.links = links;
        }
    }

    public static class Attributes {
        private String id;
        private String type;
        private String assignedId;
        private String supplierAssignedId;

        private Date issueDate;
        private String currency;
        private Float amount;
        private Float tax;

        private String supplierName;
        private String supplierStreetAddress;
        private String supplierCity;
        private String supplierCountry;
        private String customerName;
        private String customerAssignedId;
        private String customerStreetAddress;
        private String customerCity;
        private String customerCountry;

        private Boolean viewed;
        private Boolean starred;
        private Boolean checked;

        private Set<String> versions;

        @JsonProperty("created-at")
        private Date createdAt;

        @JsonProperty("updated-at")
        private Date updatedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAssignedId() {
            return assignedId;
        }

        public void setAssignedId(String assignedId) {
            this.assignedId = assignedId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public void setAmount(Float amount) {
            this.amount = amount;
        }

        public Float getAmount() {
            return amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Date getIssueDate() {
            return issueDate;
        }

        public void setIssueDate(Date issueDate) {
            this.issueDate = issueDate;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public String getSupplierAssignedId() {
            return supplierAssignedId;
        }

        public void setSupplierAssignedId(String supplierAssignedId) {
            this.supplierAssignedId = supplierAssignedId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerAssignedId() {
            return customerAssignedId;
        }

        public void setCustomerAssignedId(String customerAssignedId) {
            this.customerAssignedId = customerAssignedId;
        }

        public Boolean getStarred() {
            return starred;
        }

        public void setStarred(Boolean starred) {
            this.starred = starred;
        }

        public Float getTax() {
            return tax;
        }

        public void setTax(Float tax) {
            this.tax = tax;
        }

        public String getSupplierStreetAddress() {
            return supplierStreetAddress;
        }

        public void setSupplierStreetAddress(String supplierStreetAddress) {
            this.supplierStreetAddress = supplierStreetAddress;
        }

        public String getSupplierCity() {
            return supplierCity;
        }

        public void setSupplierCity(String supplierCity) {
            this.supplierCity = supplierCity;
        }

        public String getSupplierCountry() {
            return supplierCountry;
        }

        public void setSupplierCountry(String supplierCountry) {
            this.supplierCountry = supplierCountry;
        }

        public String getCustomerStreetAddress() {
            return customerStreetAddress;
        }

        public void setCustomerStreetAddress(String customerStreetAddress) {
            this.customerStreetAddress = customerStreetAddress;
        }

        public String getCustomerCity() {
            return customerCity;
        }

        public void setCustomerCity(String customerCity) {
            this.customerCity = customerCity;
        }

        public String getCustomerCountry() {
            return customerCountry;
        }

        public void setCustomerCountry(String customerCountry) {
            this.customerCountry = customerCountry;
        }

        public Boolean getViewed() {
            return viewed;
        }

        public void setViewed(Boolean viewed) {
            this.viewed = viewed;
        }

        public Boolean getChecked() {
            return checked;
        }

        public void setChecked(Boolean checked) {
            this.checked = checked;
        }

        public Set<String> getVersions() {
            return versions;
        }

        public void setVersions(Set<String> versions) {
            this.versions = versions;
        }
    }

    public static class Relationships {
        @JsonProperty("owned-by")
        private OwnedBy ownedBy;

        public OwnedBy getOwnedBy() {
            return ownedBy;
        }

        public void setOwnedBy(OwnedBy ownedBy) {
            this.ownedBy = ownedBy;
        }
    }

    public static class OwnedBy {
        private List<SpaceRepresentation.Data> data;
//        private GenericLinksRepresentation links;

        public List<SpaceRepresentation.Data> getData() {
            return data;
        }

        public void setData(List<SpaceRepresentation.Data> data) {
            this.data = data;
        }

//        public GenericLinksRepresentation getLinks() {
//            return links;
//        }
//
//        public void setLinks(GenericLinksRepresentation links) {
//            this.links = links;
//        }
    }

    public static class DocumentLink {
        private String self;
        private String filelink;

        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }

        public String getFilelink() {
            return filelink;
        }

        public void setFilelink(String filelink) {
            this.filelink = filelink;
        }
    }

}
