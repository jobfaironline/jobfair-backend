package org.capstone.job_fair.constants;

public class ApiEndPoint {


    public static final String ROOT_ENDPOINT = "/api";
    public static final String VERSION = "/v1";
    public static final String API_ENDPOINT = ROOT_ENDPOINT + VERSION;

    public static final class Authentication {
        public static final String AUTHENTICATION_ENDPOINT = ROOT_ENDPOINT + VERSION + "/auth";
        public static final String LOGIN_ENDPOINT = AUTHENTICATION_ENDPOINT + "/login";
        public static final String REFRESH_TOKEN_ENDPOINT = AUTHENTICATION_ENDPOINT + "/refresh-token";
        public static final String LOGOUT_ENDPOINT = AUTHENTICATION_ENDPOINT + "/logout";
        public static final String GENERATE_OTP_ENDPOINT = AUTHENTICATION_ENDPOINT + "/generate-otp";
        public static final String RESET_PASSWORD_ENDPOINT = AUTHENTICATION_ENDPOINT + "/reset-password";

    }
    public static final class JobFair{
        public static final String JOB_FAIR_PLAN = ROOT_ENDPOINT + VERSION + "/job-fairs";
        public static final String GET_OWN_PLAN = JOB_FAIR_PLAN + "/own-plan";
        public static final String DELETE_JOB_FAIR_PLAN_DRAFT = JOB_FAIR_PLAN +"/delete";
        public static final String SUBMIT_JOB_FAIR_PLAN_DRAFT = JOB_FAIR_PLAN +"/submit";
        public static final String CANCEL_PENDING_JOB_FAIR_PLAN = JOB_FAIR_PLAN + "/cancel";
        public static final String RESTORE_DELETED_JOB_FAIR_PLAN = JOB_FAIR_PLAN + "/restore";
        public static final String EVALUATE_JOB_FAIL_PLAN = JOB_FAIR_PLAN + "/evaluate";
    }

    public static final class CompanyRegistration {
        public static final String COMPANY_REGISTRATION_ENDPOINT = ROOT_ENDPOINT + VERSION + "/job-fair/company-registration";
        public static final String DRAFT = COMPANY_REGISTRATION_ENDPOINT + "/draft";
        public static final String SUBMIT = COMPANY_REGISTRATION_ENDPOINT + "/submit";
        public static final String CANCEL = COMPANY_REGISTRATION_ENDPOINT + "/cancel";
    }

    public static final class Authorization {
        public static final String VERIFY_USER = ROOT_ENDPOINT + VERSION + "/verify";
        public static final String NEW_VERIFY_LINK = ROOT_ENDPOINT + VERSION + "/new-verify-link";
    }

    public static final class Company {
        public static final String COMPANY_ENDPOINT = ROOT_ENDPOINT + VERSION + "/companies";
    }

    public static final class Job {
        public static final String JOB_POSITION_ENDPOINT = ROOT_ENDPOINT + VERSION + "/job";
    }

    public static final class Attendant {
        public static final String ATTENDANT_ENDPOINT = ROOT_ENDPOINT + VERSION + "/attendants";
        public static final String REGISTER_ENDPOINT = ATTENDANT_ENDPOINT + "/register";
        public static final String UPDATE_ENDPOINT = ATTENDANT_ENDPOINT + "/update";
    }

    public static final class Account {
        public static final String ACCOUNT_ENDPOINT = ROOT_ENDPOINT + VERSION + "/accounts";
        public static final String CHANGE_PASSWORD_ENDPOINT = ACCOUNT_ENDPOINT + "/change-password";
    }

    public static final class CompanyEmployee {
        public static final String COMPANY_EMPLOYEE_ENDPOINT = ROOT_ENDPOINT + VERSION + "/company-employees";
        public static final String REGISTER_COMPANY_MANAGER = COMPANY_EMPLOYEE_ENDPOINT + "/manager/register";
        public static final String UPDATE_PROFILE_ENDPOINT = COMPANY_EMPLOYEE_ENDPOINT + "/update";
        public static final String PROMOTE_EMPLOYEE_ENPOINT = COMPANY_EMPLOYEE_ENDPOINT + "/promote";
        public static final String PROMOTE_EMPLOYEE_ENDPOINT =  COMPANY_EMPLOYEE_ENDPOINT + "/promote";
    }

    public static final class DecoratedItem {
        public static final String DECORATED_ITEM_ENDPOINT = ROOT_ENDPOINT + VERSION + "/decorated-items";
        public static final String UPDATE = DECORATED_ITEM_ENDPOINT;
    }

    public static final class Staff{
        public static final String STAFF_ENDPOINT = ROOT_ENDPOINT + VERSION + "/staffs";
    }


    public static final class RestDataEndpoint {
        public static final String ACCOUNT = "accounts";
        public static final String MEDIA = "medias";
        public static final String COMPANY_SIZE = "company-sizes";
        public static final String COMPANY = "companies";
        public static final String CATEGORY = "categories";
        public static final String BENEFIT = "benefits";
        public static final String JOB_POSITION = "job-positions";
        public static final String JOB_TYPE = "job-types";
        public static final String ROLE = "roles";
        public static final String RESIDENCE = "residences";
        public static final String LANGUAGE = "languages";
        public static final String GENDER = "genders";
        public static final String CURRENT_JOB_LEVEL = "current-job-level";
        public static final String COUNTRY = "countries";
        public static final String ATTENDANT = "attendants";
        public static final String ACTIVITY = "activities";
        public static final String CERTIFICATION = "certifications";
        public static final String CV = "cvs";
        public static final String EDUCATION = "educations";
        public static final String REFERENCE = "references";
        public static final String RESET_PASSWORD = "reset-password";
        public static final String COMPANY_EMPLOYEE = "company-employee";
        public static final String SUB_CATEGORY = "sub-category";
        public static final String VERIFY = "verifies";
        public static final String SKILL_TAG = "skill-tag";
        public static final String JOB_FAIR = "job-fair";
        public static final String COMPANY_REGISTRATION = "company-registration";


    }


}
