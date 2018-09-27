package com.github.zesetup.vaadinspringbillet.ui;

import com.github.zesetup.vaadinspringbillet.model.Employee;
import com.github.zesetup.vaadinspringbillet.service.EmployeeService;
import com.github.zesetup.vaadinspringbillet.service.EmployeeService.EmployeeSort;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.provider.SortOrder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route
public class vaadinUI extends VerticalLayout {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private EmployeeEditorWindow employeeEditor;

  private TextField filterField = new TextField();
  private static final Logger logger = LoggerFactory.getLogger(vaadinUI.class);
  private Grid<Employee> grid = new Grid<Employee>();
  private Employee employee;
  public vaadinUI() {}


  protected void init() {
    grid.setWidth("620");
    grid.addColumn(p -> String.valueOf(p.getName())).setHeader("Name").setSortProperty("name")
        .setWidth("200");
    grid.addColumn(p -> String.valueOf(p.getSurname())).setHeader("Surname")
        .setSortProperty("surname").setWidth("200");
    grid.addColumn(p -> String.valueOf(p.getLogin())).setHeader("Login").setSortProperty("login")
        .setWidth("200");

    // build layout
    filterField.setPlaceholder("Filter..");
    filterField.setValueChangeMode(ValueChangeMode.EAGER);
    filterField.clear();

    Button addNewBtn = new Button("New");
    //addWindow(employeeEditor);
    employeeEditor.setVisible(false);
    addNewBtn.addClickListener(e -> {
      employee = new Employee();
      employeeEditor.editEmployee(employee);
      employeeEditor.setVisible(true);
    });
    Button editBtn = new Button("Edit", VaadinIcon.EDIT.create());

    editBtn.addClickListener(e -> {
      if (!grid.getSelectedItems().isEmpty()) {
        employee = grid.asSingleSelect().getValue();
        employeeEditor.editEmployee(employee);
        employeeEditor.setVisible(true);

      } else {
        Notification.show("Please select item");
      }
    });

    Label counterLabel = new Label();
    HorizontalLayout actions = new HorizontalLayout(filterField, addNewBtn, editBtn);
    VerticalLayout verticalLayout = new VerticalLayout(actions, grid, counterLabel);
    add(verticalLayout);
    filterField.addValueChangeListener(e -> {
      counterLabel.setText("Size:" + employeeService.countWithFilter(e.getValue()));
    });
    
    DataProvider<Employee, Void> dataProvider = DataProvider.fromCallbacks(
        query -> {
          List<EmployeeSort> sortOrders = new ArrayList<>();
          for(SortOrder<String> queryOrder : query.getSortOrders()) {
            EmployeeSort sort = employeeService.createSort(
              // The name of the sorted property
              queryOrder.getSorted(),
              // The sort direction for this property
              queryOrder.getDirection() == SortDirection.DESCENDING);
            sortOrders.add(sort);
          }

          return employeeService.find(
              sortOrders,
              query.getOffset(),
              query.getLimit()
            );
        },
        // The number of persons is the same regardless of ordering
        query -> employeeService.count()
      );     
    
    grid.setDataProvider(dataProvider);

    counterLabel.setText("Size:" + employeeService.count());

    employeeEditor.setChangeHandler((e) -> {
      employeeEditor.setVisible(false);
      //grid.getDataProvider().refreshAll();
      grid.getDataProvider().refreshItem(e);
      System.out.println("setChangeHandler... e:" + e.getName());
      //grid.getDataProvider().refreshItem(employee);
      //System.out.println("Selecting Employee: " + employee.getName());      
      //grid.select(employee);
    });

  }
}
