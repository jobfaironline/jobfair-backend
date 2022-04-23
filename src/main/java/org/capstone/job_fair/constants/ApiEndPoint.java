package org.capstone.job_fair.constants;

import lombok.NoArgsConstructor;

public final class ApiEndPoint {
    private ApiEndPoint() {
    }

    public static final String ROOT_ENDPOINT = "/api";
    public static final String VERSION = "/v1";
    public static final String API_ENDPOINT = ROOT_ENDPOINT + VERSION;

    public static final class Authentication {
        private Authentication() {
        }

        public static final String AUTHENTICATION_ENDPOINT = ROOT_ENDPOINT + VERSION + "/auth";
        public static final String LOGIN_ENDPOINT = AUTHENTICATION_ENDPOINT + "/login";
        public static final String REFRESH_TOKEN_ENDPOINT = AUTHENTICATION_ENDPOINT + "/refresh-token";
        public static final String LOGOUT_ENDPOINT = AUTHENTICATION_ENDPOINT + "/logout";
        public static final String GENERATE_OTP_ENDPOINT = AUTHENTICATION_ENDPOINT + "/generate-otp";
        public static final String RESET_PASSWORD_ENDPOINT = AUTHENTICATION_ENDPOINT + "/reset-password";

    }


    public static final class JobFair {
        private JobFair() {
        }

        public static final String JOB_FAIR = ROOT_ENDPOINT + VERSION + "/job-fairs";
        public static final String FOR_3D_MAP = JOB_FAIR + "/for-3d-map";
        public static final String PUBLISH = JOB_FAIR + "/publish";
        public static final String UPLOAD_THUMBNAIL = JOB_FAIR + "/upload-thumbnail";
        public static final String FOR_ATTENDANT = JOB_FAIR + "/attendants";
    }

    public static class Questions {
        private Questions() {

        }

        public static final String QUESTION = ROOT_ENDPOINT + VERSION + "/questions";
        public static final String UPDATE = QUESTION + "/update";
    }

    public static final class CompanyRegistration {
        private CompanyRegistration() {
        }

        public static final String COMPANY_REGISTRATION_ENDPOINT = ROOT_ENDPOINT + VERSION + "/job-fair/company-registration";
        public static final String DRAFT = COMPANY_REGISTRATION_ENDPOINT + "/draft";
        public static final String SUBMIT = COMPANY_REGISTRATION_ENDPOINT + "/submit";
        public static final String CANCEL = COMPANY_REGISTRATION_ENDPOINT + "/cancel";
        public static final String EVALUATE = COMPANY_REGISTRATION_ENDPOINT + "/evaluate";
        public static final String GET_ALL_OWN_COMPANY_REGISTRATION = COMPANY_REGISTRATION_ENDPOINT + "/getAllRegistrations";
        public static final String GET_ALL_COMPANY_REGISTRATION_OF_JOB_FAIR = COMPANY_REGISTRATION_ENDPOINT + "/getAllRegistrationsOfJobFair";
        public static final String GET_LATEST_APPROVE = COMPANY_REGISTRATION_ENDPOINT + "/latest-approve";
        public static final String GET_LATEST = COMPANY_REGISTRATION_ENDPOINT + "/latest";
        public static final String GET_ALL_OWN = COMPANY_REGISTRATION_ENDPOINT + "/get-all-own";
        public static final String GET_ALL_OF_COMPANY = COMPANY_REGISTRATION_ENDPOINT + "/get-all-of-company";
        public static final String GET_ALL_ADMIN = COMPANY_REGISTRATION_ENDPOINT + "/get-all-admin";

    }

    public static final class Authorization {
        private Authorization() {
        }

        public static final String VERIFY_USER = ROOT_ENDPOINT + VERSION + "/verify";
        public static final String NEW_VERIFY_LINK = ROOT_ENDPOINT + VERSION + "/new-verify-link";
    }

    public static final class Company {
        private Company() {
        }

        public static final String COMPANY_ENDPOINT = ROOT_ENDPOINT + VERSION + "/companies";
        public static final String COMPANY_LOGO_ENPOINT = COMPANY_ENDPOINT + "/upload-logo";
    }

    public static final class Job {
        private Job() {
        }

        public static final String JOB_POSITION_ENDPOINT = ROOT_ENDPOINT + VERSION + "/job";
        public static final String UPDATE_JOB_POSITION_ENDPOINT = JOB_POSITION_ENDPOINT + "/update";
        public static final String DELETE_JOB_POSITION_ENDPOINT = JOB_POSITION_ENDPOINT + "/delete";
        public static final String CREAT_JOB_POSITION_UPLOAD_CSV = JOB_POSITION_ENDPOINT + "/csv";

    }

    public static final class Attendant {
        private Attendant() {
        }

        public static final String ATTENDANT_ENDPOINT = ROOT_ENDPOINT + VERSION + "/attendants";
        public static final String REGISTER_ENDPOINT = ATTENDANT_ENDPOINT + "/register";
        public static final String UPDATE_ENDPOINT = ATTENDANT_ENDPOINT + "/update";
    }

    public static final class AttendantRegistration {
        private AttendantRegistration() {

        }

        public static final String ATTENDANT_REGISTRATION_ENDPOINT = ROOT_ENDPOINT + VERSION + "/attendant-registration";
        public static final String REGISTERED_JOB_FAIR = ATTENDANT_REGISTRATION_ENDPOINT + "/registered-jobfair";

    }

    public static final class Account {
        private Account() {
        }

