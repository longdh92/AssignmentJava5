package com.vn.service;

import com.vn.model.InvoiceStatus;
import com.vn.repository.InvoiceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InvoiceStatusServiceImpl implements InvoiceStatusService {

    @Autowired
    private InvoiceStatusRepository invoiceStatusRepository;

    @Override
    public List<InvoiceStatus> findAll() {
        return invoiceStatusRepository.findAll();
    }

    @Override
    public InvoiceStatus findById(Long id) {
        return invoiceStatusRepository.findById(id);
    }

    @Override
    public InvoiceStatus findByName(String name) {
        return invoiceStatusRepository.findByName(name);
    }
}
