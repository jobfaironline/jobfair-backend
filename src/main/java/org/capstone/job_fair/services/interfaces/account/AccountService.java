package org.capstone.job_fair.services.interfaces.account;

import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface AccountService {
    Page<AccountDTO> getAllAccounts(String searchValue, int pageSize, int offset, String sortBy, Sort.Direction direction);

    Optional<AccountEntity> getActiveAccountByEmail(String email);

    AccountEntity save(AccountEntity account);

    Optional<AccountEntity> getActiveAccountById(String id);

    Optional<AccountDTO> getAccountById(String id);


    Integer getCountActiveAccountById(String id);


    Integer getCountAccountByEmail(String email);

    void activateAccount(String id);

    void changePassword(String newPassword, String oldPassword);

    void sendVerifyAccountEmail(String accountId);

    AccountDTO createNew(AccountDTO dto);

    AccountDTO updateProfilePicture(String pictureProfileFolder, String id);

    AccountDTO deactivateOwnAccount(String userId);

    AccountDTO reactivateOwnAccount(String userId);
}
