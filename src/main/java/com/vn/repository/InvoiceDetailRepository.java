package com.vn.repository;

import com.vn.model.Invoice_detail;

import java.util.List;

public interface InvoiceDetailRepository extends Repository<Invoice_detail> {
    List<Invoice_detail> findByIdInvoice(Long id);
}
