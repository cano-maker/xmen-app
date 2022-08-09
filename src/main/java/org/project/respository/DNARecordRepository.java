package org.project.respository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import org.project.entities.DNARecord;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DNARecordRepository implements PanacheRepository<DNARecord>
{
}
