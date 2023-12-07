package com.adiha.EventScheduler.specifications;

import com.adiha.EventScheduler.models.Event;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class EventByVenue implements Specification<Event> {

    private String venue;

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (venue == null) {
            return cb.isTrue(cb.literal(true));
        }

        return cb.equal(root.get("location"), this.venue);
    }
}
