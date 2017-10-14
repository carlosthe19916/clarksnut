package org.openfact.batchs.broker;

import org.jberet.support.io.JpaItemReader;
import org.jberet.support.io.JpaItemReaderWriterBase;
import org.jboss.logging.Logger;
import org.openfact.models.db.jpa.entity.UserLinkedBrokerEntity;
import org.openfact.repositories.user.*;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemReader;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
public class PullMailMessagesReader extends JpaItemReaderWriterBase implements ItemReader {

    private static final Logger logger = Logger.getLogger(PullMailMessagesReader.class);

    @Inject
    private MailUtils mailUtils;

    /**
     * {@code javax.enterprise.inject.Instance} that holds optional injection of
     * {@code javax.persistence.Query}.
     */
    @Inject
    protected Instance<Query> queryInstance;

    /**
     * a Java Persistence query string. Optional properties, and defaults to null.
     */
    @Inject
    @BatchProperty
    protected String jpqlQuery;

    /**
     * The name of a query defined in JPA metadata.
     * Optional property, and defaults to null.
     */
    @Inject
    @BatchProperty
    protected String namedQuery;

    /**
     * A native SQL query string. Optional property, and defaults to null.
     */
    @Inject
    @BatchProperty
    protected String nativeQuery;

    /**
     * A name of the stored procedure in the database.
     * Optional procedure, and defaults to null.
     */
    @Inject
    @BatchProperty
    protected String storedProcedureQuery;

    /**
     * A name assigned to the stored procedure query in JPA metadata.
     * Optional property, and defaults to null.
     */
    @Inject
    @BatchProperty
    protected String namedStoredProcedureQuery;

    /**
     * The Java type of the query result object.
     * Optional properties, and defaults to null.
     */
    @Inject
    @BatchProperty
    protected Class beanType;

    /**
     * Name of the resultset mapping. Optional properties, and defaults to null.
     * If specified, it is used to create native query or stored procedure query.
     */
    @Inject
    @BatchProperty
    protected String resultSetMapping;

    /**
     * Query hint properties, as a list of key-value pairs separated by comma (,).
     * Optional property, and defaults to null.
     */
    @Inject
    @BatchProperty
    protected Map<String, String> hints;

    /**
     * Position of the first result, numbered from 0. Optional property, and
     * defaults to 0.
     */
    @Inject
    @BatchProperty
    protected int firstResult;

    /**
     * Maximum number of results to retrieve by the query. Optional property, and
     * defaults to 0 (no limit).
     */
    @Inject
    @BatchProperty
    protected int maxResults;

    /**
     * The JPA query object
     */
    protected Query query;

    /**
     * List to hold query result objects
     */
    protected List<MailUblMessageModel> resultList;

    /**
     * Current read position
     */
    protected int readPosition;

    private MailRepositoryModel buildRepository(UserLinkedBrokerEntity userLinkedBrokerEntity) {
        return MailRepositoryModel.builder()
                .email(userLinkedBrokerEntity.getEmail())
                .refreshToken(userLinkedBrokerEntity.getUser().getOfflineToken())
                .build();
    }

    private MailQuery buildQuery(UserLinkedBrokerEntity userLinkedBrokerEntity) {
        MailQuery.Builder queryBuilder = MailQuery.builder().fileType("xml");
        LocalDateTime lastTimeSynchronized = userLinkedBrokerEntity.getLastTimeSynchronized();
        if (lastTimeSynchronized != null) {
            queryBuilder.after(lastTimeSynchronized);
        }
        return queryBuilder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(final Serializable checkpoint) throws Exception {
        query = getQuery();

        if (firstResult != 0) {
            query.setFirstResult(firstResult);
        }
        if (maxResults != 0) {
            query.setMaxResults(maxResults);
        }

        for (Object o : query.getResultList()) {
            UserLinkedBrokerEntity userLinkedBrokerEntity = (UserLinkedBrokerEntity) o;
            MailProvider mailProvider = mailUtils.getMailReader(userLinkedBrokerEntity.getType());
            if (mailProvider != null) {
                MailRepositoryModel repository = buildRepository(userLinkedBrokerEntity);
                MailQuery query = buildQuery(userLinkedBrokerEntity);
                resultList.addAll(mailProvider.getUblMessages(repository, query));
            } else {
                logger.warn("Skipping Linked Broker because could not find MailProviders for:" + userLinkedBrokerEntity.getType());
            }
        }

        if (checkpoint == null) {
            readPosition = 0;
        } else {
            readPosition = (Integer) checkpoint;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object readItem() throws Exception {
        if (readPosition >= resultList.size()) {
            return null;
        }
        return resultList.get(readPosition++);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Serializable checkpointInfo() throws Exception {
        return readPosition;
    }

    /**
     * Creates and initializes the JPA query object based on the configuration
     * in this reader class, in the following order of precedence:
     * <ol>
     * <li>{@link #queryInstance}
     * <li>{@link #jpqlQuery}
     * <li>{@link #namedQuery}
     * <li>{@link #nativeQuery}
     * <li>{@link #storedProcedureQuery}
     * <li>{@link #namedStoredProcedureQuery}
     * </ol>
     *
     * @return JPA query object
     */
    protected Query getQuery() {
        Query q = null;
        if (queryInstance != null && !queryInstance.isUnsatisfied()) {
            q = queryInstance.get();
        }
        if (q == null) {
            if (jpqlQuery != null) {
                q = beanType != null ? em.createQuery(jpqlQuery, beanType) :
                        em.createQuery(jpqlQuery);
            } else if (namedQuery != null) {
                q = beanType != null ? em.createNamedQuery(namedQuery, beanType) :
                        em.createNamedQuery(namedQuery);
            } else if (nativeQuery != null) {
                q = beanType != null ? em.createNativeQuery(nativeQuery, beanType) :
                        resultSetMapping != null ? em.createNativeQuery(nativeQuery, resultSetMapping) :
                                em.createNativeQuery(nativeQuery);
            } else if (storedProcedureQuery != null) {
                q = beanType != null ? em.createStoredProcedureQuery(storedProcedureQuery, beanType) :
                        resultSetMapping != null ? em.createStoredProcedureQuery(storedProcedureQuery, resultSetMapping) :
                                em.createStoredProcedureQuery(storedProcedureQuery);
            } else if (namedStoredProcedureQuery != null) {
                q = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
            }
        }

        if (firstResult != 0) {
            q.setFirstResult(firstResult);
        }
        if (maxResults != 0) {
            q.setMaxResults(maxResults);
        }

        if (hints != null) {
            for (final Map.Entry<String, String> e : hints.entrySet()) {
                q.setHint(e.getKey(), e.getValue());
            }
        }

        return q;
    }

}
