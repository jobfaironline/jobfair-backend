package org.capstone.job_fair.controllers.account;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.*;
import org.capstone.job_fair.controllers.payload.requests.account.ChangePasswordRequest;
import org.capstone.job_fair.controllers.payload.responses.BasicInfoResponse;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.token.AccountVerifyTokenDTO;
import org.capstone.job_fair.models.entities.account.AccountEntity;
import org.capstone.job_fair.services.interfaces.account.AccountService;
import org.capstone.job_fair.services.interfaces.token.AccountVerifyTokenService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.services.mappers.account.AccountMapper;
import org.capstone.job_fair.services.mappers.token.AccountVerifyTokenMapper;
import org.capstone.job_fair.utils.ImageUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Optional;

@RestController
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountVerifyTokenService accountVerifyTokenService;

    @Autowired
    private AccountVerifyTokenMapper mapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private AccountMapper accountMapper;


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Account.ACCOUNT_ENDPOINT)
    public ResponseEntity<?> getAccounts(@RequestParam(value = "offset", defaultValue = AccountConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
                                         @RequestParam(value = "pageSize", defaultValue = AccountConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = AccountConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
                                         @RequestParam(value = "direction", required = false, defaultValue = AccountConstant.DEFAULT_SORT_DIRECTION) Sort.Direction direction) {
        return new ResponseEntity<>(accountService.getAllAccounts(pageSize, offset, sortBy, direction), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Account.ACCOUNT_ENDPOINT + "/{id}")
    public ResponseEntity<?> getAttendant(@PathVariable("id") String id) {
        Optional<AccountDTO> opt = accountService.getAccountById(id);
        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(opt);
        }
        return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Account.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = ApiEndPoint.Authorization.VERIFY_USER + "/{userId}/{token}")
    public ResponseEntity<?> verifyAccount(@PathVariable("userId") String userId, @PathVariable("token") String tokenId) {
        AccountVerifyTokenDTO token = accountVerifyTokenService.getLastedToken(userId);
        if (token == null) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.AccessControlMessage.INVALID_VERIFY_TOKEN), HttpStatus.BAD_REQUEST);
        }
        if (token.getIsInvalidated()) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.AccessControlMessage.INVALIDATED_VERIFY_TOKEN), HttpStatus.BAD_REQUEST);
        }
        accountVerifyTokenService.invalidateEntity(mapper.toAccountVerifyTokenEntity(token));
        if (token.getExpiredTime() < new Date().getTime()) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.AccessControlMessage.EXPIRED_TOKEN), HttpStatus.BAD_REQUEST);
        }
        accountService.activateAccount(userId);

        return GenericResponse.build(MessageUtil.getMessage(MessageConstant.AccessControlMessage.VERIFY_ACCOUNT_SUCCESSFULLY), HttpStatus.OK);
    }

    @PostMapping(path = ApiEndPoint.Account.CHANGE_PASSWORD_ENDPOINT)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            accountService.changePassword(request.getNewPassword(), request.getOldPassword());
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Account.CHANGE_PASSWORD_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = ApiEndPoint.Authorization.NEW_VERIFY_LINK + "/{id}")
    public ResponseEntity<?> getNewVerifyLink(@PathVariable String id) {
        try {
            accountService.sendVerifyAccountEmail(id);
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Account.SEND_NEW_VERIFICATION_LINK_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApiEndPoint.Account.PICTURE_PROFILE_ENDPOINT)
    public ResponseEntity<?> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        AccountDTO accountDTO;
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String id = userDetails.getId();
            byte[] image = ImageUtil.convertImage(file, DataConstraint.Account.IMAGE_TYPE, DataConstraint.Account.WIDTH_FACTOR, DataConstraint.Account.HEIGHT_FACTOR, DataConstraint.Account.IMAGE_EXTENSION_TYPE);
            String pictureProfileFolder = AWSConstant.PICTURE_PROFILE_FOLDER;
            accountDTO = accountService.updateProfilePicture(pictureProfileFolder, id);
            fileStorageService.store(image, pictureProfileFolder + "/" + accountDTO.getId());
        } catch (IOException e) {
            return GenericResponse.build((e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.created(URI.create(accountDTO.getProfileImageUrl())).build();
    }

    @GetMapping(ApiEndPoint.Account.GET_INFO)
    public ResponseEntity<?> getAccountInfo() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userDetails);
    }

    @GetMapping(ApiEndPoint.Account.GET_GENERAL_INFO)
    public ResponseEntity<?> getGeneralAccountInfo(@RequestParam String email) {
        Optional<AccountEntity> accountOpt = accountService.getActiveAccountByEmail(email);
        if (accountOpt.isPresent()) {
            AccountEntity entity = accountOpt.get();
            AccountDTO dto = accountMapper.toDTO(entity);
            BasicInfoResponse res = new BasicInfoResponse();
            res.setEmail(dto.getEmail());
            res.setProfileImageUrl(dto.getProfileImageUrl());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(ApiEndPoint.Account.DEACTIVATE_OWN_ACCOUNT)
    public ResponseEntity<?> deactivateOwnAccount() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getId();
        AccountDTO account = accountService.deactivateOwnAccount(userId);
        return ResponseEntity.ok(account);
    }


}
