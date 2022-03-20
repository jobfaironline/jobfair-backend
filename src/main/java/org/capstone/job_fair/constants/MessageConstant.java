package org.capstone.job_fair.constants;

public final class MessageConstant {
    private MessageConstant() {

    }

    public static final class Exception {
        private Exception() {
        }

        public static final String RESOURCE_NOT_FOUND = "error.exception.resource-not-found";
        public static final String JWT_ERROR = "error.exception.jwt-error";
        public static final String NO_PERMISSION = "error.exception.resource-no-permission";
        public static final String INTERNAL_ERROR = "error.exception.internal-error";
        public static final String METHOD_ARGUMENT_NOT_VALID = "error.exception.method-argument-not-valid";
        public static final String ENTITY_NOT_FOUND = "error.exception.entity-not-found";
        public static final String HTTP_MESSAGE_NOT_VALID = "error.exception.http-message-not-valid";
    }

    public static final class JobFair {
        private JobFair() {
        }

        public static final String CREATE_JOB_FAIR_PLAN_SUCCESSFULLY = "job-fair-plan.success.create";
        public static final String UPDATE_JOB_FAIR_PLAN_SUCCESSFULLY = "job-fair-plan.success.update";
        public static final String DELETE_JOB_FAIR_PLAN_SUCCESSFULLY = "job-fair-plan.success.delete";
        public static final String SUBMIT_JOB_FAIR_PLAN_SUCCESSFULLY = "job-fair-plan.success.submit";
        public static final String CANCEL_JOB_FAIR_PLAN_SUCCESSFULLY = "job-fair-plan.success.cancel";
        public static final String RESTORED_DELETED_JOB_FAIR_PLAN_SUCCESSFULLY = "job-fair-plan.success.restore";
        public static final String JOB_FAIR_NOT_FOUND = "job-fair-plan.error.not-found";
        public static final String INVALID_CREATOR_ID = "job-fair-plan.error.invalid-creator-id";
        public static final String INVALID_JOB_FAIR_STATUS = "job-fair-plan.error.invalid-job-fair-status";
        public static final String END_TIME_LESS_THAN_START_TIME_ERROR = "job-fair-plan.error.end-time-less-than-start-time";
        public static final String INVALID_END_TIME = "job-fair-plan.error.invalid-end-time";
        public static final String INVALID_BUY_BOOTH_TIME = "job-fair-plan.error.invalid-buy-booth-time";
        public static final String INVALID_STATUS_WHEN_EVALUATE = "job-fair-plan.error.invalid-status-when-evaluate";
        public static final String REJECT_MISSING_REASON = "job-fair-plan.error.reject-missing-reason";
        public static final String EVALUATE_SUCCESSFULLY = "job-fair-plan.success.evaluate-successfully";
        public static final String JOB_FAIR_IS_NOT_APPROVED = "job-fair-plan.error.not-approved";
        public static final String INVALID_COMPANY_REGISTER_START_TIME = "job-fair-plan.error.invalid-company-register-start-time";
        public static final String INVALID_JOB_FAIR = "job-fair-plan.error.invalid-job-fair";
        public static final String INVALID_SEARCH_TIME_RANGE = "job-fair-plan.error.invalid-search-time-range";
        public static final String INVALID_PAGE_NUMBER = "job-fair-plan.error.invalid-page-number";
        public static final String INVALID_JOB_FAIR_SEARCH_STATUS_OF_ADMIN = "job-fair-plan.error.invalid-job-fair-search-status-of-admin";

    }

    public static final class InvalidFormatValidationMessage {
        //the value in this class belongs to Validator annotation
        //validator on default can use expression language to lookup message source
        //=> need to have {}
        private InvalidFormatValidationMessage() {
        }

