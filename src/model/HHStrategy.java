package model;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vo.Vacancy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy{
    private static final String URL_FORMAT = "https://hh.ru/search/vacancy?text=%s&page=%d";

    @Override
    public List<Vacancy> getVacancies(String request) {
        List<Vacancy> allVacancies = new ArrayList<>();

        String[] strings = request.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string).append("+");
        }
        sb.deleteCharAt(sb.length()-1);
        request = sb.toString();

        int page = 0;
        int pageLimit = 1;
        try {
            Document doc = getDocument(request, 0);
            try {
                pageLimit = Integer.parseInt(doc.getElementsByAttributeValue("data-qa", "pager-page").last().select("span").text());
            } catch (Exception e) {
                if (doc.getElementsByAttributeValueStarting("data-qa", "bloko-header-3").first().select("h1").text().endsWith("ничего не найдено")) {
                    throw new IllegalArgumentException("Неверный запрос");
                } else {
                    pageLimit = 0;
                }
            }
            do {
                doc = getDocument(request, page);
                Elements vacanciesHTMLList = doc.getElementsByAttributeValueStarting("data-qa", "vacancy-serp__vacancy vacancy-serp__vacancy_");

                if (vacanciesHTMLList.isEmpty()) {
                    return allVacancies;
                }

                for (Element element : vacanciesHTMLList) {
                    Elements title = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title");
                    Elements salary = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation");
                    Elements city = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address");
                    Elements companyName = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer");

                    Vacancy vacancy = new Vacancy();
                    vacancy.setTitle(title.text());
                    vacancy.setSalary(salary.size() > 0 ? salary.text() : "");
                    vacancy.setCity(city.text());
                    vacancy.setCompanyName(companyName.text());
                    vacancy.setSiteName("hh.ru");
                    vacancy.setUrl(title.attr("href"));

                    allVacancies.add(vacancy);
                }
                page++;
            } while (page < pageLimit);
        } catch (IOException e) {

        }
        return allVacancies;
    }

    protected Document getDocument(String request, int page) throws IOException {
        Document doc = null;
        try {
            doc = Jsoup.connect(String.format(URL_FORMAT, request, page))
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.106 Safari/537.36")
                    .referrer("https://hh.ru/")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
