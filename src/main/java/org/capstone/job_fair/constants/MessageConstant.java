package org.capstone.job_fair.constants;

public final class MessageConstant {
    private MessageConstant() {

    }

    public static final class JobLevel {
        private JobLevel() {

        }

        public static final String NOT_FOUND = "job-level.error.not-found";
        public static final String DUPLICATED = "job-level.error.duplicate-job-level";
    }

    public static final class ProfessionalCategory {
        private ProfessionalCategory() {

        }

        public static final String NOT_FOUND = "professional-category.error.not-found";
        public static final String DUPLICATED = "professional-category.error.duplicate-job-level";
    }

    public static final class Residence {
        private Residence() {

        }

        public static final String NOT_FOUND = "residence.error.not-found";
        public static final String DUPLICATED = "residence.error.duplicate-job-level";
    }

    public static final class Role {
        private Role() {

        }

        public static final String NOT_FOUND = "role.error.not-found";
        public static final String DUPLICATED = "role.error.duplicate-job-level";
    }

    public static final class Qualification {
        private Qualification() {

        }

        public static final String NOT_FOUND = "qualification.error.not-found";
        public static final String DUPLICATED = "qualification.error.duplicate-job-level";
    }

    public static final class Exception {
        public static final String RESOURCE_NOT_FOUND = "error.exception.resource-not-found";
        public static final String JWT_ERROR = "error.exception.jwt-error";
        public static final String NO_PERMISSION = "error.exception.resource-no-permission";
        public static final String INTERNAL_ERROR = "error.exception.internal-error";
        public static final String METHOD_ARGUMENT_NOT_VALID = "error.exception.method-argument-not-valid";
        public static final String ENTITY_NOT_FOUND = "error.exception.entity-not-found";
        public static final String HTTP_MESSAGE_NOT_VALID = "error.exception.http-message-not-valid";

        private Exception() {
        }
    }

    public static final class Question {
        public static final String INVALID_DATE_RANGE = "question.error.invalid-date-range";
        public static final String NOT_FOUND = "question.error.not-found";
        public static final String LACK_CORRECT_ANSWER = "question.error.lack-correct-answer";
        public static final String CSV_WRONG_FORMAT = "question.error.csv-wrong-format";

        private Question() {


        }


    }

    public static final class JobFair {

        public static final String JOB_FAIR_NOT_FOUND = "job-fair.error.not-found";
        public static final String INVALID_DECORATE_TIME = "job-fair.error.invalid-decorate-time";
        public static final String INVALID_PUBLIC_TIME = "job-fair.error.invalid-public-time";
        public static final String INVALID_BUFFER_TIME = "job-fair.error.invalid-buffer-time";
        public static final String NOT_EDITABLE = "job-fair.error.not-editable";
        public static final String JOB_FAIR_ALREADY_PUBLISH = "job-fair.error.already-publish";

        private JobFair() {
        }


    }

    public static final class InvalidFormatValidationMessage {
        public static final String INVALID_EMAIL_FORMAT = "{error.format.invalid-email}";
        public static final String INVALID_PASSWORD_FORMAT = "{error.format.invalid-password}";
        public static final String INVALID_PHONE_FORMAT = "{error.format.invalid-phone}";
        public static final String NOT_BLANK_FORMAT = "{error.format.blank-value}";
        public static final String XSS_MESSAGE = "{error.format.xss}";
        public static final String INVALID_NAME_MESSAGE = "{error.format.name}";

        //the value in this class belongs to Validator annotation
        //validator on default can use expression language to lookup message source
        //=> need to have {}
        private InvalidFormatValidationMessage() {
        }
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
        public static final String INVALID_VERIFY_TOKEN = "access-control.error.invalid-account-verify-token";
        public static final String INVALIDATED_VERIFY_TOKEN = "access-control.error.invalidated-account-verify-token";
        public static final String EXPIRED_TOKEN = "access-control.error.expired-account-verify-token";
        public static final String VERIFY_ACCOUNT_SUCCESSFULLY = "access-control.success.verify-account";
        public static final String UNAUTHORIZED_ACTION = "access-control.error.unauthorized-action";

        private AccessControlMessage() {
        }
    }

    public static final class Account {
        public static final String NOT_FOUND = "account.error.not-found";
        public static final String ACCOUNT_INACTIVE = "account.error.inactive";

        public static final String EXISTED = "account.error.existed";
        public static final String EMAIL_EXISTED = "account.error.email-existed";
        public static final String NOT_FOUND_COUNTRY = "account.error.not-found-country";
        public static final String COUNTRY_EXISTED = "account.error.country-existed";
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
        public static final String INVALID_PAGE_NUMBER = "account.paging.invalid-page-number";

        private Account() {
        }

    }

    public static final class Attendant {
        public static final String UPDATE_PROFILE_SUCCESSFULLY = "attendant.success.update-profile";
        public static final String UPDATE_PROFILE_FAILED = "attendant.error.update-profile";
        public static final String REGISTER_SUCCESSFULLY = "attendant.success.create";
        public static final String ATTENDANT_MISMATCH = "attendant.error.mismatch";

