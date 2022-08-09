package org.project.interfaces;

import io.smallrye.mutiny.Uni;
import org.project.entities.DNARecord;
import org.project.models.DNAStats;

public interface IDNARecordService
{
    Uni<DNARecord> persistDNARecord(DNARecord record);

    Uni<DNAStats> getStats();
}
