package org.capstone.job_fair.constants;

public class MessageConstant {

    public static final class Exception {
        public static final String RESOURCE_NOT_FOUND = "error.exception.resource-not-found";
        public static final String JWT_ERROR = "error.exception.jwt-error";
        public static final String NO_PERMISSION = "error.exception.resource-no-permission";
        public static final String INTERNAL_ERROR = "error.exception.internal-error";
        public static final String METHOD_ARGUMENT_NOT_VALID = "error.exception.method-argument-not-valid";
        public static final String ENTITY_NOT_FOUND = "error.exception.entity-not-found";
        public static final String HTTP_MESSAGE_NOT_VALID = "error.exception.http-message-not-valid";
    }


    public static final class InvalidFormatValidationMessage {
        //the value in this class belongs to Validator annotation
        //validator on default can use expression language to lookup message source
        //=> need to have {}
        public static final String INVALID_EMAIL_FORMAT = "{error.format.invalid-email}";
        public static final String INVALID_PASSWORD_FORMAT = "{error.format.invalid-password}";
        public static final String INVALID_PHONE_FORMAT = "{error.format.invalid-phone}";
        public static final String NOT_BLANK_FORMAT = "{error.format.blank-value}";
        public static final String XSS_MESSAGE = "{error.format.xss}";
    }

    public static final class AccessControlMessage {
        public static final String INVALID_REFRESH_TOKEN = "access-control.error.invalid-refresh-token";
        public static final String TOKEN_CLAIM_INVALID = "access-control.error.invalid-token-claim";
        public static final String CONFIRM_PASSWORD_MISMATCH = "access-control.error.confirm-password-mismatch";
        public static final String INVALID_OTP = "access-control.error.invalid-otp";
        public static final String EXPIRED_OTP = "access-control.error.expired-otp";
        public static final String RESET_PASSWORD_SUCCESSFULLY = "access-control.success.reset-password";
        public static final String INTERVAL_OTP_REQUEST = "access-control.error.interval-otp-request";
        public static final String REQUEST_RESET_PASSWORD_SUCCESSFULLY = "access-control.success.request-reset-password";
    }

    public static final class Account {
        public static final String NOT_FOUND = "account.error.not-found";
        public static final String EXISTED = "account.error.existed";
        public static final String EMAIL_EXISTED = "account.error.email-existed";
        public static final String NOT_FOUND_COUNTRY = "account.error.not-found-country";
        public static final String NOT_FOUND_RESIDENCE = "account.error.not-found-residence";
        public static final String NOT_FOUND_JOB_LEVEL = "account.error.not-found-job-level";
        public static final String NOT_FOUND_QUALIFICATION = "account.error.not-found-qualification";
        public static final String SKILL_INVALID = "account.error.skill_invalid";
        public static final String WORK_HISTORY_INVALID = "account.error.work_history_invalid";
        public static final String EDUCATION_INVALID = "account.error.education_invalid";
        public static final String CERTIFICATION_INVALID = "account.error.certification_invalid";
        public static final String ACTIVITY_INVALID = "account.error.activity_invalid";
        public static final String DOB_INVALID = "account.error.dob_invalid";
        public static final String YEAR_OF_EXP_INVALID = "account.error.year_of_exp_invalid";
    }

    public static final class Attendant {
        public static final String UPDATE_PROFILE_SUCCESSFULLY = "attendant.success.update-profile";
        public static final String REGISTER_SUCCESSFULLY = "attendant.success.create";
    }

    public static final class Company {
        public static final String TAX_ID_EXISTED = "company.error.tax-id-existed";
        public static final String EMAIL_EXISTED = "company.error.email-existed";
        public static final String NOT_FOUND = "company.error.not-found";
        public static final String CREATE_SUCCESSFULLY = "company.success.create";
        public static final String CREATE_FAILED = "company.error.create";
        public static final String UPDATE_SUCCESSFULLY = "company.success.update";
        public static final String UPDATE_FAILED = "company.error.update";
        public static final String DELETE_SUCCESSFULLY = "company.success.delete";
        public static final String DELETE_FAILED = "company.error.delete";
        public static final String SIZE_INVALID = "company.error.size-invalid";
    }

    public static final class CompanyEmployee {
        public static final String EMAIL_EXISTED = "company-employee.error.email-existed";
        public static final String CREATE_EMPLOYEE_MANAGER_SUCCESSFULLY = "company-employee.success.create-company-manager";
        public static final String CREATE_EMPLOYEE_EMPLOYEE_SUCCESSFULLY = "company-employee.success.create-company-employee";
        public static final String UPDATE_PROFILE_SUCCESSFULLY = "company-employee.success.update-profile";
        public static final String DELETE_SUCCESSFULLY = "company-employee.success.delete-employee";
        public static final String DELETE_FAILED = "company-employee.error.delete";
        public static final String EMAIL_SUBJECT = "company-employee.email.subject";
        public static final String EMAIL_CONTENT = "company-employee.email.body";
        public static final String COMPANY_NOT_EXIST = "company-empoyee.company.not-existed";
    }

    public static final class Gender {
        public static final String NOT_FOUND = "gender.error.not-found";
    }

    public static final class Job {
        public static final String CREATE_JOB_SUCCESSFULLY = "job.success.create";
        public static final String SALARY_ERROR = "job.error.salary-error";
    }
}