        public static final String INVALID_EMAIL_FORMAT = "{error.format.invalid-email}";
        public static final String INVALID_PASSWORD_FORMAT = "{error.format.invalid-password}";
        public static final String INVALID_PHONE_FORMAT = "{error.format.invalid-phone}";
        public static final String NOT_BLANK_FORMAT = "{error.format.blank-value}";
        public static final String XSS_MESSAGE = "{error.format.xss}";
    }

    public static final class AccessControlMessage {
        private AccessControlMessage() {
        }

        public static final String INVALID_REFRESH_TOKEN = "access-control.error.invalid-refresh-token";
        public static final String TOKEN_CLAIM_INVALID = "access-control.error.invalid-token-claim";
        public static final String CONFIRM_PASSWORD_MISMATCH = "access-control.error.confirm-password-mismatch";
        public static final String INVALID_OTP = "access-control.error.invalid-otp";
        public static final String EXPIRED_OTP = "access-control.error.expired-otp";
        public static final String RESET_PASSWORD_SUCCESSFULLY = "access-control.success.reset-password";
        public static final String INTERVAL_OTP_REQUEST = "access-control.error.interval-otp-request";
        public static final String REQUEST_RESET_PASSWORD_SUCCESSFULLY = "access-control.success.request-reset-password";
        public static final String INVALID_VERIFY_TOKEN = "access-control.error.invalid-account-verify-token";
        public static final String INVALIDATED_VERIFY_TOKEN = "access-control.error.invalidated-account-verify-token";
        public static final String EXPIRED_TOKEN = "access-control.error.expired-account-verify-token";
        public static final String VERIFY_ACCOUNT_SUCCESSFULLY = "access-control.success.verify-account";
        public static final String UNAUTHORIZED_ACTION = "access-control.error.unauthorized-action";
    }

    public static final class Account {
        private Account() {
        }

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
        public static final String OLD_PASSWORD_MISMATCH = "account.error.old-password-mismatch";
        public static final String CHANGE_PASSWORD_SUCCESSFULLY = "account.success.change-password-successfully";
        public static final String SEND_NEW_VERIFICATION_LINK_SUCCESSFULLY = "account.success.send-new-verification-link";
        public static final String ACCOUNT_VERIFY_MAIL_TITLE = "account.verify.email-title";
        public static final String VERIFY_ACCOUNT_TOKEN_INTERVAL_ERROR = "account.verify.token-interval-error";
        public static final String ALREADY_VERIFIED = "account.verify.already-verified";
        public static final String NOT_VERIRIED = "account.verify.not-verified";

    }

    public static final class Attendant {
        private Attendant() {
        }

        public static final String UPDATE_PROFILE_SUCCESSFULLY = "attendant.success.update-profile";
        public static final String UPDATE_PROFILE_FAILED = "attendant.error.update-profile";
        public static final String REGISTER_SUCCESSFULLY = "attendant.success.create";
        public static final String ATTENDANT_MISMATCH = "attendant.error.mismatch";
    }

    public static final class Company {
        private Company() {
        }

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
        public static final String COMPANY_MISSMATCH = "company.error.missmatch";
    }

    public static final class CompanyEmployee {
        private CompanyEmployee() {
        }

        public static final String EMAIL_EXISTED = "company-employee.error.email-existed";
        public static final String CREATE_EMPLOYEE_MANAGER_SUCCESSFULLY = "company-employee.success.create-company-manager";
        public static final String CREATE_EMPLOYEE_EMPLOYEE_SUCCESSFULLY = "company-employee.success.create-company-employee";
        public static final String UPDATE_PROFILE_SUCCESSFULLY = "company-employee.success.update-profile";
        public static final String DELETE_SUCCESSFULLY = "company-employee.success.delete-employee";
        public static final String DELETE_FAILED = "company-employee.error.delete-failed";
        public static final String EMAIL_SUBJECT = "company-employee.email.subject";
        public static final String EMAIL_CONTENT = "company-employee.email.body";
        public static final String MANAGER_NOT_FOUND = "company-employee.error.manager-not-found";
        public static final String INVALID_ROLE = "company-employee.error.invalid-role";
        public static final String EMPLOYEE_NOT_FOUND = "company-employee.error.not-found";
        public static final String DIFFERENT_COMPANY_ERROR = "company-employee.error.different-company";
        public static final String PROMOTE_EMPLOYEE_SUCCESSFULLY = "company-employee.success.promote-employee";
        public static final String EMPLOYEE_NOT_ACTIVE = "company-employee.error.not-active";
        public static final String MAX_QUOTA_FOR_COMPANY_EMPLOYEE = "company-employee.error.max-quota";
        public static final String COMPANY_ID_BLANK_ERROR = "company-employee.error.company-id-blank";

    }

