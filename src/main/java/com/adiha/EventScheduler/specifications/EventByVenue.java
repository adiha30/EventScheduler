package com.adiha.EventScheduler.specifications;

import com.adiha.EventScheduler.models.Event;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification for filtering events by venue.
 * This class is used to create a predicate for a JPA Criteria query.
 */
@AllArgsConstructor
public class EventByVenue implements Specification<Event> {

    /**
     * The venue to filter by.
     */
    private String venue;

    /**
     * Creates a predicate for filtering events by venue.
     *
     * @param root the root type in the from clause, used to form the predicate
     * @param query the criteria query
     * @param cb the criteria builder, used to build the predicate
     * @return a predicate for filtering events by venue
     */
    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        // If the venue is null or empty, return a predicate that always evaluates to true
        if (venue == null || venue.isEmpty()) {
            return cb.isTrue(cb.literal(true));
        }
        // Otherwise, return a predicate that checks if the venue of the event equals the specified venue
        return cb.equal(root.get("venue"), this.venue);
    }
}