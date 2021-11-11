module atlj.client {
  requires atlj.model;
  requires javafx.base;
  requires javafx.graphics;
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;

  exports esi.acgt.atlj.client;
  exports esi.acgt.atlj.client.view;
  exports esi.acgt.atlj.client.controller;
}