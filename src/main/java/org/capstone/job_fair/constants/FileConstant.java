package org.capstone.job_fair.constants;

public final class FileConstant {
    private FileConstant() {
    }

    public static final class CSV_CONSTANT {
        private CSV_CONSTANT() {
        }

        public static final String TYPE = "text/csv";
        public static final String MULTIPLE_VALUE_DELIMITER = ";";
    }

    public static final class XLSX_CONSTANT {
        private XLSX_CONSTANT() {
        }

        public static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    public static final class XLS_CONSTANT {
        private XLS_CONSTANT() {
        }

        public static final String TYPE = "application/vnd.ms-excel";
    }

}
