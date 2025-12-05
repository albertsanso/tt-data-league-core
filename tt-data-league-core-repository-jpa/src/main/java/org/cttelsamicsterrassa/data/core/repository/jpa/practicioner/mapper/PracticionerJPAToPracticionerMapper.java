package org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.Practicioner;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.model.PracticionerJPA;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PracticionerJPAToPracticionerMapper implements Function<PracticionerJPA, Practicioner> {
    @Override
    public Practicioner apply(PracticionerJPA practicionerJPA) {
        return Practicioner.createExisting(
                practicionerJPA.getId(),
                practicionerJPA.getFirstName(),
                practicionerJPA.getSecondName(),
                practicionerJPA.getFullName(),
                practicionerJPA.getBirthDate()
        );
    }
}
