package org.cttelsamicsterrassa.data.core.repository.shared;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder<T> {

    private final List<Specification<T>> specs = new ArrayList<>();

    public SpecificationBuilder<T> equalIfPresent(String field, Object value) {
        if (value != null) {
            specs.add((root, query, cb) -> cb.equal(root.get(field), value));
        }
        return this;
    }

    public SpecificationBuilder<T> likeIfPresent(String field, String value) {
        if (value != null && !value.isBlank()) {
            specs.add((root, query, cb) ->
                    cb.like(root.get(field), "%" + value + "%"));
        }
        return this;
    }

    public Specification<T> build() {
        return specs.stream()
                .reduce(Specification::and)
                .orElse(null);
    }
}