        private Attendant() {
        }
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
        public static final String COMPANY_MISSMATCH = "company.error.missmatch";

        private Company() {
        }
    }

    public static final class CompanyEmployee {
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

        private CompanyEmployee() {
        }

    }

    public static final class Paging {
        public static final String INVALID_PAGE_NUMBER = "paging.error.invalid-page-number";

        private Paging() {

        }
    }

    public static final class Gender {
        public static final String NOT_FOUND = "gender.error.not-found";

        private Gender() {
        }

        public static final String DUPLICATED_GENDER = "gender.error.duplicated";

    }

    public static final class Job {
        public static final String CREATE_JOB_SUCCESSFULLY = "job.success.create";
        public static final String DELETE_JOB_SUCCESSFULLY = "job.success.delete";
        public static final String SALARY_ERROR = "job.error.salary-error";
        public static final String JOB_POSITION_NOT_FOUND = "job-position.error.not-found";
        public static final String COMPANY_MISMATCH = "job-position.error.company-mismatch";
        public static final String UPDATE_JOB_SUCCESSFULLY = "job-position.success.update";
        public static final String INVALID_PAGE_NUMBER = "job-position.error.invalid-page-number";
        public static final String CSV_LINE_ERROR = "job-position.error.csv-line-error";
        public static final String CSV_FILE_ERROR = "job-position.error.csv-file-error";

        private Job() {
        }
    }

    public static final class Skill {
        public static final String INVALID_SKILL = "skill.error.invalid";
        public static final String SKILL_NOT_FOUND = "skill.error.not-found";

        private Skill() {
        }

        public static final String DUPLICATED = "skill.error.duplicate";
    }

    public static final class WorkHistory {
        public static final String INVALID_WORK_HISTORY = "work-history.error.invalid";
        public static final String WORK_HISTORY_NOT_FOUND = "work-history.error.not-found";

        private WorkHistory() {

        }
    }

    public static final class Education {
        public static final String INVALID_EDUCATION = "education.error.invalid";
        public static final String EDUCATION_NOT_FOUND = "education.error.not-found";

        private Education() {
        }
    }

    public static final class Certification {
        public static final String INVALID_CERTIFICATION = "certification.error.invalid";
        public static final String CERTIFICATION_NOT_FOUND = "certification.error.not-found";
        public static final String EXPIRED_DATE_NOT_FOUND = "certification.error.expired-date-not-found";
        public static final String ISSUE_DATE_AND_EXPIRED_DATE_RANGE_ERROR = "certification.error.issue-date-and-expired-date-range-error";

        private Certification() {
        }

    }

    public static final class Reference {
        public static final String INVALID_REFERENCE = "reference.error.invalid";
        public static final String REFERENCE_NOT_FOUND = "reference.error.not-found";

        private Reference() {
        }
    }

    public static final class Activity {
        public static final String INVALID_ACTIVITY = "activity.error.invalid";
        public static final String ACTIVITY_NOT_FOUND = "activity.error.not-found";

        private Activity() {
        }
    }

    public static final class Benefit {
        public static final String NOT_FOUND = "benefit.error.not-found";

        private Benefit() {
        }
    }

    public static final class SubCategory {
        public static final String NOT_FOUND = "sub-category.error.not-found";

        private SubCategory() {
        }
    }

    public static final class SkillTag {
        public static final String NOT_FOUND = "skill-tag.error.not-found";

        private SkillTag() {
        }
    }

    public static final class Mail {
        public static final String NAME = "mail.sender.name";
        public static final String SEND_FAILED = "mail.error.send-failed";

        private Mail() {
        }
    }


    public static final class Staff {
        public static final String CREATE_SUCCESSFULLY = "staff.success.create-successfully";

        private Staff() {
        }
    }

    public static final class DecoratedItem {
        public static final String NOT_FOUND = "decorated-item.error.not-found";
        public static final String UPDATE_SUCCESSFULLY = "decorated-item.success.update-item";

        private DecoratedItem() {
        }
    }

    public static final class Layout {
        public static final String NOT_FOUND = "layout.error.not-found";
        public static final String UPDATE_SUCCESSFULLY = "layout.success.update-item";
        public static final String INVALID_GLB_FILE = "layout.error.invalid-glb-file";

        private Layout() {
        }
    }


    public static final class CompanyBoothLayout {
        public static final String NOT_FOUND = "company-booth-layout.error.not-found";

        private CompanyBoothLayout() {
        }

    }

