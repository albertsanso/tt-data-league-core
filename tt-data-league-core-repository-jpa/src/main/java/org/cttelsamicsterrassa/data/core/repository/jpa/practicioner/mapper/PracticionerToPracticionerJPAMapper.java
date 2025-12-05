package org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.Practicioner;
import org.cttelsamicsterrassa.data.core.repository.jpa.practicioner.model.PracticionerJPA;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PracticionerToPracticionerJPAMapper implements Function<Practicioner, PracticionerJPA> {
    @Override
    public PracticionerJPA apply(Practicioner practicioner) {
        PracticionerJPA practicionerJPA = new PracticionerJPA();
        practicionerJPA.setId(practicioner.getId());
        practicionerJPA.setFirstName(practicioner.getFirstName());
        practicionerJPA.setSecondName(practicioner.getSecondName());
        practicionerJPA.setFullName(practicioner.getFullName());
        practicionerJPA.setBirthDate(practicioner.getBirthDate());
        return practicionerJPA;
    }
}
