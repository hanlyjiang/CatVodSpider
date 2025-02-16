package com.github.catvod.spider;

import android.content.Context;

import com.github.catvod.bean.Class;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多盘
 * http://www.54271.fun/
 */
public class Duopan extends Spider {

    private final String url = "http://www.54271.fun/";

    @Override
    public void init(Context context) throws Exception {
        super.init(context);
    }

    @Override
    public void init(Context context, String extend) throws Exception {
        super.init(context, extend);
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        List<com.github.catvod.bean.Class> classes = new ArrayList<>();
        List<Vod> list = new ArrayList<>();
        Document doc = Jsoup.parse(OkHttp.string(url));
        for (Element a : doc.select(".nav-link")) {
            String typeName = a.text();
            String typeId = a.attr("href");
            String typeUrl = null;
            if (typeId.contains(url)) {
                typeUrl = typeId.replace(url, "");
            } else if (typeId.startsWith("/")) {
                typeUrl = typeId;
            } else {
                System.err.println("homeContent error:" + a);
            }
            if (typeUrl != null) {
                classes.add(new Class(typeUrl, typeName));
            }
        }
        for (Element div : doc.select("div.module-item")) {
            Element picDiv = doc.select(".module-item-cover").get(0);
            Element a = picDiv.select("a").get(0);
            String id = a.attr("href").replace(url, "");
            String name = a.attr("title");
            String pic = div.select("img").attr("data-src");
            Vod vod = new Vod(id, name, pic, "");
            // 其他信息添加
            Elements spans = div.select(".module-item-caption").select("span");
            for (int i = 0; i < spans.size(); i++) {
                Element element = spans.get(i);
                if (i == 0) {
                    vod.setVodYear(element.text());
                } else if (i == 1) {
                    vod.setVodTag(element.text());
                } else if (i == 2) {
                    vod.setVodArea(element.text());
                }
            }
            list.add(vod);
        }
        return Result.string(classes, list);
    }

    @Override
    public String homeVideoContent() throws Exception {
        return super.homeVideoContent();
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {
        return super.categoryContent(tid, pg, filter, extend);
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        String html = OkHttp.string(url + ids.get(0));
        Document doc = Jsoup.parse(html);
        String pic = doc.select("meta[property=og:image]").attr("content");
        String name = doc.select("meta[property=og:title]").attr("content");
        Vod vod = new Vod();
        vod.setVodId(ids.get(0));
        vod.setVodPic(pic);
        vod.setVodName(name);
        vod.setVodPlayFrom("玩偶姐姐");
        vod.setVodPlayUrl("播放$" + url + ids.get(0));
        return Result.string(vod);
    }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        return super.searchContent(key, quick);
    }

    @Override
    public String searchContent(String key, boolean quick, String pg) throws Exception {
        return super.searchContent(key, quick, pg);
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        return super.playerContent(flag, id, vipFlags);
    }

    @Override
    public String liveContent(String url) throws Exception {
        return super.liveContent(url);
    }

    @Override
    public boolean manualVideoCheck() throws Exception {
        return super.manualVideoCheck();
    }

    @Override
    public boolean isVideoFormat(String url) throws Exception {
        return super.isVideoFormat(url);
    }

    @Override
    public Object[] proxyLocal(Map<String, String> params) throws Exception {
        return super.proxyLocal(params);
    }

    @Override
    public String action(String action) {
        return super.action(action);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
