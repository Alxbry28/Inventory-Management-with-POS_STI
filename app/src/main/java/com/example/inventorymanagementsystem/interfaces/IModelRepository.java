package com.example.inventorymanagementsystem.interfaces;

public interface IModelRepository<Model> {
    void Create(final TransactionStatusListener transactionStatus);
    void Update(final TransactionStatusListener transactionStatus);
    void Delete(final TransactionStatusListener transactionStatus);
    void GetById(final IEntityModelListener<Model> entityModelListener);
    void GetAll(final IEntityModelListener<Model> entityModelListener);
}