    public static final class Paging {
        private Paging() {

        }

        public static final String INVALID_PAGE_NUMBER = "paging.error.invalid-page-number";
    }

    public static final class Gender {
        private Gender() {
        }

        public static final String NOT_FOUND = "gender.error.not-found";
    }

    public static final class Job {
        private Job() {
        }

        public static final String CREATE_JOB_SUCCESSFULLY = "job.success.create";
        public static final String DELETE_JOB_SUCCESSFULLY = "job.success.delete";
        public static final String SALARY_ERROR = "job.error.salary-error";
        public static final String JOB_POSITION_NOT_FOUND = "job-position.error.not-found";
        public static final String COMPANY_MISMATCH = "job-position.error.company-mismatch";
        public static final String UPDATE_JOB_SUCCESSFULLY = "job-position.success.update";
        public static final String INVALID_PAGE_NUMBER = "job-position.error.invalid-page-number";
        public static final String CSV_LINE_ERROR = "job-position.error.csv-line-error";
        public static final String CSV_FILE_ERROR = "job-position.error.csv-file-error";
    }

    public static final class Skill {
        private Skill() {
        }

        public static final String INVALID_SKILL = "skill.error.invalid";
        public static final String SKILL_NOT_FOUND = "skill.error.not-found";
    }

    public static final class WorkHistory {
        private WorkHistory() {

        }

        public static final String INVALID_WORK_HISTORY = "work-history.error.invalid";
        public static final String WORK_HISTORY_NOT_FOUND = "work-history.error.not-found";
    }

    public static final class Education {
        private Education() {
        }

        public static final String INVALID_EDUCATION = "education.error.invalid";
        public static final String EDUCATION_NOT_FOUND = "education.error.not-found";
    }

    public static final class Certification {
        private Certification() {
        }

        public static final String INVALID_CERTIFICATION = "certification.error.invalid";
        public static final String CERTIFICATION_NOT_FOUND = "certification.error.not-found";
    }

    public static final class Reference {
        private Reference() {
        }

        public static final String INVALID_REFERENCE = "reference.error.invalid";
        public static final String REFERENCE_NOT_FOUND = "reference.error.not-found";
    }

    public static final class Activity {
        private Activity() {
        }

        public static final String INVALID_ACTIVITY = "activity.error.invalid";
        public static final String ACTIVITY_NOT_FOUND = "activity.error.not-found";
    }

    public static final class Benefit {
        private Benefit() {
        }

        public static final String NOT_FOUND = "benefit.error.not-found";
    }

    public static final class SubCategory {
        private SubCategory() {
        }

        public static final String NOT_FOUND = "sub-category.error.not-found";
    }

    public static final class SkillTag {
        private SkillTag() {
        }

        public static final String NOT_FOUND = "skill-tag.error.not-found";
    }

    public static final class Mail {
        private Mail() {
        }

        public static final String NAME = "mail.sender.name";
        public static final String SEND_FAILED = "mail.error.send-failed";
    }

    public static final class CompanyRegistration {
        private CompanyRegistration() {
        }

