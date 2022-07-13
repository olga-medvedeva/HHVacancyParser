package controller;

import model.Model;

public class Controller {
    private Model model;

    public Controller(Model model) {
        if (model == null) {
            throw new IllegalArgumentException();
        }
        this.model = model;
    }


    public void onRequest(String request) {
        model.request(request);
    }
}
