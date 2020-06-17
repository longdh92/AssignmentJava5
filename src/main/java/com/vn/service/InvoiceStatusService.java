package com.vn.service;

import com.vn.model.InvoiceStatus;

import java.util.List;

public interface InvoiceStatusService {
    List<InvoiceStatus> findAll();

    InvoiceStatus findById(Long id);

    InvoiceStatus findByName(String name);
}
