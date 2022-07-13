
import controller.Controller;
import model.HHStrategy;
import model.Model;
import model.Provider;
import view.HtmlView;

public class Main {
    public static void main(String[] args) {
        HtmlView htmlView = new HtmlView();
        Model model = new Model(htmlView, new Provider(new HHStrategy()));
        Controller controller = new Controller(model);
        htmlView.setController(controller);
        htmlView.userCitySelectEmulationMethod();
    }
}
