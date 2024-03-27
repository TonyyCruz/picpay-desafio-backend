package br.com.anthony.picpay.transaction;

import br.com.anthony.picpay.authorization.AuthorizerService;
import br.com.anthony.picpay.exception.NotFoundException;
import br.com.anthony.picpay.notification.NotificationService;
import br.com.anthony.picpay.wallet.Wallet;
import br.com.anthony.picpay.wallet.WalletRepository;
import br.com.anthony.picpay.wallet.WalletType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {
    TransactionRepository transactionRepository;
    WalletRepository walletRepository;
    AuthorizerService authorizerService;
    NotificationService notificationService;

    public TransactionService(
            TransactionRepository transactionRepository,
            WalletRepository walletRepository,
            AuthorizerService authorizerService,
            NotificationService notificationService
    ) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
        this.notificationService = notificationService;
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found."));
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Transactional // Desfaz a operação em caso de erro.
    public Transaction create(Transaction transaction) {
        validate(transaction);
        Transaction newTransaction = transactionRepository.save(transaction);
        Wallet payerWallet = findWalletById(transaction.payer());
        Wallet payeeWallet = findWalletById(transaction.payee());
        walletRepository.save(payerWallet.debit(transaction.value()));
        authorizerService.authorize(transaction);
        walletRepository.save(payeeWallet.credit(transaction.value()));
        notificationService.notify(transaction);
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
