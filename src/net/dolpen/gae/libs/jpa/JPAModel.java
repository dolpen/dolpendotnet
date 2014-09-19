package net.dolpen.gae.libs.jpa;

import javax.persistence.Query;
import java.sql.SQLException;
import java.util.*;

/**
 * JPAModel for parent
 */
public class JPAModel {


    /**
     * save(例外を処理したい場合)
     */
    public void save() {
        JPA.em().persist(this);
    }

    /**
     * delete
     */
    public void delete() {
        JPA.em().remove(this);
    }

    /**
     * merge
     */
    public void merge() {
        JPA.em().merge(this);
    }

    /**
     * refresh
     */
    public void refresh() {
        JPA.em().refresh(this);
    }


    /**
     * find
     *
     * @param c
     * @param query
     * @param params
     * @return
     */
    protected static JPAQuery find(Class c, String query, Object... params) {
        return new JPQL(JPA.em()).find(c.getSimpleName(), query, params);
    }

    /**
     * count
     *
     * @param c
     * @param query
     * @param params
     * @return
     */
    protected static long count(Class c, String query, Object... params) {
        return new JPQL(JPA.em()).count(c.getSimpleName(), query, params);
    }


    /**
     * count
     *
     * @param c
     * @param query
     * @param params
     * @return
     */
    protected static JPAQuery countQuery(Class c, String query, Object... params) {
        return new JPQL(JPA.em()).countQuery(c.getSimpleName(), query, params);
    }

    /**
     * findAll
     *
     * @param c
     * @return
     */
    protected static JPAQuery all(Class c) {
        return new JPQL(JPA.em()).all(c.getSimpleName());
    }

    /**
     * A JPAQuery
     */
    public static class JPAQuery {

        public Query query;
        public String sq;

        public JPAQuery(String sq, Query query) {
            this.query = query;
            this.sq = sq;
        }

        public JPAQuery(Query query) {
            this.query = query;
            this.sq = query.toString();
        }


        public long count() {
            return Long.parseLong(query.getSingleResult().toString());
        }

        public <T> T first() {
            try {
                List<T> results = query.setMaxResults(1).getResultList();
                if (results.isEmpty()) {
                    return null;
                }
                return results.get(0);
            } catch (Exception e) {
                throw new JPAQueryException("Error while executing query <strong>" + sq + "</strong>", JPAQueryException.findBestCause(e));
            }
        }

        /**
         * Bind a JPQL named parameter to the current query.
         * Careful, this will also bind count results. This means that Integer get transformed into long
         * so hibernate can do the right thing. Use the setParameter if you just want to set parameters.
         */
        public JPAQuery bind(String name, Object param) {
            if (param.getClass().isArray()) {
                param = Arrays.asList((Object[]) param);
            }
            if (param instanceof Integer) {
                param = ((Integer) param).longValue();
            }
            query.setParameter(name, param);
            return this;
        }

        /**
         * Set a named parameter for this query.
         */
        public JPAQuery setParameter(String name, Object param) {
            query.setParameter(name, param);
            return this;
        }

        /**
         * Retrieve all results of the query
         *
         * @return A list of entities
         */
        public <T> List<T> fetch() {
            try {
                return query.getResultList();
            } catch (Exception e) {
                throw new JPAQueryException("Error while executing query <strong>" + sq + "</strong>", JPAQueryException.findBestCause(e));
            }
        }

        /**
         * Retrieve results of the query
         *
         * @param max Max results to fetch
         * @return A list of entities
         */
        public <T> List<T> fetch(int max) {
            try {
                query.setMaxResults(max);
                return query.getResultList();
            } catch (Exception e) {
                throw new JPAQueryException("Error while executing query <strong>" + sq + "</strong>", JPAQueryException.findBestCause(e));
            }
        }

        /**
         * Set the position to start
         *
         * @param position Position of the first element
         * @return A new query
         */
        public <T> JPAQuery from(int position) {
            query.setFirstResult(position);
            return this;
        }

        /**
         * Retrieve a page of result
         *
         * @param page   Page number (start at 1)
         * @param length (page length)
         * @return a list of entities
         */
        public <T> List<T> fetch(int page, int length) {
            if (page < 1) {
                page = 1;
            }
            query.setFirstResult((page - 1) * length);
            query.setMaxResults(length);
            try {
                return query.getResultList();
            } catch (Exception e) {
                throw new JPAQueryException("Error while executing query <strong>" + sq + "</strong>", JPAQueryException.findBestCause(e));
            }
        }

    }

    /**
     * Exception
     */
    public static class JPAQueryException extends RuntimeException {

        public JPAQueryException(String message) {
            super(message);
        }

        public JPAQueryException(String message, Throwable e) {
            super(message + ": " + e.getMessage(), e);
        }

        public static Throwable findBestCause(Throwable e) {
            Throwable best = e;
            Throwable cause = e;
            int it = 0;
            while ((cause = cause.getCause()) != null && it++ < 10) {
                if (cause instanceof ClassCastException) {
                    best = cause;
                    break;
                }
                if (cause instanceof SQLException) {
                    best = cause;
                    break;
                }
            }
            return best;
        }
    }

}
