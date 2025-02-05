package com.github.hanlyjiang.config_tester;

import java.util.Arrays;

public class Urls {
    public Url[] urls;

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
                "urls=" + Arrays.toString(urls) +
                '}';
    }
}