        public static final String ACCOUNT_ENDPOINT = ROOT_ENDPOINT + VERSION + "/accounts";
        public static final String CHANGE_PASSWORD_ENDPOINT = ACCOUNT_ENDPOINT + "/change-password";
        public static final String PICTURE_PROFILE_ENDPOINT = ACCOUNT_ENDPOINT + "/upload-picture-profile";
        public static final String GET_INFO = ACCOUNT_ENDPOINT + "/get-info";
    }

    public static final class CompanyEmployee {
        private CompanyEmployee() {
        }

        public static final String COMPANY_EMPLOYEE_ENDPOINT = ROOT_ENDPOINT + VERSION + "/company-employees";
        public static final String REGISTER_COMPANY_MANAGER = COMPANY_EMPLOYEE_ENDPOINT + "/manager/register";
        public static final String UPDATE_PROFILE_ENDPOINT = COMPANY_EMPLOYEE_ENDPOINT + "/update";
        public static final String PROMOTE_EMPLOYEE_ENDPOINT = COMPANY_EMPLOYEE_ENDPOINT + "/promote";
    }

    public static final class ThreeDimensionMedia {
        private ThreeDimensionMedia() {
        }

        public static final String THREE_DIMENSION_MEDIA_ENDPOINT = ROOT_ENDPOINT + VERSION + "/3d-media";
    }

    public static final class Staff {
        private Staff() {
        }

        public static final String STAFF_ENDPOINT = ROOT_ENDPOINT + VERSION + "/staffs";
    }

    public static final class Layout {
        private Layout() {
        }

        public static final String LAYOUT_ENDPOINT = ROOT_ENDPOINT + VERSION + "/layouts";
        public static final String GET_BY_JOB_FAIR_AND_AVAILABLE_BOOTH_SLOT = LAYOUT_ENDPOINT + "/job-fair/available-slot";
        public static final String TEMPLATE_LAYOUT = LAYOUT_ENDPOINT + "/template";
        public static final String FOR_COMPANY_MANAGER = LAYOUT_ENDPOINT + "/company-manager";
        public static final String PICK_JOB_FAIR_LAYOUT = LAYOUT_ENDPOINT + "/pick-job-fair-layout";
        public static final String UPLOAD_THUMBNAIL = LAYOUT_ENDPOINT + "/upload-thumbnail";
        public static final String LAYOUT_BY_JOB_FAIR = LAYOUT_ENDPOINT + "/job-fair";
    }

    public static final class AgoraToken {
        private AgoraToken() {
        }

        public static final String AGORA_RTM_TOKEN = ROOT_ENDPOINT + VERSION + "/agora-rtm-token";
        public static final String AGORA_RTC_TOKEN = ROOT_ENDPOINT + VERSION + "/agora-rtc-token";
    }

    public static final class Application {
        private Application() {

        }

        public static final String APPLICATION_ENDPOINT = ROOT_ENDPOINT + VERSION + "/applications";
        public static final String GET_APPLICATION_FOR_COMPANY_BY_CRITERIA = APPLICATION_ENDPOINT + "/company";
        public static final String EVALUTATE = APPLICATION_ENDPOINT + "/evaluate";
        public static final String GET_APPLICATION_GENERAL_DATA = APPLICATION_ENDPOINT + "/company-general";
        public static final String SUBMIT_APPLICATION = APPLICATION_ENDPOINT + "/submit";
        public static final String DRAFT_APPLICATION = APPLICATION_ENDPOINT + "/draft";

    }

    public static final class JobFairBoothLayout {
        private JobFairBoothLayout() {
        }

        public static final String JOB_FAIR_BOOTH_LAYOUT = ROOT_ENDPOINT + VERSION + "/job-fair-booth-layout";
        public static final String LATEST_VERSION = JOB_FAIR_BOOTH_LAYOUT + "/latest";
        public static final String VIDEO_LAYOUT_WITH_FILE = JOB_FAIR_BOOTH_LAYOUT + "/videos/file";
        public static final String VIDEO_LAYOUT_WITH_URL = JOB_FAIR_BOOTH_LAYOUT + "/videos/url";


    }

    public static final class Country {
        private Country() {

        }

        public static final String COUNTRY_ENPOINT = ROOT_ENDPOINT + VERSION + "/country";
    }


    public static final class JobFairBooth {
        private JobFairBooth() {
        }

        public static final String COMPANY_BOOTH = ROOT_ENDPOINT + VERSION + "/job-fair-booth";
    }

    public static final class Cv {
        private Cv() {
        }

        public static final String CV = ROOT_ENDPOINT + VERSION + "/cv";
    }

    public static final class Assignment {
        private Assignment() {
        }

        public static final String ASSIGNMENT = ROOT_ENDPOINT + VERSION + "/assignments";
        public static final String ASSIGN = ASSIGNMENT + "/assign";
        public static final String UNASSIGN = ASSIGNMENT + "/unassign";
        public static final String JOB_FAIR = ASSIGNMENT + "/job-fair";
        public static final String JOB_FAIR_BOOTH = ASSIGNMENT + "/job-fair-booth";
        public static final String JOB_FAIR_AVAILABLE = ASSIGNMENT + "/available";
        public static final String JOB_FAIR_STATISTICS = ASSIGNMENT + "/statistics";
        public static final String OF_EMPLOYEE = ASSIGNMENT + "/employee";
    }

    public static final class Notification {
        private Notification() {
        }

        public static final String NOTIFICATION = ROOT_ENDPOINT + VERSION + "/notifications";
        public static final String READ = NOTIFICATION + "/read";
        public static final String READ_ALL = NOTIFICATION + "/read-all";

    }
}