        public static final String JOB_FAIR_REGISTRATION_OUT_OF_REGISTER_TIME = "company-registration.error.out-of-register-time";
        public static final String UNIQUE_JOB_POSITION_ERROR = "company-registration.error.unique-job-position";
        public static final String MIN_MAX_SALARY_ERROR = "company-registration.error.min-max-salary";
        public static final String SUBMIT_SUCCESSFULLY = "company-registration.success.submit";
        public static final String NOT_FOUND = "company-registration.error.not-found";
        public static final String COMPANY_MISMATCH = "company-registration.error.mismatch";
        public static final String EXISTED_PENDING_REGISTRATION = "company-registration.error.existed-pending-registration";
        public static final String NOT_ALLOW_CANCEL = "company-registration.error.not-allow-cancel";
        public static final String CANCEL_SUCCESSFULLY = "company-registration.success.cancel";
        public static final String REJECT_MISSING_REASON = "company-registration.error.reject-missing-reason";
        public static final String INVALID_STATUS_WHEN_EVALUATE = "company-registration.error.invalid-status-when-evaluate";
        public static final String INVALID_COMPANY_REGISTRATION_STATUS_WHEN_EVALUATE = "company-registration.error.invalid-company-registration-status-when-evaluate";
        public static final String EVALUATE_SUCCESSFULLY = "company-registration.success.evaluate";
        public static final String COMPANY_REGISTER_SUCCESSFULLY = "company-registration.success.register";
        public static final String INVALID_STATUS_WHEN_UPDATE = "company-registration.error.invalid-status-when-update";
        public static final String UPDATE_COMPANY_REGISTRATION_DRAF_SUCCESSFULLY = "company-registration.success.update-company-registration-draft";
        public static final String INVALID_PAGE_NUMBER = "company-registration.error.invalid-page-number";
        public static final String CREATOR_MISMATCH = "company-registration.error.creator-mismatch";
        public static final String INVALID_SEARCH_STATUS = "company-registration.error.invalid-search-status";


    }

    public static final class Staff {
        private Staff() {
        }

        public static final String CREATE_SUCCESSFULLY = "staff.success.create-successfully";
    }

    public static final class DecoratedItem {
        private DecoratedItem() {
        }

        public static final String NOT_FOUND = "decorated-item.error.not-found";
        public static final String UPDATE_SUCCESSFULLY = "decorated-item.success.update-item";
    }

    public static final class Layout {
        private Layout() {
        }

        public static final String NOT_FOUND = "layout.error.not-found";
        public static final String UPDATE_SUCCESSFULLY = "layout.success.update-item";
        public static final String INVALID_GLB_FILE = "layout.error.invalid-glb-file";
    }


    public static final class CompanyBoothLayout {
        private CompanyBoothLayout() {
        }

        public static final String NOT_FOUND = "company-booth-layout.error.not-found";

    }

    public static final class Application {
        private Application() {

        }

        public static final String NOT_FOUND_ATTENDANT = "application.error.not-found-attendant";
        public static final String NOT_FOUND_REGISTRATION_JOB_POSITION = "application.error.not-found-registration-job-position";
        public static final String INALID_TIME = "application.error.invalid-time";
        public static final String INVALID_PAGE_NUMBER = "application.error.invalid-page-number";
        public static final String JOB_POSITION_ID_AND_JOBFAIR_ID_BOTH_PRESENT_ERROR = "application.error.job-position-id-and-job-fair-id-both-present";
        public static final String UNSUPPORTED_SORT_VALUE_FOR_APPLICATION_FOR_COMPANY_ERROR = "application.error.unsupported-sort-value-for-application-for-company";
        public static final String CV_NOT_FOUND = "application.error.cv-not-found";
    }

    public static final class Order {
        private Order() {
        }

        public static final String NOT_FOUND = "order.error.not-found";
    }

    public static final class Booth {
        private Booth() {
        }

        public static final String NOT_FOUND = "booth.error.not-found";
    }

    public static final class BoothPurchase {
        private BoothPurchase() {
        }

        public static final String ALREADY_HAS_BOOTH = "booth-purchase.error.already-buy-booth";
    }

    public static final class Cv {
        private Cv() {
        }
    }

}

