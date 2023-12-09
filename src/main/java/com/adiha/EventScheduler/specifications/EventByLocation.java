package com.adiha.EventScheduler.specifications;

import com.adiha.EventScheduler.models.Event;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification for filtering events by location.
 * This class is used to create a predicate for a JPA Criteria query.
 */
@AllArgsConstructor
public class EventByLocation implements Specification<Event> {

    /**
     * The location to filter by.
     */
    private String location;

    /**
     * Creates a predicate for filtering events by location.
     *
     * @param root  the root type in the from clause, used to form the predicate
     * @param query the criteria query
     * @param cb    the criteria builder, used to build the predicate
     * @return a predicate for filtering events by location
     */
    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (location == null || location.isEmpty()) {
            return cb.isTrue(cb.literal(true));
        }

        return cb.equal(root.get("location"), this.location);
    }
}