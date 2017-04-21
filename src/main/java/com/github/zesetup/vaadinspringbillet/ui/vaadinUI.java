package com.github.zesetup.vaadinspringbillet.ui;

import com.github.zesetup.vaadinspringbillet.model.Employee;
import com.github.zesetup.vaadinspringbillet.service.EmployeeService;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@SpringUI
@Theme("valo")
public class vaadinUI extends UI {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private EmployeeEditorWindow editorWindow;

  private TextField filterField = new TextField("Filter...");
  private static final Logger logger = LoggerFactory.getLogger(vaadinUI.class);
  private Grid<Employee> grid = new Grid<Employee>();


  public vaadinUI() {}

  @Override
  protected void init(VaadinRequest request) {

    grid.setSizeFull();
    grid.addColumn(p -> String.valueOf(p.getName())).setCaption("Name").setSortProperty("name")
        .setWidth(200);
    grid.addColumn(p -> String.valueOf(p.getSurname())).setCaption("Surname")
        .setSortProperty("surname").setWidth(200);
    grid.addColumn(p -> String.valueOf(p.getLogin())).setCaption("Login").setSortProperty("login")
        .setWidth(200);

    // build layout
    filterField.setPlaceholder("Filter..");
    filterField.setValueChangeMode(ValueChangeMode.LAZY);
    filterField.clear();
    Button addNewBtn = new Button("New", VaadinIcons.PLUS);
    addNewBtn.addClickListener(e -> {

    });
    Button editBtn = new Button("Edit", VaadinIcons.EDIT);
    editBtn.addClickListener(e -> {
      if (!grid.getSelectedItems().isEmpty()) {

      } else {
        Notification.show("Please select item");
      }
    });
    HorizontalLayout actions = new HorizontalLayout(filterField, addNewBtn, editBtn);
    VerticalLayout verticalLayout = new VerticalLayout(actions, grid);
    setContent(verticalLayout);
    filterField.addValueChangeListener(e -> {
      grid.setDataProvider((sortOrder, offset, limit) -> employeeService.findWithFilter(sortOrder,
          offset, limit, e.getValue()), () -> employeeService.countWithFilter(e.getValue()));
    });
    grid.setDataProvider(
        (sortOrder, offset, limit) -> employeeService.find(sortOrder, offset, limit),
        () -> employeeService.count());
  }
}