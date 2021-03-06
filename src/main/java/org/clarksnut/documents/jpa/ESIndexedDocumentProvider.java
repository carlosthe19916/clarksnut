package org.clarksnut.documents.jpa;

import org.apache.lucene.search.Sort;
import org.clarksnut.documents.IndexedDocumentModel;
import org.clarksnut.documents.IndexedDocumentProvider;
import org.clarksnut.documents.IndexedDocumentQueryModel;
import org.clarksnut.documents.SearchResultModel;
import org.clarksnut.documents.jpa.IndexedManagerType.Type;
import org.clarksnut.documents.jpa.entity.IndexedDocumentEntity;
import org.clarksnut.models.SpaceModel;
import org.clarksnut.models.UserModel;
import org.clarksnut.query.SimpleQuery;
import org.clarksnut.query.es.ESQueryParser;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.hibernate.search.elasticsearch.ElasticsearchQueries;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.sort.SortFieldContext;
import org.hibernate.search.query.engine.spi.QueryDescriptor;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
@IndexedManagerType(type = Type.ELASTICSEARCH)
public class ESIndexedDocumentProvider extends AbstractIndexedDocumentProvider implements IndexedDocumentProvider {

    private static final Logger logger = Logger.getLogger(ESIndexedDocumentProvider.class);

    @PersistenceContext
    private EntityManager em;

    public String getQuery(UserModel user, IndexedDocumentQueryModel query, SpaceModel... space) {
        if (query.getFilters().isEmpty()) {
            throw new IllegalStateException("Invalid query, at least one query should be requested");
        }

        // Space query
        Set<String> userPermittedSpaceIds = getUserPermittedSpaces(user, space);
        if (userPermittedSpaceIds.isEmpty()) {
            return null;
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (SimpleQuery q : query.getFilters()) {
            boolQueryBuilder.filter(ESQueryParser.toQueryBuilder(q, new DocumentFieldMapper()));
        }

        DocumentFieldMapper fieldMapper = new DocumentFieldMapper();
        boolQueryBuilder.should(QueryBuilders.termsQuery(fieldMapper.apply(IndexedDocumentModel.SUPPLIER_ASSIGNED_ID), userPermittedSpaceIds));
        boolQueryBuilder.should(QueryBuilders.termsQuery(fieldMapper.apply(IndexedDocumentModel.CUSTOMER_ASSIGNED_ID), userPermittedSpaceIds));
        boolQueryBuilder.minimumShouldMatch(1);
        return boolQueryBuilder.toString();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public SearchResultModel<IndexedDocumentModel> getDocumentsUser(UserModel user, IndexedDocumentQueryModel query, SpaceModel... space) {
        String esQuery = getQuery(user, query, space);

        // No results
        if (esQuery == null) {
            // User do not have any space assigned
            return new EmptySearchResultAdapter<>();
        }

        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
        QueryDescriptor queryDescriptor = ElasticsearchQueries.fromJson("{\"query\":" + esQuery + "}");

        FullTextQuery fullTextQuery = fullTextEm.createFullTextQuery(queryDescriptor, IndexedDocumentEntity.class);

        // Sort
        Sort sort = null;
        if (query.getOrderBy() != null) {
            QueryBuilder queryBuilder = fullTextEm.getSearchFactory().buildQueryBuilder().forEntity(IndexedDocumentEntity.class).get();

            SortFieldContext sortFieldContext = queryBuilder.sort().byField(new DocumentFieldMapper().apply(query.getOrderBy()));
            if (query.isAsc()) {
                sort = sortFieldContext.asc().createSort();
            } else {
                sort = sortFieldContext.desc().createSort();
            }
        }

        if (sort != null) {
            fullTextQuery.setSort(sort);
        }

        // Pagination
        if (query.getOffset() != null && query.getOffset() != -1) {
            fullTextQuery.setFirstResult(query.getOffset());
        }
        if (query.getLimit() != null && query.getLimit() != -1) {
            fullTextQuery.setMaxResults(query.getLimit());
        }

        // Result
        List<IndexedDocumentEntity> resultList = fullTextQuery.getResultList();
        List<IndexedDocumentModel> items = resultList.stream()
                .map(f -> new IndexedDocumentAdapter(em, f))
                .collect(Collectors.toList());

        return new SearchResultModel<IndexedDocumentModel>() {
            @Override
            public List<IndexedDocumentModel> getItems() {
                return items;
            }

            @Override
            public int getTotalResults() {
                return fullTextQuery.getResultSize();
            }
        };
    }

}
