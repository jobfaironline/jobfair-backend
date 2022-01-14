package org.capstone.job_fair.models.enums;

public enum Language {
    Afrikaans("af"),
    Arabic("ar"),
    Azeri("az"),
    Belarusian("be"),
    Bulgarian("bg"),
    Catalan("ca"),
    Czech("cs"),
    Welsh("cy"),
    Danish("da"),
    German("de"),
    Divehi("dv"),
    Greek("el"),
    English("en"),
    Esperanto("eo"),
    Spanish("es"),
    Estonian("et"),
    Basque("eu"),
    Farsi("fa"),
    Finnish("fi"),
    Faroese("fo"),
    French("fr"),
    Galician("gl"),
    Gujarati("gu"),
    Hebrew("he"),
    Hindi("hi"),
    Croatian("hr"),
    Hungarian("hu"),
    Armenian("hy"),
    Indonesian("id"),
    Icelandic("is"),
    Italian("it"),
    Japanese("ja"),
    Georgian("ka"),
    Kazakh("kk"),
    Kannada("kn"),
    Korean("ko"),
    Konkani("kok"),
    Kyrgyz("ky"),
    Lithuanian("lt"),
    Latvian("lv"),
    Maori("mi"),
    Macedonian("mk"),
    Mongolian("mn"),
    Marathi("mr"),
    Malay("ms"),
    Maltese("mt"),
    Norwegian("nb"),
    Dutch("nl"),
    NorthernSotho("ns"),
    Punjabi("pa"),
    Polish("pl"),
    Pashto("ps"),
    Portuguese("pt"),
    Quechua("qu"),
    Romanian("ro"),
    Russian("ru"),
    Sanskrit("sa"),
    Sami("se"),
    Slovak("sk"),
    Slovenian("sl"),
    Albanian("sq"),
    Swedish("sv"),
    Swahili("sw"),
    Syriac("syr"),
    Tamil("ta"),
    Telugu("te"),
    Thai("th"),
    Tagalog("tl"),
    Tswana("tn"),
    Turkish("tr"),
    Tsonga("ts"),
    Tatar("tt"),
    Ukrainian("uk"),
    Urdu("ur"),
    Uzbek("uz"),
    Vietnamese("vi"),
    Xhosa("xh"),
    Chinese("zh"),
    zu("Zulu");

    private final String code;

    Language(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}
