package org.clarksnut.representations.idm;

import com.google.common.base.Splitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DocumentQueryRepresentation {

    private String filterText;

    private SpaceRole role;

    private Set<String> types;

    private Set<String> currencies;

    private Date after;

    private Date before;

    private Float lessThan;

    private Float greaterThan;

    private Set<String> tags;

    private Set<String> spaces;

    private String orderBy;

    private boolean asc;

    private Integer offset;
    private Integer limit;

    private Boolean starred;
    private Boolean viewed;
    private Boolean checked;

    public DocumentQueryRepresentation() {

    }

    public DocumentQueryRepresentation(String query) throws ParseException {
        if (query == null || "*".equals(query)) {
            return;
        }

        Map<String, String> split = Splitter.on(", ")
                .trimResults()
                .omitEmptyStrings()
                .withKeyValueSeparator(":")
                .split(query);

        if (split.get("filterText") != null) {
            this.setFilterText(split.get("filterText").trim());
        }

        if (split.get("role") != null) {
            this.setRole(SpaceRole.valueOf(split.get("role").toUpperCase()));
        }

        if (split.get("types") != null) {
            this.setTypes(new HashSet<>(Arrays.asList(split.get("types").split(","))));
        }

        if (split.get("currencies") != null) {
            this.setCurrencies(new HashSet<>(Arrays.asList(split.get("currencies").split(","))));
        }

        if (split.get("after") != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            this.setAfter(df.parse(split.get("after")));
        }
        if (split.get("before") != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            this.setBefore(df.parse(split.get("before")));
        }

        if (split.get("greaterThan") != null) {
            this.setGreaterThan(Float.parseFloat(split.get("greaterThan")));
        }
        if (split.get("lessThan") != null) {
            this.setLessThan(Float.parseFloat(split.get("lessThan")));
        }

        if (split.get("tags") != null) {
            this.setTags(new HashSet<>(Arrays.asList(split.get("tags").split(","))));
        }

        if (split.get("spaces") != null) {
            this.setSpaces(new HashSet<>(Arrays.asList(split.get("spaces").split(","))));
        }

        if (split.get("orderBy") != null) {
            this.setOrderBy(split.get("orderBy").trim());
        }
        if (split.get("asc") != null) {
            this.setAsc(Boolean.parseBoolean(split.get("asc")));
        }

        if (split.get("offset") != null) {
            this.setOffset(Integer.parseInt(split.get("offset").trim()));
        }
        if (split.get("limit") != null) {
            this.setLimit(Integer.parseInt(split.get("limit").trim()));
        }

        if (split.get("starred") != null) {
            this.setStarred(Boolean.parseBoolean(split.get("starred").trim()));
        }
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    /**
     * Space role: sender, receiver, neutral
     */
    public SpaceRole getRole() {
        return role;
    }

    public void setRole(SpaceRole role) {
        this.role = role;
    }

    /**
     * Document type
     */
    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    /**
     * Currency
     */
    public Set<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Set<String> currencies) {
        this.currencies = currencies;
    }

    /**
     * After
     */
    public Date getAfter() {
        return after;
    }

    public void setAfter(Date after) {
        this.after = after;
    }

    /**
     * Before
     */
    public Date getBefore() {
        return before;
    }

    public void setBefore(Date before) {
        this.before = before;
    }

    /**
     * Less than
     */
    public Float getLessThan() {
        return lessThan;
    }

    public void setLessThan(Float lessThan) {
        this.lessThan = lessThan;
    }

    /**
     * Greater than
     */
    public Float getGreaterThan() {
        return greaterThan;
    }

    public void setGreaterThan(Float greaterThan) {
        this.greaterThan = greaterThan;
    }

    /**
     * Documen tags
     */
    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    /**
     * Space filter
     */
    public Set<String> getSpaces() {
        return spaces;
    }

    public void setSpaces(Set<String> spaces) {
        this.spaces = spaces;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
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

    public static enum SpaceRole {
        SENDER, RECEIVER
    }

}
