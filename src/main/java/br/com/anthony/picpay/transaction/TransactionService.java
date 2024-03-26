package br.com.anthony.picpay.transaction;

import br.com.anthony.picpay.exception.InvalidTransactionException;
import br.com.anthony.picpay.exception.NotFoundException;
import br.com.anthony.picpay.wallet.Wallet;
import br.com.anthony.picpay.wallet.WalletRepository;
import br.com.anthony.picpay.wallet.WalletType;
import org.springframework.transaction.annotation.Transactional;

public class TransactionService {
    TransactionRepository transactionRepository;
    WalletRepository walletRepository;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public Transaction create(Transaction transaction) {
        validate(transaction);
        Transaction newTransaction = transactionRepository.save(transaction);
        Wallet payerWallet = findWalletById(transaction.payer());
        walletRepository.save(payerWallet.debit(transaction.value()));

        return newTransaction;
    }

    private void validate(Transaction transaction) {
        if (!isValidTransaction(transaction)) {
            throw new InvalidTransactionException("Invalid transaction %s".formatted(transaction));
        }
    }

    private Wallet findWalletById(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Wallet %d not found.".formatted(id))
                );
    }

    /**
     * - The payee has a common wallet.
     * - The payee has enough balance.
     * - The payee is not the payer.
     */
    private boolean isValidTransaction(Transaction transaction) {
        Wallet payer = findWalletById(transaction.payer());
        Wallet payee = findWalletById(transaction.payee());
        return !payer.equals(payee) &&
                payer.type() == WalletType.COMMON.getValue() &&
                payer.balance().compareTo(transaction.value()) >= 0;

    }

}
