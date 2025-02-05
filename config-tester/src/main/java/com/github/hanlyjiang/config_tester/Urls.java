package com.github.hanlyjiang.config_tester;

import java.util.ArrayList;
import java.util.List;

public class Urls {
    public List<Url> urls = new ArrayList<>();

    public static class Url {
        public String url;
        public String name;

        @Override
        public String toString() {
            return "Url{" +
                    "url='" + url + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "Urls{" +
                "urls=" + urls.toString() +
                '}';
    }
}
