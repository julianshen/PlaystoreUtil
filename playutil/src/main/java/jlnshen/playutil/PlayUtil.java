/*
 * Copyright [2014]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jlnshen.playutil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlayUtil {
    private static final String URL_PLAYSTORE_APPS = "https://play.google.com/store/apps";
    public static final String ATTR_TITLE = "title";
    public static final String ATTR_HREF = "href";

    public static List<Category> getCategories(Locale locale) throws IOException {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        Document doc = Jsoup.connect(URL_PLAYSTORE_APPS + "?hl=" + locale.getISO3Language()).get();
        Elements elements = doc.select(".child-submenu-link");

        List<Category> categories = new ArrayList<Category>();
        for (Element element : elements) {
            Category category = new Category(element.attr(ATTR_TITLE), element.attr(ATTR_HREF));
            categories.add(category);
        }

        return categories;
    }

    public static List<Category> getCategories() throws IOException {
        return getCategories(null);
    }

    public static Category getCategory(String packageName, Locale locale) throws IOException {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        Document doc = Jsoup.connect(URL_PLAYSTORE_APPS + "/details?id=" + packageName + "&hl=" + locale.getISO3Language()).get();

        Elements elements = doc.select("a.document-subtitle.category");

        if (elements.size() > 0) {
            Element element = elements.first();
            String href = element.attr("href");

            elements = element.select("span[itemprop=genre]");

            if (elements.size() > 0) {
                element = elements.first();
                String name = element.text();

                return new Category(name, href);
            }
        }

        return null;
    }

    public static Category getCategory(String packageName) throws IOException {
        return getCategory(packageName, null);
    }

    public static String getCoverImage(String packageName) throws IOException {
        Document doc = Jsoup.connect(URL_PLAYSTORE_APPS + "/details?id=" + packageName).get();

        Elements elements = doc.select(".cover-image");
        if(elements.size() > 0) {
            return elements.first().attr("src");
        }

        return null;
    }

    public static class Category {
        private String name;
        private String href;

        Category(String name, String href) {
            this.name = name;
            this.href = href;
        }

        public String name() {
            return name;
        }

        public String href() {
            return href;
        }

        public boolean isGame() {
            return href.contains("GAME_");
        }
    }
}