    public static final class Application {
        public static final String NOT_FOUND_ATTENDANT = "application.error.not-found-attendant";
        public static final String NOT_FOUND_REGISTRATION_JOB_POSITION = "application.error.not-found-registration-job-position";
        public static final String INALID_TIME = "application.error.invalid-time";
        public static final String INVALID_PAGE_NUMBER = "application.error.invalid-page-number";
        public static final String JOB_POSITION_ID_AND_JOBFAIR_ID_BOTH_PRESENT_ERROR = "application.error.job-position-id-and-job-fair-id-both-present";
        public static final String UNSUPPORTED_SORT_VALUE_FOR_APPLICATION_FOR_COMPANY_ERROR = "application.error.unsupported-sort-value-for-application-for-company";
        public static final String CV_NOT_FOUND = "application.error.cv-not-found";
        public static final String APPLICATION_NOT_FOUND = "application.error.not-found";
        public static final String INVALID_EVALUATE_STATUS = "application.error.invalid-evaluate-status";
        public static final String EVALUATE_MESSAGE_IS_EMPTY = "application.error.empty-evaluate-message";
        public static final String ALREADY_APPLY_CV = "application.error.already-apply-cv";
        public static final String CV_NOT_HAVE_TEST = "application.error.cv-not-have-test";
        public static final String MISS_MATCH_INTERVIEWER = "application.error.miss-match-interviewer";

        public static final String EVALUATE_MESSAGE_TO_ATTENDANT = "application.success.evaluate-message-to-attendant";

        private Application() {

        }
    }

    public static final class Quiz {
        private Quiz() {
        }

        public static final String NOT_FOUND = "quiz.error.not-found";
        public static final String JOB_POSITION_NOT_ALLOW_QUIZ = "quiz.error.job-position-not-allow-quiz";
        public static final String TIME_UP = "quiz.error.time-up";
        public static final String TEST_ALREADY_TAKEN = "quiz.error.already-taken";
        public static final String CURRENT_QUIZ_STILL_IN_PROGRESS = "quiz.error.current-quiz-still-in-progress";
    }


    public static final class Cv {
        private Cv() {
        }

        public static final String NOT_FOUND = "cv.error.not-found";
    }

    public static final class Assignment {
        public static final String NOT_FOUND = "assignment.error.not-found";
        public static final String WRONG_ASSIGNMENT_TYPE = "assignment.error.wrong-assignment-type";
        public static final String INVALID_ASSIGNMENT_ORGANIZE_JOB_FAIR = "assignment.error.invalid-assignment-organize-job-fair";
        public static final String UNAVAILABLE_EMPLOYEE = "assignment.error.unavailable-employee";

        private Assignment() {
        }
    }

    public static final class JobFairBooth {
        public static final String NOT_FOUND = "job-fair-booth.error.not-found";
        public static final String UNIQUE_JOB_POSITION_ERROR = "job-fair-booth.error.unique-job-position";
        public static final String NAME_INVALID_LENGTH = "job-fair-booth.error.name-invalid-length";

        private JobFairBooth() {
        }

    }

    public static final class Notification {
        public static final String NOT_FOUND = "notification.error.not-found";

        private Notification() {
        }
    }

    public static final class NotificationMessage {
        private NotificationMessage() {
        }

        public static final class ASSIGN_EMPLOYEE {
            private ASSIGN_EMPLOYEE() {
            }

            public static final String TITLE = "notification-message.assign-employee.title";
            public static final String MESSAGE = "notification-message.assign-employee.message";
        }

        public static final class REQUEST_CHANGE_INTERVIEW {
            private REQUEST_CHANGE_INTERVIEW() {
            }

            public static final String TITLE = "notification-message.request-change-interview.title";
            public static final String MESSAGE = "notification-message.request-change-interview.message";
        }
    }

    public static final class InterviewSchedule {
        private InterviewSchedule() {
        }

        public static final String SCHEDULE_CONFLICT = "interview-schedule.error.schedule-conflict";
        public static final String NOT_FOUND = "interview-schedule.error.not-found";
        public static final String CANNOT_EDIT = "interview-schedule.error.cannot-edit";
        public static final String INVALID_REQUEST_SCHEDULE = "interview-schedule.error.invalid-schedule";
        public static final String INVALID_APPLICATION_STATUS = "interview-schedule.error.invalid-application-status";
        public static final String ALREADY_SCHEDULE_INTERVIEW = "interview-schedule.error.already-schedule-interview";
        public static final String MAXIMUM_SCHEDULE_ALLOW = "interview-schedule.error.maximum-schedule-allow";
    }

    public static final class InterviewRequestChange {
        private InterviewRequestChange() {
        }

        public static final String NOT_FOUND = "interview-request-change.error.not-found";
        public static final String INVALID_STATUS = "interview-request-change.error.invalid-status";
        public static final String CANNOT_EDIT = "interview-request-change.error.cannot-edit";
    }

    public static final class File {
        private File() {
        }

        public static final String XSL_NO_SHEET = "file.error.xsl-no-sheet";
        public static final String NOT_ALLOWED = "file.error.not-allowed";
    }

    public static final class Language {
        private Language() {
        }

        public static final String NOT_FOUND = "language.error.not-found";
    }

    public static final class JobType {
        private JobType() {
        }

        public static final String NOT_FOUND = "job-type.error.not-found";
    }

    public static final class Interview {
        private Interview() {
        }

        public static final String ATTENDANT_NOT_FOUND = "interview.error.attendant-not-found";
        public static final String REVIEWER_NOT_FOUND = "interview.error.reviewer-not-found";
        public static final String INVALID_STATUS = "interview.error.invalid-status";
        public static final String INTERVIEW_ROOM_NOT_FOUND = "interview.error.interview-room-not-found";
    }
}

