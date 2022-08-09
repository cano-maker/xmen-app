package org.project.interfaces;

import io.smallrye.mutiny.Uni;
import org.project.entities.DNARecord;

public interface IDNARecordService
{
    Uni<DNARecord> persistDNARecord(DNARecord record);
}
