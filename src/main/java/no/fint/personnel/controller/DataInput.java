package no.fint.personnel.controller;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class DataInput {
    private Operation operation;
    private Date timestamp;
    private UUID corrId;
    private String datatype;
    private String orgId;
    private String client;
    private List data;

    public enum Operation { FULL, INCREMENTAL }
}
