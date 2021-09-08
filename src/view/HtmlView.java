package view;


import controller.Controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vo.Vacancy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class HtmlView implements View{
    private Controller controller;
    private final String filePath = "src\\vacancies.html";

    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            String newContent = getUpdatedFileContent(vacancies);
            updateFile(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        try {
            Document document = getDocument();
            Elements elements = document.getElementsByClass("template");
            Element template = elements.clone().removeAttr("style").removeClass("template").first();

            Elements oldVacancies = document.getElementsByClass("vacancy");

            for (Element red : oldVacancies) {
                if (!red.hasClass("template")) {
                    red.remove();
                }
            }

            for (Vacancy vacancy : vacancies) {
                Element element = template.clone();

                Element vacancyLink = element.getElementsByAttribute("href").get(0);
                vacancyLink.appendText(vacancy.getTitle());
                vacancyLink.attr("href", vacancy.getUrl());
                Element city = element.getElementsByClass("city").get(0);
                city.appendText(vacancy.getCity());
                Element companyName = element.getElementsByClass("companyName").get(0);
                companyName.appendText(vacancy.getCompanyName());
                Element salary = element.getElementsByClass("salary").get(0);
                salary.appendText(vacancy.getSalary());

                elements.before(element.outerHtml());
            }
            return document.html();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateFile(String string) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(string);
        } catch (IOException e) {

        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        System.out.println("Введите запрос:");
        Scanner console = new Scanner(System.in);
        String request = console.nextLine();
        System.out.println("Вычисление...");

        controller.onRequest(request);
    }

    protected Document getDocument() throws IOException {
        File file = new File(filePath);
        return Jsoup.parse(file, "UTF-8");
    }
}
